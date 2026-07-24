package bestfood.model;

public class ShopItem {

    private String image;
    private String productName;
    private float price;
    private int productId;
    private String suggestedItem;

    public ShopItem(String image, String productName, float price, int productId, String suggestedItem) {
        this.image = image;
        this.productName = productName;
        this.price = price;
        this.productId = productId;
        this.suggestedItem = suggestedItem;
    }

    public String getProductName() {
        return productName;
    }

    public String getImage() {
        return image;
    }

    public float getPrice() {
        return price;
    }

    public int getProductId() {
        return productId;
    }

    public String getSuggestedItem() {
        return suggestedItem;
    }
    
}