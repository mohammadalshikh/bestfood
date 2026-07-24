package bestfood.unit_tests;

import bestfood.model.CustomCartItem;
import org.junit.Test;
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
        assertEquals(totalPrice, customCartItem.getTotalNoTaxNoCoupons(), 0.001);
        assertEquals(productID, customCartItem.getProductId());
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
        assertEquals(totalPrice, customCartItem.getTotalNoTaxNoCoupons(), 0.001);
        assertEquals(productID, customCartItem.getProductId());
    }
}