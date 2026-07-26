package bestfood.repo;

import bestfood.model.Product;
import bestfood.model.ProductLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductLinkRepo extends JpaRepository<ProductLink, Integer> {
    
    ProductLink findByOriginalProductAndBoughtWithProduct(Product originalProduct, Product boughtWithProduct);
    
    ProductLink findTopByOriginalProductOrderByPurchaseCountDesc(Product product);

}