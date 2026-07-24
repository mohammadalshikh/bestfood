package bestfood.controller;

import bestfood.model.*;
import bestfood.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
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
    private ProductMatrixService productMatrixService;

    @Autowired
    private SupabaseStorageService storageService;

    @GetMapping("/admin/login")
    public String adminLoginPage() {

        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String adminLogin(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        HttpSession session,
        Model model) {

        User admin = userService.authenticate(username, password);

        if (admin != null && admin.getRole().equals("ROLE_ADMIN")) {

            session.setAttribute("admin", admin);
            return "redirect:/admin/home";
        }

        model.addAttribute("message", "Invalid admin credentials");
        return "admin/login";
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {

        session.removeAttribute("admin");

        return "redirect:/admin/login";
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

    @PostMapping("/admin/categories/{id}/delete")
    public String deleteCategory(@PathVariable int id) {

        categoryService.deleteCategory(id);
        productMatrixService.updateProductPairs();

        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}")
    public String updateCategory(@PathVariable int id, @RequestParam("category-name") String categoryName) {

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

        List<Category> categories = categoryService.getAllCategories();
        List<Product> products = productService.getAllProducts();

        int nextId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;

        model.addAttribute("categories", categories);
        model.addAttribute("nextProductId", nextId);

        return "admin/products/create";
    }

    @GetMapping("/admin/products/{id}/update")
    public String productsUpdatePage(
        @PathVariable int id,
        Model model) {

        Product product = productService.getProductById(id);

        if (product != null) {
            model.addAttribute("product", product);
        }

        return "admin/products/update";
    }

    @PostMapping("/admin/products/create")
    public String createProduct(
        @RequestParam("product-name") String name,
        @RequestParam("product-category-id") int categoryId,
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

    @PostMapping("/admin/products/{id}/delete")
    public String deleteProduct(@PathVariable int id) {

        productService.deleteProduct(id);
        productMatrixService.updateProductPairs();

        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/{id}/update")
    public String updateProduct(
        @PathVariable int id,
        @RequestParam("product-name") String name,
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

        productService.updateProduct(id, name, imagePath, quantity, price, weight, description, discount);

        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/{id}/suggest")
    public String suggestProduct(
        @PathVariable int id,
        @RequestParam("suggested-product-id") int suggestedProductId) {

        productService.updateProductSuggestedItem(id, suggestedProductId);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/users")
    public String usersPage(Model model) {

        model.addAttribute("users", userService.getAllUsers());

        return "admin/users";
    }

}