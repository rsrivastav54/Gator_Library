// Book class: contains all attributes of Book model class to store the book in Library
public class Book {
    int bookID;	// ID of the book
    private String bookName;	// Name of the Book
    private String bookAuthor;	// Name of book author
    boolean isAvailable;	// Status of availability book, true by default
    int borrowerID;	// ID of borrower if borrowed, 0 by default

    // Default constructor
    public Book() {
        this.bookID = 0;
        this.bookName = "";
        this.bookAuthor = "";
        this.isAvailable = true;
        this.borrowerID = 0;
    }

    // Parameterized constructor
    public Book(int bookID, String bookName, String bookAuthor, boolean isAvailable) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.isAvailable = isAvailable;
        this.borrowerID = 0;
    }
    
    // Overridden toString() method, which is used in printBook() method to print book details
    @Override
    public String toString() {
    	String avail = isAvailable ? "Yes":"No";
    	String borrow = borrowerID == 0 ? "None": Integer.toString(borrowerID);
    	return "BookID = " + bookID +
                "\nTitle = \"" + bookName + "\"" +
                "\nAuthor = \"" + bookAuthor + "\"" +
                "\nAvailability = \"" + avail + "\"" +
                "\nBorrowedBy = " + borrow;
    }
}