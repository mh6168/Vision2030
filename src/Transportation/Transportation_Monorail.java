/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Transportation;

public class Transportation_Monorail extends TransportationReservation {

    public Transportation_Monorail(int userId, int sourceId, int destinationId, int date, int passengerCount) {
        super(userId, sourceId, destinationId, date, passengerCount);
    }

    @Override
    public double calculatePrice() {
        double price;
        
        if (sourceId < 1 || sourceId > 22 || destinationId < 1 || destinationId > 22) {
            return 0; // invalid station
        }

        int diff = Math.abs(destinationId - sourceId);

        if (diff == 0) {
            return 0;
        } 
        
        if (diff <= 3) {
            price = 20;
        }
        else if (diff <= 7) {
            price = 25;
        }
        else {
            price = 30;
        }

        return price * passengerCount;
    }
}
