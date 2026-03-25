import java.util.*;

/**
 * UseCase6RoomAllocationService - Version 6.0
 * Goal: Safe room allocation using Set for uniqueness and FIFO processing.
 */

// --- Domain Model ---
class ReservationRequest {
    String guestName;
    String roomType;

    public ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// --- Allocation & Inventory Service ---
class BookingService {
    // Inventory: Room Type -> Count
    private Map<String, Integer> inventory = new HashMap<>();

    // Allocations: Room Type -> Set of Unique Room IDs (Prevents Double-Booking)
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public void setupInventory(String type, int count) {
        inventory.put(type, count);
        allocatedRooms.put(type, new HashSet<>()); // Initialize empty set for each type
    }

    public void processRequest(ReservationRequest request) {
        String type = request.roomType;
        int available = inventory.getOrDefault(type, 0);

        System.out.println("\nProcessing: " + request.guestName + " for " + type);

        if (available > 0) {
            // 1. Generate a Unique Room ID (e.g., SINGLE-101)
            String roomID = type.toUpperCase() + "-" + (100 + allocatedRooms.get(type).size() + 1);

            // 2. Uniqueness Enforcement using Set
            if (!allocatedRooms.get(type).contains(roomID)) {
                allocatedRooms.get(type).add(roomID); // Add to Set

                // 3. Inventory Synchronization (Atomic-like update)
                inventory.put(type, available - 1);

                System.out.println("CONFIRMED: Room " + roomID + " assigned to " + request.guestName);
            } else {
                System.out.println("ERROR: Room ID Collision detected!");
            }
        } else {
            System.out.println("REJECTED: No " + type + " rooms available for " + request.guestName);
        }
    }

    public void displayStatus() {
        System.out.println("\n--- Final System State ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> Available: " + inventory.get(type) +
                    " | Assigned IDs: " + allocatedRooms.get(type));
        }
    }
}

// --- Main Application ---
public class BookMyStay {
    public static void main(String[] args) {
        System.out.println("BookMyStay v6.0 - Room Allocation & Set Uniqueness");

        // 1. Initialize Services
        BookingService bookingService = new BookingService();
        bookingService.setupInventory("Single", 2); // Limited supply for testing
        bookingService.setupInventory("Suite", 1);

        // 2. Setup FIFO Request Queue (From Use Case 5)
        Queue<ReservationRequest> requestQueue = new LinkedList<>();
        requestQueue.add(new ReservationRequest("Alice", "Suite"));
        requestQueue.add(new ReservationRequest("Bob", "Single"));
        requestQueue.add(new ReservationRequest("Charlie", "Suite")); // Should be rejected (Sold out)
        requestQueue.add(new ReservationRequest("Diana", "Single"));

        // 3. Process Queue in FIFO Order
        System.out.println("Starting Allocation Engine...");
        while (!requestQueue.isEmpty()) {
            ReservationRequest nextRequest = requestQueue.poll();
            bookingService.processRequest(nextRequest);
        }

        // 4. Final Audit
        bookingService.displayStatus();
    }
}