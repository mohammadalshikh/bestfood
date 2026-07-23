package bestfood.repo;

import bestfood.model.CustomCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomCartRepo extends JpaRepository<CustomCart, Integer> {

    List<CustomCart> findByUserUserId(int userId);

    CustomCart findByUserUserIdAndProductId(int userId, int productId);

    void deleteByUserUserIdAndProductId(int userId, int productId);

    void deleteByUserUserId(int userId);
}