package bestfood.repo;

import bestfood.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionHistoryRepo extends JpaRepository<TransactionHistory, Integer> {

    List<TransactionHistory> findByUserId(int userId);
    
}