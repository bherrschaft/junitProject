
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    public List<Book> bookDatabase = new ArrayList<>(); // A list to simulate a book database

    public List<Book> searchBook(String keyword) {
        // Search book by title, author, or genre
        return bookDatabase.stream()
                .filter(book -> book.getTitle().contains(keyword) ||
                        book.getAuthor().contains(keyword) ||
                        book.getGenre().contains(keyword))
                .collect(Collectors.toList());
    }

    public boolean purchaseBook(User user, Book book) {
        if (bookDatabase.contains(book) && user.getBalance() >= book.getPrice()) {
            user.getPurchasedBooks().add(book); // Add the book to the user's purchased list
            user.setBalance(user.getBalance() - book.getPrice()); // Deduct the price from the user's balance
            return true; // Purchase successful
        }
        return false; // Purchase failed
    }


    public boolean addBookReview(User user, Book book, String review) {
        // logic to add book review
        if (!user.getPurchasedBooks().contains(book)) {
            return false; // User has not purchased this book
        }

        book.getReviews().add(review);
        return true; // Review added successfully
    }

    public boolean addBook(Book book) {
        if (bookDatabase.contains(book)) {
            return false; // Book is already in the database
        }

        bookDatabase.add(book);
        return true; // Book added successfully
    }

    public boolean removeBook(Book book) {
        return bookDatabase.remove(book); // Book removed successfully if it was in the database
    }
}
