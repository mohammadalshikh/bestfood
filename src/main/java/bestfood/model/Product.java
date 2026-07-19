package bestfood.model;

public class Product {
    private int id;
    private String name;
    private String image;
    private int categoryId;
    private int quantity;
    private float price;
    private int weight;
    private String description;
    private double discount;
    private int productPair;
    private int suggestedItem;

    public Product() {
    }

    public Product(int id, String name, String image, int categoryId, int quantity, float price, int weight,
            String description, double discount) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.price = price;
        this.weight = weight;
        this.description = description;
        this.discount = discount;
        this.productPair = 0;
        this.suggestedItem = 0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getProductPair() {
        return productPair;
    }

    public void setProductPair(int productPair) {
        this.productPair = productPair;
    }

    public int getSuggestedItem() {
        return suggestedItem;
    }

    public void setSuggestedItem(int suggestedItem) {
        this.suggestedItem = suggestedItem;
    }
}
