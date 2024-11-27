package HostelManagementSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

class Admin {
    private final String userID = "1132";
    private final String password = "12345";

    public boolean login(String enteredUserID, String enteredPassword) {
        return userID.equals(enteredUserID) && password.equals(enteredPassword);
    }

    public void viewHostelOwners() {
        System.out.println("--- Registered Hostel Owners ---");
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ResultSet resultSet = dbHandler.getAllHostelOwners();
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("ownerID") + " - " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewNormalUsers() {
        System.out.println("--- Registered Normal Users ---");
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ResultSet resultSet = dbHandler.getAllNormalUsers();
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("userID") + " - " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHostelOwner(String ownerID) {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.deleteHostelOwner(ownerID);
        System.out.println("Hostel owner deleted successfully.");
    }

    public void deleteNormalUser(String userID) {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.deleteNormalUser(userID);
        System.out.println("Normal user deleted successfully.");
    }

    // Method to manage payments
    public void managePayments(List<Payment> payments, List<Users> users, List<Hostel> hostels, Scanner scanner) {
        System.out.println("--- Payments ---");
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ResultSet resultSet = dbHandler.getAllPayments();
        try {
            while (resultSet.next()) {
                System.out.println("Payment ID: " + resultSet.getString("paymentID") +
                                   ", User ID: " + resultSet.getString("userID") +
                                   ", Booking ID: " + resultSet.getString("bookingID") +
                                   ", Amount: " + resultSet.getDouble("amount") +
                                   ", Date: " + resultSet.getString("date") +
                                   ", Status: " + resultSet.getString("status"));
                System.out.println("-----------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("1) Refund a payment\n2) Go back to main menu");
        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.print("Enter Payment ID to refund: ");
            String paymentID = scanner.next();

            for (Payment payment : payments) {
                if (payment.getPaymentID().equals(paymentID) && payment.getStatus().equals("Processed")) {
                    // Refund the payment
                    dbHandler.updatePaymentStatus(paymentID, "Refunded");

                    // Cancel the associated booking
                    String bookingID = payment.getBookingID();
                    for (Users user : users) {
                        if (user instanceof NormalUser) {
                            NormalUser normalUser = (NormalUser) user;
                            for (Booking booking : normalUser.getBookings()) {
                                if (booking.getBookingID().equals(bookingID)) {
                                    // Free up the room
                                    for (Hostel hostel : hostels) {
                                        for (Room room : hostel.getRooms()) {
                                            if (room.getRoomID().equals(booking.getRoomID())) {
                                                room.setAvailability(true);
                                                dbHandler.updateRoom(room.getRoomID(), room.getRoomName(), room.getPrice(), true);
                                                System.out.println("Room " + room.getRoomID() + " is now available.");
                                                break;
                                            }
                                        }
                                    }

                                    // Remove the booking from the user's list
                                    normalUser.getBookings().remove(booking);
                                    dbHandler.deleteBooking(bookingID);
                                    System.out.println("Booking cancelled successfully.");
                                    break;
                                }
                            }

                            System.out.println("Refund processed for Payment ID: " + paymentID);
                            return;
                        }
                    }
                }
            }

            System.out.println("Payment ID not found or already refunded.");
        } else {
            System.out.println("Returning to main menu.");
        }
    }
}
