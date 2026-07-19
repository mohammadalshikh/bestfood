package bestfood.model;

public class TransactionHistory {
    private int userId;
    private int productId;
    private int quantity;
    private int transactionId;

    public TransactionHistory() {
    }

    public TransactionHistory(int userId, int productId, int quantity, int transactionId) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.transactionId = transactionId;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
