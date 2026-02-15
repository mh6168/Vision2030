package My_Tickets;

import java.util.List;

public class Testingdemosss {

    public static void main(String[] args) {

        int userId = 81;   // change this to any user you want to test

        System.out.println("==== MONORAIL BOOKINGS ====");
        List<handleTickets> mono = handleTickets.getMonorailBookings(userId);
        printList(mono);

        System.out.println("\n==== AIR TAXI BOOKINGS ====");
        List<handleTickets> air = handleTickets.getAirTaxiBookings(userId);
        printList(air);

        System.out.println("\n==== GEM BOOKINGS ====");
        List<handleTickets> gem = handleTickets.getTouristSiteBookings(userId);
        printList(gem);

        System.out.println("\n==== ALL BOOKINGS MERGED ====");
        List<handleTickets> all = handleTickets.getAllTicketsForUser(userId);
        printList(all);
    }


    // Helper method to print ticket objects
    private static void printList(List<handleTickets> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (handleTickets t : tickets) {
            System.out.println(
                    "Type: " + t.getTicketType() +
                    " | ID: " + t.getTicketId() +
                    " | Description: " + t.getDescription() +
                    " | Time: " + t.getEventTime() +
                    " | Price: " + t.getTotalPrice()
            );
        }
    }
}
