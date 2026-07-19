package bestfood.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import bestfood.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class JsonDatabaseService {

    private static final String DB_DIR = "database";
    private static final String USERS_FILE = DB_DIR + "/users.json";
    private static final String PRODUCTS_FILE = DB_DIR + "/products.json";
    private static final String CATEGORIES_FILE = DB_DIR + "/categories.json";
    private static final String CART_FILE = DB_DIR + "/cart.json";
    private static final String CUSTOM_CART_FILE = DB_DIR + "/custom_cart.json";
    private static final String TRANSACTION_HISTORY_FILE = DB_DIR + "/transaction_history.json";
    private static final String PRODUCT_MATRIX_FILE = DB_DIR + "/product_matrix.json";

    private final ObjectMapper objectMapper;

    private List<User> users;
    private List<Product> products;
    private List<Category> categories;
    private List<Cart> carts;
    private List<CustomCart> customCarts;
    private List<TransactionHistory> transactionHistories;
    private List<ProductMatrix> productMatrices;

    private AtomicInteger userIdCounter;
    private AtomicInteger productIdCounter;
    private AtomicInteger categoryIdCounter;

    public JsonDatabaseService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        createDatabaseDirectory();
        loadAllData();
        initializeDefaultData();
    }

    private void createDatabaseDirectory() {
        File dbDir = new File(DB_DIR);
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
    }

    private void loadAllData() {
        users = loadData(USERS_FILE, User[].class);
        products = loadData(PRODUCTS_FILE, Product[].class);
        categories = loadData(CATEGORIES_FILE, Category[].class);
        carts = loadData(CART_FILE, Cart[].class);
        customCarts = loadData(CUSTOM_CART_FILE, CustomCart[].class);
        transactionHistories = loadData(TRANSACTION_HISTORY_FILE, TransactionHistory[].class);
        productMatrices = loadData(PRODUCT_MATRIX_FILE, ProductMatrix[].class);

        // Initialize ID counters
        userIdCounter = new AtomicInteger(users.stream().mapToInt(User::getUserId).max().orElse(0));
        productIdCounter = new AtomicInteger(products.stream().mapToInt(Product::getId).max().orElse(0));
        categoryIdCounter = new AtomicInteger(categories.stream().mapToInt(Category::getCategoryId).max().orElse(0));
    }

    private <T> List<T> loadData(String filename, Class<T[]> clazz) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                T[] data = objectMapper.readValue(file, clazz);
                return new ArrayList<>(Arrays.asList(data));
            } catch (IOException e) {
                System.err.println("Error loading " + filename + ": " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    private void saveData(String filename, Object data) {
        try {
            objectMapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            System.err.println("Error saving " + filename + ": " + e.getMessage());
        }
    }

    private void initializeDefaultData() {
        // Initialize default users if empty
        if (users.isEmpty()) {
            User user1 = new User(1, "jay", "123", "ROLE_USER", null, "jay190912@gmail.com");
            User admin = new User(2, "admin", "123", "ROLE_ADMIN", null, "admin190912@gmail.com");
            users.add(user1);
            users.add(admin);
            userIdCounter.set(2);
            saveUsers();
        }

        // Initialize default categories if empty
        if (categories.isEmpty()) {
            categories.add(new Category(1, "Fruits"));
            categories.add(new Category(2, "Vegetables"));
            categories.add(new Category(3, "Meats"));
            categoryIdCounter.set(3);
            saveCategories();
        }

        // Initialize default products if empty
        if (products.isEmpty()) {
            products.add(new Product(1, "Orange",
                    "https://clipart-library.com/images_k/orange-transparent-background/orange-transparent-background-2.png",
                    1, 484, 3, 40, "Sweet", 0));
            products.add(new Product(2, "Onion",
                    "https://clipart-library.com/images_k/onion-transparent-background/onion-transparent-background-16.png",
                    2, 352, 3, 40, "Fresh", 0));
            products.add(new Product(3, "Beef",
                    "https://clipart-library.com/images_k/meat-transparent/meat-transparent-4.png", 3, 321, 30, 1000,
                    "Fresh", 0));
            products.add(new Product(4, "Apple", "https://clipart-library.com/img/1565435.png", 1, 593, 2, 30, "", 0));
            products.add(new Product(5, "Watermelon",
                    "https://clipart-library.com/new_gallery/44-444830_share-this-article-watermelon-png.png", 1, 423,
                    10, 1000, "", 0));
            products.add(new Product(6, "Banana", "https://clipart-library.com/image_gallery2/Banana.png", 1, 463, 2,
                    1000, "", 0));
            products.add(new Product(7, "Grapes",
                    "https://clipart-library.com/new_gallery/11-119102_frutas-grape-png.png", 1, 481, 5, 340, "", 0));
            products.add(new Product(8, "Pineapple",
                    "https://static.vecteezy.com/system/resources/previews/008/848/362/original/fresh-pineapple-free-png.png",
                    1, 294, 6, 500, "", 0));
            products.add(new Product(9, "Lettuce",
                    "https://www.pngmart.com/files/16/Green-Lettuce-PNG-Transparent-Image.png", 2, 234, 4, 300, "", 0));
            products.add(new Product(10, "Tomato",
                    "https://cdn.discordapp.com/attachments/1106825532508733460/1136204069439016960/image-removebg-preview.png",
                    1, 222, 2, 30, "", 0));
            products.add(new Product(11, "Corn",
                    "https://www.pngall.com/wp-content/uploads/2016/05/Corn-Free-Download-PNG.png", 2, 220, 4, 160, "",
                    0));
            products.add(new Product(12, "Cucumber",
                    "https://www.freepnglogos.com/uploads/cucumber-png/cucumber-png-image-purepng-transparent-png-26.png",
                    2, 120, 2, 30, "", 0));
            products.add(new Product(13, "Potato",
                    "https://clipart-library.com/image_gallery2/Potato-Free-Download-PNG.png", 2, 190, 2, 40, "", 0));
            products.add(new Product(14, "Cantaloupe",
                    "https://www.pngmart.com/files/15/Yellow-Cantaloupe-PNG-Transparent-Image.png", 1, 139, 6, 500, "",
                    0));
            productIdCounter.set(14);
            saveProducts();

            // Initialize product matrices for all products
            for (Product product : products) {
                ProductMatrix pm = new ProductMatrix(product.getId());
                productMatrices.add(pm);
            }
            saveProductMatrices();
        }
    }

    // ==================== USER OPERATIONS ====================

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public User getUserById(int userId) {
        return users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public boolean isUsernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public boolean isEmailExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equals(email));
    }

    public void addUser(String username, String password, String email, String address) {
        int newId = userIdCounter.incrementAndGet();
        User user = new User(newId, username, password, "ROLE_USER", address, email);
        users.add(user);
        saveUsers();
    }

    public void updateUser(int userId, String username, String email, String password, String address) {
        User user = getUserById(userId);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setAddress(address);
            saveUsers();
        }
    }

    public void updateUserCoupons(int userId, int coupons) {
        User user = getUserById(userId);
        if (user != null) {
            user.setCoupons(coupons);
            saveUsers();
        }
    }

    public void updateUserCumulativeTotal(int userId, float total) {
        User user = getUserById(userId);
        if (user != null) {
            user.setCumulativeTotal(total);
            saveUsers();
        }
    }

    private void saveUsers() {
        saveData(USERS_FILE, users);
    }

    // ==================== PRODUCT OPERATIONS ====================

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Product getProductById(int productId) {
        return products.stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElse(null);
    }

    public void addProduct(String name, String image, int categoryId, int quantity, float price, int weight,
            String description, double discount) {
        int newId = productIdCounter.incrementAndGet();
        Product product = new Product(newId, name, image, categoryId, quantity, price, weight, description, discount);
        products.add(product);

        // Add to product matrix
        ProductMatrix pm = new ProductMatrix(newId);
        productMatrices.add(pm);

        saveProducts();
        saveProductMatrices();
    }

    public void updateProduct(int productId, String name, String image, int quantity, float price, int weight,
            String description, double discount) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setName(name);
            product.setImage(image);
            product.setQuantity(quantity);
            product.setPrice(price);
            product.setWeight(weight);
            product.setDescription(description);
            product.setDiscount(discount);
            saveProducts();
        }
    }

    public void updateProductQuantity(int productId, int newQuantity) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setQuantity(newQuantity);
            saveProducts();
        }
    }

    public void updateProductSuggestedItem(int productId, int suggestedItemId) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setSuggestedItem(suggestedItemId);
            saveProducts();
        }
    }

    public void updateProductPair(int productId, int pairId) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setProductPair(pairId);
            saveProducts();
        }
    }

    public void deleteProduct(int productId) {
        products.removeIf(p -> p.getId() == productId);
        productMatrices.removeIf(pm -> pm.getProduct() == productId);

        // Remove from all product matrices
        for (ProductMatrix pm : productMatrices) {
            pm.getProductPairs().remove("p" + productId);
        }

        saveProducts();
        saveProductMatrices();
    }

    private void saveProducts() {
        saveData(PRODUCTS_FILE, products);
    }

    // ==================== CATEGORY OPERATIONS ====================

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public Category getCategoryById(int categoryId) {
        return categories.stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .findFirst()
                .orElse(null);
    }

    public Category getCategoryByName(String name) {
        return categories.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void addCategory(String name) {
        int newId = categoryIdCounter.incrementAndGet();
        Category category = new Category(newId, name);
        categories.add(category);
        saveCategories();
    }

    public void updateCategory(int categoryId, String name) {
        Category category = getCategoryById(categoryId);
        if (category != null) {
            category.setName(name);
            saveCategories();
        }
    }

    public void deleteCategory(int categoryId) {
        categories.removeIf(c -> c.getCategoryId() == categoryId);
        // Also delete products in this category
        products.removeIf(p -> p.getCategoryId() == categoryId);
        saveCategories();
        saveProducts();
    }

    private void saveCategories() {
        saveData(CATEGORIES_FILE, categories);
    }

    // ==================== CART OPERATIONS ====================

    public List<Cart> getCartByUserId(int userId) {
        return carts.stream()
                .filter(c -> c.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public Cart getCartItem(int userId, int productId) {
        return carts.stream()
                .filter(c -> c.getUserId() == userId && c.getProductId() == productId)
                .findFirst()
                .orElse(null);
    }

    public void addToCart(int userId, int productId, int quantity) {
        Cart existingItem = getCartItem(userId, productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            carts.add(new Cart(userId, productId, quantity));
        }
        saveCarts();
    }

    public void updateCartItemQuantity(int userId, int productId, int quantity) {
        Cart cart = getCartItem(userId, productId);
        if (cart != null) {
            cart.setQuantity(quantity);
            saveCarts();
        }
    }

    public void deleteCartItem(int userId, int productId) {
        carts.removeIf(c -> c.getUserId() == userId && c.getProductId() == productId);
        saveCarts();
    }

    public void clearCart(int userId) {
        carts.removeIf(c -> c.getUserId() == userId);
        saveCarts();
    }

    private void saveCarts() {
        saveData(CART_FILE, carts);
    }

    // ==================== CUSTOM CART OPERATIONS ====================

    public List<CustomCart> getCustomCartByUserId(int userId) {
        return customCarts.stream()
                .filter(c -> c.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public CustomCart getCustomCartItem(int userId, int productId) {
        return customCarts.stream()
                .filter(c -> c.getUserId() == userId && c.getProductId() == productId)
                .findFirst()
                .orElse(null);
    }

    public void addToCustomCart(int userId, int productId, int quantity) {
        CustomCart existingItem = getCustomCartItem(userId, productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            customCarts.add(new CustomCart(userId, productId, quantity));
        }
        saveCustomCarts();
    }

    public void updateCustomCartItemQuantity(int userId, int productId, int quantity) {
        CustomCart cart = getCustomCartItem(userId, productId);
        if (cart != null) {
            cart.setQuantity(quantity);
            saveCustomCarts();
        }
    }

    public void deleteCustomCartItem(int userId, int productId) {
        customCarts.removeIf(c -> c.getUserId() == userId && c.getProductId() == productId);
        saveCustomCarts();
    }

    public void clearCustomCart(int userId) {
        customCarts.removeIf(c -> c.getUserId() == userId);
        saveCustomCarts();
    }

    public void moveCustomCartToCart(int userId) {
        List<CustomCart> userCustomCart = getCustomCartByUserId(userId);
        for (CustomCart item : userCustomCart) {
            addToCart(userId, item.getProductId(), item.getQuantity());
        }
        clearCustomCart(userId);
    }

    private void saveCustomCarts() {
        saveData(CUSTOM_CART_FILE, customCarts);
    }

    // ==================== TRANSACTION HISTORY OPERATIONS ====================

    public int getMaxTransactionId(int userId) {
        return transactionHistories.stream()
                .filter(t -> t.getUserId() == userId)
                .mapToInt(TransactionHistory::getTransactionId)
                .max()
                .orElse(0);
    }

    public void addTransactionHistory(int userId, int productId, int quantity, int transactionId) {
        transactionHistories.add(new TransactionHistory(userId, productId, quantity, transactionId));
        saveTransactionHistory();
    }

    private void saveTransactionHistory() {
        saveData(TRANSACTION_HISTORY_FILE, transactionHistories);
    }

    // ==================== PRODUCT MATRIX OPERATIONS ====================

    public ProductMatrix getProductMatrix(int productId) {
        return productMatrices.stream()
                .filter(pm -> pm.getProduct() == productId)
                .findFirst()
                .orElse(null);
    }

    public void updateProductMatrixPair(int productId1, int productId2, int count) {
        ProductMatrix pm = getProductMatrix(productId1);
        if (pm != null) {
            pm.setPairCount(productId2, count);
            saveProductMatrices();
        }
    }

    public Map.Entry<Integer, Integer> getMaxPairForProduct(int productId) {
        ProductMatrix pm = getProductMatrix(productId);
        if (pm != null && !pm.getProductPairs().isEmpty()) {
            return pm.getProductPairs().entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(e -> new AbstractMap.SimpleEntry<>(
                            Integer.parseInt(e.getKey().substring(1)),
                            e.getValue()))
                    .orElse(null);
        }
        return null;
    }

    public List<ProductMatrix> getAllProductMatrices() {
        return new ArrayList<>(productMatrices);
    }

    private void saveProductMatrices() {
        saveData(PRODUCT_MATRIX_FILE, productMatrices);
    }
}
