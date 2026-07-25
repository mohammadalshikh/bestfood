package bestfood.unit_tests;

import bestfood.controller.AdminController;
import bestfood.model.User;
import bestfood.service.UserService;
import org.springframework.ui.Model;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class AdminControllerTest {

    private AdminController adminController;
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        adminController = new AdminController();

        userService = mock(UserService.class);

        java.lang.reflect.Field field = AdminController.class.getDeclaredField("userService");

        field.setAccessible(true);
        field.set(adminController, userService);
    }

    @Test
    public void testAdminLoginAuthenticatesAdmin() {
        
        Model model = mock(Model.class);
        User admin = new User();
        
        admin.setId(1);
        admin.setUsername("admin");
        admin.setRole("ROLE_ADMIN");

        when(userService
                .authenticate("admin", "password"))
                .thenReturn(admin);

        String result = adminController.adminLogIn("admin", "password", model);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertEquals("1", auth.getName());
        assertEquals("redirect:/admin/home", result);
    }

    @Test
    public void testAdminLoginWithInvalidCredentials() {
        Model model = mock(Model.class);

        when(userService
            .authenticate("admin", "wrong"))
            .thenReturn(null);

        String result = adminController.adminLogIn("admin", "wrong", model);

        verify(model).addAttribute("message", "Invalid admin credentials");

        assertEquals("admin/login", result);
    }

    @Test
    public void testAdminLoginWithNonAdminUser() {
        
        Model model = mock(Model.class);
        User user = new User();

        user.setId(1);
        user.setRole("ROLE_USER");

        when(userService
            .authenticate("user", "password"))
            .thenReturn(user);

        String result = adminController.adminLogIn("user", "password", model);

        verify(model).addAttribute("message", "Invalid admin credentials");

        assertEquals("admin/login", result);
    }

    @Test
    public void testAdminLoginPage() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        assertEquals("admin/login", adminController.adminLoginPage(auth));
    }
}