// Node class: Contains information about whether the Book node is red or black, details about
// the Book class, the min-priority heap (reservation list) associated with that book, and 
// other details like children and parent of the Book node.
public class Node {
    public Node left, right; // left and right children nodes
    public Node parent;	// parent node
    public boolean isRed;	// to determine whether a node is red, true by default
    public Book book;	// contains all information associated with a Book       
    public MinHeap reservationList;	// minHeap that maintains reservations for a book
    
    // Default constructor
    public Node() {
    	this.left = null;
        this.right = null;
        this.parent = null;
        this.isRed = true;
        this.book = null;
        this.reservationList = new MinHeap();
    }

    // Implementation for printing book details
    public void printBook() {
        System.out.println(book);
        System.out.print("Reservations = [");
        if (!this.reservationList.heap.isEmpty()) {
            for (int j = 0; j < this.reservationList.heap.size() - 1; j++) {
                System.out.print(this.reservationList.heap.get(j).patronID + ", ");
            }
            System.out.print(this.reservationList.heap.get(this.reservationList.heap.size() - 1).patronID);
        }
        System.out.println("]" + System.lineSeparator());

    }
    	
}
