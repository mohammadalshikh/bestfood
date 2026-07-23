package bestfood.service;

import bestfood.model.Category;
import bestfood.model.Product;
import bestfood.repo.CategoryRepo;
import bestfood.repo.ProductRepo;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ProductMatrixService productMatrixService;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo,
            ProductMatrixService productMatrixService) {

        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.productMatrixService = productMatrixService;
    }

    public List<Product> getAllProducts() {

        return productRepo.findAll();

    }

    public Product getProductById(int productId) {

        return productRepo.findById(productId)
                .orElse(null);

    }

    public Product addProduct(String name,
            String image,
            int categoryId,
            int quantity,
            float price,
            int weight,
            String description,
            double discount) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

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

    public Product updateProduct(int productId,
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

    public Product updateProductPair(int productId, int pairId) {

        Product product = getProductById(productId);

        if (product == null) {
            return null;
        }

        product.setProductPair(pairId);

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

}