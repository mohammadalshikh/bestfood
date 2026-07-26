package bestfood.service;

import bestfood.model.CartItem;
import bestfood.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CheckoutService {

    private final CartService cartService;
    private final UserService userService;
    private final TransactionHistoryService transactionHistoryService;
    private final ProductLinkService productMatrixService;

    public CheckoutService(
        CartService cartService,
        UserService userService,
        TransactionHistoryService transactionHistoryService,
        ProductLinkService productMatrixService) {

        this.cartService = cartService;
        this.userService = userService;
        this.transactionHistoryService = transactionHistoryService;
        this.productMatrixService = productMatrixService;
    }

    @Transactional
    public void checkOut(Integer userId) {

        int basketId = transactionHistoryService.getMaxTransactionId(userId) + 1;
        Set<Integer> products = new HashSet<>();
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);

        for (CartItem cartItem : cartItems) {

            Integer productId = cartItem.getProduct().getId();

            transactionHistoryService.addTransactionHistory(
                userId,
                productId,
                cartItem.getQuantity(),
                basketId
            );

            if (productId != 0) {
                products.add(productId);
            }
        }

        
        productMatrixService.updateSomeBoughtWithProductsToBest(products);

        updateUserPostCheckout(userId);

        cartService.removeCartItems(userId);
    }

    public float getTotalAfterTaxNoCoupons(Integer userId) {

        double cartTotalNoTaxNoCoupons = cartService.getTotalNoTaxNoCoupons(userId);

        return (float) (cartTotalNoTaxNoCoupons * 1.15);
    }

    public float getTotalFinal(Integer userId) {

        float totalFinal = getTotalAfterTaxNoCoupons(userId);
        int coupons = getAppliedCouponsCount(userId);

        totalFinal -= coupons * 5;

        return totalFinal;
    }

    public int getAppliedCouponsCount(Integer userId) {

        CartItem couponCartItem = cartService.getCartItemByUserIdAndProductId(userId, 0);

        return couponCartItem != null ? couponCartItem.getQuantity() : 0;
    }

    public void updateAppliedCouponsCount(Integer userId, int couponsCount) {

        int ownedCoupons = userService.getOwnedCouponsCount(userId);

        float totalAfterTaxes = getTotalAfterTaxNoCoupons(userId);
        int maxCoupons = (int) (totalAfterTaxes / 5);

        couponsCount = Math.max(0, Math.min(couponsCount, ownedCoupons));
        couponsCount = Math.min(couponsCount, maxCoupons);

        CartItem couponItem = cartService.getCartItemByUserIdAndProductId(userId, 0);

        if (couponItem != null) {
            cartService.updateCartItemQuantity(userId, 0, couponsCount);

        } else {
            cartService.addCartItem(userId, 0, couponsCount);
        }
    }

    public void updateUserPostCheckout(Integer userId) {

        User user = userService.getUserById(userId);

        if (user != null) {

            float currentTransaction = getTotalFinal(userId);
            float cumulativeTotal = user.getCumulativeTotal() + currentTransaction;

            if ((int) cumulativeTotal / 100 != 0) {

                int newCoupons = (int) cumulativeTotal / 100;
                cumulativeTotal = cumulativeTotal % 100;

                user.setOwnedCoupons(user.getOwnedCoupons() - getAppliedCouponsCount(userId) + newCoupons);
            }

            user.setCumulativeTotal(cumulativeTotal);

            userService.saveUser(user);
        }
    }
    
}
