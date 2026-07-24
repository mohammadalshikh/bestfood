package bestfood.repo;

import bestfood.model.ProductPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPairRepo extends JpaRepository<ProductPair, Integer> {}