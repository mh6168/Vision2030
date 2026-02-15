package My_Tickets;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Login.OracleConnection;

public class handleTickets {

    // Ticket model fields
    private String ticketType;      // "GEM", "MONORAIL", "AIRTAXI"
    private int ticketId;
    private int userId;
    private String description;
    private Timestamp eventTime;
    private double totalPrice;

    // Constructor
    public handleTickets(String ticketType, int ticketId, int userId,
                         String description, Timestamp eventTime, double totalPrice) {

        this.ticketType = ticketType;
        this.ticketId = ticketId;
        this.userId = userId;
        this.description = description;
        this.eventTime = eventTime;
        this.totalPrice = totalPrice;
    }

    // Getters
    public String getTicketType() { return ticketType; }
    public int getTicketId() { return ticketId; }
    public int getUserId() { return userId; }
    public String getDescription() { return description; }
    public Timestamp getEventTime() { return eventTime; }
    public double getTotalPrice() { return totalPrice; }


    // ===========================
    // 1) GET MONORAIL BOOKINGS
    // ===========================
    public static List<handleTickets> getMonorailBookings(int userId) {

        List<handleTickets> tickets = new ArrayList<>();

        String query = """
            SELECT BOOKING_ID, USER_ID, ORIGIN, DESTINATION,
                   DEPARTURE_TIME, TOTAL_PRICE
            FROM TRANSPORTATION_BOOKINGS
            WHERE USER_ID = ?
            AND TOTAL_PRICE < 200
            ORDER BY CREATED_AT DESC
        """;

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String desc = "Monorail — " +
                        rs.getString("ORIGIN") + " → " +
                        rs.getString("DESTINATION");

                tickets.add(new handleTickets(
                        "MONORAIL",
                        rs.getInt("BOOKING_ID"),
                        rs.getInt("USER_ID"),
                        desc,
                        rs.getTimestamp("DEPARTURE_TIME"),
                        rs.getDouble("TOTAL_PRICE")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }


    // ===========================
    // 2) GET AIR TAXI BOOKINGS
    // ===========================
    public static List<handleTickets> getAirTaxiBookings(int userId) {

        List<handleTickets> tickets = new ArrayList<>();

        String query = """
            SELECT BOOKING_ID, USER_ID, ORIGIN, DESTINATION,
                   DEPARTURE_TIME, TOTAL_PRICE
            FROM TRANSPORTATION_BOOKINGS
            WHERE USER_ID = ?
            AND TOTAL_PRICE >= 200
            ORDER BY CREATED_AT DESC
        """;

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String desc = "Air Taxi — " +
                        rs.getString("ORIGIN") + " → " +
                        rs.getString("DESTINATION");

                tickets.add(new handleTickets(
                        "AIRTAXI",
                        rs.getInt("BOOKING_ID"),
                        rs.getInt("USER_ID"),
                        desc,
                        rs.getTimestamp("DEPARTURE_TIME"),
                        rs.getDouble("TOTAL_PRICE")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }


    // ================================
    // 3) GET TOURIST SITE BOOKINGS
    // ================================
    public static List<handleTickets> getTouristSiteBookings(int userId) {

        List<handleTickets> tickets = new ArrayList<>();

        String query = """
            SELECT BOOKING_ID, USER_ID, VISIT_TIME, TOTAL_PRICE
            FROM GEM_BOOKINGS
            WHERE USER_ID = ?
            ORDER BY CREATED_AT DESC
        """;

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tickets.add(new handleTickets(
                        "GEM",
                        rs.getInt("BOOKING_ID"),
                        rs.getInt("USER_ID"),
                        "GEM Visit Ticket",
                        rs.getTimestamp("VISIT_TIME"),
                        rs.getDouble("TOTAL_PRICE")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }


    // ======================================================
    // GET ALL TICKETS MERGED (GEM + MONORAIL + AIR TAXI)
    // ======================================================
    public static List<handleTickets> getAllTicketsForUser(int userId) {
            List<handleTickets> tickets = new ArrayList<>();

            tickets.addAll(getTouristSiteBookings(userId));
            tickets.addAll(getMonorailBookings(userId));
            tickets.addAll(getAirTaxiBookings(userId));

            return tickets;
        }


        public static List<handleTickets> getAirTaxiBookingsByIds(List<String> bookingIds) {
        List<handleTickets> tickets = new ArrayList<>();
        if (bookingIds == null || bookingIds.isEmpty()) return tickets;

        String placeholders = String.join(",", bookingIds.stream().map(id -> "?").toArray(String[]::new));

        String query = """
            SELECT BOOKING_ID, USER_ID, ORIGIN, DESTINATION, DEPARTURE_TIME, TOTAL_PRICE
            FROM TRANSPORTATION_BOOKINGS
            WHERE TOTAL_PRICE >= 200
            AND BOOKING_ID IN (%s)
            ORDER BY CREATED_AT DESC
        """.formatted(placeholders);

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            for (int i = 0; i < bookingIds.size(); i++) {
                ps.setInt(i + 1, Integer.parseInt(bookingIds.get(i)));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String desc = "Air Taxi — " + rs.getString("ORIGIN") + " → " + rs.getString("DESTINATION");
                tickets.add(new handleTickets(
                        "AIRTAXI",
                        rs.getInt("BOOKING_ID"),
                        rs.getInt("USER_ID"),
                        desc,
                        rs.getTimestamp("DEPARTURE_TIME"),
                        rs.getDouble("TOTAL_PRICE")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public static List<handleTickets> getMonorailBookingsByIds(List<String> bookingIds) {
        List<handleTickets> tickets = new ArrayList<>();

        if (bookingIds == null || bookingIds.isEmpty()) {
            return tickets;
        }

        // Build dynamic IN (?, ?, ?, ...)
        String placeholders = String.join(",", bookingIds.stream().map(id -> "?").toArray(String[]::new));

        String query = """
            SELECT BOOKING_ID, USER_ID, ORIGIN, DESTINATION, 
                   DEPARTURE_TIME, TOTAL_PRICE
            FROM TRANSPORTATION_BOOKINGS
            WHERE TOTAL_PRICE < 200
            AND BOOKING_ID IN ( %s )
            ORDER BY CREATED_AT DESC
        """.formatted(placeholders);

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Set dynamic parameters
            for (int i = 0; i < bookingIds.size(); i++) {
                ps.setInt(i + 1, Integer.parseInt(bookingIds.get(i)));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String desc = "Monorail — " +
                        rs.getString("ORIGIN") + " → " +
                        rs.getString("DESTINATION");

                tickets.add(new handleTickets(
                        "MONORAIL",
                        rs.getInt("BOOKING_ID"),
                        rs.getInt("USER_ID"),
                        desc,
                        rs.getTimestamp("DEPARTURE_TIME"),
                        rs.getDouble("TOTAL_PRICE")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }
    
    public static List<handleTickets> getTouristSiteBookingsByIds(List<String> bookingIds) {
        List<handleTickets> tickets = new ArrayList<>();
        if (bookingIds == null || bookingIds.isEmpty()) return tickets;

        String placeholders = String.join(",", bookingIds.stream().map(id -> "?").toArray(String[]::new));
        String query = """
            SELECT BOOKING_ID, USER_ID, VISIT_TIME, TOTAL_PRICE
            FROM GEM_BOOKINGS
            WHERE BOOKING_ID IN (%s)
            ORDER BY CREATED_AT DESC
        """.formatted(placeholders);

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            for (int i = 0; i < bookingIds.size(); i++) {
                ps.setInt(i + 1, Integer.parseInt(bookingIds.get(i)));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tickets.add(new handleTickets(
                    "GEM",
                    rs.getInt("BOOKING_ID"),
                    rs.getInt("USER_ID"),
                    "GEM Visit Ticket",
                    rs.getTimestamp("VISIT_TIME"),
                    rs.getDouble("TOTAL_PRICE")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }


}
