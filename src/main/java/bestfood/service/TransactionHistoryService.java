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

    public TransactionHistoryService(TransactionHistoryRepo transactionHistoryRepo,
            UserRepo userRepo,
            ProductRepo productRepo) {

        this.transactionHistoryRepo = transactionHistoryRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }
    

    public List<TransactionHistory> getAllTransactionHistory() {
        return transactionHistoryRepo.findAll();
    }

    public List<TransactionHistory> getUserTransactionHistory(int userId) {

        return transactionHistoryRepo.findByUserUserId(userId);
    }

    public int getMaxTransactionId(int userId) {

        return transactionHistoryRepo.findByUserUserId(userId)
                .stream()
                .mapToInt(TransactionHistory::getTransactionId)
                .max()
                .orElse(0);
    }

    public void addTransactionHistory(int userId, int productId, int quantity, int transactionId) {

        User user = userRepo.findById(userId)
                .orElse(null);

        Product product = productRepo.findById(productId)
                .orElse(null);

        if (user != null && product != null) {

            TransactionHistory history = new TransactionHistory(
                    user,
                    product,
                    quantity,
                    transactionId);

            transactionHistoryRepo.save(history);
        }
    }
}