package HostelManagementSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Hostel {
    private static int idCounter = 1; // Static counter for unique IDs
    private int hostelID;
    private String hostelName;
    private final String ownerID;
    private List<Review> reviews;
    private List<Room> rooms;

    public Hostel(String hostelName, String ownerID, List<Room> rooms) {
        this.hostelID = idCounter++; // Assign the current counter value and then increment it
        this.hostelName = hostelName;
        this.ownerID = ownerID;
        this.rooms = rooms;
        this.reviews = new ArrayList<>(); // Initialize the reviews list
    }

    public boolean removeRoom(String roomID) {
        for (Room room : rooms) {
            if (room.getRoomID().equals(roomID)) {
                rooms.remove(room); // Remove the room
                DatabaseHandler dbHandler = DatabaseHandler.getInstance();
                dbHandler.deleteRoom(roomID);
                System.out.println("Room " + roomID + " has been removed successfully.");
                return true;
            }
        }
        System.out.println("Room ID " + roomID + " not found in Hostel " + hostelID);
        return false;
    }

    public void setHostelName(String newName) {
        this.hostelName = newName;
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.updateHostel(hostelID, newName);
    }

    public String getOwnerID() {
        return ownerID;
    }

    public int getHostelID() {
        return hostelID;
    }

    public String getName() {
        return hostelName;
    }

    public List<Room> getRooms() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        rooms = new ArrayList<>();
        ResultSet resultSet = dbHandler.getRooms(hostelID);
        try {
            while (resultSet.next()) {
                rooms.add(new Room(
                    resultSet.getString("roomID"),
                    resultSet.getString("roomName"),
                    resultSet.getDouble("price"),
                    resultSet.getBoolean("isAvailable")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Review> getReviews() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        reviews = new ArrayList<>();
        ResultSet resultSet = dbHandler.getReviews(hostelID);
        try {
            while (resultSet.next()) {
                reviews.add(new Review(
                    resultSet.getString("reviewID"),
                    resultSet.getString("userID"),
                    resultSet.getInt("hostelID"),
                    resultSet.getString("comment")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.addReview(review);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.deleteReview(review.getReviewID());
    }

    @Override
    public String toString() {
        return "Hostel ID: " + hostelID + ", Name: " + hostelName;
    }
}
