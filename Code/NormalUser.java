package HostelManagementSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NormalUser extends Users {
    private String city;
    private List<Booking> bookings;
    private List<Payment> payments;

    public NormalUser(String userID, String name, String email, String phoneNumber, String password, String city) {
        super(userID, name, email, phoneNumber, password);
        this.city = city;
        this.bookings = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    // Search for available hostels and display them
    public void searchHostel() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ResultSet resultSet = dbHandler.getAllHostels();
        try {
            System.out.println("Available Hostels:");
            while (resultSet.next()) {
                System.out.println("Hostel ID: " + resultSet.getInt("hostelID"));
                System.out.println("Hostel Name: " + resultSet.getString("hostelName"));
                System.out.println("Owner ID: " + resultSet.getString("ownerID"));
                System.out.println("1) View Reviews\n2) View Rooms\n-----------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void viewAccountDetails() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ResultSet resultSet = dbHandler.getNormalUser(userID);
        try {
            if (resultSet.next()) {
                System.out.println("User ID: " + resultSet.getString("userID"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Phone: " + resultSet.getString("phone"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("City: " + city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editAccountDetails(Scanner scanner) {
        System.out.println("Current Name: " + name);
        System.out.print("Enter new name (or press Enter to keep current): ");
        scanner.nextLine(); // Consume leftover newline
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            name = newName;
        }

        System.out.println("Current Phone: " + phone);
        System.out.print("Enter new phone (or press Enter to keep current): ");
        String newPhone = scanner.nextLine();
        if (!newPhone.isEmpty()) {
            phone = newPhone;
        }

        System.out.println("Current Email: " + email);
        System.out.print("Enter new email (or press Enter to keep current): ");
        String newEmail = scanner.nextLine();
        if (!newEmail.isEmpty()) {
            email = newEmail;
        }

        System.out.println("City: " + city);
        System.out.print("Enter new city (or press Enter to keep current): ");
        String newCity = scanner.nextLine();
        if (!newCity.isEmpty()) {
            city = newCity;
        }

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.updateNormalUser(userID, name, email, phone);
        System.out.println("Account details updated successfully.");
    }

    // Review a specific hostel by ID
    public void reviewHostel(int hostelID, String review) {
        Review newReview = new Review(this.getUserID(), hostelID, review);
        newReview.saveToDatabase();
        System.out.println("Review added successfully. Your review ID is: " + newReview.getReviewID());
    }

    public void deleteReview(String reviewID, List<Hostel> hostels) {
        for (Hostel hostel : hostels) {
            for (Review review : hostel.getReviews()) {
                if (review != null && review.getReviewID() != null &&
                    review.getReviewID().equals(reviewID) &&
                    review.getUserID().equals(this.getUserID())) {
                    
                    hostel.removeReview(review);
                    review.deleteFromDatabase();
                    System.out.println("Review deleted successfully.");
                    return;
                }
            }
        }
        System.out.println("Review not found or you do not have permission to delete this review.");
    }



    // View all bookings made by the user
    public void viewBookings() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ResultSet resultSet = dbHandler.getBookings(userID);
        try {
            System.out.println("No bookings found.");
            while (resultSet.next()) {
                System.out.println("Booking ID: " + resultSet.getString("bookingID"));
                System.out.println("Room ID: " + resultSet.getString("roomID"));
                System.out.println("Check-In Date: " + resultSet.getString("checkInDate"));
                System.out.println("Check-Out Date: " + resultSet.getString("checkOutDate"));
                System.out.println("Cost: " + resultSet.getDouble("cost"));
                System.out.println("-----------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBookings(String roomID) {
        bookings.removeIf(booking -> booking.getRoomID().equals(roomID));
        System.out.println("Removed bookings for Room ID: " + roomID);
    }

    // Add a booking to the user's list of bookings
    public void addBooking(Booking booking) {
        bookings.add(booking);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.addBooking(booking);
    }

    // Getter for bookings
    public List<Booking> getBookings() {
        return bookings;
    }

    // Getter for payments
    public List<Payment> getPayments() {
        return payments;
    }
    
    public String getCity()
	{
		return city;
	}

    // Make a payment for a booking
    public void makePayment(Payment payment) {
        payments.add(payment);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.addPayment(payment);
    }

    // Book a room (this method is similar to addBooking)
    public void bookRoom(Booking booking, Payment payment) {
        bookings.add(booking);
        payments.add(payment);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.addBooking(booking);
        dbHandler.addPayment(payment);
    }

    // Cancel a booking and process the refund
    public void cancelBooking(String bookingID, List<Hostel> hostels) {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        for (Booking booking : bookings) {
            if (booking.getBookingID().equals(bookingID)) {
                double refundAmount = booking.getCost() * 0.5;
                System.out.println("Refund amount: $" + refundAmount);

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

                bookings.remove(booking);
                dbHandler.deleteBooking(bookingID);
                System.out.println("Booking cancelled successfully.");
                return;
            }
        }
        System.out.println("Booking not found.");
    }

	
}
