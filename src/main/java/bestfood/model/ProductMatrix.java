package bestfood.model;

import java.util.HashMap;
import java.util.Map;

public class ProductMatrix {
    private int product;
    private Map<String, Integer> productPairs; // key format: "p{productId}"

    public ProductMatrix() {
        this.productPairs = new HashMap<>();
    }

    public ProductMatrix(int product) {
        this.product = product;
        this.productPairs = new HashMap<>();
    }

    // Getters and Setters
    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public Map<String, Integer> getProductPairs() {
        return productPairs;
    }

    public void setProductPairs(Map<String, Integer> productPairs) {
        this.productPairs = productPairs;
    }

    public int getPairCount(int productId) {
        String key = "p" + productId;
        return productPairs.getOrDefault(key, 0);
    }

    public void setPairCount(int productId, int count) {
        String key = "p" + productId;
        productPairs.put(key, count);
    }
}
