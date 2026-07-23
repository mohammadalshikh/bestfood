package bestfood.repo;

import bestfood.model.Product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByNameContainingIgnoreCase(String name);
}