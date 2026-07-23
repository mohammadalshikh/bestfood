package bestfood.repo;

import bestfood.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart, Integer> {

    List<Cart> findByUserUserId(int userId);

    Cart findByUserUserIdAndProductId(int userId, int productId);

    void deleteByUserUserIdAndProductId(int userId, int productId);

    void deleteByUserUserId(int userId);
}