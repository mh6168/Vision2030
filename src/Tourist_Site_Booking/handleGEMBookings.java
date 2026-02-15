package Tourist_Site_Booking;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import Login.OracleConnection;

public class handleGEMBookings {

    private int bookingId;
    private int userId;
    private int egyptianAdults;
    private int egyptianChildren;
    private int egyptianStudents;
    private int egyptianSeniors;
    private int foreignAdults;
    private int foreignChildren;
    private int foreignStudents;
    private double totalPrice;
    private String visitTime; // User input string
    private Timestamp createdAt;

    // Constructor
    public handleGEMBookings(int userId, int egyptianAdults, int egyptianChildren, int egyptianStudents,
                             int egyptianSeniors, int foreignAdults, int foreignChildren, int foreignStudents,
                             double totalPrice, String visitTime) {
        this.userId = userId;
        this.egyptianAdults = egyptianAdults;
        this.egyptianChildren = egyptianChildren;
        this.egyptianStudents = egyptianStudents;
        this.egyptianSeniors = egyptianSeniors;
        this.foreignAdults = foreignAdults;
        this.foreignChildren = foreignChildren;
        this.foreignStudents = foreignStudents;
        this.totalPrice = totalPrice;
        this.visitTime = visitTime;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getUserId() { return userId; }
    public int getEgyptianAdults() { return egyptianAdults; }
    public int getEgyptianChildren() { return egyptianChildren; }
    public int getEgyptianStudents() { return egyptianStudents; }
    public int getEgyptianSeniors() { return egyptianSeniors; }
    public int getForeignAdults() { return foreignAdults; }
    public int getForeignChildren() { return foreignChildren; }
    public int getForeignStudents() { return foreignStudents; }
    public double getTotalPrice() { return totalPrice; }
    public String getVisitTime() { return visitTime; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Save booking to DB
    public void saveBooking() {
        String seqSql = "SELECT GEM_BOOKINGS_SEQ.NEXTVAL FROM dual"; // your sequence
        String insertSql = "INSERT INTO GEM_BOOKINGS "
                + "(BOOKING_ID, USER_ID, EGYPTIAN_ADULTS, EGYPTIAN_CHILDREN, EGYPTIAN_STUDENTS, EGYPTIAN_SENIORS, "
                + "FOREIGN_ADULTS, FOREIGN_CHILDREN, FOREIGN_STUDENTS, TOTAL_PRICE, VISIT_TIME, CREATED_AT) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = OracleConnection.getConnection()) {

            // Get next sequence value
            try (PreparedStatement seqStmt = conn.prepareStatement(seqSql);
                 ResultSet rs = seqStmt.executeQuery()) {
                if (rs.next()) {
                    this.bookingId = rs.getInt(1);
                }
            }

            // Parse visitTime
            Timestamp visitTimestamp;
            try {
                String trimmed = this.visitTime.trim();

                // Append default time if only date is entered
                if (trimmed.length() == 10) {
                    trimmed += " 09:00 AM"; // default time
                }

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a"); 
                sdf1.setLenient(false);
                java.util.Date utilDate = sdf1.parse(trimmed);
                visitTimestamp = new Timestamp(utilDate.getTime());

            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null,
                    "Invalid date format. Please use YYYY-MM-DD or YYYY-MM-DD hh:mm AM/PM");
                return; // stop saving
            }

            // 3️⃣ Set PreparedStatement parameters
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, this.bookingId);
                pstmt.setInt(2, this.userId);
                pstmt.setInt(3, this.egyptianAdults);
                pstmt.setInt(4, this.egyptianChildren);
                pstmt.setInt(5, this.egyptianStudents);
                pstmt.setInt(6, this.egyptianSeniors);
                pstmt.setInt(7, this.foreignAdults);
                pstmt.setInt(8, this.foreignChildren);
                pstmt.setInt(9, this.foreignStudents);
                pstmt.setDouble(10, this.totalPrice);
                pstmt.setTimestamp(11, visitTimestamp); // ✅ use the parsed Timestamp
                pstmt.setTimestamp(12, this.createdAt);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "GEM Booking created successfully! Booking ID: " + this.bookingId);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
}
