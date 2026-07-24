package bestfood.config;

import bestfood.model.Category;
import bestfood.service.CategoryService;
import bestfood.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductService productService;
    private final CategoryService categoryService;

    public DataInitializer(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) {

        if (!productService.getAllProducts().isEmpty()) {
            System.out.println("Database already initialized.");
            return;
        }

        System.out.println("Initializing BestFood database...");

        Category fruits = categoryService.createCategory("Fruits");
        Category vegetables = categoryService.createCategory("Vegetables");
        Category meat = categoryService.createCategory("Meat");

        productService.createProduct(
            "Apple",
            "/images/apple.png",
            fruits.getId(),
            100,
            2.99f,
            1,
            "Fresh apples",
            0
        );

        productService.createProduct(
            "Banana",
            "/images/banana.png",
            fruits.getId(),
            100,
            1.99f,
            1,
            "Fresh bananas",
            0
        );

        productService.createProduct(
            "Cherry",
            "/images/cherry.png",
            fruits.getId(),
            100,
            4.99f,
            1,
            "Fresh cherries",
            0
        );

        productService.createProduct(
            "Grapes",
            "/images/grapes.png",
            fruits.getId(),
            100,
            3.99f,
            1,
            "Fresh grapes",
            0
        );

        productService.createProduct(
            "Orange",
            "/images/orange.png",
            fruits.getId(),
            100,
            2.49f,
            1,
            "Fresh oranges",
            0
        );

        productService.createProduct(
            "Pineapple",
            "/images/pineapple.png",
            fruits.getId(),
            50,
            5.99f,
            2,
            "Fresh pineapple",
            0
        );

        productService.createProduct(
            "Watermelon",
            "/images/watermelon.png",
            fruits.getId(),
            50,
            7.99f,
            5,
            "Fresh watermelon",
            0
        );

        productService.createProduct(
            "Corn",
            "/images/corn.png",
            vegetables.getId(),
            100,
            1.49f,
            1,
            "Fresh corn",
            0
        );

        productService.createProduct(
            "Cucumber",
            "/images/cucumber.png",
            vegetables.getId(),
            100,
            1.29f,
            1,
            "Fresh cucumbers",
            0
        );

        productService.createProduct(
            "Lettuce",
            "/images/lettuce.png",
            vegetables.getId(),
            100,
            2.29f,
            1,
            "Fresh lettuce",
            0
        );

        productService.createProduct(
            "Onion",
            "/images/onion.png",
            vegetables.getId(),
            100,
            1.19f,
            1,
            "Fresh onions",
            0
        );

        productService.createProduct(
            "Potato",
            "/images/potato.png",
            vegetables.getId(),
            100,
            1.99f,
            2,
            "Fresh potatoes",
            0
        );

        productService.createProduct(
            "Tomato",
            "/images/tomato.png",
            vegetables.getId(),
            100,
            2.49f,
            1,
            "Fresh tomatoes",
            0
        );

        productService.createProduct(
            "Beef",
            "/images/beef.png",
            meat.getId(),
            50,
            12.99f,
            5,
            "Fresh beef",
            0
        );

        System.out.println("BestFood database initialized successfully.");
    }
    
}