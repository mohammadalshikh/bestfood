package bestfood.service;

import bestfood.model.Category;
import bestfood.model.Product;
import bestfood.repo.ProductRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductMatrixService productMatrixService;
    private final CategoryService categoryService;

    public ProductService(
        ProductRepo productRepo,
        CategoryService categoryService,
        ProductMatrixService productMatrixService) {

        this.productRepo = productRepo;
        this.productMatrixService = productMatrixService;
        this.categoryService = categoryService;
    }

    public List<Product> getAllProducts() {

        return productRepo.findAll();
    }

    public Product getProductById(int productId) {

        return productRepo.findById(productId).orElse(null);
    }

    public Product createProduct(
        String name,
        String image,
        int categoryId,
        int quantity,
        float price,
        int weight,
        String description,
        double discount) {

        Category category = categoryService.getCategoryById(categoryId);

        Product product = new Product();

        product.setName(name);
        product.setImage(image);
        product.setCategory(category);
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setWeight(weight);
        product.setDescription(description);
        product.setDiscount(discount);
        product.setProductPair(0);
        product.setSuggestedItem(0);

        return productRepo.save(product);
    }

    public Product updateProduct(
        int productId,
        String name,
        String image,
        int quantity,
        float price,
        int weight,
        String description,
        double discount) {

        Product product = getProductById(productId);

        if (product == null) {
            return null;
        }

        product.setName(name);
        product.setImage(image);
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setWeight(weight);
        product.setDescription(description);
        product.setDiscount(discount);

        return productRepo.save(product);
    }

    public Product updateProductQuantity(int productId, int quantity) {

        Product product = getProductById(productId);

        if (product == null) {
            return null;
        }

        product.setQuantity(quantity);

        return productRepo.save(product);
    }

    public Product updateProductSuggestedItem(int productId, int suggestedItemId) {

        Product product = getProductById(productId);

        if (product == null) {
            return null;
        }

        product.setSuggestedItem(suggestedItemId);

        return productRepo.save(product);
    }

    public void deleteProduct(int productId) {

        Product product = getProductById(productId);

        if (product == null) {
            return;
        }

        productMatrixService.deleteProductReferences(product);

        productRepo.delete(product);

    }

    public List<Product> searchProducts(String query) {
        return productRepo.findByNameContainingIgnoreCase(query);
    }

    public float getProductPrice(int productID, int quantity) {

        Product product = getProductById(productID);

        if (product != null) {

            float productPrice = product.getPrice();

            double discountFromPrice = 1 - product.getDiscount();

            productPrice *= quantity;
            productPrice *= discountFromPrice;

            return productPrice;
        }

        return 0;
    }

}