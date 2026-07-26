package bestfood.service;

import bestfood.model.Product;
import bestfood.model.TransactionHistory;
import bestfood.model.User;
import bestfood.repo.ProductRepo;
import bestfood.repo.TransactionHistoryRepo;
import bestfood.repo.UserRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionHistoryService {

    private final TransactionHistoryRepo transactionHistoryRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    public TransactionHistoryService(
        TransactionHistoryRepo transactionHistoryRepo, 
        UserRepo userRepo,
        ProductRepo productRepo) {

        this.transactionHistoryRepo = transactionHistoryRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    public int getLastBasketId(Integer userId) {

        return transactionHistoryRepo
            .findByUserId(userId).stream()
            .mapToInt(TransactionHistory::getBasketId)
            .max().orElse(0);
    }

    public void addTransactionHistory(Integer userId, Integer productId, int quantity, int basketId) {

        User user = userRepo.findById(userId).orElse(null);

        Product product = productRepo.findById(productId).orElse(null);

        if (user != null && product != null) {

            TransactionHistory history = new TransactionHistory(user, product, quantity, basketId);

            transactionHistoryRepo.save(history);
        }
    }
    
}