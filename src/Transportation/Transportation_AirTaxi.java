package Transportation;

public class Transportation_AirTaxi extends TransportationReservation {

    public Transportation_AirTaxi(int userId, int sourceId, int destinationId, int date, int passengerCount) {
        super(userId, sourceId, destinationId, date, passengerCount);
    }

    @Override
    public double calculatePrice() {

        if ((sourceId == 1 && destinationId == 2) || (sourceId == 2 && destinationId == 1)) {
            return 200 * passengerCount;
        }
        else if ((sourceId == 1 && destinationId == 3) || (sourceId == 3 && destinationId == 1)) {
            return 350 * passengerCount;
        }
        else if ((sourceId == 4 && destinationId == 1) || (sourceId == 1 && destinationId == 4)) {
            return 300 * passengerCount;
        }
        else if ((sourceId == 6 && destinationId == 7) || (sourceId == 7 && destinationId == 6)) {
            return 1500 * passengerCount;
        }
        else if ((sourceId == 1 && destinationId == 5) || (sourceId == 5 && destinationId == 1)) {
            return 300 * passengerCount;
        }
        else if ((sourceId == 1 && destinationId == 8) || (sourceId == 8 && destinationId == 1)) {
            return 1500 * passengerCount;
        }
        else if ((sourceId == 9 && destinationId == 10) || (sourceId == 10 && destinationId == 9)) {
            return 900 * passengerCount;
        }

        return 0; // invalid route
    }
}
