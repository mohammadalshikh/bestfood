package bestfood.unit_tests;
import org.junit.Test;

import bestfood.model.CustomCartItem;

import static org.junit.Assert.assertEquals;

public class CustomCartItemTest {

    @Test
    public void testCustomCartItem() {
        String productName = "Product 1";
        int quantity = 2;
        float totalPrice = 50.0f;
        int productID = 101;

        CustomCartItem customCartItem = new CustomCartItem(productName, quantity, totalPrice, productID);

        assertEquals(productName, customCartItem.getProductName());
        assertEquals(quantity, customCartItem.getQuantity());
        assertEquals(totalPrice, customCartItem.getTotalPrice(), 0.001); // Using delta to handle float comparison
        assertEquals(productID, customCartItem.getProductID());
    }

    @Test
    public void testGettersWithDifferentValues() {
        String productName = "Product 2";
        int quantity = 5;
        float totalPrice = 20.0f;
        int productID = 102;

        CustomCartItem customCartItem = new CustomCartItem(productName, quantity, totalPrice, productID);

        assertEquals(productName, customCartItem.getProductName());
        assertEquals(quantity, customCartItem.getQuantity());
        assertEquals(totalPrice, customCartItem.getTotalPrice(), 0.001); // Using delta to handle float comparison
        assertEquals(productID, customCartItem.getProductID());
    }
}