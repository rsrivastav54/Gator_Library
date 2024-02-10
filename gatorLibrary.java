import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class gatorLibrary {
    private int colorFlips = 0;
    private RedBlackTree redBlackTree = new RedBlackTree();
    
    // Program Execution begins here. Takes in class name, followed by input file name
	public static void main(String[] args) throws IOException, IOException {
        gatorLibrary gatorLibrary = new gatorLibrary();

        if (args.length < 1) {
            System.out.println("Please enter valid number of arguments");
            System.out.println("Usage: java gatorLibrary file_name");
            System.exit(1);
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            // Create the output file name
            String outputFileName = args[0] + "_output_file.txt";
            try (PrintWriter writer = new PrintWriter(outputFileName)) {
                // Redirect System.out to the output file
                System.setOut(new PrintStream(new FileOutputStream(outputFileName)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        try (Scanner scanner = new Scanner(new File(args[0]))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    processCommand(gatorLibrary, line);
                }
            }
        } catch (FileNotFoundException e) {
	            System.out.println("Error opening file");
	            System.exit(1);
        	}
        }
    }

	// Performs operations based on commands in input file
    private static void processCommand(gatorLibrary gatorLibrary, String command) {
        String[] lineCommand = command.split("\\(", 2);
        String methodName = lineCommand[0].trim();
        String paramsLine = lineCommand[1].substring(0, lineCommand[1].length() - 1).trim();

        String[] params = paramsLine.split(",");
        List<String> paramsList = new ArrayList<>();

        for (String p : params) {
            String processedParam = p.trim();
            if (!processedParam.isEmpty() && (processedParam.charAt(0) == '\"' || processedParam.charAt(0) == '\'')) {
                processedParam = processedParam.substring(1, processedParam.length() - 1);
            }
            paramsList.add(processedParam);
        }

        switch (methodName) {
            case "PrintBook":
                int bookID = Integer.parseInt(paramsList.get(0));
                gatorLibrary.printBook(bookID);
                break;

            case "PrintBooks":
                int bookID1 = Integer.parseInt(paramsList.get(0));
                int bookID2 = Integer.parseInt(paramsList.get(1));
                gatorLibrary.printBooks(bookID1, bookID2);
                break;

            case "InsertBook":
                int insertBookID = Integer.parseInt(paramsList.get(0));
                String bookName = paramsList.get(1);
                String authorName = paramsList.get(2);
                boolean availabilityStatus = paramsList.get(3).equals("Yes") ? true : false;
                gatorLibrary.insertBook(insertBookID, bookName, authorName, availabilityStatus);
                break;

            case "BorrowBook":
                int patronID = Integer.parseInt(paramsList.get(0));
                int borrowBookID = Integer.parseInt(paramsList.get(1));
                int patronPriority = Integer.parseInt(paramsList.get(2));
                gatorLibrary.borrowBook(patronID, borrowBookID, patronPriority);
                break;

            case "ReturnBook":
                int returnPatronID = Integer.parseInt(paramsList.get(0));
                int returnBookID = Integer.parseInt(paramsList.get(1));
                gatorLibrary.returnBook(returnPatronID, returnBookID);
                break;

            case "DeleteBook":
                int deleteBookID = Integer.parseInt(paramsList.get(0));
                gatorLibrary.deleteBook(deleteBookID);
                break;

            case "FindClosestBook":
                int closestBookID = Integer.parseInt(paramsList.get(0));
                gatorLibrary.findClosestBook(closestBookID);
                break;

            case "ColorFlipCount":
                gatorLibrary.colorFlipCount();
                break;

            case "Quit":
                gatorLibrary.quit();
                break;

            default:
                System.out.println("Invalid Input\n");
                System.exit(1);
        }
    }

    private static int getLeastBigger(List<Node> books, int bookID) {
        int lptr = 0, rptr = books.size() - 1;
        while (lptr <= rptr) {
            int mid = lptr + (rptr - lptr) / 2;
            if (books.get(mid).book.bookID < bookID) {
                lptr = mid + 1;
            } else {
                rptr = mid - 1;
            }
        }
        return lptr;
    }

    private void printBook(int bookID) {
        Node bookNode = redBlackTree.search(bookID);
        if (bookNode == null) {
            System.out.println("Book " + bookID + " not found in the library\n");
            return;
        }
        redBlackTree.printBook(bookNode);
    }

    private void printBooks(int bookID1, int bookID2) {
        if (bookID1 > bookID2) {
            System.out.println("Range is invalid");
            return;
        }
        redBlackTree.printBooks(bookID1, bookID2);
    }

    private void insertBook(int bookID, String bookName, String authorName, boolean availabilityStatus) {
        if (redBlackTree.search(bookID) != null) {
            System.out.println("BookID already exists");
            return;
        }
        Node bookNode = new Node();
        bookNode.book = new Book(bookID, bookName, authorName, availabilityStatus);
        colorFlips += redBlackTree.insertBook(bookNode);
    }

    private void borrowBook(int patronID, int bookID, int patronPriority) {
        Node bookNode = redBlackTree.search(bookID);
        if (bookNode == null) {
            System.out.println("Book " + bookID + " not found in the library\n");
            return;
        }
        if (bookNode.book.borrowerID == patronID) {
            System.out.println("Book " + bookID + " Reserved by Patron " + patronID + "\n");
            return;
        }
        if (!bookNode.book.isAvailable) {
            if (bookNode.reservationList.patronMap.containsKey(patronID)) {
                System.out.println("Patron already in the waitlist\n");
                return;
            }
            bookNode.reservationList.addReservation(new Reservation(patronID, patronPriority));
            System.out.println("Book " + bookID + " Reserved by Patron " + patronID + "\n");
            return;
        }
        bookNode.book.isAvailable = false;
        bookNode.book.borrowerID = patronID;
        System.out.println("Book " + bookID + " borrowed by Patron " + patronID + "\n");
    }

    private void returnBook(int patronID, int bookID) {
        Node bookNode = redBlackTree.search(bookID);
        if (bookNode == null) {
            System.out.println("Book " + bookID + " not found in the library\n");
            return;
        }
        if (bookNode.book.isAvailable) {
            System.out.println("Book already available in the Library\n");
            return;
        }
        if (bookNode.book.borrowerID != patronID) {
            System.out.println("Invalid return\n");
            return;
        }
        bookNode.book.isAvailable = true;
        bookNode.book.borrowerID = 0;
        System.out.println("Book " + bookID + " Returned by Patron " + patronID + "\n");
        if (!bookNode.reservationList.heap.isEmpty()) {
            int borrowerID = bookNode.reservationList.removeReservation();
            bookNode.book.borrowerID = borrowerID;
            bookNode.book.isAvailable = false;
            System.out.println("Book " + bookID + " Allotted to Patron " + borrowerID + "\n");
        }
    }

    private void deleteBook(int bookID) {
    	Node bookNode = redBlackTree.search(bookID);
        if (bookNode == null) {
            System.out.println("Book not found in the Library\n");
            return;
        }
        System.out.print("Book " + bookID + " is no longer available. ");
        if (!bookNode.reservationList.heap.isEmpty()) {
            String reservations = bookNode.reservationList.heap.size() > 1 ? "Reservations" : "Reservation";
            String patrons = bookNode.reservationList.heap.size() > 1 ? "Patrons " : "Patron ";
            String hasHave = bookNode.reservationList.heap.size() > 1 ? " have" : " has";
            System.out.print(reservations + " made by " + patrons);
            for (int i = 0; i < bookNode.reservationList.heap.size() - 1; i++) {
                System.out.print(bookNode.reservationList.heap.get(i).patronID + ", ");
            }
            System.out.println(bookNode.reservationList.heap.get(bookNode.reservationList.heap.size() - 1).patronID + hasHave + " been cancelled!\n");
        } else {
            System.out.println("\n");
        }
        colorFlips += redBlackTree.deleteBook(bookID);
    }

    private void findClosestBook(int bookID) {
        if (redBlackTree.root == null) {
            System.out.println("No books in library\n");
            return;
        }
        if (redBlackTree.search(bookID) != null) {
            printBook(bookID);
            return;
        }
        List<Node> books = new ArrayList<>();
        redBlackTree.inorderTraversal(redBlackTree.root, books, Integer.MIN_VALUE, Integer.MAX_VALUE);
        int idx = getLeastBigger(books, bookID);
        if (idx == -1) {
            printBook(0);
        } else if (idx == books.size()) {
            printBook(books.get(books.size() - 1).book.bookID);
        } else {
            if (bookID - books.get(idx - 1).book.bookID == books.get(idx).book.bookID - bookID) {
                printBook(books.get(idx - 1).book.bookID);
                printBook(books.get(idx).book.bookID);
            } else if (bookID - books.get(idx - 1).book.bookID < books.get(idx).book.bookID - bookID) {
                printBook(books.get(idx - 1).book.bookID);
            } else {
                printBook(books.get(idx).book.bookID);
            }
        }
    }

    private void colorFlipCount() {
        System.out.println("Color Flip Count: " + colorFlips + "\n");
    }

    private void quit() {
        System.out.println("Program Terminated!!");
        System.exit(0);
    }

}
