package HostelManagementSystem;

import java.sql.*;

public class DatabaseHandler {
    private static DatabaseHandler instance;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3305/HMS";
    private String username = "root";
    private String password = "12345";

    private DatabaseHandler()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection successful.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        } else {
            try {
                if (instance.getConnection().isClosed()) {
                    instance = new DatabaseHandler();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                instance = new DatabaseHandler();
            }
        }
        return instance;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        // Methods for HostelOwner
        public void addHostelOwner(String ownerID, String name, String email, String phone, String password) {
            String query = "INSERT INTO HostelOwner (ownerID, name, email, phone, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, ownerID);
                statement.setString(2, name);
                statement.setString(3, email);
                statement.setString(4, phone);
                statement.setString(5, password);
                statement.executeUpdate();
                System.out.println("Hostel owner added successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public ResultSet getHostelOwner(String ownerID) {
            String query = "SELECT * FROM HostelOwner WHERE ownerID = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, ownerID);
                return statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void updateHostelOwner(String ownerID, String name, String email, String phone) {
            String query = "UPDATE HostelOwner SET name = ?, email = ?, phone = ? WHERE ownerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, phone);
                statement.setString(4, ownerID);
                statement.executeUpdate();
                System.out.println("Hostel owner updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteHostelOwner(String ownerID) {
            String query = "DELETE FROM HostelOwner WHERE ownerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, ownerID);
                statement.executeUpdate();
                System.out.println("Hostel owner deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public ResultSet getAllHostelOwners() {
            String query = "SELECT * FROM HostelOwner";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                return statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Methods for NormalUser
        public void addNormalUser(String userID, String name, String email, String phone, String password, String city) {
            String query = "INSERT INTO NormalUser (userID, name, email, phone, password, city) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, userID);
                statement.setString(2, name);
                statement.setString(3, email);
                statement.setString(4, phone);
                statement.setString(5, password);
                statement.setString(6, city);
                statement.executeUpdate();
                System.out.println("Normal user added successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public ResultSet getNormalUser(String userID) {
            String query = "SELECT * FROM NormalUser WHERE userID = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, userID);
                return statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void updateNormalUser(String userID, String name, String email, String phone) {
            String query = "UPDATE NormalUser SET name = ?, email = ?, phone = ? WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, phone);
                statement.setString(4, userID);
                statement.executeUpdate();
                System.out.println("Normal user updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteNormalUser(String userID) {
            String query = "DELETE FROM NormalUser WHERE userID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, userID);
                statement.executeUpdate();
                System.out.println("Normal user deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public ResultSet getAllNormalUsers() {
            String query = "SELECT * FROM NormalUser";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                return statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

    // CRUD operations for Hostel table
    public void addHostel(Hostel hostel) {
        String query = "INSERT INTO Hostel (hostelID, hostelName, ownerID) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, hostel.getHostelID());
            statement.setString(2, hostel.getName());
            statement.setString(3, hostel.getOwnerID());
            statement.executeUpdate();
            System.out.println("Hostel added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getHostel(int hostelID) {
        String query = "SELECT * FROM Hostel WHERE hostelID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, hostelID);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateHostel(int hostelID, String hostelName) {
        String query = "UPDATE Hostel SET name = ? WHERE hostelID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, hostelName);
            statement.setInt(2, hostelID);
            statement.executeUpdate();
            System.out.println("Hostel updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet getAllHostels() {
        String query = "SELECT * FROM Hostel";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void deleteHostel(int hostelID) {
        String query = "DELETE FROM Hostel WHERE hostelID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, hostelID);
            statement.executeUpdate();
            System.out.println("Hostel deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CRUD operations for Room table
    public void addRoom(Room room, int hostelID) {
        String query = "INSERT INTO Room (roomID, roomName, price, isAvailable, hostelID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, room.getRoomID());
            statement.setString(2, room.getRoomName());
            statement.setDouble(3, room.getPrice());
            statement.setBoolean(4, room.isAvailable());
            statement.setInt(5, hostelID);
            statement.executeUpdate();
            System.out.println("Room added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getRooms(int hostelID) {
        String query = "SELECT * FROM Room WHERE hostelID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, hostelID);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void updateRoom(String roomID, String roomName, double price, boolean isAvailable) {
        String query = "UPDATE Room SET roomName = ?, price = ?, isAvailable = ? WHERE roomID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomName);
            statement.setDouble(2, price);
            statement.setBoolean(3, isAvailable);
            statement.setString(4, roomID);
            statement.executeUpdate();
            System.out.println("Room updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoom(String roomID) {
        String query = "DELETE FROM Room WHERE roomID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomID);
            statement.executeUpdate();
            System.out.println("Room deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CRUD operations for Booking table
    public void addBooking(Booking booking) {
        String query = "INSERT INTO Booking (bookingID, roomID, userID, checkInDate, checkOutDate, cost) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, booking.getBookingID());
            statement.setString(2, booking.getRoomID());
            statement.setString(3, booking.getUserID());
            statement.setString(4, booking.getCheckInDate());
            statement.setString(5, booking.getCheckOutDate());
            statement.setDouble(6, booking.getCost());
            statement.executeUpdate();
            System.out.println("Booking added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getBookings(String userID) {
        String query = "SELECT * FROM Booking WHERE userID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteBooking(String bookingID) {
        String query = "DELETE FROM Booking WHERE bookingID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookingID);
            statement.executeUpdate();
            System.out.println("Booking deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CRUD operations for Review table
    public void addReview(Review review) {
        String query = "INSERT INTO Review (reviewID, userID, hostelID, comment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, review.getReviewID());
            statement.setString(2, review.getUserID());
            statement.setInt(3, review.getHostelID());
            statement.setString(4, review.getComment());
            statement.executeUpdate();
            System.out.println("Review added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getReviews(int hostelID) {
        String query = "SELECT * FROM Review WHERE hostelID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, hostelID);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteReview(String reviewID) {
        String query = "DELETE FROM Review WHERE reviewID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reviewID);
            statement.executeUpdate();
            System.out.println("Review deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CRUD operations for Payment table
    public void addPayment(Payment payment) {
        String query = "INSERT INTO Payment (paymentID, userID, bookingID, amount, date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, payment.getPaymentID());
            statement.setString(2, payment.getUserID());
            statement.setString(3, payment.getBookingID());
            statement.setDouble(4, payment.getAmount());
            statement.setString(5, payment.getDate());
            statement.setString(6, payment.getStatus());
            statement.executeUpdate();
            System.out.println("Payment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getPayments(String userID) {
        String query = "SELECT * FROM Payment WHERE userID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updatePaymentStatus(String paymentID, String status) {
        String query = "UPDATE Payment SET status = ? WHERE paymentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);
            statement.setString(2, paymentID);
            statement.executeUpdate();
            System.out.println("Payment status updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePayment(String paymentID) {
        String query = "DELETE FROM Payment WHERE paymentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, paymentID);
            statement.executeUpdate();
            System.out.println("Payment deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // Method to get all payments
    public ResultSet getAllPayments() {
        String query = "SELECT * FROM Payment";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ResultSet getSupportMessages() {
        String query = "SELECT * FROM support_messages";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // Method to add support message
    public void addSupportMessage(SupportMessage supportMessage) {
        String query = "INSERT INTO support_messages (userID, message, ticketID) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, supportMessage.getUserID());
            statement.setString(2, supportMessage.getMessage());
            statement.setInt(3, supportMessage.getTicketID());
            statement.executeUpdate();
            System.out.println("Support message added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete support message
    public void deleteSupportMessage(int ticketID) {
        String query = "DELETE FROM support_messages WHERE ticketID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ticketID);
            statement.executeUpdate();
            System.out.println("Support message deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

    