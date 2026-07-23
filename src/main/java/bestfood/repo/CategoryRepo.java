package bestfood.repo;

import bestfood.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

    Category findByName(String name);

}