package bestfood.controller;

import bestfood.model.*;
import bestfood.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.MultiValueMap;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomCartService customCartService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CheckoutService checkoutService;

    @GetMapping("/")
    public String landingPage(Authentication auth) {

        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/home";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Authentication auth) {

        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/home";
        }
        
        return "login";
    }

    @PostMapping("/login")
    public String logIn(
        @RequestParam("login-username") String username,
        @RequestParam("login-password") String password,
        Model model) {

        User user = userService.authenticate(username, password);

        if (user != null) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                String.valueOf(user.getId()),
                null,
                List.of(new SimpleGrantedAuthority(user.getRole()))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            return "redirect:/home";
        }

        model.addAttribute("errorMessage", "Invalid Username or Password");
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(
        @RequestParam("register-username") String username,
        @RequestParam("register-password") String password,
        @RequestParam("register-email") String email,
        @RequestParam(value = "register-address", required = false) String address,
        Model model) {

        if (userService.isUsernameExists(username)) {

            model.addAttribute("errorMessage", "Username already exists");
            return "login";
        }

        if (userService.isEmailExists(email)) {

            model.addAttribute("errorMessage", "Email already exists");
            return "login";
        }

        userService.createUser(username, password, email, address);

        return "redirect:/login";
    }

    @ResponseBody @GetMapping("/users/check-username")
    public Map<String, Boolean> checkUsername(@RequestParam("register-username") String username) {

        return Map.of("exists", userService.isUsernameExists(username));
    }

    @ResponseBody @GetMapping("/users/check-email")
    public Map<String, Boolean> checkEmail(@RequestParam("register-email") String email) {

        return Map.of("exists", userService.isEmailExists(email));
    }

    @GetMapping("/logout")
    public String logOut() {

        SecurityContextHolder.clearContext();

        return "redirect:/login";
    }

    @GetMapping("/home")
    public String homePage(Model model) {

        model.addAttribute("products", productService.getAllProductsExceptCoupon());

        return "home";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @PostMapping("/contact")
    public String requestContact(
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("subject") String subject,
        @RequestParam("message") String message,
        @RequestParam(value = "subscribe", required = false) boolean subscribe,
        @RequestParam("inquiry-type") String inquiryType,
        RedirectAttributes redirectAttributes) {

        String userMessage = 
            "Dear " + name + ",\n\n"
            + "Thank you for contacting us. Your request has been received.\n\n"
            + "Here is a summary of your message:\n\n" + "Name: " + name + "\n" + "Email: " + email
            + "\n"
            + "Subject: " + subject + "\n" + "Message: " + message + "\n"
            + "Subscribe to newsletter: "
            + (subscribe ? "Yes" : "No") + "\n" + "Inquiry Type: " + inquiryType + "\n\n"
            + "We will get back to you as soon as possible.\n\n" + "Best regards,\n"
            + "The BestFood Team";

        contactService.sendEmail(email, "bestfood102@gmail.com", "Your Contact Request", userMessage);

        String adminMessage = 
            "A new contact request has been submitted:\n\n" + "Name: " + name
            + "\n" + "Email: "
            + email + "\n" + "Subject: " + subject + "\n" + "Message: " + message + "\n"
            + "Subscribe to newsletter: " + (subscribe ? "Yes" : "No") + "\n" + "Inquiry Type: "
            + inquiryType;

        contactService.sendEmail(
            "bestfood102@gmail.com",
            email,
            "New Contact Request",
            adminMessage
        );

        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Your contact request has been submitted successfully!"
        );

        return "redirect:/contact";
    }

    @GetMapping("/shop")
    public String shopPage(Model model) {

        model.addAttribute("products", productService.getAllProductsExceptCoupon());
        
        return "shop";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());
        User user = userService.getUserById(userId);
        String untilNextCoupon = new DecimalFormat("0.00").format(100 - user.getCumulativeTotal());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("address", user.getAddress());
        model.addAttribute("ownedCoupons", user.getOwnedCoupons());
        model.addAttribute("untilNextCoupon", untilNextCoupon);

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
        @RequestParam("username") String username,
        @RequestParam("email") String email,
        @RequestParam("address") String address,
        Authentication auth) {
        
        userService.updateUser(Integer.parseInt(auth.getName()), username, email, address);

        return "redirect:/profile";
    }

    @GetMapping("/cart")
    public String cartPage(Model model, Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalNoTaxNoCoupons", String.format("%.2f", cartService.getTotalNoTaxNoCoupons(userId)));
        
        return "cart";
    }

    @PostMapping("/cart/items")
    public String addItemToCart(
        @RequestParam("product-id") Integer id, 
        @RequestParam("product-quantity") int quantity,
        Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        cartService.addCartItem(userId, id, quantity);

        return "redirect:/cart";
    }

    @PostMapping("/cart/items/quantities/update")
    public String updateCartItemsQuantities(
        @RequestParam MultiValueMap<String, String> params, Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        for (String key : params.keySet()) {

            if (key.matches(".+\\|quantity")) {

                String productIdString = key.substring(0, key.indexOf('|'));
                String quantityString = params.getFirst(key);

                Integer productId = Integer.parseInt(productIdString);
                int quantity = Integer.parseInt(quantityString);

                cartService.updateCartItemQuantity(userId, productId, quantity);
            }
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        cartService.removeCartItems(userId);

        return "redirect:/cart";
    }

    @PostMapping("/cart/items/{productId}/remove")
    public String removeCartItem(@PathVariable Integer productId, Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        cartService.removeCartItem(userId, productId);

        return "redirect:/cart";
    }

    @GetMapping("/custom-cart")
    public String customCart(Model model, Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        model.addAttribute(
            "customCartItems", 
            customCartService.getCustomCartItemsByUserId(userId)
        );
        model.addAttribute(
            "totalNoTaxNoCoupons", 
            String.format("%.2f", customCartService.getTotalNoTaxNoCoupons(userId))
        );
        return "custom-cart";
    }

    @PostMapping("/custom-cart/items")
    public String addItemToCustomCart(
        @RequestParam("product-id") Integer id,
        @RequestParam("product-quantity") int quantity,
        Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        customCartService.addCustomCartItem(userId, id, quantity);

        return "redirect:/custom-cart";
    }

    @PostMapping("/custom-cart/items/quantities/update")
    public String updateCustomCartItemsQuantities(
        @RequestParam MultiValueMap<String, String> params,
        Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        for (String key : params.keySet()) {

            if (key.matches(".+\\|quantity")) {

                String productIdString = key.substring(0, key.indexOf('|'));
                String quantityString = params.getFirst(key);

                Integer productId = Integer.parseInt(productIdString);
                int quantity = Integer.parseInt(quantityString);

                customCartService.updateCustomCartItemQuantity(userId, productId, quantity);
            }
        }

        return "redirect:/custom-cart";
    }

    @PostMapping("/custom-cart/clear")
    public String clearCustomCart(Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        customCartService.removeCustomCartItems(userId);

        return "redirect:/custom-cart";
    }

    @PostMapping("/custom-cart/items/{productId}/remove")
    public String removeCustomCartItem(@PathVariable Integer productId, Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        customCartService.removeCustomCartItem(userId, productId);

        return "redirect:/custom-cart";
    }

    @PostMapping("/custom-cart/add-to-cart")
    public String addCustomCartToCart(Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        customCartService.addCustomCartToCart(userId);

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model, Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        model.addAttribute(
            "totalNoTaxNoCoupons",
            String.format("%.2f", 
            cartService.getTotalNoTaxNoCoupons(userId))
        );
        model.addAttribute(
            "totalAfterTaxNoCoupons",
            String.format("%.2f", 
            checkoutService.getTotalAfterTaxNoCoupons(userId))
        );
        model.addAttribute(
            "totalFinal",
            String.format("%.2f", 
            checkoutService.getTotalFinal(userId))
        );
        model.addAttribute(
            "couponsApplied",
            checkoutService.getAppliedCouponsCount(userId)
        );
        model.addAttribute(
            "couponsOwned",
            userService.getOwnedCouponsCount(userId)
        );
        return "checkout";
    }

    @PostMapping("/checkout")
    public String checkOut(Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        checkoutService.checkOut(userId);

        return "redirect:/cart";
    }

    @PostMapping("/checkout/coupons")
    public String updateAppliedCoupons(
        @RequestParam(value = "coupons-count", required = false, defaultValue = "0") Integer couponsCount,
        Authentication auth) {

        Integer userId = Integer.parseInt(auth.getName());

        checkoutService.updateAppliedCouponsCount(userId, couponsCount);

        return "redirect:/checkout";
    }

    @GetMapping("/search") 
    @ResponseBody
    public List<Product> searchProducts(@RequestParam String query) {

        return productService.searchProducts(query);
    }

}