package HostelManagementSystem;

class Payment {
    private final String paymentID;
    private final String userID;
    private final String bookingID;
    private final double amount;
    private final String date;
    private String status;

    public Payment(String paymentID, String userID, String bookingID, double amount, String date) {
        this.paymentID = paymentID;
        this.userID = userID;
        this.bookingID = bookingID;
        this.amount = amount;
        this.date = date;
        this.status = "Pending"; // Initial status
    }

    // Getter for paymentID
    public String getPaymentID() {
        return paymentID;
    }

    // Getter for userID
    public String getUserID() {
        return userID;
    }

    // Getter for bookingID
    public String getBookingID() {
        return bookingID;
    }

    // Getter for amount
    public double getAmount() {
        return amount;
    }

    // Getter for date
    public String getDate() {
        return date;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Method to process payment
    public boolean processPayment() {
        // Here you can add the logic to actually process the payment
        // For now, let's assume the payment is processed successfully if the amount is greater than 0
        if (amount > 0) {
            status = "Processed";
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            dbHandler.updatePaymentStatus(paymentID, "Processed");
            System.out.println("Payment processed successfully.");
            return true;
        } else {
            System.out.println("Payment failed. Invalid amount.");
            status = "Failed";
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            dbHandler.updatePaymentStatus(paymentID, "Failed");
            return false;
        }
    }
    
    // Method to save payment to database
    public void saveToDatabase() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.addPayment(this);
    }

    // Method to delete payment from database
    public void deleteFromDatabase() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.deletePayment(paymentID);
    }

    @Override
    public String toString() {
        return "Payment ID: " + paymentID + ", User ID: " + userID + ", Booking ID: " + bookingID + ", Amount: $" + amount + ", Date: " + date + ", Status: " + status;
    }
}
