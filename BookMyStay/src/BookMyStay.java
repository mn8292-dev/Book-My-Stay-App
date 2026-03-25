import java.util.*;

/**
 * UseCase10BookingCancellation - Version 10.0
 * Goal: Safe state reversal using Stack for rollback and Map for synchronization.
 */

class Reservation {
    String id;
    String type;
    String guest;

    public Reservation(String id, String type, String guest) {
        this.id = id;
        this.type = type;
        this.guest = guest;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + guest + " (" + type + ")";
    }
}

class CancellationService {
    private Map<String, Integer> inventory;
    private Map<String, Reservation> activeBookings;
    // Stack tracks IDs for LIFO rollback (Last-In-First-Out undo)
    private Stack<String> cancellationHistory;

    public CancellationService(Map<String, Integer> inventory, Map<String, Reservation> activeBookings) {
        this.inventory = inventory;
        this.activeBookings = activeBookings;
        this.cancellationHistory = new Stack<>();
    }

    public void cancelBooking(String bookingId) {
        System.out.println("\nInitiating cancellation for ID: " + bookingId);

        // 1. Validation: Ensure the reservation exists before rolling back
        if (!activeBookings.containsKey(bookingId)) {
            System.err.println("Error: Cancellation failed. Booking ID not found.");
            return;
        }

        // 2. State Retrieval
        Reservation res = activeBookings.get(bookingId);
        String roomType = res.type;

        // 3. Controlled Mutation: LIFO Rollback
        activeBookings.remove(bookingId); // Remove from active records
        inventory.put(roomType, inventory.get(roomType) + 1); // Restore inventory
        cancellationHistory.push(bookingId); // Record the rollback action

        System.out.println("SUCCESS: Inventory restored for " + roomType + ".");
        System.out.println("Rollback Log: ID " + bookingId + " moved to cancellation stack.");
    }

    public void displayState() {
        System.out.println("\n--- Final System State ---");
        System.out.println("Current Inventory: " + inventory);
        System.out.println("Active Bookings  : " + activeBookings.values());
        System.out.println("Recent Rollbacks : " + cancellationHistory);
        System.out.println("--------------------------");
    }
}

public class BookMyStay {
    public static void main(String[] args) {
        System.out.println("BookMyStay v10.0 - State Reversal & Rollback System");

        // Initialize System State
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Suite", 0); // Currently sold out
        inventory.put("Single", 5);

        Map<String, Reservation> activeBookings = new HashMap<>();
        activeBookings.put("S-101", new Reservation("S-101", "Suite", "Alice"));
        activeBookings.put("S-102", new Reservation("S-102", "Suite", "Bob"));

        CancellationService service = new CancellationService(inventory, activeBookings);

        // Test Case 1: Valid Cancellation
        service.cancelBooking("S-101");

        // Test Case 2: Attempt to cancel a non-existent booking
        service.cancelBooking("X-999");

        // Test Case 3: Cancel another valid booking
        service.cancelBooking("S-102");

        // Final Audit
        service.displayState();
    }
}