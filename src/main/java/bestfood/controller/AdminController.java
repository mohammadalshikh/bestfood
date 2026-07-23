package bestfood.controller;

import bestfood.model.*;
import bestfood.service.*;

import java.text.DecimalFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomCartService customCartService;

    @Autowired
    private ProductMatrixService productMatrixService;

    @Autowired
    private SupabaseStorageService storageService;

    @GetMapping("/admin/login")
    public String adminLoginPage() {

        return "admin/login";
    }
    

    @GetMapping("/admin/categories")
    public String categories(Model model) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "categories";
    }

    @PostMapping("/admin/categories")
    public String addCategory(@RequestParam("categoryname") String categoryName) {

        categoryService.addCategory(categoryName);

        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}/delete")
    public String deleteCategory(@PathVariable int id) {

        categoryService.deleteCategory(id);
        updateProductPairs();

        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}")
    public String updateCategory(@PathVariable int id, @RequestParam("categoryname") String categoryName) {

        categoryService.updateCategory(id, categoryName);

        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/products")
    public String getProducts(Model model) {

        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "products";
    }

    @GetMapping("/admin/products/new")
    public String getAddProduct(Model model) {

        List<Category> categories = categoryService.getAllCategories();
        List<Product> products = productService.getAllProducts();

        int nextId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;

        model.addAttribute("categories", categories);
        model.addAttribute("nextProductId", nextId);

        return "product-add";
    }

    @GetMapping("/admin/products/update")
    public String updateproduct(
            @RequestParam("pid") int id,
            Model model) {

        Product product = productService.getProductById(id);

        if (product != null) {

            model.addAttribute(
                    "pid",
                    product.getId());

            model.addAttribute(
                    "pname",
                    product.getName());

            model.addAttribute(
                    "pimage",
                    product.getImage());

            if (product.getCategory() != null) {

                model.addAttribute(
                        "pcategory",
                        product.getCategory().getName());
            }

            model.addAttribute(
                    "pquantity",
                    product.getQuantity());

            model.addAttribute(
                    "pprice",
                    product.getPrice());

            model.addAttribute(
                    "pweight",
                    product.getWeight());

            model.addAttribute(
                    "pdescription",
                    product.getDescription());

            model.addAttribute(
                    "pdiscount",
                    product.getDiscount());
        }

        return "productsUpdate";
    }

    @RequestMapping(value = "admin/products/updateData", method = RequestMethod.POST)
    public String updateproducttodb(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("price") float price,
            @RequestParam("weight") int weight,
            @RequestParam("quantity") int quantity,
            @RequestParam("description") String description,
            @RequestParam(value = "productImage", required = false) MultipartFile file,
            @RequestParam(value = "oldImagePath", required = false) String oldImagePath,
            @RequestParam("discount") double discount)
            throws java.io.IOException {

        String imagePath = oldImagePath;

        if (file != null && !file.isEmpty()) {
            imagePath = storageService.upload(file);
        }

        productService.updateProduct(
                id,
                name,
                imagePath,
                quantity,
                price,
                weight,
                description,
                discount);

        return "redirect:/admin/products";
    }

    public void updateProductStockFromCart(String username) {

        User user = userService.getUserByUsername(username);

        if (user != null) {

            List<Cart> cartItems = cartService.getCartByUserId(
                    user.getUserId());

            for (Cart cart : cartItems) {

                if (cart.getProduct() != null) {

                    Product product = productService.getProductById(
                            cart.getProduct().getId());

                    if (product != null) {

                        int newQuantity = product.getQuantity()
                                - cart.getQuantity();

                        productService.updateProductQuantity(
                                product.getId(),
                                newQuantity);
                    }
                }
            }
        }
    }

    public void updateUserCouponsFromCart(String username) {

        User user = userService.getUserByUsername(username);

        if (user != null) {

            Cart couponCart = cartService.getCartItem(
                    user.getUserId(),
                    0);

            if (couponCart != null) {

                int newCoupons = user.getCoupons()
                        - couponCart.getQuantity();

                user.setCoupons(newCoupons);

                userService.saveUser(user);
            }
        }
    }

    public void updateUserTotalAndCoupons(String username) {

        User user = userService.getUserByUsername(username);

        if (user != null) {

            float currentTransaction = getOrderTotal(username);

            float cumulativeTotal = user.getCumulativeTotal()
                    + currentTransaction;

            if ((int) cumulativeTotal / 100 != 0) {

                int newCoupons = (int) cumulativeTotal / 100;

                cumulativeTotal = cumulativeTotal % 100;

                user.setCoupons(
                        user.getCoupons()
                                + newCoupons);
            }

            user.setCumulativeTotal(
                    cumulativeTotal);

            userService.saveUser(user);
        }
    }

    public int getCouponsForUser(String username) {

        User user = userService.getUserByUsername(username);

        return user != null
                ? user.getCoupons()
                : 0;
    }

    @RequestMapping(value = "/applyCoupon", method = RequestMethod.POST)
    public String applyCoupon(
            @RequestParam("apply") int coupons) {

        int userId = getUserID();

        User user = userService.getUserByUsername(
                usernameForClass);

        if (user == null
                || coupons > user.getCoupons()
                || coupons < 0) {

            return "redirect:/buy";
        }

        Cart couponCart = cartService.getCartItem(
                userId,
                0);

        if (couponCart != null) {

            cartService.updateCartItemQuantity(
                    userId,
                    0,
                    coupons);

        } else {

            cartService.addItemToCart(
                    userId,
                    0,
                    coupons);
        }

        return "redirect:/buy";
    }

    public int getCouponsApplied(String username) {

        User user = userService.getUserByUsername(username);

        if (user != null) {

            Cart couponCart = cartService.getCartItem(
                    user.getUserId(),
                    0);

            return couponCart != null
                    ? couponCart.getQuantity()
                    : 0;
        }

        return 0;
    }

    @GetMapping("/admin/products/delete")
    public String removeProductDb(
            @RequestParam("id") int id) {

        productService.deleteProduct(id);

        updateProductPairs();

        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products")
    public String postproduct() {

        return "redirect:/admin/categories";
    }

    @RequestMapping(value = "admin/products/sendData", method = RequestMethod.POST)
    public String addproducttodb(
            @RequestParam("name") String name,
            @RequestParam("categoryid") String catid,
            @RequestParam("price") float price,
            @RequestParam("weight") int weight,
            @RequestParam("quantity") int quantity,
            @RequestParam("description") String description,
            @RequestParam("productImage") MultipartFile file,
            @RequestParam("discount") double discount)
            throws java.io.IOException {

        String imagePath = storageService.upload(file);

        Category category = categoryService.getCategoryByName(catid);

        if (category != null) {

            productService.addProduct(
                    name,
                    imagePath,
                    category.getCategoryId(),
                    quantity,
                    price,
                    weight,
                    description,
                    discount);
        }

        return "redirect:/admin/products";
    }

    public void updateProductPairs() {

        List<ProductMatrix> matrices = productMatrixService.getAllProductMatrices();

        for (ProductMatrix matrix : matrices) {

            ProductPair maxPair = productMatrixService.getMaxPairForProduct(
                    matrix.getProduct().getId());

            if (maxPair != null) {

                productService.updateProductPair(
                        matrix.getProduct().getId(),
                        maxPair.getPairedProduct().getId());
            }
        }
    }

    @GetMapping("/suggestItem")
    public String suggestItem(
            @RequestParam("productID") int productID,
            @RequestParam("suggestedID") int suggestedID) {

        productService.updateProductSuggestedItem(
                productID,
                suggestedID);

        return "redirect:/admin/products";
    }

    public float getProductPrice(
            int productID,
            int quantity) {

        Product product = productService.getProductById(productID);

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

        User user = userService.getUserByUsername(username);

        if (user != null) {

            List<Cart> cartItems = cartService.getCartByUserId(
                    user.getUserId());

            for (Cart cart : cartItems) {

                if (cart.getProduct() != null) {

                    runningTotal += getProductPrice(
                            cart.getProduct().getId(),
                            cart.getQuantity());
                }
            }
        }

        return runningTotal < 0
                ? 0
                : runningTotal;
    }

    public float getCustomCartPrice(String username) {

        float runningTotal = 0;

        User user = userService.getUserByUsername(username);

        if (user != null) {

            List<CustomCart> customCartItems = customCartService.getCustomCartByUserId(
                    user.getUserId());

            for (CustomCart cart : customCartItems) {

                if (cart.getProduct() != null) {

                    runningTotal += getProductPrice(
                            cart.getProduct().getId(),
                            cart.getQuantity());
                }
            }
        }

        return runningTotal < 0
                ? 0
                : runningTotal;
    }

    public float getTotalAfterTexesNoCoup(String username) {

        double cartPrice = getCartPrice(username);

        return (float) (cartPrice * 1.15);
    }

    public float getOrderTotal(String username) {

        float orderTotal = getCartPrice(username);

        orderTotal *= 1.15;

        int noOfCoupons = getCouponsApplied(username);

        User user = userService.getUserByUsername(username);

        if (user != null) {

            for (int i = 0; i < noOfCoupons; i++) {

                orderTotal -= 5;

                if (orderTotal <= 0) {

                    cartService.updateCartItemQuantity(
                            user.getUserId(),
                            0,
                            i);

                    orderTotal = 0;

                    break;
                }
            }
        }

        return orderTotal;
    }

    @GetMapping("/admin/customers")
    public String getCustomerDetail(Model model) {

        model.addAttribute(
                "users",
                userService.getAllUsers());

        return "displayCustomers";
    }

    @RequestMapping(value = "updateuser", method = RequestMethod.POST)
    public String updateUserProfile(
            @RequestParam("userid") int userid,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("address") String address) {

        User user = userService.getUserById(userid);

        if (user != null) {

            user.setUsername(username);

            user.setEmail(email);

            user.setPassword(password);

            user.setAddress(address);

            userService.saveUser(user);
        }

        usernameForClass = username;

        return "redirect:/index";
    }

    @GetMapping("profileDisplay")
    public String profileDisplay(Model model) {

        User user = userService.getUserByUsername(
                usernameForClass);

        if (user != null) {

            String displaytotal = new DecimalFormat("0.00")
                    .format(user.getCumulativeTotal());

            model.addAttribute(
                    "userid",
                    user.getUserId());

            model.addAttribute(
                    "username",
                    user.getUsername());

            model.addAttribute(
                    "email",
                    user.getEmail());

            model.addAttribute(
                    "password",
                    user.getPassword());

            model.addAttribute(
                    "address",
                    user.getAddress());

            model.addAttribute(
                    "userCoupons",
                    user.getCoupons());

            model.addAttribute(
                    "cumulativeTotal",
                    displaytotal);
        }

        return "updateProfile";
    }

}