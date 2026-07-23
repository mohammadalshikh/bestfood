package bestfood.controller;

import bestfood.model.*;
import bestfood.service.*;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.MultiValueMap;

@Controller
public class UserController {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomCartService customCartService;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private ProductMatrixService productMatrixService;

    @Autowired
    private AdminController adminController;

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
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
    public String register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam(value = "address", required = false) String address,
            Model model) {

        if (userService.isUsernameExists(username)) {

            model.addAttribute("failMessage", "Username already exists");
            return "login";
        }

        if (userService.isEmailExists(email)) {

            model.addAttribute("failMessage", "Email already exists");
            return "login";
        }

        userService.addUser(username, password, email, address);
        return "redirect:/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("shop")
    public String shop(Model model) {

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

            shopItems.add(new ShopItem(
                    product.getImage(),
                    product.getName(),
                    product.getPrice(),
                    product.getId(),
                    suggestedItem));
        }

        model.addAttribute("shopItems", shopItems);

        return "shop";
    }

    @RequestMapping(value = "submit-contact", method = RequestMethod.POST)
    public String submitContact(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            @RequestParam(value = "subscribe", required = false) boolean subscribe,
            @RequestParam("inquiry-type") String inquiryType,
            Model model,
            RedirectAttributes redirectAttributes) {

        String userMessage = "Dear " + name + ",\n\nThank you for contacting us. Your request has been received. " +
                "Here is a summary of your message:\n\n" +
                "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Subject: " + subject + "\n" +
                "Message: " + message + "\n" +
                "Subscribe to newsletter: " + (subscribe ? "Yes" : "No") + "\n" +
                "Inquiry Type: " + inquiryType + "\n\n" +
                "We will get back to you as soon as possible.\n\nBest regards,\nThe BestFood Team";

        sendEmail(email, "bestfood102@gmail.com", "Your Contact Request", userMessage);

        String adminMessage = "A new contact request has been submitted:\n\n" +
                "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Subject: " + subject + "\n" +
                "Message: " + message + "\n" +
                "Subscribe to newsletter: " + (subscribe ? "Yes" : "No") + "\n" +
                "Inquiry Type: " + inquiryType + "\n";

        sendEmail("bestfood102@gmail.com", email, "New Contact Request", adminMessage);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Your contact request has been submitted successfully!");

        return "redirect:/contact";
    }

    private void sendEmail(String recipient, String sender, String subject, String body) {

        String host = "smtp.gmail.com";
        String port = "587";

        Properties props = new Properties();

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUsername, mailPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully to " + recipient);

        } catch (MessagingException e) {

            e.printStackTrace();
            System.err.println("Failed to send email to " + recipient);
        }
    }

    @GetMapping("/buy")
    public String buy(Model model) {

        model.addAttribute(
                "total",
                adminController.getCartPrice(usernameforclass));

        model.addAttribute(
                "orderTotal",
                adminController.getOrderTotal(usernameforclass));

        model.addAttribute(
                "totalAfterTexesNoCoup",
                adminController.getTotalAfterTexesNoCoup(usernameforclass));

        model.addAttribute(
                "couponsForUser",
                adminController.getCouponsForUser(usernameforclass));

        model.addAttribute(
                "couponsApplied",
                adminController.getCouponsApplied(usernameforclass));

        return "/buy";
    }

    @ResponseBody
    @GetMapping("/check-username-availability")
    public Map<String, Boolean> checkUsernameAvailability(@RequestParam("username") String username) {

        Map<String, Boolean> response = new HashMap<>();

        response.put("exists", userService.isUsernameExists(username));

        return response;
    }

    @ResponseBody
    @GetMapping("/check-email-availability")
    public Map<String, Boolean> checkEmailAvailability(@RequestParam("email") String email) {

        Map<String, Boolean> response = new HashMap<>();

        response.put("exists", userService.isEmailExists(email));

        return response;
    }

    @RequestMapping(value = "add-user", method = RequestMethod.POST)
    public String addUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("address") String address) {

        userService.addUser(username, password, email, address);

        return "redirect:/";
    }

    @GetMapping("clear-cart")
    public String clearCart() {

        User user = userService.getUserByUsername(usernameforclass);

        if (user != null) {
            cartService.clearCart(user.getUserId());
        }

        return "redirect:/cart";
    }

    @GetMapping("clear-custom-cart")
    public String clearCustomCart() {

        User user = userService.getUserByUsername(usernameforclass);

        if (user != null) {
            customCartService.clearCustomCart(user.getUserId());
        }

        return "redirect:/custom-cart";
    }

    @GetMapping("add-custom-cart-to-cart")
    public String addCustomCartToCart() {

        User user = userService.getUserByUsername(usernameforclass);

        if (user != null) {

            customCartService.moveCustomCartToCart(
                    user.getUserId());
        }

        return "redirect:/cart";
    }

    @GetMapping("remove-cart-item")
    public String removeCartItem(@RequestParam("productID") int productID) {

        User user = userService.getUserByUsername(usernameforclass);

        if (user != null) {
            cartService.removeCartItem(user.getUserId(), productID);
        }

        return "redirect:/cart";
    }

    @GetMapping("remove-custom-cart-item")
    public String removeCustomCartItem(@RequestParam("productID") int productID) {

        User user = userService.getUserByUsername(usernameforclass);

        if (user != null) {
            customCartService.removeCustomCartItem(user.getUserId(), productID);
        }

        return "redirect:/custom-cart";
    }

    @GetMapping("add-item-to-cart")
    public String addItemToCart(
            @RequestParam("productID") int productID,
            @RequestParam("quantity") int quantity) {

        User user = userService.getUserByUsername(usernameforclass);

        if (user != null) {

            cartService.addItemToCart(user.getUserId(), productID, quantity);
        }
        return "redirect:/cart";
    }

    @GetMapping("add-item-to-custom-cart")
    public String addItemToCustomCart(
            @RequestParam("productID") int productID,
            @RequestParam("quantity") int quantity) {

        User user = userService.getUserByUsername(usernameforclass);

        if (user != null) {

            customCartService.addItemToCustomCart(user.getUserId(), productID, quantity);
        }

        return "redirect:/custom-cart";
    }

    @RequestMapping(value = "/buy-cart", method = RequestMethod.POST)
    public String buyCart() {

        User user = userService.getUserByUsername(usernameforclass);

        if (user != null) {

            int transactionID = transactionHistoryService.getMaxTransactionId(user.getUserId()) + 1;

            Set<Integer> products = new HashSet<>();

            List<Cart> cartItems = cartService.getCartByUserId(user.getUserId());

            for (Cart cart : cartItems) {

                transactionHistoryService.addTransactionHistory(
                        user.getUserId(),
                        cart.getProduct().getId(),
                        cart.getQuantity(),
                        transactionID);

                if (cart.getProduct().getId() != 0) {
                    products.add(cart.getProduct().getId());
                }
            }

            if (products.size() >= 2) {

                for (int p : products) {

                    for (int q : products) {

                        if (p != q) {

                            ProductMatrix pm = productMatrixService.getProductMatrix(p);

                            if (pm != null) {

                                int oldCount = pm.getPairCount(
                                        productService.getProductById(q));

                                productMatrixService.updateProductMatrixPair(
                                        p,
                                        q,
                                        oldCount + 1);
                            }
                        }
                    }
                }
            }

            adminController.updateProductPairs();

            adminController.updateProductStockFromCart(
                    usernameforclass);

            adminController.updateUserCouponsFromCart(
                    usernameforclass);

            adminController.updateUserTotalAndCoupons(
                    usernameforclass);

            cartService.clearCart(
                    user.getUserId());
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart(Model model) {

        ArrayList<CartItem> cartItems = new ArrayList<>();

        int userId = adminController.getUserID();

        List<Cart> carts = cartService.getCartByUserId(userId);

        for (Cart cart : carts) {
            if (cart.getProduct().getId() != 0) {
                Product product = productService.getProductById(cart.getProduct().getId());

                if (product != null) {
                    float totalPrice = adminController.getProductPrice(
                            cart.getProduct().getId(),
                            cart.getQuantity());

                    cartItems.add(
                            new CartItem(
                                    product.getName(),
                                    cart.getQuantity(),
                                    totalPrice,
                                    cart.getProduct().getId()));
                }
            }
        }

        model.addAttribute(
                "cartItems",
                cartItems);

        model.addAttribute(
                "total",
                adminController.getCartPrice(usernameforclass));

        return "cart";
    }

    @GetMapping("update-cart-item-quantity")
    public String updateCartItemQuantity(
            @RequestParam MultiValueMap<String, String> params) {

        int userId = adminController.getUserID();

        for (String key : params.keySet()) {

            if (key.matches(".+\\|quantity")) {

                String productIDString = key.substring(0, key.indexOf('|'));

                String quantityString = params.getFirst(key);

                int productID = Integer.parseInt(productIDString);

                int quantity = Integer.parseInt(quantityString);

                if (quantity != 0) {
                    cartService.updateCartItemQuantity(userId, productID, quantity);
                } else {
                    addItemToCart(productID, quantity);
                }
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/custom-cart")
    public String customCart(Model model) {

        ArrayList<CustomCartItem> customCartItems = new ArrayList<>();

        int userId = adminController.getUserID();

        List<CustomCart> customCarts = customCartService.getCustomCartByUserId(userId);

        for (CustomCart cart : customCarts) {

            if (cart.getProduct().getId() != 0) {

                Product product = productService.getProductById(
                        cart.getProduct().getId());

                if (product != null) {

                    float totalPrice = adminController.getProductPrice(
                            cart.getProduct().getId(),
                            cart.getQuantity());

                    customCartItems.add(
                            new CustomCartItem(
                                    product.getName(),
                                    cart.getQuantity(),
                                    totalPrice,
                                    cart.getProduct().getId()));
                }
            }
        }

        model.addAttribute(
                "customCartItems",
                customCartItems);

        model.addAttribute(
                "total",
                adminController.getCustomCartPrice(usernameforclass));

        return "custom-cart";
    }

    @GetMapping("update-custom-cart-item-quantity")
    public String updateCustomCartItemQuantity(
            @RequestParam MultiValueMap<String, String> params) {

        int userId = adminController.getUserID();

        for (String key : params.keySet()) {

            if (key.matches(".+\\|quantity")) {

                String productIDString = key.substring(0, key.indexOf('|'));

                String quantityString = params.getFirst(key);

                int productID = Integer.parseInt(productIDString);

                int quantity = Integer.parseInt(quantityString);

                if (quantity != 0) {

                    customCartService.updateCustomCartItemQuantity(
                            userId,
                            productID,
                            quantity);

                } else {

                    addItemToCustomCart(
                            productID,
                            quantity);
                }
            }
        }

        return "redirect:/custom-cart";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Product> searchProducts(
            @RequestParam String query) {

        return productService.searchProducts(query);
    }

}