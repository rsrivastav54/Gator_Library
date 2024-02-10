import java.util.ArrayList;
import java.util.List;

// RedBlackTree Class: To implement Red Black Tree and perform rotations 
public class RedBlackTree {
    Node root;

    public RedBlackTree() {
        root = null;
    }

    // To left rotate and balance a red black tree
    private void leftRotate(Node node) {
        Node temp = node.right;
        node.right = temp.left;
        if (temp.left != null) {
            temp.left.parent = node;
        }
        temp.parent = node.parent;
        if (node.parent == null) {
            root = temp;
        } else if (node == node.parent.left) {
            node.parent.left = temp;
        } else {
            node.parent.right = temp;
        }
        temp.left = node;
        node.parent = temp;
    }

    // To right rotate and balance a red black tree
    private void rightRotate(Node node) {
        Node temp = node.left;
        node.left = temp.right;
        if (temp.right != null) {
            temp.right.parent = node;
        }
        temp.parent = node.parent;
        if (node.parent == null) {
            root = temp;
        } else if (node == node.parent.right) {
            node.parent.right = temp;
        } else {
            node.parent.left = temp;
        }
        temp.right = node;
        node.parent = temp;
    }

	// We must rotate and flip colors when there is a Red-Red violation.
	// Continue adding entries to colorFlips, which will ultimately be returned.
    // The if-else statements consider various scenarios where a rotation is required.
    private int balanceDoubleRed(Node node) {
        int colorFlips = 0;
        while (node.parent != null && node.parent.isRed) {
            Node p = node.parent;
            if (p.parent == null) {
                colorFlips += p.isRed ? 1 : 0;
                p.isRed = false;
                return colorFlips;
            }
            Node gp = p.parent;
            if (gp.right != null && gp.right.isRed) {
                colorFlips += p.isRed ? 1 : 0;
                p.isRed = false;
                colorFlips += gp.right.isRed ? 1 : 0;
                gp.right.isRed = false;
                colorFlips += gp.isRed ? 0 : 1;
                gp.isRed = true;
                node = gp;
            } else {
                boolean xlc = (p == gp.left);
                boolean ylc = (node == p.left);
                if (xlc && ylc) {
                    colorFlips += p.isRed ? 1 : 0;
                    p.isRed = false;
                    colorFlips += gp.isRed ? 0 : 1;
                    gp.isRed = true;
                    rightRotate(gp);
                } else if (!xlc && ylc) {
                    node = p;
                    rightRotate(node);
                    colorFlips += node.parent.isRed ? 1 : 0;
                    node.parent.isRed = false;
                    colorFlips += node.parent.parent.isRed ? 0 : 1;
                    node.parent.parent.isRed = true;
                    leftRotate(node.parent.parent);
                } else if (!xlc && !ylc) {
                    colorFlips += p.isRed ? 1 : 0;
                    p.isRed = false;
                    colorFlips += gp.isRed ? 0 : 1;
                    gp.isRed = true;
                    leftRotate(gp);
                } else {
                    node = p;
                    leftRotate(node);
                    colorFlips += node.parent.isRed ? 1 : 0;
                    node.parent.isRed = false;
                    colorFlips += node.parent.parent.isRed ? 0 : 1;
                    node.parent.parent.isRed = true;
                    rightRotate(node.parent.parent);
                }
            }
        }
        colorFlips += root.isRed ? 1 : 0;
        root.isRed = false;
        return colorFlips;
    }

    private int deleteNode(int key) {
    	// Removes the node from the tree that contains the key and restores the tree's equilibrium.
        // Makes a function call to balanceDoubleBlack function to address the violation of double black nodes
    	// Continue adding values to colorFlips, which will ultimately be returned.
    	int colorFlips = 0;
        Node bookNode = search(key);
        if (bookNode == null) {
            System.out.println("Book not found in the library");
            return 0;
        }

        Node child, parent;
        boolean isRed;

        if (bookNode.left != null && bookNode.right != null) {
            Node replace = bookNode;
            replace = replace.right;
            while (replace.left != null) {
                replace = replace.left;
            }

            if (bookNode.parent != null) {
                if (bookNode.parent.left == bookNode) {
                    bookNode.parent.left = replace;
                } else {
                    bookNode.parent.right = replace;
                }
            } else {
                root = replace;
            }

            child = replace.right;
            parent = replace.parent;
            isRed = replace.isRed;

            if (parent == bookNode) {
                parent = replace;
            } else {
                if (child != null) {
                    child.parent = parent;
                }
                parent.left = child;
                replace.right = bookNode.right;
                bookNode.right.parent = replace;
            }

            replace.parent = bookNode.parent;
            colorFlips += (replace.isRed != bookNode.isRed) ? 1 : 0;
            replace.isRed = bookNode.isRed;
            replace.left = bookNode.left;
            bookNode.left.parent = replace;

            if (!isRed) {
                colorFlips += balanceDoubleBlack(child, parent);
            } else {
                if (child != null) {
                    colorFlips += child.isRed ? 1 : 0;
                    child.isRed = false;
                }
            }
         
            return colorFlips;
        }

        if (bookNode.left != null) {
            child = bookNode.left;
        } else {
            child = bookNode.right;
        }

        parent = bookNode.parent;
        isRed = bookNode.isRed;

        if (child != null) {
            child.parent = parent;
        }

        if (parent != null) {
            if (parent.left == bookNode) {
                parent.left = child;
            } else {
                parent.right = child;
            }
        } else {
            root = child;
        }

        if (!isRed) {
            colorFlips += balanceDoubleBlack(child, parent);
        } else {
            if (child != null) {
                colorFlips += child.isRed ? 1 : 0;
                child.isRed = false;
            }
        }

        return colorFlips;
    }


    private int balanceDoubleBlack(Node child, Node parent) {
        int flipCount = 0;
        while (child != null && child != root && child.isRed == false) {
            if (child == parent.left) {
                Node sibling = parent.right;
                if (sibling != null && sibling.isRed) {
                    flipCount += sibling.isRed ? 1 : 0;
                    sibling.isRed = false;
                    flipCount += parent.isRed ? 1 : 0;
                    parent.isRed = true;
                    leftRotate(parent);
                    sibling = parent.right;
                }
                if (sibling.left.isRed == false && sibling.right.isRed == false) {
                    flipCount += sibling.isRed ? 0 : 1;
                    sibling.isRed = true;
                    child = parent;
                    parent = child.parent;
                } else {
                    if (sibling.right.isRed == false) {
                        flipCount += sibling.left.isRed ? 1 : 0;
                        sibling.left.isRed = false;
                        flipCount += sibling.isRed ? 0 : 1;
                        sibling.isRed = true;
                        rightRotate(sibling);
                        sibling = parent.right;
                    }
                    flipCount += sibling.isRed != parent.isRed ? 1 : 0;
                    sibling.isRed = parent.isRed;
                    flipCount += parent.isRed ? 1 : 0;
                    parent.isRed = false;
                    flipCount += sibling.right.isRed ? 1 : 0;
                    sibling.right.isRed = false;
                    leftRotate(parent);
                    child = root;
                }
            } else {
                Node sibling = parent.left;
                if (sibling != null && sibling.isRed) {
                    flipCount += sibling.isRed ? 1 : 0;
                    sibling.isRed = false;
                    flipCount += parent.isRed ? 0 : 1;
                    parent.isRed = true;
                    rightRotate(parent);
                    sibling = parent.left;
                }
                if (sibling.left.isRed == false && sibling.right.isRed == false) {
                    flipCount += sibling.isRed ? 0 : 1;
                    sibling.isRed = true;
                    child = parent;
                    parent = child.parent;
                } else {
                    if (sibling.left.isRed == false) {
                        flipCount += sibling.right.isRed ? 1 : 0;
                        sibling.right.isRed = false;
                        flipCount += sibling.isRed ? 0 : 1;
                        sibling.isRed = true;
                        leftRotate(sibling);
                        sibling = parent.left;
                    }
                    flipCount += sibling.isRed != parent.isRed ? 1 : 0;
                    sibling.isRed = parent.isRed;
                    flipCount += parent.isRed ? 1 : 0;
                    parent.isRed = false;
                    flipCount += sibling.left.isRed ? 1 : 0;
                    sibling.left.isRed = false;
                    rightRotate(parent);
                    child = root;
                }
            }
        }
        if (child != null) {
            flipCount += child.isRed ? 1 : 0;
            child.isRed = false;
        }
        return flipCount;
    }

    public int insertBook(Node newNode) {
        int colorFlips = 0;
        if (root == null) {
            root = newNode;
            root.isRed = false;
            return 0;
        }
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            if (newNode.book.bookID < x.book.bookID) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        newNode.parent = y;
        if (y == null) {
            root = newNode;
        } else if (newNode.book.bookID < y.book.bookID) {
            y.left = newNode;
        } else {
            y.right = newNode;
        }
        colorFlips += balanceDoubleRed(newNode);
        // Print PreOrder traversal
        return colorFlips;
    }

    public int deleteBook(int key) {
        return deleteNode(key);
    }

    public Node search(int key) {
        Node node = root;
        while (node != null) {
            if (key == node.book.bookID) {
                return node;
            } else if (key < node.book.bookID) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    public void printBook(Node node) {
        node.printBook();
    }

    public void printBooks(int bookID1, int bookID2) {
        ArrayList<Node> books = new ArrayList<>();
        inorderTraversal(root, books, bookID1, bookID2);
        for (Node currBook : books) {
            currBook.printBook();
        }
    }

    void inorderTraversal(Node node, List<Node> books, int start, int end) {
        if (node == null) {
            return;
        }
        if (node.book.bookID > start) {
            inorderTraversal(node.left, books, start, end);
        }
        if (node.book.bookID >= start && node.book.bookID <= end) {
            books.add(node);
        }
        if (node.book.bookID < end) {
            inorderTraversal(node.right, books, start, end);
        }
    }
}
