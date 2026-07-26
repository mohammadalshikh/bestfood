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

    public CategoryService(
        CategoryRepo categoryRepo, 
        ProductRepo productRepo) {

        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    public List<Category> getAllCategories() {

        return categoryRepo.findAllByOrderByIdAsc();
    }

    public List<Category> getAllCategoriesExceptCoupons() {

        return categoryRepo.findByIdNotOrderByIdAsc(1);
    }

    public Category getCategoryById(Integer categoryId) {

        return categoryRepo
            .findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public List<Product> getAllProductsByCategoryId(Integer categoryId) {

        return productRepo.findAllByCategoryIdOrderByIdAsc(categoryId);

    }

    public Category createCategory(String categoryName) {

        Category category = new Category();

        category.setName(categoryName);

        return categoryRepo.save(category);
    }

    public Category updateCategory(Integer categoryId, String categoryName) {

        Category category = getCategoryById(categoryId);

        if (categoryId == 1) {
            return category;
        }

        if (category == null) {
            return null;
        }

        category.setName(categoryName);

        return categoryRepo.save(category);
    }

}