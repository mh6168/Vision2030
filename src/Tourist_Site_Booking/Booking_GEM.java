/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tourist_Site_Booking;

/**
 *
 * @author memo1
 */
public class Booking_GEM {
    // Pricing constants for different visitor categories
    private static final double EGYPTIAN_ADULT_PRICE = 200;
    private static final double EGYPTIAN_REDUCED_PRICE = 100; // For children, students, and seniors
    private static final double FOREIGN_ADULT_PRICE = 1450;
    private static final double FOREIGN_REDUCED_PRICE = 730;    // For children and students

    public double calculateBulkPrice(int egyptianAdults, int egyptianChildren, int egyptianStudents, int egyptianSeniors, int foreignAdults, int foreignChildren, int foreignStudents) {
        double totalPrice = 0;

        // Calculate price for Egyptian visitors
        totalPrice += egyptianAdults * EGYPTIAN_ADULT_PRICE;
        totalPrice += (egyptianChildren + egyptianStudents + egyptianSeniors) * EGYPTIAN_REDUCED_PRICE;

        // Calculate price for Arab/Foreign visitors
        totalPrice += foreignAdults * FOREIGN_ADULT_PRICE;
        totalPrice += (foreignChildren + foreignStudents) * FOREIGN_REDUCED_PRICE;

        return totalPrice;
    }    
}
