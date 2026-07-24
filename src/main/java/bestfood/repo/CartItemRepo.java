package bestfood.repo;

import bestfood.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByUserId(int userId);

    CartItem findByUserIdAndProductId(int userId, int productId);

    void deleteByUserIdAndProductId(int userId, int productId);

    void deleteByUserId(int userId);
    
}