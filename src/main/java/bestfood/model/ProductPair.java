package bestfood.model;

import javax.persistence.*;

@Entity @Table(name = "product_pairs")
public class ProductPair {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne @JoinColumn(name = "matrix_id")
    private ProductMatrix matrix;

    @ManyToOne @JoinColumn(name = "paired_product_id")
    private Product pairedProduct;

    private int count;

    public ProductPair() {
    }

    public ProductPair(ProductMatrix matrix, Product pairedProduct, int count) {

        this.matrix = matrix;
        this.pairedProduct = pairedProduct;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductMatrix getMatrix() {
        return matrix;
    }

    public void setMatrix(ProductMatrix matrix) {
        this.matrix = matrix;
    }

    public Product getPairedProduct() {
        return pairedProduct;
    }

    public void setPairedProduct(Product pairedProduct) {
        this.pairedProduct = pairedProduct;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
}