package bestfood.service;

import bestfood.model.CustomCartItem;
import bestfood.model.Product;
import bestfood.model.User;
import bestfood.repo.CustomCartItemRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CustomCartService {

    private final CustomCartItemRepo customCartItemRepo;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    public CustomCartService(
        CustomCartItemRepo customCartItemRepo,
        CartService cartService,
        ProductService productService, 
        UserService userService) {

        this.customCartItemRepo = customCartItemRepo;
        this.cartService = cartService;
        this.productService = productService;
        this.userService = userService;
    }

    public List<CustomCartItem> getAllCustomCartItems() {

        return customCartItemRepo.findAll();
    }

    public List<CustomCartItem> getCustomCartItemsByUserId(int userId) {

        return customCartItemRepo.findByUserId(userId);
    }

    public CustomCartItem getCustomCartItemById(Integer id) {

        return customCartItemRepo.findById(id).orElse(null);
    }

    public CustomCartItem getCustomCartItemByUserIdAndProductId(int userId, int productId) {

        return customCartItemRepo.findByUserIdAndProductId(userId, productId);
    }

    public CustomCartItem addCustomCartItem(int userId, int productId, int quantity) {

        CustomCartItem existing = getCustomCartItemByUserIdAndProductId(userId, productId);

        if (existing != null) {

            existing.setQuantity(existing.getQuantity() + quantity);

            return customCartItemRepo.save(existing);
        }

        User user = userService.getUserById(userId);

        Product product = productService.getProductById(productId);

        CustomCartItem customCartItem = new CustomCartItem();

        customCartItem.setUser(user);
        customCartItem.setProduct(product);
        customCartItem.setQuantity(quantity);

        return customCartItemRepo.save(customCartItem);
    }

    public CustomCartItem updateCustomCartItemQuantity(int userId, int productId, int quantity) {

        CustomCartItem customCartItem = getCustomCartItemByUserIdAndProductId(userId, productId);

        if (customCartItem == null) {
            return null;
        }

        if (quantity >= 0) {
            customCartItem.setQuantity(quantity);
        }

        return customCartItemRepo.save(customCartItem);
    }

    @Transactional
    public void removeCustomCartItem(int userId, int productId) {

        customCartItemRepo.deleteByUserIdAndProductId(userId, productId);
    }

    @Transactional
    public void removeCustomCartItems(int userId) {

        customCartItemRepo.deleteByUserId(userId);
    }

    public CustomCartItem saveCustomCartItem(CustomCartItem customCartItem) {

        return customCartItemRepo.save(customCartItem);
    }

    public float getTotalNoTaxNoCoupons(int userId) {

        float customCartTotal = 0;

        List<CustomCartItem> customCartItems = getCustomCartItemsByUserId(userId);

        for (CustomCartItem customCartItem : customCartItems) {

            if (customCartItem.getProduct() != null) {

                customCartTotal += productService.getProductPrice(
                    customCartItem.getProduct().getId(),
                    customCartItem.getQuantity()
                );
            }
        }
        return customCartTotal;
    }

    @Transactional
    public void addCustomCartToCart(int userId) {

        List<CustomCartItem> customCartItems = getCustomCartItemsByUserId(userId);

        for (CustomCartItem customCartItem : customCartItems) {

            cartService.addCartItem(userId, customCartItem.getProduct().getId(), customCartItem.getQuantity());
        }
    }
    
}