package HostelManagementSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class HostelOwner extends Users {
    private List<Hostel> ownedHostels;

    public HostelOwner(String userID, String name, String email, String phoneNumber, String password) {
        super(userID, name, email, phoneNumber, password);
        this.ownedHostels = new ArrayList<>();
    }

    @Override
    public void viewAccountDetails() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ResultSet resultSet = dbHandler.getHostelOwner(userID);
        try {
            if (resultSet.next()) {
                System.out.println("User ID: " + resultSet.getString("ownerID"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Phone: " + resultSet.getString("phone"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Hostels Owned: ");
                for (Hostel hostel : ownedHostels) {
                    System.out.println(hostel);
                }
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

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.updateHostelOwner(userID, name, email, phone);
        System.out.println("Account details updated successfully.");
    }

    public void listHostel(String hostelName, int numberOfRooms, List<Room> rooms, List<Hostel> allHostels) {
        Hostel hostel = new Hostel(hostelName, this.userID, rooms);
        ownedHostels.add(hostel);
        allHostels.add(hostel);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.addHostel(hostel);
        System.out.println("Hostel listed successfully.");
    }

    public void viewListedHostels() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ResultSet resultSet = dbHandler.getAllHostels();
        ownedHostels.clear(); // Ensure the list is fresh

        try {
            while (resultSet.next()) {
                if (resultSet.getString("ownerID").equals(this.userID)) {
                    Hostel hostel = new Hostel(
                        resultSet.getString("hostelName"),
                        resultSet.getString("ownerID"),
                        new ArrayList<Room>() // You might want to retrieve rooms as well
                    );
                    ownedHostels.add(hostel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (ownedHostels.isEmpty()) {
            System.out.println("No hostels listed.");
        } else {
            for (Hostel hostel : ownedHostels) {
                System.out.println(hostel);
            }
        }
    }


    public void updateHostelDetails(Scanner scanner, List<Hostel> allHostels) {
        System.out.print("Enter Hostel ID to update: ");
        int hostelID = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (Hostel hostel : allHostels) {
            if (hostel.getHostelID() == hostelID && hostel.getOwnerID().equals(this.userID)) {
                System.out.println("Current Hostel Name: " + hostel.getName());
                System.out.print("Enter new Hostel Name: ");
                String newHostelName = scanner.nextLine();
                if (!newHostelName.isEmpty()) {
                    hostel.setHostelName(newHostelName); // Update name
                    DatabaseHandler dbHandler = DatabaseHandler.getInstance();
                    dbHandler.updateHostel(hostelID, newHostelName);
                    System.out.println("Hostel name updated successfully.");
                } else {
                    System.out.println("No changes made.");
                }
                return;
            }
        }
        System.out.println("Hostel not found or you don't own it.");
    }

    public void updateRoomDetails(Scanner scanner, List<Hostel> allHostels) {
        System.out.print("Enter Hostel ID to update room details: ");
        int hostelID = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (Hostel hostel : allHostels) {
            if (hostel.getHostelID() == hostelID && hostel.getOwnerID().equals(this.userID)) {
                System.out.println("Current rooms in " + hostel.getName() + ":");
                List<Room> rooms = hostel.getRooms();

                for (Room room : rooms) {
                    System.out.println("Room ID: " + room.getRoomID() +
                                       ", Name: " + room.getRoomName() +
                                       ", Price: " + room.getPrice());
                }

                System.out.print("Enter Room ID to update: ");
                String roomID = scanner.nextLine();

                for (Room room : rooms) {
                    if (room.getRoomID().equals(roomID)) {
                        System.out.print("Enter new Room Name: ");
                        String newRoomName = scanner.nextLine();

                        System.out.print("Enter new Room Price: ");
                        double newRoomPrice = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline

                        if (!newRoomName.isEmpty()) {
                            room.setRoomName(newRoomName);
                        }
                        room.setPrice(newRoomPrice);

                        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
                        dbHandler.updateRoom(roomID, newRoomName, newRoomPrice, room.isAvailable());
                        System.out.println("Room details updated successfully.");
                        return;
                    }
                }

                System.out.println("Room not found.");
                return;
            }
        }

        System.out.println("Hostel not found or you don't own it.");
    }

    public void deleteRoom(Scanner scanner, List<Hostel> hostels) {
        System.out.println("Enter Hostel ID:");
        int hostelID = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Hostel targetHostel = null;
        for (Hostel hostel : hostels) {
            if (hostel.getHostelID() == hostelID) {
                targetHostel = hostel;
                break;
            }
        }

        if (targetHostel == null) {
            System.out.println("Hostel ID not found.");
            return;
        }

        System.out.println("Enter Room ID to delete:");
        String roomID = scanner.nextLine();

        boolean removed = targetHostel.removeRoom(roomID);
        if (removed) {
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            dbHandler.deleteRoom(roomID);
            System.out.println("Room deleted successfully.");
        } else {
            System.out.println("Room not found.");
        }
    }

    
    public void deleteFromDatabase() 
    {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        // Delete all hostels associated with this owner
        for (Hostel hostel : ownedHostels) {
            dbHandler.deleteHostel(hostel.getHostelID());
        }
        dbHandler.deleteHostelOwner(userID);
    }

}
