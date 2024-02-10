import java.time.LocalDateTime;

// Reservation class: contains attributes to maintain the reservation queue for a book based
// on borrower's priority
public class Reservation {
    int patronID;	// ID of borrowing Patron
    int priority;	// Priority of borrowing Patron
    LocalDateTime timestamp;

    // Default constructor
    public Reservation() {
    }

    // Parameterized constructor
    public Reservation(int patronID, int priority) {
        this.patronID = patronID;
        this.priority = priority;
        this.timestamp = LocalDateTime.now();
    }
}