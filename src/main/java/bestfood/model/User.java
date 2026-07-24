package bestfood.model;

import javax.persistence.*;

@Entity @Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String role;

    private String address;

    @Column(unique = true)
    private String email;

    private int ownedCoupons;

    private float cumulativeTotal;

    public User() {
        this.role = "ROLE_USER";
        this.ownedCoupons = 0;
        this.cumulativeTotal = 0f;
    }

    public User(Integer userId, String username, String password, String role, String address, String email) {
        this.id = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.email = email;
        this.ownedCoupons = 0;
        this.cumulativeTotal = 0f;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getOwnedCoupons() {
        return ownedCoupons;
    }

    public void setOwnedCoupons(int couponsCount) {
        this.ownedCoupons = couponsCount;
    }

    public float getCumulativeTotal() {
        return cumulativeTotal;
    }

    public void setCumulativeTotal(float cumulativeTotal) {
        this.cumulativeTotal = cumulativeTotal;
    }
    
}