/**
 * BookMyStay - Hotel Booking Management System (v2.0)
 * Focusing on Domain Modeling and Inheritance.
 */

// --- Abstract Domain Layer ---
abstract class Room {
    private String roomType;
    private double pricePerNight;
    private int capacity;

    public Room(String roomType, double pricePerNight, int capacity) {
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
    }

    // Abstract method to enforce specific behavior in subclasses
    public abstract void displayAmenities();

    // Getters for encapsulation
    public String getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public int getCapacity() { return capacity; }
}

// --- Concrete Room Implementations ---
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 100.0, 1);
    }

    @Override
    public void displayAmenities() {
        System.out.println("Amenities: High-speed Wi-Fi, Single Bed, Workspace.");
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 150.0, 2);
    }

    @Override
    public void displayAmenities() {
        System.out.println("Amenities: Queen Size Bed, Mini-bar, City View.");
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 300.0, 4);
    }

    @Override
    public void displayAmenities() {
        System.out.println("Amenities: King Size Bed, Separate Living Area, Kitchenette.");
    }
}

// --- Main Application Class ---
public class BookMyStay {

    // Use Case 2 Requirement: Static Availability Representation
    // These variables represent the "System State" separately from the "Domain Objects"
    private static int singleRoomInventory = 10;
    private static int doubleRoomInventory = 7;
    private static int suiteRoomInventory = 3;

    public static void main(String[] args) {
        printHeader();

        // 1. Instantiate Room Objects (Polymorphism)
        Room sRoom = new SingleRoom();
        Room dRoom = new DoubleRoom();
        Room stRoom = new SuiteRoom();

        // 2. Display Room Details and Static Availability
        printRoomStatus(sRoom, singleRoomInventory);
        printRoomStatus(dRoom, doubleRoomInventory);
        printRoomStatus(stRoom, suiteRoomInventory);

        System.out.println("\nApplication terminated successfully.");
    }

    private static void printHeader() {
        System.out.println("===========================================");
        System.out.println("        WELCOME TO BOOKMYSTAY v2.0        ");
        System.out.println("===========================================");
        System.out.println("Current Catalog & Availability:");
        System.out.println("-------------------------------------------");
    }

    private static void printRoomStatus(Room room, int count) {
        System.out.println("Type:     " + room.getRoomType());
        System.out.println("Price:    $" + room.getPricePerNight() + " /night");
        System.out.println("Capacity: " + room.getCapacity() + " Guest(s)");
        room.displayAmenities();
        System.out.println("IN STOCK: " + count);
        System.out.println("-------------------------------------------");
    }
}