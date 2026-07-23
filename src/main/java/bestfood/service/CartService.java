package bestfood.service;

import bestfood.model.Cart;
import bestfood.model.Product;
import bestfood.model.User;
import bestfood.repo.CartRepo;
import bestfood.repo.ProductRepo;
import bestfood.repo.UserRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    public CartService(
            CartRepo cartRepo,
            UserRepo userRepo,
            ProductRepo productRepo) {

        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    public List<Cart> getAllCarts() {
        return cartRepo.findAll();
    }

    public Cart getCartById(Integer id) {
        return cartRepo.findById(id)
                .orElse(null);
    }

    public List<Cart> getCartByUserId(int userId) {
        return cartRepo.findByUserUserId(userId);
    }

    public Cart getCartItem(int userId, int productId) {
        return cartRepo.findByUserUserIdAndProductId(
                userId,
                productId);
    }

    public Cart addItemToCart(
            int userId,
            int productId,
            int quantity) {

        Cart existing = getCartItem(userId, productId);

        if (existing != null) {

            existing.setQuantity(
                    existing.getQuantity() + quantity);

            return cartRepo.save(existing);
        }

        User user = userRepo.findById(userId)
                .orElseThrow();

        Product product = productRepo.findById(productId)
                .orElseThrow();

        Cart cart = new Cart();

        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(quantity);

        return cartRepo.save(cart);
    }

    public Cart updateCartItemQuantity(
            int userId,
            int productId,
            int quantity) {

        Cart cart = getCartItem(userId, productId);

        if (cart == null) {
            return null;
        }

        cart.setQuantity(quantity);

        return cartRepo.save(cart);
    }

    @Transactional
    public void removeCartItem(
            int userId,
            int productId) {

        cartRepo.deleteByUserUserIdAndProductId(
                userId,
                productId);
    }
    
    @Transactional
    public void clearCart(Integer id) {
        cartRepo.deleteById(id);
    }
    
    public Cart saveCart(Cart cart) {
        return cartRepo.save(cart);
    }

}