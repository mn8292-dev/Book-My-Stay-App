import java.util.*;

/**
 * UseCase5BookingRequestQueue - Version 5.0
 * Goal: Implement a FIFO Queue to handle booking requests fairly.
 */

// --- Domain Model: The Booking Request ---
class ReservationRequest {
    private String guestName;
    private String roomType;
    private int nights;

    public ReservationRequest(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    @Override
    public String toString() {
        return String.format("Request[Guest: %-10s | Type: %-8s | Nights: %d]",
                guestName, roomType, nights);
    }
}

// --- Request Management Component ---
class BookingQueueManager {
    // Queue follows FIFO: First-In, First-Out
    private Queue<ReservationRequest> requestQueue;

    public BookingQueueManager() {
        this.requestQueue = new LinkedList<>();
    }

    // Add request to the back of the line
    public void submitRequest(ReservationRequest request) {
        requestQueue.add(request);
        System.out.println("ADMITTED: " + request.toString());
    }

    // Check how many people are waiting
    public int getQueueSize() {
        return requestQueue.size();
    }

    // Display the current line without removing anyone
    public void displayQueue() {
        System.out.println("\n--- Current Booking Queue (Waiting for Processing) ---");
        if (requestQueue.isEmpty()) {
            System.out.println("The queue is currently empty.");
        } else {
            int position = 1;
            for (ReservationRequest req : requestQueue) {
                System.out.println(position + ". " + req);
                position++;
            }
        }
        System.out.println("------------------------------------------------------");
    }

    // Method to be used in the next Use Case for processing
    public ReservationRequest nextInLine() {
        return requestQueue.poll();
    }
}

// --- Main Application ---
public class BookMyStay {
    public static void main(String[] args) {
        System.out.println("BookMyStay v5.0 - Fair Request Intake System");
        System.out.println("Initializing Booking Queue...\n");

        BookingQueueManager queueManager = new BookingQueueManager();

        // Simulating simultaneous requests arriving at the system
        // These are added in a specific order to demonstrate FIFO
        queueManager.submitRequest(new ReservationRequest("Alice", "Suite", 3));
        queueManager.submitRequest(new ReservationRequest("Bob", "Single", 1));
        queueManager.submitRequest(new ReservationRequest("Charlie", "Double", 2));
        queueManager.submitRequest(new ReservationRequest("Diana", "Suite", 5));

        // Display the state of the system
        queueManager.displayQueue();

        System.out.println("Total requests waiting: " + queueManager.getQueueSize());
        System.out.println("\nStatus: Requests are safely queued. No inventory has been modified yet.");
        System.out.println("Ready for Allocation Engine (Use Case 6).");
    }
}