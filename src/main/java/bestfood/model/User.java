package bestfood.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String address;
    private String email;
    private int coupons;
    private float cumulativeTotal;

    public User() {
        this.role = "ROLE_USER";
        this.coupons = 0;
        this.cumulativeTotal = 0f;
    }

    public User(int userId, String username, String password, String role, String address, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.email = email;
        this.coupons = 0;
        this.cumulativeTotal = 0f;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCoupons() {
        return coupons;
    }

    public void setCoupons(int coupons) {
        this.coupons = coupons;
    }

    public float getCumulativeTotal() {
        return cumulativeTotal;
    }

    public void setCumulativeTotal(float cumulativeTotal) {
        this.cumulativeTotal = cumulativeTotal;
    }
}
