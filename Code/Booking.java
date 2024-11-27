package HostelManagementSystem;

class Booking {
    private final String bookingID;
    private final String roomID;
    private final String userID;
    private final String checkInDate;
    private final String checkOutDate;
    private final double cost;

    public Booking(String bookingID, String roomID, String userID, String checkInDate, String checkOutDate, double cost) {
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.userID = userID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getUserID() {
        return userID;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    // Save booking to database
    public void saveToDatabase() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.addBooking(this);
    }

    // Delete booking from database
    public void deleteFromDatabase() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.deleteBooking(bookingID);
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingID + ", Room ID: " + roomID + ", User ID: " + userID + ", Check-In Date: " + checkInDate + ", Check-Out Date: " + checkOutDate + ", Cost: " + cost;
    }
}
