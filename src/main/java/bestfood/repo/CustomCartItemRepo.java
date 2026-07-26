package bestfood.repo;

import bestfood.model.CustomCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomCartItemRepo extends JpaRepository<CustomCartItem, Integer> {

    List<CustomCartItem> findByUserId(Integer userId);

    CustomCartItem findByUserIdAndProductId(Integer userId, Integer productId);

    void deleteByUserIdAndProductId(Integer userId, Integer productId);

    void deleteByUserId(Integer userId);
    
}