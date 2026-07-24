package bestfood.repo;

import bestfood.model.CustomCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomCartItemRepo extends JpaRepository<CustomCartItem, Integer> {

    List<CustomCartItem> findByUserId(int userId);

    CustomCartItem findByUserIdAndProductId(int userId, int productId);

    void deleteByUserIdAndProductId(int userId, int productId);

    void deleteByUserId(int userId);
    
}