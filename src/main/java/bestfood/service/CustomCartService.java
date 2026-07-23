package bestfood.service;

import bestfood.model.CustomCart;
import bestfood.model.Product;
import bestfood.model.User;

import bestfood.repo.CustomCartRepo;
import bestfood.repo.ProductRepo;
import bestfood.repo.UserRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomCartService {

    private final CustomCartRepo customCartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final CartService cartService;

    public CustomCartService(
            CustomCartRepo customCartRepo,
            UserRepo userRepo,
            ProductRepo productRepo,
            CartService cartService) {

        this.customCartRepo = customCartRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.cartService = cartService;
    }

    public List<CustomCart> getAllCustomCarts() {
        return customCartRepo.findAll();
    }

    public CustomCart getCustomCartById(Integer id) {
        return customCartRepo.findById(id)
                .orElse(null);
    }

    public List<CustomCart> getCustomCartByUserId(int userId) {

        return customCartRepo.findByUserUserId(userId);
    }

    public CustomCart getCustomCartItem(
            int userId,
            int productId) {

        return customCartRepo
                .findByUserUserIdAndProductId(
                        userId,
                        productId);
    }

    public CustomCart addItemToCustomCart(
            int userId,
            int productId,
            int quantity) {

        CustomCart existing = getCustomCartItem(userId, productId);

        if (existing != null) {

            existing.setQuantity(
                    existing.getQuantity() + quantity);

            return customCartRepo.save(existing);
        }

        User user = userRepo.findById(userId)
                .orElseThrow();

        Product product = productRepo.findById(productId)
                .orElseThrow();

        CustomCart cart = new CustomCart();

        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(quantity);

        return customCartRepo.save(cart);
    }

    public CustomCart updateCustomCartItemQuantity(
            int userId,
            int productId,
            int quantity) {

        CustomCart cart = getCustomCartItem(
                userId,
                productId);

        if (cart == null) {
            return null;
        }

        cart.setQuantity(quantity);

        return customCartRepo.save(cart);
    }

    @Transactional
    public void removeCustomCartItem(int userId, int productId) {
        customCartRepo.deleteByUserUserIdAndProductId(userId, productId);
    }
    
    @Transactional
    public void clearCustomCart(Integer id) {
        customCartRepo.deleteById(id);
    }
    
    public void moveCustomCartToCart(int userId) {

        List<CustomCart> items = getCustomCartByUserId(userId);

        for (CustomCart item : items) {

            cartService.addItemToCart(
                    userId,
                    item.getProduct().getId(),
                    item.getQuantity());
        }
    }

    public CustomCart saveCustomCart(CustomCart customCart) {
        return customCartRepo.save(customCart);
    }

    
}