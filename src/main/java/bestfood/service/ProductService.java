package bestfood.service;

import bestfood.model.Category;
import bestfood.model.Product;
import bestfood.repo.ProductRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductLinkService productLinkService;
    private final CategoryService categoryService;

    public ProductService(
        ProductRepo productRepo,
        CategoryService categoryService,
        ProductLinkService productMatrixService) {

        this.productRepo = productRepo;
        this.productLinkService = productMatrixService;
        this.categoryService = categoryService;
    }

    public List<Product> getAllProductsExceptCoupon() {
        
        return productRepo.findAllByIdNot(0);
    }

    public Product getProductById(Integer productId) {

        return productRepo.findById(productId).orElse(null);
    }

    public Product createProduct(
        String name,
        String image,
        Integer categoryId,
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
        product.setBoughtWithProduct(null);

        return productRepo.save(product);
    }

    public Product updateProduct(
        Integer productId,
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
        if (productId == 0) {
            return product;
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

    public Product updateProductQuantity(Integer productId, int quantity) {

        Product product = getProductById(productId);

        if (product == null) {
            return null;
        }

        product.setQuantity(quantity);

        return productRepo.save(product);
    }

    public void deleteProduct(Integer productId) {

        if (productId == 0) {
            return;
        }

        Product product = getProductById(productId);

        if (product == null) {
            return;
        }

        productLinkService.deleteAllLinksForProduct(productId);

        productRepo.delete(product);

    }

    public List<Product> searchProducts(String query) {
        return productRepo.findByNameContainingIgnoreCase(query);
    }

    public float getProductPriceTimesQuantityTimesDiscount(Integer productID, int quantity) {

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