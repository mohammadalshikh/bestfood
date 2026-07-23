package bestfood.unit_tests;

import bestfood.controller.AdminController;
import bestfood.controller.UserController;
import org.junit.Test;
import org.springframework.ui.Model;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class AdminControllerTest {

    @Test
    public void TestSetUsername() {
        String username = "user";
        AdminController.setUsername("user");
        assertEquals(username, AdminController.usernameforclass);
    }

    @Test
    public void testReturnIndex() {
        UserController userControllerMock = mock(UserController.class);

        // Create an instance of the class under test
        AdminController adminController = new AdminController();
        String result = adminController.returnIndex();

        // Verify that the method `setUsername` was called with an empty string
        verify(userControllerMock, times(1)).setUsername("");
        // Verify that the return value is as expected
        assertEquals("userLogin", result);
    }

    @Test
    public void testIndexMethodWithEmptyUsername() {
        AdminController.usernameforclass = "";
        AdminController adminController = new AdminController();
        UserController userControllerMock = mock(UserController.class);
        Model model = mock(Model.class);

        String result = adminController.index(model);

        // Verify that the method `setUsername` was not called
        verifyNoInteractions(userControllerMock);
        // Verify that the return value is as expected
        assertEquals("userLogin", result);
    }

    @Test
    public void testIndexMethodWithNonEmptyUsername() {
        AdminController adminController = new AdminController();
        Model modelMock = mock(Model.class);
        String username = "non_empty";
        AdminController.usernameforclass = username;
        UserController userControllerMock = mock(UserController.class);

        String result = adminController.index(modelMock);

        // Verify that the model contains the attribute "username" with the expected
        // value
        verify(modelMock, times(1)).addAttribute("username", username);
        // Verify that the return value is as expected
        assertEquals("index", result);
    }

    @Test
    public void TestUserLog() {
        AdminController adminController = new AdminController();
        Model model = mock(Model.class);
        String result = adminController.userlog(model);
        // Verify that the return value is as expected
        assertEquals("userLogin", result);
    }
}