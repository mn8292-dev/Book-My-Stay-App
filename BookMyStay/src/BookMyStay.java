import java.util.HashMap;
import java.util.Map;

/**
 * UseCase3InventorySetup - Version 3.0
 * Focus: Centralized Room Inventory Management using HashMap.
 */

// --- Domain Model (From Use Case 2) ---
abstract class Room {
    private String roomType;
    private double pricePerNight;

    public Room(String roomType, double pricePerNight) {
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public abstract void displayFeatures();
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single", 100.0); }
    @Override public void displayFeatures() { System.out.print("Basic WiFi, Single Bed"); }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double", 150.0); }
    @Override public void displayFeatures() { System.out.print("Mini-bar, Queen Bed"); }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite", 300.0); }
    @Override public void displayFeatures() { System.out.print("King Bed, Kitchenette"); }
}

// --- New Inventory Component (Use Case 3) ---
class RoomInventory {
    // Single Source of Truth: Mapping Room Type -> Available Count
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    // Register/Initialize room types in the system
    public void initializeRoomType(String type, int initialCount) {
        inventory.put(type, initialCount);
    }

    // O(1) Lookup: Check availability
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Controlled Update: Method to adjust stock (e.g., after a booking)
    public void updateAvailability(String type, int change) {
        if (inventory.containsKey(type)) {
            int current = inventory.get(type);
            inventory.put(type, current + change);
        }
    }

    public void displayFullInventory() {
        System.out.println("\n--- Current Inventory Status ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }
    }
}

// --- Main Application ---
public class BookMyStay{
    public static void main(String[] args) {
        System.out.println("Initializing BookMyStay v3.0 - Centralized Inventory...");

        // 1. Setup Inventory Manager
        RoomInventory manager = new RoomInventory();

        // 2. Register Rooms (Replacing scattered variables)
        manager.initializeRoomType("Single", 10);
        manager.initializeRoomType("Double", 7);
        manager.initializeRoomType("Suite", 3);

        // 3. Display Initial State
        System.out.println("Initial Availability for Double: " + manager.getAvailability("Double"));
        manager.displayFullInventory();

        // 4. Demonstrate Controlled Update (Simulating a booking)
        System.out.println("\nAction: Booking 1 Double Room...");
        manager.updateAvailability("Double", -1);

        // 5. Verify Consistency
        System.out.println("Updated Availability for Double: " + manager.getAvailability("Double"));
        manager.displayFullInventory();

        System.out.println("\nInventory state maintained successfully.");
    }
}