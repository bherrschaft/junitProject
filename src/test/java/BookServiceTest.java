import static org.junit.Assert.*; // Importing JUnit assertion methods
import static org.mockito.Mockito.*; // Importing Mockito methods

import org.junit.Before; // Importing JUnit's Before annotation
import org.junit.Test; // Importing JUnit's Test annotation
import org.mockito.InjectMocks; // Importing Mockito's InjectMocks annotation
import org.mockito.Mock; // Importing Mockito's Mock annotation
import org.mockito.MockitoAnnotations; // Importing Mockito's initialization utility

import java.util.ArrayList; // Importing ArrayList class
import java.util.List; // Importing List interface

public class BookServiceTest {

    @InjectMocks
    private BookService bookService; // Creating an instance of BookService with injected mocks

    @Mock
    private List<Book> bookDatabase; // Mocking the List that simulates the book database

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks and inject them into bookService
        bookService = new BookService(); // Create a new instance of BookService
        bookService.bookDatabase = bookDatabase; // Assign the mock bookDatabase to the BookService
    }

    @Test
    public void testSearchBookByTitleSuccess() {
        // Arrange: Create a book and define the mock behavior
        List<Book> books = new ArrayList<>();
        Book book1 = new Book("Title1", "Author1", "Genre1", 29.99, new ArrayList<>());
        books.add(book1);

        when(bookDatabase.stream()).thenReturn(books.stream()); // Simulate streaming the books

        // Act: Search for the book by title
        List<Book> result = bookService.searchBook("Title1");

        // Assert: Verify that the search returns the correct book
        assertEquals(1, result.size()); // Check that one book was found
        assertTrue(result.contains(book1)); // Ensure that the correct book was returned
    }

    @Test
    public void testSearchBookByAuthorSuccess() {
        // Arrange: Create a book and define the mock behavior
        List<Book> books = new ArrayList<>();
        Book book2 = new Book("Title2", "Author2", "Genre2", 39.99, new ArrayList<>());
        books.add(book2);

        when(bookDatabase.stream()).thenReturn(books.stream()); // Simulate streaming the books

        // Act: Search for the book by author
        List<Book> result = bookService.searchBook("Author2");

        // Assert: Verify that the search returns the correct book
        assertEquals(1, result.size()); // Check that one book was found
        assertTrue(result.contains(book2)); // Ensure that the correct book was returned
    }

    @Test
    public void testSearchBookByGenreFailure() {
        // Arrange: Define the mock behavior
        when(bookDatabase.stream()).thenReturn(new ArrayList<Book>().stream()); // Simulate an empty book list

        // Act: Search for a non-existent genre
        List<Book> result = bookService.searchBook("NonExistentGenre");

        // Assert: Verify that no books are found
        assertTrue(result.isEmpty()); // Check that no books were found
    }

    @Test
    public void testPurchaseBookSuccess() {
        // Arrange: Create a user and book, define the mock behavior
        User user = new User("jane_doe", "password456", "jane@example.com", 100.00); // Set initial balance
        Book book1 = new Book("Title1", "Author1", "Genre1", 29.99, new ArrayList<>());

        when(bookDatabase.contains(book1)).thenReturn(true); // Simulate that the book is in the database

        // Act: Attempt to purchase the book
        boolean result = bookService.purchaseBook(user, book1);

        // Assert: Verify that the purchase is successful
        assertTrue(result); // Check that the purchase was successful
        assertEquals(70.01, user.getBalance(), 0.01); // Check the updated balance
    }

    @Test
    public void testPurchaseBookNotInDatabase() {
        // Arrange: Create a user and book, define the mock behavior
        User user = new User("jane_doe", "password456", "jane@example.com", 100.00); // Set initial balance
        Book book1 = new Book("Title1", "Author1", "Genre1", 29.99, new ArrayList<>());

        when(bookDatabase.contains(book1)).thenReturn(false); // Simulate that the book is not in the database

        // Act: Attempt to purchase the book
        boolean result = bookService.purchaseBook(user, book1);

        // Assert: Verify that the purchase fails
        assertFalse(result); // Check that the purchase failed
    }

    @Test
    public void testAddBookReviewSuccess() {
        // Arrange: Create a user and book
        User user = new User("jane_doe", "password456", "jane@example.com");
        Book book1 = new Book("Title1", "Author1", "Genre1", 29.99, new ArrayList<>());
        user.getPurchasedBooks().add(book1); // Simulate that the user purchased the book

        // Act: Attempt to add a review
        boolean result = bookService.addBookReview(user, book1, "Great book!");

        // Assert: Verify that the review is added
        assertTrue(result); // Check that the review was added
        assertTrue(book1.getReviews().contains("Great book!")); // Ensure the review is present
    }

    @Test
    public void testAddBookReviewFailureNotPurchased() {
        // Arrange: Create a user and book
        User user = new User("jane_doe", "password456", "jane@example.com");
        Book book2 = new Book("Title2", "Author2", "Genre2", 39.99, new ArrayList<>());

        // Act: Attempt to add a review for a book not purchased
        boolean result = bookService.addBookReview(user, book2, "Good read");

        // Assert: Verify that the review is not added
        assertFalse(result); // Check that the review was not added
        assertFalse(book2.getReviews().contains("Good read")); // Ensure the review is not present
    }

    @Test
    public void testAddBookAlreadyExists() {
        // Arrange: Create a book and define the mock behavior
        Book book1 = new Book("Title1", "Author1", "Genre1", 29.99, new ArrayList<>());

        when(bookDatabase.contains(book1)).thenReturn(true); // Simulate that the book is already in the database

        // Act: Attempt to add the book
        boolean result = bookService.addBook(book1);

        // Assert: Verify that the book is not added
        assertFalse(result); // Check that the book was not added
        verify(bookDatabase, never()).add(book1); // Ensure the book was not added to the database
    }

    @Test
    public void testRemoveBookSuccess() {
        // Arrange: Create a book and define the mock behavior
        Book book1 = new Book("Title1", "Author1", "Genre1", 29.99, new ArrayList<>());

        when(bookDatabase.remove(book1)).thenReturn(true); // Simulate successful removal

        // Act: Attempt to remove the book
        boolean result = bookService.removeBook(book1);

        // Assert: Verify that the book is removed
        assertTrue(result); // Check that the book was removed
    }

    @Test
    public void testRemoveBookNotInDatabase() {
        // Arrange: Create a book and define the mock behavior
        Book book2 = new Book("Title2", "Author2", "Genre2", 39.99, new ArrayList<>());

        when(bookDatabase.remove(book2)).thenReturn(false); // Simulate unsuccessful removal

        // Act: Attempt to remove the book
        boolean result = bookService.removeBook(book2);

        // Assert: Verify that the book is not removed
        assertFalse(result); // Check that the book was not removed
    }
}
