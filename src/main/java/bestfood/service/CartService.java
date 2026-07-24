package bestfood.service;

import bestfood.model.Product;
import bestfood.model.User;
import bestfood.model.CartItem;
import bestfood.repo.CartItemRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CartService {

    private final CartItemRepo cartItemRepo;
    private final ProductService productService;
    private final UserService userService;

    public CartService(CartItemRepo cartItemRepo, ProductService productService, UserService userService) {

        this.cartItemRepo = cartItemRepo;
        this.productService = productService;
        this.userService = userService;
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepo.findAll();
    }

    public List<CartItem> getCartItemsByUserId(int userId) {
        return cartItemRepo.findByUserId(userId);
    }

    public CartItem getCartItemById(Integer id) {
        return cartItemRepo.findById(id).orElse(null);
    }

    public CartItem getCartItemByUserIdAndProductId(int userId, int productId) {
        return cartItemRepo.findByUserIdAndProductId(userId, productId);
    }

    public CartItem addCartItem(int userId, int productId, int quantity) {

        CartItem existing = getCartItemByUserIdAndProductId(userId, productId);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            return cartItemRepo.save(existing);
        }

        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = new CartItem();

        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        return cartItemRepo.save(cartItem);
    }

    public CartItem updateCartItemQuantity(int userId, int productId, int quantity) {

        CartItem cartItem = getCartItemByUserIdAndProductId(userId, productId);

        if (cartItem == null) {
            return null;
        }

        if (quantity >= 0) {
            cartItem.setQuantity(quantity);
        }
        return cartItemRepo.save(cartItem);
    }

    @Transactional
    public void removeCartItem(int userId, int productId) {

        cartItemRepo.deleteByUserIdAndProductId(userId, productId);
    }

    @Transactional
    public void removeCartItems(int userId) {
        cartItemRepo.deleteByUserId(userId);
    }

    public float getTotalNoTaxNoCoupons(int userId) {

        float cartTotalNoTaxNoCoupons = 0;

        List<CartItem> cartItems = getCartItemsByUserId(userId);

        for (CartItem cartItem : cartItems) {

            if (cartItem.getProduct() != null && cartItem.getProduct().getId() != 0) {

                cartTotalNoTaxNoCoupons += productService
                    .getProductPrice(
                        cartItem.getProduct().getId(),
                        cartItem.getQuantity());
            }
        }
        
        return cartTotalNoTaxNoCoupons;
    }

}