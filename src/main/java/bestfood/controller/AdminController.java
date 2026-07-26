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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupabaseStorageService storageService;

    @GetMapping("/admin/login")
    public String adminLoginPage(Authentication auth) {

        if (auth != null && auth.isAuthenticated()) {

            if (auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

                return "redirect:/admin/home";
            }

            SecurityContextHolder.clearContext();
        }

        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String adminLogIn(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        Model model) {

        SecurityContextHolder.clearContext();

        User user = userService.authenticate(username, password);

        if (user != null && user.getRole().equals("ROLE_ADMIN")) {

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                String.valueOf(user.getId()),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            return "redirect:/admin/home";
        }

        model.addAttribute("errorMessage", "Invalid admin credentials");
        return "admin/login";
    }

    @GetMapping("/admin/logout")
    public String adminLogOut() {

        SecurityContextHolder.clearContext();

        return "redirect:/admin/login";
    }

    @GetMapping("/admin/home")
    public String adminHome(Model model) {

        return "admin/home";
    }

    @GetMapping("/admin/categories")
    public String categoriesPage(Model model) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "admin/categories";
    }

    @PostMapping("/admin/categories")
    public String createCategory(@RequestParam("category-name") String categoryName) {

        categoryService.createCategory(categoryName);

        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}")
    public String updateCategory(@PathVariable Integer id, @RequestParam("category-name") String categoryName) {

        categoryService.updateCategory(id, categoryName);

        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/products")
    public String productsPage(Model model) {

        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "admin/products/view";
    }

    @GetMapping("/admin/products/create")
    public String productsCreatePage(Model model) {

        model.addAttribute("categories", categoryService.getAllCategoriesExceptCoupons());

        return "admin/products/create";
    }

    @GetMapping("/admin/products/{id}/update")
    public String productsUpdatePage(@PathVariable Integer id, Model model) {

        Product product = productService.getProductById(id);

        if (product != null) {
            model.addAttribute("product", product);
        }
        model.addAttribute("categories", categoryService.getAllCategoriesExceptCoupons());

        return "admin/products/update";
    }

    @PostMapping("/admin/products/create")
    public String createProduct(
        @RequestParam("product-name") String name,
        @RequestParam("product-category-id") Integer categoryId,
        @RequestParam("product-price") float price,
        @RequestParam("product-weight") int weight,
        @RequestParam("product-quantity") int quantity,
        @RequestParam("product-description") String description,
        @RequestParam("product-image-file") MultipartFile file,
        @RequestParam("product-discount") double discount)
        throws IOException {

        String imagePath = storageService.upload(file);

        Category category = categoryService.getCategoryById(categoryId);

        if (category != null) {
            productService.createProduct(
                name,
                imagePath,
                category.getId(),
                quantity,
                price,
                weight,
                description,
                discount
            );
        }

        return "redirect:/admin/products";
    }
    
    @PostMapping("/admin/products/{id}/update")
    public String updateProduct(
        @PathVariable Integer id,
        @RequestParam("product-name") String name,
        @RequestParam("product-category-id") Integer categoryId,
        @RequestParam("product-price") float price,
        @RequestParam("product-weight") int weight,
        @RequestParam("product-quantity") int quantity,
        @RequestParam("product-description") String description,
        @RequestParam(value = "product-image-file", required = false) MultipartFile file,
        @RequestParam(value = "product-old-image-path", required = false) String oldImagePath,
        @RequestParam("product-discount") double discount) throws IOException {

        String imagePath = oldImagePath;

        if (file != null && !file.isEmpty()) {
            imagePath = storageService.upload(file);
        }

        Category category = categoryService.getCategoryById(categoryId);

        if (category != null) {
            
            productService.updateProduct(
                id, name, categoryId, imagePath, quantity, price, weight, description, discount
            );
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/users")
    public String usersPage(Model model) {

        model.addAttribute("users", userService.getAllUsers());

        return "admin/users";
    }

}