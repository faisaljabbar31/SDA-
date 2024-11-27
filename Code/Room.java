package HostelManagementSystem;

class Room {
    private final String roomID;
    private String roomName;
    private double price;
    private boolean isAvailable;

    public Room(String roomID, String roomName, double price, boolean isAvailable) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.updateRoom(roomID, roomName, price, availability);
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.updateRoom(roomID, roomName, price, isAvailable);
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            dbHandler.updateRoom(roomID, roomName, price, isAvailable);
        } else {
            System.out.println("Price must be a positive value.");
        }
    }

    @Override
    public String toString() {
        return "Room ID: " + roomID + ", Name: " + roomName + ", Price: " + price + ", Available: " + isAvailable;
    }
}
