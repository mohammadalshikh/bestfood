package bestfood.controller;

import bestfood.model.*;
import bestfood.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.MultiValueMap;
import javax.servlet.http.HttpSession;
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
    public String landingPage(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user != null) {
            return "redirect:/home";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String logIn(
        @RequestParam("login-username") String username,
        @RequestParam("login-password") String password,
        HttpSession session,
        Model model) {

        User user = userService.authenticate(username, password);

        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/home";
        }

        model.addAttribute("failMessage", "Invalid Username or Password");
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

            model.addAttribute("failMessage", "Username already exists");
            return "login";
        }

        if (userService.isEmailExists(email)) {

            model.addAttribute("failMessage", "Email already exists");
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
    public String logOut(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("products", productService.getAllProducts());

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

        contactService.sendEmail(
            email,
            "bestfood102@gmail.com",
            "Your Contact Request",
            userMessage
        );

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

        ArrayList<ShopItem> shopItems = new ArrayList<>();
        List<Product> products = productService.getAllProducts();

        for (Product product : products) {

            if (product.getId() == 0)
                continue;

            String suggestedItem = "";

            if (product.getSuggestedItem() != 0) {

                Product suggested = productService.getProductById(product.getSuggestedItem());
                if (suggested != null) {
                    suggestedItem = suggested.getName();
                }
            }

            shopItems.add(
                new ShopItem(
                    product.getImage(),
                    product.getName(),
                    product.getPrice(),
                    product.getId(),
                    suggestedItem
                )
            );
        }

        model.addAttribute("shopItems", shopItems);
        return "shop";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {

            String cumulativeTotal = new DecimalFormat("0.00").format(user.getCumulativeTotal());

            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("password", user.getPassword());
            model.addAttribute("address", user.getAddress());
            model.addAttribute("ownedCoupons", user.getOwnedCoupons());
            model.addAttribute("cumulativeTotal", cumulativeTotal);
        }

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
        @RequestParam("username") String username,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("address") String address) {

        userService.updateUser(userService.getUserByUsername(username).getId(), username, email, password, address);

        return "redirect:/profile";
    }

    @GetMapping("/cart")
    public String cartPage(Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        int userId = user.getId();

        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);

        model.addAttribute("cartItems", cartItems);

        model.addAttribute("totalNoTaxNoCoupons", cartService.getTotalNoTaxNoCoupons(userId));

        return "cart";
    }

    @PostMapping("/cart/items")
    public String addItemToCart(
        @RequestParam("product-id") int id, 
        @RequestParam("product-quantity") int quantity,
        HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {
            cartService.addCartItem(user.getId(), id, quantity);
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/items/quantities/update")
    public String updateCartItemsQuantities(
        @RequestParam MultiValueMap<String, String> params,
        HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {

            int userId = user.getId();

            for (String key : params.keySet()) {

                if (key.matches(".+\\|quantity")) {

                    String productIdString = key.substring(0, key.indexOf('|'));
                    String quantityString = params.getFirst(key);

                    int productId = Integer.parseInt(productIdString);
                    int quantity = Integer.parseInt(quantityString);

                    cartService.updateCartItemQuantity(userId, productId, quantity);
                }
            }
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {
            cartService.removeCartItems(user.getId());
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/items/{productId}/remove")
    public String removeCartItem(@PathVariable int productId, HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {
            cartService.removeCartItem(user.getId(), productId);
        }

        return "redirect:/cart";
    }

    @GetMapping("/custom-cart")
    public String customCart(Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        int userId = user.getId();

        model.addAttribute(
            "customCartItems", 
            customCartService.getCustomCartItemsByUserId(userId)
        );

        model.addAttribute(
            "totalNoTaxNoCoupons",
            customCartService.getTotalNoTaxNoCoupons(userId)
        );

        return "custom-cart";
    }

    @PostMapping("/custom-cart/items")
    public String addItemToCustomCart(
        @RequestParam("product-id") int id,
        @RequestParam("product-quantity") int quantity,
        HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {
            customCartService.addCustomCartItem(user.getId(), id, quantity);
        }

        return "redirect:/custom-cart";
    }

    @PostMapping("/custom-cart/items/quantities/update")
    public String updateCustomCartItemsQuantities(
        @RequestParam MultiValueMap<String, String> params,
        HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {

            int userId = user.getId();

            for (String key : params.keySet()) {

                if (key.matches(".+\\|quantity")) {

                    String productIdString = key.substring(0, key.indexOf('|'));
                    String quantityString = params.getFirst(key);

                    int productId = Integer.parseInt(productIdString);
                    int quantity = Integer.parseInt(quantityString);

                    customCartService.updateCustomCartItemQuantity(userId, productId, quantity);
                }
            }
        }

        return "redirect:/custom-cart";
    }

    @PostMapping("/custom-cart/clear")
    public String clearCustomCart(HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {
            customCartService.removeCustomCartItems(user.getId());
        }

        return "redirect:/custom-cart";
    }

    @PostMapping("/custom-cart/items/{productId}/remove")
    public String removeCustomCartItem(@PathVariable int productId, HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {
            customCartService.removeCustomCartItem(user.getId(), productId);
        }

        return "redirect:/custom-cart";
    }

    @PostMapping("/custom-cart/add-to-cart")
    public String addCustomCartToCart(HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {
            customCartService.addCustomCartToCart(user.getId());
        }

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        int userId = user.getId();

        model.addAttribute(
            "totalNoTaxNoCoupons",
            cartService.getTotalNoTaxNoCoupons(userId)
        );
        model.addAttribute(
            "totalAfterTaxNoCoupons",
            checkoutService.getTotalAfterTaxNoCoupons(userId)
        );
        model.addAttribute(
            "totalFinal",
            checkoutService.getTotalFinal(userId)
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
    public String checkOut(HttpSession session) {

        String username = (String) session.getAttribute("username");

        User user = userService.getUserByUsername(username);

        if (user != null) {
            checkoutService.checkOut(user.getId());
        }

        return "redirect:/cart";
    }

    @PostMapping("/checkout/coupons")
    public String updateAppliedCoupons(
        @RequestParam("coupons-count") int couponsCount,
        HttpSession session) {

        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);

        if (user != null) {
            checkoutService.updateAppliedCouponsCount(user.getId(), couponsCount);
        }

        return "redirect:/checkout";
    }

    @GetMapping("/search") 
    @ResponseBody
    public List<Product> searchProducts(@RequestParam String query) {

        return productService.searchProducts(query);
    }

}