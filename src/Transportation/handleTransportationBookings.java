/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Transportation;

import java.sql.*;
import java.util.Date;
import javax.swing.JOptionPane;

import Login.OracleConnection;

public class handleTransportationBookings {

    private int bookingId;
    private int userId;
    private String origin;
    private String destination;
    private Timestamp departureTime;
    private int passengerCount;
    private String paymentMethod;
    private double totalPrice;
    private String bookingStatus;
    private Timestamp createdAt;

    // Constructor
    public handleTransportationBookings(int userId, String origin, String destination, Timestamp departureTime,
                                 int passengerCount, String paymentMethod, double totalPrice, String bookingStatus) {
        this.userId = userId;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.passengerCount = passengerCount;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
        this.createdAt = new Timestamp(System.currentTimeMillis()); // auto set current time
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public Timestamp getDepartureTime() { return departureTime; }
    public int getPassengerCount() { return passengerCount; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getTotalPrice() { return totalPrice; }
    public String getBookingStatus() { return bookingStatus; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Save booking to DB
    public void saveBooking() {
        String seqSql = "SELECT AIR_TAXI_SEQ.NEXTVAL FROM dual";
        String insertSql = "INSERT INTO TRANSPORTATION_BOOKINGS "
                + "(BOOKING_ID, USER_ID, ORIGIN, DESTINATION, DEPARTURE_TIME, PASSENGER_COUNT, "
                + "PAYMENT_METHOD, TOTAL_PRICE, BOOKING_STATUS, CREATED_AT) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = OracleConnection.getConnection()) {

            // 1. Get next sequence value
            try (PreparedStatement seqStmt = conn.prepareStatement(seqSql);
                 ResultSet rs = seqStmt.executeQuery()) {
                if (rs.next()) {
                    this.bookingId = rs.getInt(1);
                }
            }

            // 2. Insert using BOOKING_ID from sequence
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

                pstmt.setInt(1, this.getBookingId());
                pstmt.setInt(2, this.getUserId());
                pstmt.setString(3, this.getOrigin());
                pstmt.setString(4, this.getDestination());
                pstmt.setTimestamp(5, this.getDepartureTime());
                pstmt.setInt(6, this.getPassengerCount());
                pstmt.setString(7, this.getPaymentMethod());
                pstmt.setDouble(8, this.getTotalPrice());
                pstmt.setString(9, this.getBookingStatus());
                pstmt.setTimestamp(10, this.getCreatedAt());

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Booking created successfully! Booking ID: " + this.getBookingId()
                    );
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    // Fetch booking by ID
    public static handleTransportationBookings getBookingById(int bookingId) {
        handleTransportationBookings booking = null;
        String sql = "SELECT * FROM TRANSPORTATION_BOOKINGS WHERE BOOKING_ID = ?";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    booking = new handleTransportationBookings(
                        rs.getInt("USER_ID"),
                        rs.getString("ORIGIN"),
                        rs.getString("DESTINATION"),
                        rs.getTimestamp("DEPARTURE_TIME"),
                        rs.getInt("PASSENGER_COUNT"),
                        rs.getString("PAYMENT_METHOD"),
                        rs.getDouble("TOTAL_PRICE"),
                        rs.getString("BOOKING_STATUS")
                    );
                    booking.setBookingId(rs.getInt("BOOKING_ID"));
                    booking.createdAt = rs.getTimestamp("CREATED_AT");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return booking;
    }

    // Example: update booking status
    public void updateBookingStatus(String newStatus) {
        String sql = "UPDATE TRANSPORTATION_BOOKINGS SET BOOKING_STATUS = ? WHERE BOOKING_ID = ?";
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, this.getBookingId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                this.bookingStatus = newStatus;
                JOptionPane.showMessageDialog(null, "Booking status updated to " + newStatus);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

