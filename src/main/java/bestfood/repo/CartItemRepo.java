package bestfood.repo;

import bestfood.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByUserId(Integer userId);

    CartItem findByUserIdAndProductId(Integer userId, Integer productId);

    void deleteByUserIdAndProductId(Integer userId, Integer productId);

    void deleteByUserId(Integer userId);
    
}