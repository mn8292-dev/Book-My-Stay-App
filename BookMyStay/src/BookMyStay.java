import java.util.*;
import java.util.concurrent.*;

/**
 * BookMyStay - Final Integrated Version
 * Features: Multi-threading, Centralized Inventory, FIFO Queuing, and Unique Sets.
 */

// --- Domain Model ---
abstract class Room {
    private String type;
    private double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() { return type; }
    public double getPrice() { return price; }
    public abstract void showDescription();
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite", 350.0); }
    @Override public void showDescription() { System.out.print("Luxury King Bed & Balcony"); }
}

// --- Core System Engine ---
public class BookMyStay {
    // 1. Centralized Inventory (Shared State)
    private static final Map<String, Integer> inventory = new ConcurrentHashMap<>();

    // 2. Allocation Tracking (Prevents Double-Booking)
    private static final Map<String, Set<String>> allocatedRooms = new ConcurrentHashMap<>();

    // 3. FIFO Request Queue (Fairness)
    private static final Queue<String> requestQueue = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== WELCOME TO BOOKMYSTAY SYSTEM ==========");

        // Initialize Inventory
        inventory.put("Suite", 2);
        allocatedRooms.put("Suite", new HashSet<>());

        // Simulate incoming guest requests
        String[] guests = {"Alice", "Bob", "Charlie", "Diana"};
        for (String guest : guests) {
            requestQueue.add(guest);
            System.out.println("QUEUE: Request received from " + guest);
        }

        System.out.println("\nStarting Thread-Safe Allocation Engine...\n");

        // Process Queue using multiple threads (Simulating concurrent processing)
        List<Thread> activeThreads = new ArrayList<>();
        while (!requestQueue.isEmpty()) {
            String currentGuest = requestQueue.poll();
            Thread t = new Thread(() -> processBooking(currentGuest, "Suite"));
            activeThreads.add(t);
            t.start();
        }

        // Ensure all threads finish before showing final report
        for (Thread t : activeThreads) t.join();

        generateFinalReport();
    }

    /**
     * Critical Section: Synchronized to ensure inventory consistency
     * and unique room ID generation.
     */
    private static synchronized void processBooking(String guest, String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            // Generate Unique Room ID
            String roomID = roomType.toUpperCase() + "-" + (101 + allocatedRooms.get(roomType).size());

            // Update State
            allocatedRooms.get(roomType).add(roomID);
            inventory.put(roomType, available - 1);

            System.out.println("[CONFIRMED] " + guest + " assigned to " + roomID);
        } else {
            System.out.println("[REJECTED]  " + guest + " - No " + roomType + " rooms left.");
        }
    }

    private static void generateFinalReport() {
        System.out.println("\n--- ADMINISTRATIVE AUDIT REPORT ---");
        System.out.println("Remaining Inventory: " + inventory);
        System.out.println("Total Allocations:   " + allocatedRooms);
        System