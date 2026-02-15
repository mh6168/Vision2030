package Login;

import java.util.Date;
import java.sql.*;
import javax.swing.JOptionPane;

public class handleUsers {

    private int userId; 
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String nationalId;
    private String city;
    private Date dob;
    private String nationality;
    private String pref_lang;
    private Timestamp createdAt; // new column

    // Constructor
    public handleUsers(String fullName, String email, String password, String phone,
                       String nationalId, String city, Date dob, String nationality, String pref_lang) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nationalId = nationalId;
        this.city = city;
        this.dob = dob;
        this.nationality = nationality;
        this.pref_lang = pref_lang;
        this.createdAt = new Timestamp(System.currentTimeMillis()); // auto set current time
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getNationalId() { return nationalId; }
    public String getCity() { return city; }
    public Date getDob() { return dob; }
    public String getNationality() { return nationality; }
    public String getPref_lang() { return pref_lang; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Save user to DB
    public void saveUser() {
        String sql = "INSERT INTO users (FULL_NAME, EMAIL, PASSWORD, PHONE, NATIONAL_ID, CITY, DOB, CREATED_AT, NATIONALITY, PREF_LANG) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"USER_ID"})) {

            pstmt.setString(1, this.getFullName());
            pstmt.setString(2, this.getEmail());
            pstmt.setString(3, this.getPassword()); // hashed password
            pstmt.setString(4, this.getPhone());
            pstmt.setString(5, this.getNationalId());
            pstmt.setString(6, this.getCity());
            if (this.getDob() != null) {
                pstmt.setDate(7, new java.sql.Date(this.getDob().getTime()));
            } else {
                pstmt.setDate(7, null);
            }
            pstmt.setTimestamp(8, this.getCreatedAt());
            pstmt.setString(9, this.getNationality());
            pstmt.setString(10, this.getPref_lang());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) this.setUserId(rs.getInt(1));
                }
                JOptionPane.showMessageDialog(null, "User registered successfully! Your ID: " + this.getUserId());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    // SHA-256 password hashing
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean checkLogin(String email, String password) {
        String sql = "SELECT PASSWORD FROM USERS WHERE EMAIL = ?"; // Replace USERS with your table name

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("PASSWORD");

                    // Hash the entered password and compare
                    String hashedInputPassword = hashPassword(password);
                    return storedHashedPassword != null && storedHashedPassword.equals(hashedInputPassword);
                } else {
                    // No user found with this email
                    return false;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static String getUserFullName(String email) {
        String fullName = "";
        try (Connection con = OracleConnection.getConnection()) {
            String sql = "SELECT full_name FROM users WHERE email = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                fullName = rs.getString("full_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullName;
    }
    
    public static int getUserId(String email) {
        int userId = -1;
        try (Connection con = OracleConnection.getConnection()) {
            String sql = "SELECT user_id FROM users WHERE email = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static String getNationalIdByUserId(int userId) {
        String nationalId = "";
        String sql = "SELECT NATIONAL_ID FROM users WHERE USER_ID = ?";

        try (Connection con = OracleConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nationalId = rs.getString("NATIONAL_ID");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nationalId;
    }
    
    public static String getUserPrefLanguage(int userId) {
        String prefLang = null;
        String sql = "SELECT PREF_LANG FROM users WHERE USER_ID = ?";

        try (Connection con = OracleConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    prefLang = rs.getString("PREF_LANG");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prefLang;
    }
    
    public static boolean updateUserPrefLanguage(int userId, String newPrefLang) {
    String sql = "UPDATE users SET PREF_LANG = ? WHERE USER_ID = ?";

    try (Connection con = OracleConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, newPrefLang);
        ps.setInt(2, userId);

        int rowsUpdated = ps.executeUpdate();

        return rowsUpdated > 0;  // success if 1 row updated

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


}
