package bestfood.repo;

import bestfood.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findByNameContainingIgnoreCase(String name);
    
}