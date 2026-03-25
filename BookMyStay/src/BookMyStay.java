import java.util.HashMap;
import java.util.Map;

/**
 * UseCase4RoomSearch - Version 4.0
 * Goal: Read-only search functionality and separation of concerns.
 */

// --- Domain Model ---
abstract class Room {
    private String roomType;
    private double pricePerNight;
    private String amenities;

    public Room(String roomType, double pricePerNight, String amenities) {
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
    }

    public String getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public String getAmenities() { return amenities; }
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single", 100.0, "WiFi, Single Bed, Desk"); }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double", 150.0, "Mini-bar, Queen Bed, TV"); }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite", 300.0, "King Bed, Kitchenette, Balcony"); }
}

// --- Inventory Manager (State Holder) ---
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void initializeRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllInventory() {
        // Returning a copy or the map for read-only purposes
        return new HashMap<>(inventory);
    }
}

// --- Search Service (Read-Only Logic) ---
class SearchService {
    private RoomInventory inventory;
    private Map<String, Room> roomTemplates;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
        this.roomTemplates = new HashMap<>();

        // Pre-loading room details (Domain Model Usage)
        roomTemplates.put("Single", new SingleRoom());
        roomTemplates.put("Double", new DoubleRoom());
        roomTemplates.put("Suite", new SuiteRoom());
    }

    public void searchAvailableRooms() {
        System.out.println("\n--- Guest Search Results (Available Only) ---");
        boolean found = false;

        for (String type : roomTemplates.keySet()) {
            int count = inventory.getAvailability(type);

            // Validation Logic: Only show rooms with availability > 0
            if (count > 0) {
                Room details = roomTemplates.get(type);
                displayRoomCard(details, count);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Sorry, no rooms are currently available.");
        }
    }

    private void displayRoomCard(Room room, int availableCount) {
        System.out.println("[" + room.getRoomType() + " Room]");
        System.out.println(" > Price: $" + room.getPricePerNight());
        System.out.println(" > Amenities: " + room.getAmenities());
        System.out.println(" > Availability: " + availableCount + " left");
        System.out.println("-------------------------------------------");
    }
}

// --- Main Application ---
public class BookMyStay {
    public static void main(String[] args) {
        System.out.println("BookMyStay v4.0 - Search & Discovery System");

        // 1. Setup Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.initializeRoomType("Single", 5);
        inventory.initializeRoomType("Double", 0); // Out of stock
        inventory.initializeRoomType("Suite", 2);

        // 2. Initialize Search Service
        SearchService searchService = new SearchService(inventory);

        // 3. Guest performs a search
        // Note: The Double Room should be filtered out automatically
        searchService.searchAvailableRooms();

        System.out.println("Search complete. No system state was modified.");
    }
}