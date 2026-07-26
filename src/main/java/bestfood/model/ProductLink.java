package bestfood.model;

import javax.persistence.*;

// Directional (double records, simple queries)
@Entity
@Table(
    name = "product_links",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"original_product_id", "bought_with_product_id"}
    )
)
public class ProductLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "original_product_id")
    private Product originalProduct;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bought_with_product_id")
    private Product boughtWithProduct;

    @Column(nullable = false)
    private int purchaseCount;

    public ProductLink() {
    }

    public ProductLink(Product orignalProduct, Product boughtWithProduct, int purchaseCount) {
        this.originalProduct = orignalProduct;
        this.boughtWithProduct = boughtWithProduct;
        this.purchaseCount = purchaseCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getOriginalProduct() {
        return originalProduct;
    }

    public void setOriginalProduct(Product originalProduct) {
        this.originalProduct = originalProduct;
    }

    public Product getBoughtWithProduct() {
        return boughtWithProduct;
    }

    public void setBoughtWithProduct(Product boughtWithProduct) {
        this.boughtWithProduct = boughtWithProduct;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }
}