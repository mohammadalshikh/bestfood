package bestfood.service;

import bestfood.model.User;
import bestfood.repo.UserRepo;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Integer id) {
        return userRepo.findById(id)
                .orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User authenticate(
            String username,
            String password) {

        User user = userRepo.findByUsername(username);

        if (user != null &&
                passwordEncoder.matches(password, user.getPassword())) {

            return user;
        }

        return null;
    }

    public boolean isUsernameExists(String username) {
        return userRepo.existsByUsername(username);
    }

    public boolean isEmailExists(String email) {
        return userRepo.existsByEmail(email);
    }

    public User addUser(
            String username,
            String password,
            String email,
            String address) {

        User user = new User();

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setAddress(address);
        user.setRole("ROLE_USER");
        user.setCoupons(0);
        user.setCumulativeTotal(0);

        return userRepo.save(user);
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public void updateUser(
            int userId,
            String username,
            String email,
            String password,
            String address) {

        User user = getUserById(userId);

        if (user != null) {

            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setAddress(address);

            userRepo.save(user);
        }
    }

    public void updateUserCoupons(
            int userId,
            int coupons) {

        User user = getUserById(userId);

        if (user != null) {

            user.setCoupons(coupons);

            userRepo.save(user);
        }
    }

    public void updateUserCumulativeTotal(
            int userId,
            float total) {

        User user = getUserById(userId);

        if (user != null) {

            user.setCumulativeTotal(total);

            userRepo.save(user);
        }
    }

    public void deleteUser(Integer id) {
        userRepo.deleteById(id);
    }
}