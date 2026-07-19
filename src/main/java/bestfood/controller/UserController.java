package bestfood.controller;

import java.util.*;

import bestfood.model.*;
import bestfood.service.JsonDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.web.bind.annotation.*;

import org.springframework.util.MultiValueMap;

@Controller
public class UserController {

    @Autowired
    private JsonDatabaseService db;

    @Autowired
    private AdminController adminController;

    static String usernameforclass = "";

    public static void setUsername(String usernameforclass) {
        UserController.usernameforclass = usernameforclass;
    }

    @GetMapping("/register")
    public String registerUser() {
        return "register";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("shop")
    public String shop(Model model) {
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        List<Product> products = db.getAllProducts();

        for (Product product : products) {
            String suggestedItem = "";
            if (product.getSuggestedItem() != 0) {
                Product suggested = db.getProductById(product.getSuggestedItem());
                if (suggested != null) {
                    suggestedItem = suggested.getName();
                }
            }
            shopItems.add(new ShopItem(product.getImage(), product.getName(),
                    product.getPrice(), product.getId(), suggestedItem));
        }

        model.addAttribute("shopItems", shopItems);
        return "shop";
    }

    @RequestMapping(value = "submitContact", method = RequestMethod.POST)
    public String submitContact(@RequestParam("name") String name,
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
        redirectAttributes.addFlashAttribute("successMessage", "Your contact request has been submitted successfully!");

        return "redirect:/contact";
    }

    private void sendEmail(String recipient, String sender, String subject, String body) {
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "bestfood102@gmail.com";
        String password = "euhmknxomgnoefgt";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
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
        model.addAttribute("total", adminController.getCartPrice(usernameforclass));
        model.addAttribute("orderTotal", adminController.getOrderTotal(usernameforclass));
        model.addAttribute("totalAfterTexesNoCoup", adminController.getTotalAfterTexesNoCoup(usernameforclass));
        model.addAttribute("couponsForUser", adminController.getCouponsForUser(usernameforclass));
        model.addAttribute("couponsApplied", adminController.getCouponsApplied(usernameforclass));
        return "/buy";
    }

    @GetMapping("/user/products")
    public String getproduct(Model model) {
        List<Product> products = db.getAllProducts();
        List<Category> categories = db.getAllCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "uproduct";
    }

    @ResponseBody
    @GetMapping("/checkUsernameAvailability")
    public Map<String, Boolean> checkUsernameAvailability(@RequestParam("username") String username) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", db.isUsernameExists(username));
        return response;
    }

    @ResponseBody
    @GetMapping("/checkEmailAvailability")
    public Map<String, Boolean> checkEmailAvailability(@RequestParam("email") String email) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", db.isEmailExists(email));
        return response;
    }

    @RequestMapping(value = "newuserregister", method = RequestMethod.POST)
    public String newUseRegister(@RequestParam("username") String username, @RequestParam("password") String password,
            @RequestParam("email") String email, @RequestParam("address") String address) {
        db.addUser(username, password, email, address);
        return "redirect:/";
    }

    @GetMapping("clearcart")
    public String clearcart() {
        User user = db.getUserByUsername(usernameforclass);
        if (user != null) {
            db.clearCart(user.getUserId());
        }
        return "redirect:/cart";
    }

    @GetMapping("clearcustomcart")
    public String clearCustomCart() {
        User user = db.getUserByUsername(usernameforclass);
        if (user != null) {
            db.clearCustomCart(user.getUserId());
        }
        return "redirect:/customCart";
    }

    @GetMapping("movecustomtocart")
    public String moveCustomToCart(Model model) {
        User user = db.getUserByUsername(usernameforclass);
        if (user != null) {
            db.moveCustomCartToCart(user.getUserId());
        }
        return "redirect:/cart";
    }

    @GetMapping("deleteitem")
    public String deleteItemFromCart(@RequestParam("productID") int productID) {
        User user = db.getUserByUsername(usernameforclass);
        if (user != null) {
            db.deleteCartItem(user.getUserId(), productID);
        }
        return "redirect:/cart";
    }

    @GetMapping("deletecustom")
    public String deleteFromCustomCart(@RequestParam("productID") int productID) {
        User user = db.getUserByUsername(usernameforclass);
        if (user != null) {
            db.deleteCustomCartItem(user.getUserId(), productID);
        }
        return "redirect:/custom-cart";
    }

    @GetMapping("addtocart")
    public String addItemToCart(@RequestParam("productID") int productID, @RequestParam("quantity") int quantity) {
        User user = db.getUserByUsername(usernameforclass);
        if (user != null) {
            db.addToCart(user.getUserId(), productID, quantity);
        }
        return "redirect:/cart";
    }

    @GetMapping("addtocustomcart")
    public String addItemToCustomCart(@RequestParam("productID") int productID,
            @RequestParam("quantity") int quantity) {
        User user = db.getUserByUsername(usernameforclass);
        if (user != null) {
            db.addToCustomCart(user.getUserId(), productID, quantity);
        }
        return "redirect:/custom-cart";
    }

    @RequestMapping(value = "/buyCart", method = RequestMethod.POST)
    public String buyCart() {
        User user = db.getUserByUsername(usernameforclass);
        if (user != null) {
            int transactionID = db.getMaxTransactionId(user.getUserId()) + 1;
            Set<Integer> products = new HashSet<>();

            List<Cart> cartItems = db.getCartByUserId(user.getUserId());

            for (Cart cart : cartItems) {
                db.addTransactionHistory(user.getUserId(), cart.getProductId(),
                        cart.getQuantity(), transactionID);
                if (cart.getProductId() != 0) {
                    products.add(cart.getProductId());
                }
            }

            if (products.size() >= 2) {
                for (int p : products) {
                    for (int q : products) {
                        if (p != q) {
                            ProductMatrix pm = db.getProductMatrix(p);
                            if (pm != null) {
                                int oldCount = pm.getPairCount(q);
                                db.updateProductMatrixPair(p, q, oldCount + 1);
                            }
                        }
                    }
                }
            }

            adminController.updateProductPairs();
            adminController.updateProductStockFromCart(usernameforclass);
            adminController.updateUserCouponsFromCart(usernameforclass);
            adminController.updateUserTotalAndCoupons(usernameforclass);
            clearcart();
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        int userId = adminController.getUserID();

        List<Cart> carts = db.getCartByUserId(userId);
        for (Cart cart : carts) {
            if (cart.getProductId() != 0) {
                Product product = db.getProductById(cart.getProductId());
                if (product != null) {
                    float totalPrice = adminController.getProductPrice(cart.getProductId(), cart.getQuantity());
                    cartItems.add(new CartItem(product.getName(), cart.getQuantity(),
                            totalPrice, cart.getProductId()));
                }
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", adminController.getCartPrice(usernameforclass));
        return "cart";
    }

    @GetMapping("updateCartItemQuantity")
    public String updateCartItemQuantity(@RequestParam MultiValueMap<String, String> params) {
        int userId = adminController.getUserID();
        for (String key : params.keySet()) {
            if (key.matches(".+\\|quantity")) {
                String productIDString = key.substring(0, key.indexOf('|'));
                String quantityString = params.getFirst(key);
                int productID = Integer.parseInt(productIDString);
                int quantity = Integer.parseInt(quantityString);

                if (quantity != 0) {
                    db.updateCartItemQuantity(userId, productID, quantity);
                } else {
                    addItemToCart(productID, quantity);
                }
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/custom-cart")
    public String viewCustomCart(Model model) {
        ArrayList<CustomCartItem> customCartItems = new ArrayList<>();
        int userId = adminController.getUserID();

        List<CustomCart> customCarts = db.getCustomCartByUserId(userId);
        for (CustomCart cart : customCarts) {
            if (cart.getProductId() != 0) {
                Product product = db.getProductById(cart.getProductId());
                if (product != null) {
                    float totalPrice = adminController.getProductPrice(cart.getProductId(), cart.getQuantity());
                    customCartItems.add(new CustomCartItem(product.getName(), cart.getQuantity(),
                            totalPrice, cart.getProductId()));
                }
            }
        }

        model.addAttribute("customCartItems", customCartItems);
        model.addAttribute("total", adminController.getCustomCartPrice(usernameforclass));
        return "custom-cart";
    }

    @GetMapping("updateCustomCartItemQuantity")
    public String updateCustomCartItemQuantity(@RequestParam MultiValueMap<String, String> params) {
        int userId = adminController.getUserID();
        for (String key : params.keySet()) {
            if (key.matches(".+\\|quantity")) {
                String productIDString = key.substring(0, key.indexOf('|'));
                String quantityString = params.getFirst(key);
                int productID = Integer.parseInt(productIDString);
                int quantity = Integer.parseInt(quantityString);

                if (quantity != 0) {
                    db.updateCustomCartItemQuantity(userId, productID, quantity);
                } else {
                    addItemToCustomCart(productID, quantity);
                }
            }
        }
        return "redirect:/custom-cart";
    }
}
