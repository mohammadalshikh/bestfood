package bestfood.service;

import bestfood.model.Category;
import bestfood.model.Product;
import bestfood.repo.CategoryRepo;
import bestfood.repo.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final ProductLinkService productLinkService;
    private final ProductService productService;

    public CategoryService(
        CategoryRepo categoryRepo, 
        ProductRepo productRepo, 
        ProductLinkService productLinkService,
        ProductService productService) {

        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.productLinkService = productLinkService;
        this.productService = productService;
    }

    public List<Category> getAllCategories() {

        return categoryRepo.findAll();
    }

    public Category getCategoryById(Integer categoryId) {

        return categoryRepo
            .findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category getCategoryByName(String categoryName) {

        return categoryRepo.findByName(categoryName);
    }

    public List<Product> getAllProductsByCategoryId(Integer categoryId) {

        return productRepo.findAllByCategoryId(categoryId);

    }

    public Category createCategory(String categoryName) {

        Category category = new Category();

        category.setName(categoryName);

        return categoryRepo.save(category);
    }

    public Category updateCategory(Integer categoryId, String categoryName) {

        Category category = getCategoryById(categoryId);

        if (categoryId == 0) {
            return category;
        }

        if (category == null) {
            return null;
        }

        category.setName(categoryName);

        return categoryRepo.save(category);
    }

    @Transactional
    public void deleteCategory(Integer categoryId) {

        if (categoryId == 0) {
            return;
        }

        Category category = getCategoryById(categoryId);
        
        if (category == null) {
            return;
        }
        
        List<Product> products = getAllProductsByCategoryId(categoryId);
        Set<Integer> affectedProductIdsUnique = new HashSet<>();

        for (Product product : products) {

            affectedProductIdsUnique.addAll(productLinkService.getAffectedProductIds(product.getId()));

            productService.deleteProduct(product.getId());
        }

        categoryRepo.delete(category);

        productLinkService.updateSomeBoughtWithProductsToBest(affectedProductIdsUnique);
    }

}