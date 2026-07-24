package bestfood.service;

import bestfood.model.Category;
import bestfood.model.Product;
import bestfood.repo.CategoryRepo;
import bestfood.repo.ProductRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    public CategoryService(CategoryRepo categoryRepo, ProductRepo productRepo) {

        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    public List<Category> getAllCategories() {

        return categoryRepo.findAll();
    }

    public Category getCategoryById(int categoryId) {

        return categoryRepo
            .findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category getCategoryByName(String name) {

        return categoryRepo.findByName(name);
    }

    public Category createCategory(String name) {

        Category category = new Category();

        category.setName(name);

        return categoryRepo.save(category);
    }

    public Category updateCategory(int categoryId, String name) {

        Category category = getCategoryById(categoryId);

        if (category == null) {
            return null;
        }

        category.setName(name);

        return categoryRepo.save(category);
    }

    public void deleteCategory(int categoryId) {

        Category category = getCategoryById(categoryId);

        if (category == null) {
            return;
        }

        List<Product> products = productRepo.findAll();

        for (Product product : products) {

            if (product.getCategory() != null && product.getCategory().getId().equals(categoryId)) {

                productRepo.delete(product);
            }
        }

        categoryRepo.delete(category);
    }

}