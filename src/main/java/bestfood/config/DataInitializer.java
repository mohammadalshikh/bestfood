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

    public DataInitializer(
            ProductService productService,
            CategoryService categoryService) {

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

        Category fruits = categoryService.addCategory("Fruits");

        Category vegetables = categoryService.addCategory("Vegetables");

        Category meat = categoryService.addCategory("Meat");

        productService.addProduct(
                "Apple",
                "/images/apple.png",
                fruits.getCategoryId(),
                100,
                2.99f,
                1,
                "Fresh apples",
                0);

        productService.addProduct(
                "Banana",
                "/images/banana.png",
                fruits.getCategoryId(),
                100,
                1.99f,
                1,
                "Fresh bananas",
                0);

        productService.addProduct(
                "Cherry",
                "/images/cherry.png",
                fruits.getCategoryId(),
                100,
                4.99f,
                1,
                "Fresh cherries",
                0);

        productService.addProduct(
                "Grapes",
                "/images/grapes.png",
                fruits.getCategoryId(),
                100,
                3.99f,
                1,
                "Fresh grapes",
                0);

        productService.addProduct(
                "Orange",
                "/images/orange.png",
                fruits.getCategoryId(),
                100,
                2.49f,
                1,
                "Fresh oranges",
                0);

        productService.addProduct(
                "Pineapple",
                "/images/pineapple.png",
                fruits.getCategoryId(),
                50,
                5.99f,
                2,
                "Fresh pineapple",
                0);

        productService.addProduct(
                "Watermelon",
                "/images/watermelon.png",
                fruits.getCategoryId(),
                50,
                7.99f,
                5,
                "Fresh watermelon",
                0);

        productService.addProduct(
                "Corn",
                "/images/corn.png",
                vegetables.getCategoryId(),
                100,
                1.49f,
                1,
                "Fresh corn",
                0);

        productService.addProduct(
                "Cucumber",
                "/images/cucumber.png",
                vegetables.getCategoryId(),
                100,
                1.29f,
                1,
                "Fresh cucumbers",
                0);

        productService.addProduct(
                "Lettuce",
                "/images/lettuce.png",
                vegetables.getCategoryId(),
                100,
                2.29f,
                1,
                "Fresh lettuce",
                0);

        productService.addProduct(
                "Onion",
                "/images/onion.png",
                vegetables.getCategoryId(),
                100,
                1.19f,
                1,
                "Fresh onions",
                0);

        productService.addProduct(
                "Potato",
                "/images/potato.png",
                vegetables.getCategoryId(),
                100,
                1.99f,
                2,
                "Fresh potatoes",
                0);

        productService.addProduct(
                "Tomato",
                "/images/tomato.png",
                vegetables.getCategoryId(),
                100,
                2.49f,
                1,
                "Fresh tomatoes",
                0);

        productService.addProduct(
                "Beef",
                "/images/beef.png",
                meat.getCategoryId(),
                50,
                12.99f,
                5,
                "Fresh beef",
                0);

        System.out.println("BestFood database initialized successfully.");
    }
}