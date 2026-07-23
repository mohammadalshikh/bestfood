package bestfood.repo;

import bestfood.model.Product;
import bestfood.model.ProductMatrix;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMatrixRepo
        extends JpaRepository<ProductMatrix, Integer> {

    ProductMatrix findByProduct(Product product);
}