package HostelManagementSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

abstract class Users implements Manageable {
    protected String userID;
    protected String name;
    protected String email;
    protected String phone;
    protected String password;

    public Users(String userID, String name, String email, String phoneNumber, String password) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phoneNumber;
        this.password = password;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Getter for phone
    public String getPhone() {
        return phone;
    }

    public String getUserID() {
        return userID;
    }

    public boolean login(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }

    // Save user to database
    public void saveToDatabase() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        if (this instanceof NormalUser) {
            dbHandler.addNormalUser(userID, name, email, phone, password, ((NormalUser) this).getCity());
        } else if (this instanceof HostelOwner) {
            dbHandler.addHostelOwner(userID, name, email, phone, password);
        }
    }

    // Load user from database
    public static Users loadFromDatabase(String userID) {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ResultSet resultSet = dbHandler.getNormalUser(userID);
        try {
            if (resultSet.next()) {
                return new NormalUser(
                    resultSet.getString("userID"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getString("password"),
                    resultSet.getString("city")  // Ensure "city" column is present in the normaluser table
                );
            }
        } catch (SQLException e) {
            // Handle error
        }

        resultSet = dbHandler.getHostelOwner(userID);
        try {
            if (resultSet.next()) {
                return new HostelOwner(
                    resultSet.getString("ownerID"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update user in database
    public void updateInDatabase() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        if (this instanceof NormalUser) {
            dbHandler.updateNormalUser(userID, name, email, phone);
        } else if (this instanceof HostelOwner) {
            dbHandler.updateHostelOwner(userID, name, email, phone);
        }
    }

    // Delete user from database
    public void deleteFromDatabase() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        if (this instanceof NormalUser) {
            dbHandler.deleteNormalUser(userID);
        } else if (this instanceof HostelOwner) {
            dbHandler.deleteHostelOwner(userID);
        }
    }

    // Abstract methods
    public abstract void viewAccountDetails();
    public abstract void editAccountDetails(Scanner scanner);
}
