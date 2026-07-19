package bestfood.model;

public class ShopItem {

    private String image;
    private String productName;
    private float price;
    private int productID;
    private String suggestedItem;

    public ShopItem(String image, String productName, float price, int productID, String suggestedItem) {
        this.image = image;
        this.productName = productName;
        this.price = price;
        this.productID = productID;
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

    public int getProductID() {
        return productID;
    }

    public String getSuggestedItem() {
        return suggestedItem;
    }
}