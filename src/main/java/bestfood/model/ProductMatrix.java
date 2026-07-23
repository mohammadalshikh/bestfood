package bestfood.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_matrices")
public class ProductMatrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "matrix", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPair> productPairs = new ArrayList<>();

    public ProductMatrix() {
    }

    public ProductMatrix(Product product) {
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ProductPair> getProductPairs() {
        return productPairs;
    }

    public void setProductPairs(List<ProductPair> productPairs) {
        this.productPairs = productPairs;
    }

    public int getPairCount(Product product) {

        return productPairs.stream()
                .filter(p -> p.getPairedProduct().getId().equals(product.getId()))
                .map(ProductPair::getCount)
                .findFirst()
                .orElse(0);
    }

    public void setPairCount(Product product, int count) {

        for (ProductPair pair : productPairs) {

            if (pair.getPairedProduct().getId().equals(product.getId())) {

                pair.setCount(count);
                return;
            }
        }

        ProductPair pair = new ProductPair(
                this,
                product,
                count);

        productPairs.add(pair);
    }
}