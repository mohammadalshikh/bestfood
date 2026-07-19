package bestfood.unit_tests;
import org.junit.Test;

import bestfood.model.ShopItem;

import static org.junit.Assert.assertEquals;

public class ShopItemTest {

    @Test
    public void testShopItem() {
        String image = "item.jpg";
        String productName = "Product 1";
        float price = 50.0F;
        int productID = 101;
        String suggestedItem = "Suggested Product";

        ShopItem shopItem = new ShopItem(image, productName, price, productID, suggestedItem);

        assertEquals(image, shopItem.getImage());
        assertEquals(productName, shopItem.getProductName());
        assertEquals(price, shopItem.getPrice(), 0.1);
        assertEquals(productID, shopItem.getProductID());
        assertEquals(suggestedItem, shopItem.getSuggestedItem());
    }

    @Test
    public void testGettersWithDifferentValues() {
        String image = "other_item.jpg";
        String productName = "Product 2";
        float price = 25F;
        int productID = 102;
        String suggestedItem = "Other Suggested Product";

        ShopItem shopItem = new ShopItem(image, productName, price, productID, suggestedItem);

        assertEquals(image, shopItem.getImage());
        assertEquals(productName, shopItem.getProductName());
        assertEquals(price, shopItem.getPrice(), 0.1);
        assertEquals(productID, shopItem.getProductID());
        assertEquals(suggestedItem, shopItem.getSuggestedItem());
    }
}