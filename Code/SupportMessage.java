package HostelManagementSystem;

class SupportMessage {
    private static int idCounter = 1;
    private int ticketID;
    private String userID;
    private String message;

    public SupportMessage(String userID, String message) {
        this.ticketID = idCounter++;
        this.userID = userID;
        this.message = message;
    }

    public SupportMessage(String userID, String message, int ticketID) {
        this.ticketID = ticketID;
        this.userID = userID;
        this.message = message;
    }

    public int getTicketID() {
        return ticketID;
    }

    public String getUserID() {
        return userID;
    }

    public String getMessage() {
        return message;
    }

    // Save support message to database
    public void saveToDatabase() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.addSupportMessage(this);
    }

    // Delete support message from database
    public void deleteFromDatabase() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.deleteSupportMessage(ticketID);
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketID + ", User ID: " + userID + ", Message: " + message;
    }
}
