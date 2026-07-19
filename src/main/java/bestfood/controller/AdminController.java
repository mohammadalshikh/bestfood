package bestfood.controller;

import java.text.DecimalFormat;
import java.util.*;

import bestfood.model.*;
import bestfood.service.JsonDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    private JsonDatabaseService db;

    int adminlogcheck = 0;
    public static String usernameforclass = "";

    public static void setUsername(String usernameforclass) {
        AdminController.usernameforclass = usernameforclass;
    }

    @RequestMapping(value = { "/", "/logout" })
    public String returnIndex() {
        adminlogcheck = 0;
        usernameforclass = "";
        UserController.setUsername("");
        return "userLogin";
    }

    @GetMapping("/index")
    public String index(Model model) {
        if (usernameforclass.equalsIgnoreCase(""))
            return "userLogin";
        else {
            model.addAttribute("username", usernameforclass);
            UserController.setUsername(usernameforclass);
            return "index";
        }

    }

    @GetMapping("/userloginvalidate")
    public String userlog(Model model) {

        return "userLogin";
    }

    @RequestMapping(value = "userloginvalidate", method = RequestMethod.POST)
    public String userlogin(@RequestParam("username") String username, @RequestParam("password") String pass,
            Model model) {
        User user = db.getUserByUsernameAndPassword(username, pass);
        if (user != null) {
            usernameforclass = user.getUsername();
            UserController.setUsername(usernameforclass);
            AdminController.setUsername(usernameforclass);
            return "redirect:/index";
        } else {
            model.addAttribute("failMessage", "Invalid Username or Password");
            return "userLogin";
        }
    }

    public int getUserID() {
        User user = db.getUserByUsername(usernameforclass);
        return user != null ? user.getUserId() : 0;
    }

    @GetMapping("/admin")
    public String adminlogin(Model model) {

        return "adminlogin";
    }

    @GetMapping("/adminhome")
    public String adminHome(Model model) {
        if (adminlogcheck != 0)
            return "adminHome";
        else
            return "redirect:/admin";
    }

    @GetMapping("/loginvalidate")
    public String adminlog(Model model) {

        return "adminlogin";
    }

    @RequestMapping(value = "loginvalidate", method = RequestMethod.POST)
    public String adminlogin(@RequestParam("username") String username, @RequestParam("password") String pass,
            Model model) {

        if (username.equalsIgnoreCase("admin") && pass.equalsIgnoreCase("123")) {
            adminlogcheck = 1;
            return "redirect:/adminhome";
        } else {
            model.addAttribute("message", "Invalid Username or Password");
            return "adminlogin";
        }
    }

    @GetMapping("/admin/categories")
    public String getcategory(Model model) {
        List<Category> categories = db.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories";
    }

    @RequestMapping(value = "admin/sendcategory", method = RequestMethod.GET)
    public String addcategorytodb(@RequestParam("categoryname") String catname) {
        db.addCategory(catname);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete")
    public String removeCategoryDb(@RequestParam("id") int id) {
        db.deleteCategory(id);
        updateProductPairs();
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update")
    public String updateCategoryDb(@RequestParam("categoryid") int id,
            @RequestParam("categoryname") String categoryname) {
        db.updateCategory(id, categoryname);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/products")
    public String getproduct(Model model) {
        List<Product> products = db.getAllProducts();
        List<Category> categories = db.getAllCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String addproduct(Model model) {
        List<Category> categories = db.getAllCategories();
        List<Product> products = db.getAllProducts();
        int nextId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
        model.addAttribute("categories", categories);
        model.addAttribute("nextProductId", nextId);
        return "productsAdd";
    }

    @GetMapping("/admin/products/update")
    public String updateproduct(@RequestParam("pid") int id, Model model) {
        Product product = db.getProductById(id);
        if (product != null) {
            model.addAttribute("pid", product.getId());
            model.addAttribute("pname", product.getName());
            model.addAttribute("pimage", product.getImage());
            Category category = db.getCategoryById(product.getCategoryId());
            if (category != null) {
                model.addAttribute("pcategory", category.getName());
            }
            model.addAttribute("pquantity", product.getQuantity());
            model.addAttribute("pprice", product.getPrice());
            model.addAttribute("pweight", product.getWeight());
            model.addAttribute("pdescription", product.getDescription());
            model.addAttribute("pdiscount", product.getDiscount());
        }
        return "productsUpdate";
    }

    @RequestMapping(value = "admin/products/updateData", method = RequestMethod.POST)
    public String updateproducttodb(@RequestParam("id") int id, @RequestParam("name") String name,
            @RequestParam("price") float price, @RequestParam("weight") int weight,
            @RequestParam("quantity") int quantity, @RequestParam("description") String description,
            @RequestParam("productImage") String picture, @RequestParam("discount") double discount) {
        db.updateProduct(id, name, picture, quantity, price, weight, description, discount);
        return "redirect:/admin/products";
    }

    public void updateProductStockFromCart(String username) {
        User user = db.getUserByUsername(username);
        if (user != null) {
            List<Cart> cartItems = db.getCartByUserId(user.getUserId());
            for (Cart cart : cartItems) {
                if (cart.getProductId() != 0) {
                    Product product = db.getProductById(cart.getProductId());
                    if (product != null) {
                        int newQuantity = product.getQuantity() - cart.getQuantity();
                        db.updateProductQuantity(product.getId(), newQuantity);
                    }
                }
            }
        }
    }

    public void updateUserCouponsFromCart(String username) {
        User user = db.getUserByUsername(username);
        if (user != null) {
            Cart couponCart = db.getCartItem(user.getUserId(), 0);
            if (couponCart != null) {
                int newCoupons = user.getCoupons() - couponCart.getQuantity();
                db.updateUserCoupons(user.getUserId(), newCoupons);
            }
        }
    }

    public void updateUserTotalAndCoupons(String username) {
        User user = db.getUserByUsername(username);
        if (user != null) {
            float currentTransaction = getOrderTotal(username);
            float cumulativeTotal = user.getCumulativeTotal() + currentTransaction;

            if ((int) cumulativeTotal / 100 != 0) {
                int newCoupons = (int) cumulativeTotal / 100;
                cumulativeTotal = cumulativeTotal % 100;
                db.updateUserCoupons(user.getUserId(), user.getCoupons() + newCoupons);
            }

            db.updateUserCumulativeTotal(user.getUserId(), cumulativeTotal);
        }
    }

    public int getCouponsForUser(String username) {
        User user = db.getUserByUsername(username);
        return user != null ? user.getCoupons() : 0;
    }

    @RequestMapping(value = "/applyCoupon", method = RequestMethod.POST)
    public String applyCoupon(@RequestParam("apply") int coupons) {
        int userId = getUserID();
        if (getCouponsApplied(usernameforclass) != 0) {
            db.updateCartItemQuantity(userId, 0, coupons);
        } else {
            db.addToCart(userId, 0, coupons);
        }
        return "redirect:/buy";
    }

    public int getCouponsApplied(String username) {
        User user = db.getUserByUsername(username);
        if (user != null) {
            Cart couponCart = db.getCartItem(user.getUserId(), 0);
            return couponCart != null ? couponCart.getQuantity() : 0;
        }
        return 0;
    }

    @GetMapping("/admin/products/delete")
    public String removeProductDb(@RequestParam("id") int id) {
        db.deleteProduct(id);
        updateProductPairs();
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products")
    public String postproduct() {
        return "redirect:/admin/categories";
    }

    @RequestMapping(value = "admin/products/sendData", method = RequestMethod.POST)
    public String addproducttodb(@RequestParam("name") String name, @RequestParam("categoryid") String catid,
            @RequestParam("price") float price, @RequestParam("weight") int weight,
            @RequestParam("quantity") int quantity, @RequestParam("description") String description,
            @RequestParam("productImage") String picture, @RequestParam("discount") double discount) {
        Category category = db.getCategoryByName(catid);
        if (category != null) {
            db.addProduct(name, picture, category.getCategoryId(), quantity, price, weight, description, discount);
        }
        return "redirect:/admin/products";
    }

    public void updateProductPairs() {
        List<ProductMatrix> matrices = db.getAllProductMatrices();
        for (ProductMatrix matrix : matrices) {
            Map.Entry<Integer, Integer> maxPair = db.getMaxPairForProduct(matrix.getProduct());
            if (maxPair != null) {
                db.updateProductPair(matrix.getProduct(), maxPair.getKey());
            }
        }
    }

    @GetMapping("/suggestItem")
    public String suggestItem(@RequestParam("productID") int productID, @RequestParam("suggestedID") int suggestedID) {
        db.updateProductSuggestedItem(productID, suggestedID);
        return "redirect:/admin/products";
    }

    public float getProductPrice(int productID, int quantity) {
        Product product = db.getProductById(productID);
        if (product != null) {
            float productPrice = product.getPrice();
            double discountFromPrice = 1 - product.getDiscount();
            productPrice *= quantity;
            productPrice *= discountFromPrice;
            return productPrice;
        }
        return 0;
    }

    public float getCartPrice(String username) {
        float runningTotal = 0;
        User user = db.getUserByUsername(username);
        if (user != null) {
            List<Cart> cartItems = db.getCartByUserId(user.getUserId());
            for (Cart cart : cartItems) {
                runningTotal += getProductPrice(cart.getProductId(), cart.getQuantity());
            }
        }
        return runningTotal < 0 ? 0 : runningTotal;
    }

    public float getCustomCartPrice(String username) {
        float runningTotal = 0;
        User user = db.getUserByUsername(username);
        if (user != null) {
            List<CustomCart> customCartItems = db.getCustomCartByUserId(user.getUserId());
            for (CustomCart cart : customCartItems) {
                runningTotal += getProductPrice(cart.getProductId(), cart.getQuantity());
            }
        }
        return runningTotal < 0 ? 0 : runningTotal;
    }

    public float getTotalAfterTexesNoCoup(String username) {
        double cartPrice = getCartPrice(username);
        float totalAfterTexesNoCoup = (float) (cartPrice * 1.15);

        return totalAfterTexesNoCoup;
    }

    public float getOrderTotal(String username) {
        float orderTotal = getCartPrice(username);
        orderTotal *= 1.15;

        int noOfCoupons = getCouponsApplied(username);
        User user = db.getUserByUsername(username);

        if (user != null) {
            for (int i = 0; i < noOfCoupons; i++) {
                orderTotal -= 5;
                if (orderTotal <= 0) {
                    db.updateCartItemQuantity(user.getUserId(), 0, i);
                    orderTotal = 0;
                    break;
                }
            }
        }
        return orderTotal;
    }

    @GetMapping("/admin/customers")
    public String getCustomerDetail(Model model) {
        model.addAttribute("users", db.getAllUsers());
        return "displayCustomers";
    }

    @RequestMapping(value = "updateuser", method = RequestMethod.POST)
    public String updateUserProfile(@RequestParam("userid") int userid, @RequestParam("username") String username,
            @RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam("address") String address) {
        db.updateUser(userid, username, email, password, address);
        usernameforclass = username;
        return "redirect:/index";
    }

    @GetMapping("profileDisplay")
    public String profileDisplay(Model model) {
        User user = db.getUserByUsername(usernameforclass);
        if (user != null) {
            String displaytotal = new DecimalFormat("0.00").format(user.getCumulativeTotal());
            model.addAttribute("userid", user.getUserId());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("password", user.getPassword());
            model.addAttribute("address", user.getAddress());
            model.addAttribute("userCoupons", user.getCoupons());
            model.addAttribute("cumulativeTotal", displaytotal);
        }
        return "updateProfile";
    }

}
