package bestfood.repo;

import bestfood.model.Product;
import bestfood.model.ProductLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductLinkRepo extends JpaRepository<ProductLink, Integer> {

    List<ProductLink> findByOriginalProduct(Product product);

    ProductLink findTopByOriginalProductOrderByPurchaseCountDesc(Product product);

    ProductLink findByOriginalProductAndBoughtWithProduct(Product originalProduct, Product boughtWithProduct);

    List<ProductLink> findByOriginalProductIdOrBoughtWithProductId(
        Integer originalProductId, 
        Integer boughtWithProductId
    );

}