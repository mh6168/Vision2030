/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Transportation;

/**
 *
 * @author memo1
 */
public abstract class TransportationReservation {

    protected int userId;
    protected int sourceId;
    protected int destinationId;
    protected int date;
    protected int passengerCount;

    public TransportationReservation(int userId, int sourceId, int destinationId, int date, int passengerCount) {
        this.userId = userId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.date = date;
        this.passengerCount = passengerCount;
    }

    public abstract double calculatePrice();
}
