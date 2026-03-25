import java.util.*;

/**
 * UseCase7AddOnServiceSelection - Version 7.0
 * Goal: Handle optional services using Map and List combination.
 */

// --- Domain Model: Add-On Services ---
class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}

// --- Manager for Optional Features ---
class AddOnManager {
    // Map: ReservationID -> List of selected services
    private Map<String, List<AddOnService>> selections = new HashMap<>();

    // Add a service to a specific reservation
    public void addService(String reservationId, AddOnService service) {
        // ComputeIfAbsent is a clean way to initialize the list if it doesn't exist
        selections.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
        System.out.println("Service Added: [" + service.getName() + "] to Reservation: " + reservationId);
    }

    // Calculate total cost of all add-ons for a reservation
    public double getTotalAddOnCost(String reservationId) {
        List<AddOnService> services = selections.get(reservationId);
        if (services == null) return 0.0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.getPrice();
        }
        return total;
    }

    public void displayAddOns(String reservationId) {
        List<AddOnService> services = selections.get(reservationId);
        if (services != null && !services.isEmpty()) {
            System.out.println("Selected Add-Ons for " + reservationId + ": " + services);
            System.out.println("Total Add-On Cost: $" + getTotalAddOnCost(reservationId));
        } else {
            System.out.println("No add-ons selected for " + reservationId);
        }
    }
}

// --- Main Application ---
public class BookMyStay{
    public static void main(String[] args) {
        System.out.println("--- BookMyStay v7.0: Add-On Service Selection ---");

        // 1. Initialize Manager and available services
        AddOnManager addOnManager = new AddOnManager();
        AddOnService breakfast = new AddOnService("Buffet Breakfast", 25.0);
        AddOnService spa = new AddOnService("Spa Treatment", 80.0);
        AddOnService wifi = new AddOnService("Premium WiFi", 15.0);

        // 2. Assume we have existing Reservation IDs from Use Case 6
        String resAlice = "SUITE-101";
        String resBob = "SINGLE-101";

        System.out.println("\n--- Processing Guest Selections ---");

        // Alice selects multiple services (One-to-Many)
        addOnManager.addService(resAlice, breakfast);
        addOnManager.addService(resAlice, spa);

        // Bob selects one service
        addOnManager.addService(resBob, wifi);

        // 3. Display and Verify Costs
        System.out.println("\n--- Final Billing Summary (Add-Ons Only) ---");
        addOnManager.displayAddOns(resAlice);
        System.out.println("-------------------------------------------");
        addOnManager.displayAddOns(resBob);

        System.out.println("\nCore booking state remains untouched. System extended successfully.");
    }
}