package bestfood.model;

import javax.persistence.*;

@Entity @Table(name = "transaction_history")
public class TransactionHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private int basketId;

    public TransactionHistory() {
    }

    public TransactionHistory(User user, Product product, int quantity, int basketId) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.basketId = basketId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBasketId() {
        return basketId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }
    
}