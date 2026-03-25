import java.util.*;

/**
 * UseCase11ConcurrentBookingSimulation - Version 11.0
 * Goal: Ensure Thread Safety and prevent Race Conditions in a multi-user environment.
 */

class BookingProcessor extends Thread {
    private String guestName;
    private String roomType;
    private InventoryManager inventoryManager;

    public BookingProcessor(String guestName, String roomType, InventoryManager manager) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.inventoryManager = manager;
    }

    @Override
    public void run() {
        // Simulating network latency
        try { Thread.sleep((long) (Math.random() * 100)); } catch (InterruptedException e) {}

        inventoryManager.bookRoom(guestName, roomType);
    }
}

class InventoryManager {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRooms(String type, int count) {
        inventory.put(type, count);
    }

    /**
     * The 'synchronized' keyword creates a Critical Section.
     * Only one thread can execute this method at a time for this instance.
     */
    public synchronized void bookRoom(String guest, String type) {
        int available = inventory.getOrDefault(type, 0);

        System.out.println("[Thread: " + Thread.currentThread().getName() + "] Checking for " + guest + "...");

        if (available > 0) {
            // Simulate processing time within the critical section
            try { Thread.sleep(50); } catch (InterruptedException e) {}

            inventory.put(type, available - 1);
            System.out.println(">>> SUCCESS: Room confirmed for " + guest + ". Remaining " + type + ": " + (available - 1));
        } else {
            System.out.println(">>> FAILURE: Sold out! Could not book for " + guest);
        }
    }

    public void displayFinalState() {
        System.out.println("\nFinal Inventory State: " + inventory);
    }
}

public class BookMyStay {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- BookMyStay v11.0: Concurrent Booking Simulation ---");

        InventoryManager manager = new InventoryManager();
        // Only 2 Luxury Suites available, but 5 guests will try to book at once!
        manager.addRooms("Luxury Suite", 2);

        System.out.println("Initial Inventory: 2 Luxury Suites available.\n");

        // Simulating 5 concurrent guests
        String[] guests = {"Alice", "Bob", "Charlie", "Diana", "Edward"};
        List<Thread> threads = new ArrayList<>();

        for (String guest : guests) {
            Thread t = new BookingProcessor(guest, "Luxury Suite", manager);
            threads.add(t);
            t.start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            t.join();
        }

        manager.displayFinalState();
        System.out.println("\nConcurrency test complete. Thread safety maintained.");
    }
}