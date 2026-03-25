import java.util.*;

/**
 * UseCase9ErrorHandlingValidation - Version 9.0
 * Goal: Robustness through Custom Exceptions and Input Validation.
 */

// --- Custom Exception Layer ---
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

class InvalidRoomTypeException extends BookingException {
    public InvalidRoomTypeException(String type) {
        super("Error: Room type '" + type + "' does not exist in our catalog.");
    }
}

class InsufficientInventoryException extends BookingException {
    public InsufficientInventoryException(String type) {
        super("Error: No inventory available for room type: " + type);
    }
}

// --- Validation and Core Logic ---
class BookingValidator {
    private Map<String, Integer> inventory;

    public BookingValidator(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void validateRequest(String roomType) throws BookingException {
        // 1. Validation for Null/Empty input
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new BookingException("Error: Room type cannot be empty.");
        }

        // 2. Validation for Existence (Case Sensitive)
        if (!inventory.containsKey(roomType)) {
            throw new InvalidRoomTypeException(roomType);
        }

        // 3. Validation for Availability (Guarding System State)
        if (inventory.get(roomType) <= 0) {
            throw new InsufficientInventoryException(roomType);
        }
    }
}

// --- Main Application ---
public class BookMyStay {
    public static void main(String[] args) {
        System.out.println("--- BookMyStay v9.0: Error Handling & Validation ---");

        // Initialize State
        Map<String, Integer> hotelInventory = new HashMap<>();
        hotelInventory.put("Single", 2);
        hotelInventory.put("Suite", 0); // Out of stock

        BookingValidator validator = new BookingValidator(hotelInventory);

        // Test Cases for Validation
        String[] testRequests = {"Single", "Penthouse", "Suite", "", null};

        for (String request : testRequests) {
            System.out.println("\nChecking request for: [" + request + "]");
            try {
                // Fail-Fast: Validate before any processing happens
                validator.validateRequest(request);

                // If we reach here, validation passed
                System.out.println("SUCCESS: Request is valid. Proceeding to allocation...");
                hotelInventory.put(request, hotelInventory.get(request) - 1);
                System.out.println("Updated Inventory: " + hotelInventory.get(request));

            } catch (BookingException e) {
                // Graceful Failure Handling
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println("An unexpected system error occurred.");
            }
        }

        System.out.println("\n--- Validation Test Complete ---");
        System.out.println("System remains stable and inventory is consistent.");
    }
}