import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// MinHeap class: To maintain min-priority heap of reservations based on patron priority
public class MinHeap {
    ArrayList<Reservation> heap;
    Map<Integer, Integer> patronMap;

    public MinHeap() {
        heap = new ArrayList<>();
        patronMap = new HashMap<>();
    }
    
    // If book is already borrowed, then add reservation to wait list
    public void addReservation(Reservation reservation) {
        heap.add(reservation);
        patronMap.put(reservation.patronID, heap.size() - 1);
        upHeapify(heap.size() - 1);
    }

    // Return patronID of user that is next in the wait list and remove the reservation
    public int removeReservation() {
        if (heap.isEmpty()) {
            System.out.println("Heap is empty");
            return -1;
        }

        int userID = heap.get(0).patronID;
        patronMap.remove(userID);

        replace(0, heap.size() - 1);
        heap.remove(heap.size() - 1);

        downHeapify(0);

        return userID;
    }

    // To determine parent index for a node
    private int parent(int idx) {
        return (idx - 1) / 2;
    }

    // To determine left child index for a node
    private int leftChild(int idx) {
        return 2 * idx + 1;
    }

    // To determine right child index for a node
    private int rightChild(int idx) {
        return 2 * idx + 2;
    }

    // To re-arrange heap after node insertion
    private void upHeapify(int idx) {
        if (idx > 0 &&
                (heap.get(parent(idx)).priority > heap.get(idx).priority ||
                        (heap.get(parent(idx)).priority == heap.get(idx).priority &&
                                heap.get(parent(idx)).timestamp.isAfter(heap.get(idx).timestamp)))) {
            replace(idx, parent(idx));
            upHeapify(parent(idx));
        }
    }

    // To re-arrange heap after node deletion
    private void downHeapify(int idx) {
        int minIdx = idx;
        if (leftChild(idx) < heap.size() &&
                (heap.get(leftChild(idx)).priority < heap.get(minIdx).priority ||
                        (heap.get(leftChild(idx)).priority == heap.get(minIdx).priority &&
                                heap.get(leftChild(idx)).timestamp.isBefore(heap.get(minIdx).timestamp)))) {
            minIdx = leftChild(idx);
        }
        if (rightChild(idx) < heap.size() &&
                (heap.get(rightChild(idx)).priority < heap.get(minIdx).priority ||
                        (heap.get(rightChild(idx)).priority == heap.get(minIdx).priority &&
                                heap.get(rightChild(idx)).timestamp.isBefore(heap.get(minIdx).timestamp)))) {
            minIdx = rightChild(idx);
        }
        if (minIdx != idx) {
            replace(idx, minIdx);
            downHeapify(minIdx);
        }
    }

    private void replace(int idx1, int idx2) {
        Reservation temp = heap.get(idx1);
        heap.set(idx1, heap.get(idx2));
        heap.set(idx2, temp);
        patronMap.put(heap.get(idx1).patronID, idx1);
        patronMap.put(heap.get(idx2).patronID, idx2);
    }

}
