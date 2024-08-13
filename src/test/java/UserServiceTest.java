import static org.junit.Assert.*; // Importing JUnit assertion methods
import static org.mockito.Mockito.*; // Importing Mockito methods

import org.junit.Before; // Importing JUnit's Before annotation
import org.junit.Test; // Importing JUnit's Test annotation
import org.mockito.InjectMocks; // Importing Mockito's InjectMocks annotation
import org.mockito.Mock; // Importing Mockito's Mock annotation
import org.mockito.MockitoAnnotations; // Importing Mockito's initialization utility

import java.util.Map; // Importing the Map interface

public class UserServiceTest {

    @InjectMocks
    private UserService userService; // Creating an instance of UserService with injected mocks

    @Mock
    private Map<String, User> userDatabase; // Mocking the Map that simulates the user database

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks and inject them into userService
        userService = new UserService(); // Create a new instance of UserService
        userService.userDatabase = userDatabase; // Assign the mock userDatabase to the UserService
    }

    @Test
    public void testRegisterUserSuccess() {
        // Arrange: Create a user and define the mock behavior
        User user = new User("john_doe", "password123", "john@example.com");
        when(userDatabase.containsKey(user.getUsername())).thenReturn(false); // Simulate that the username is not taken

        // Act: Register the user
        boolean result = userService.registerUser(user);

        // Assert: Verify that registration is successful and the user is added
        assertTrue(result); // Check that the user was registered
        verify(userDatabase).put(user.getUsername(), user); // Ensure that the user was added to the database
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        // Arrange: Create a user and define the mock behavior
        User user = new User("john_doe", "password123", "john@example.com");
        when(userDatabase.containsKey(user.getUsername())).thenReturn(true); // Simulate that the username is already taken

        // Act: Attempt to register the user
        boolean result = userService.registerUser(user);

        // Assert: Verify that registration fails
        assertFalse(result); // Check that the user was not registered
        verify(userDatabase, never()).put(user.getUsername(), user); // Ensure that the user was not added to the database
    }

    @Test
    public void testLoginUserSuccess() {
        // Arrange: Create a user and define the mock behavior
        User user = new User("jane_doe", "password456", "jane@example.com");
        when(userDatabase.get("jane_doe")).thenReturn(user); // Simulate retrieving the user from the database

        // Act: Attempt to log in the user
        User loggedInUser = userService.loginUser("jane_doe", "password456");

        // Assert: Verify that login is successful
        assertNotNull(loggedInUser); // Check that a user was returned
    }

    @Test
    public void testLoginUserWrongPassword() {
        // Arrange: Create a user and define the mock behavior
        User user = new User("jane_doe", "password456", "jane@example.com");
        when(userDatabase.get("jane_doe")).thenReturn(user); // Simulate retrieving the user from the database

        // Act: Attempt to log in with an incorrect password
        User loggedInUser = userService.loginUser("jane_doe", "wrongpassword");

        // Assert: Verify that login fails
        assertNull(loggedInUser); // Check that no user was returned
    }

    @Test
    public void testLoginUserNotFound() {
        // Arrange: Define the mock behavior
        when(userDatabase.get("nonexistent_user")).thenReturn(null); // Simulate that the user does not exist

        // Act: Attempt to log in a non-existent user
        User loggedInUser = userService.loginUser("nonexistent_user", "password123");

        // Assert: Verify that login fails
        assertNull(loggedInUser); // Check that no user was returned
    }

    @Test
    public void testUpdateUserProfileSuccess() {
        // Arrange: Create a user and define the mock behavior
        User user = new User("jane_doe", "password456", "jane@example.com");
        when(userDatabase.containsKey("jane_doe_updated")).thenReturn(false); // Simulate that the new username is not taken
        when(userDatabase.containsKey("jane_doe")).thenReturn(true); // Simulate that the old username exists

        // Act: Attempt to update the user profile
        boolean result = userService.updateUserProfile(user, "jane_doe_updated", "newpassword", "jane_updated@example.com");

        // Assert: Verify that the update is successful
        assertTrue(result); // Check that the profile was updated
        verify(userDatabase).put("jane_doe_updated", user); // Ensure that the user was updated in the database
    }

    @Test
    public void testUpdateUserProfileUsernameTaken() {
        // Arrange: Create a user and define the mock behavior
        User user = new User("jane_doe", "password456", "jane@example.com");
        when(userDatabase.containsKey("john_doe")).thenReturn(true); // Simulate that the new username is already taken

        // Act: Attempt to update the user profile with a taken username
        boolean result = userService.updateUserProfile(user, "john_doe", "newpassword", "jane_updated@example.com");

        // Assert: Verify that the update fails
        assertFalse(result); // Check that the profile was not updated
        verify(userDatabase, never()).put("john_doe", user); // Ensure that the user was not updated in the database
    }

    @Test
    public void testUpdateUserProfileWithSameUsername() {
        // Arrange: Create a user and define the mock behavior
        User user = new User("jane_doe", "password456", "jane@example.com");
        when(userDatabase.containsKey("jane_doe")).thenReturn(true); // Simulate that the username exists

        // Act: Attempt to update the user profile with the same username
        boolean result = userService.updateUserProfile(user, "jane_doe", "newpassword", "jane_updated@example.com");

        // Assert: Verify that the update is successful
        assertTrue(result); // Check that the profile was updated
        verify(userDatabase).put("jane_doe", user); // Ensure that the user was updated in the database
    }
}
