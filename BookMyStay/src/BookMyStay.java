import java.util.*;

/**
 * UseCase8BookingHistoryReport - Version 8.0
 * Goal: Historical tracking and administrative reporting using Lists.
 */

// --- Domain Model: The Final Reservation Object ---
class ConfirmedBooking {
    private String bookingId;
    private String guestName;
    private String roomType;
    private double totalCost;

    public ConfirmedBooking(String bookingId, String guestName, String roomType, double totalCost) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.totalCost = totalCost;
    }

    public double getTotalCost() { return totalCost; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return String.format("ID: %-12s | Guest: %-10s | Type: %-8s | Revenue: $%.2f",
                bookingId, guestName, roomType, totalCost);
    }
}

// --- History Service: The Persistence Layer ---
class BookingHistory {
    // List preserves the chronological order of confirmation
    private List<ConfirmedBooking> history = new ArrayList<>();

    public void archiveBooking(ConfirmedBooking booking) {
        history.add(booking);
    }

    public List<ConfirmedBooking> getRecords() {
        // Return an unmodifiable view to ensure reporting doesn't change history
        return Collections.unmodifiableList(history);
    }
}

// --- Reporting Service: The Admin View ---
class ReportingService {
    public void generateSummaryReport(List<ConfirmedBooking> records) {
        System.out.println("\n========= ADMIN OPERATIONS REPORT =========");
        if (records.isEmpty()) {
            System.out.println("No records found in history.");
            return;
        }

        double totalRevenue = 0;
        Map<String, Integer> popularityMap = new HashMap<>();

        for (ConfirmedBooking b : records) {
            System.out.println(b);
            totalRevenue += b.getTotalCost();

            // Track room type popularity
            popularityMap.put(b.getRoomType(), popularityMap.getOrDefault(b.getRoomType(), 0) + 1);
        }

        System.out.println("-------------------------------------------");
        System.out.println("TOTAL CONFIRMED BOOKINGS: " + records.size());
        System.out.println("TOTAL REVENUE GENERATED : $" + String.format("%.2f", totalRevenue));
        System.out.println("ROOM TYPE POPULARITY    : " + popularityMap);
        System.out.println("===========================================");
    }
}

// --- Main Application ---
public class BookMyStay {
    public static void main(String[] args) {
        System.out.println("BookMyStay v8.0 - Booking History & Reporting Service");

        // 1. Initialize our storage and reporting components
        BookingHistory historyStore = new BookingHistory();
        ReportingService adminService = new ReportingService();

        // 2. Simulate incoming confirmed bookings (usually from Use Case 6/7)
        System.out.println("Archiving confirmed transactions...");
        historyStore.archiveBooking(new ConfirmedBooking("SUITE-101", "Alice", "Suite", 380.0));
        historyStore.archiveBooking(new ConfirmedBooking("SINGLE-101", "Bob", "Single", 115.0));
        historyStore.archiveBooking(new ConfirmedBooking("SINGLE-102", "Charlie", "Single", 100.0));
        historyStore.archiveBooking(new ConfirmedBooking("DOUBLE-101", "Diana", "Double", 150.0));

        // 3. Admin requests a report
        // Notice: The ReportingService doesn't own the data; it just processes the list
        adminService.generateSummaryReport(historyStore.getRecords());

        System.out.println("\nReport generated successfully. Data persistence mindset established.");
    }
}