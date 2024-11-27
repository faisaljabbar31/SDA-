package HostelManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Main Class
public class main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3305/HMS", "root", "12345");
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        List<Users> users = new ArrayList<>();
        List<Hostel> hostels = new ArrayList<>();
        List<Payment> payments = new ArrayList<>();

        Admin admin = new Admin();

        while (true) {
            System.out.println("Welcome to Hostel Management System");
            System.out.println("A) Hostel Owner\nB) Normal User\nC) Admin\nD) Exit");
            System.out.print("Choose an option: ");
            char choice = scanner.next().toUpperCase().charAt(0);

            switch (choice) {
                case 'A':
                    System.out.println("1) Login\n2) Register");
                    int ownerChoice = scanner.nextInt();

                    if (ownerChoice == 1) {
                        // Login
                        System.out.print("Enter ID: ");
                        String ownerID = scanner.next();
                        System.out.print("Enter Password: ");
                        String password = scanner.next();

                        HostelOwner owner = (HostelOwner) Users.loadFromDatabase(ownerID);
                        if (owner != null && owner.login(password)) {
                            System.out.println("Login successful.");
                            // Owner Menu
                            System.out.println("1) List Hostels\n2) View Listed Hostels\n3) Contact Support\n4) View/Edit Account Details\n5) Delete Account\n6) Update Hostel Details\n7) Delete room");
                            int ownerAction = scanner.nextInt();

                            switch (ownerAction) {
                                case 1:
                                    String hostelName = readAlphabeticString(scanner, "Enter Hostel Name: ");
                                    int numRooms = readPositiveInt(scanner, "Enter Number of Rooms: ");
                                    List<Room> rooms = new ArrayList<>();
                                    for (int i = 1; i <= numRooms; i++) {
                                        String roomName = readAlphabeticString(scanner, "Enter Room Name for Room " + i + ": ");
                                        double price = readPositiveDouble(scanner, "Enter Price for Room " + i + ": ");
                                        rooms.add(new Room("R" + i, roomName, price, true));
                                    }
                                    owner.listHostel(hostelName, numRooms, rooms, hostels);
                                    break;

                                case 2:
                                    owner.viewListedHostels();
                                    break;

                                case 3:
                                    Support.contactSupport(scanner, owner.getUserID());
                                    break;

                                case 4:
                                    // View/Edit Account Details
                                    System.out.println("1) View Account Details\n2) Edit Account Details");
                                    int accountAction = scanner.nextInt();
                                    if (accountAction == 1) {
                                        owner.viewAccountDetails();
                                    } else if (accountAction == 2) {
                                        String newName = readValidName(scanner, "Enter Name: ");
                                        String newEmail = readValidEmail(scanner, "Enter Email: ");
                                        owner.editAccountDetails(scanner);
                                    }
                                    break;

                                case 5:
                                    // Delete Account
                                    System.out.println("Are you sure you want to delete your account? (yes/no)");
                                    String confirmDelete = scanner.next().toLowerCase();
                                    if (confirmDelete.equals("yes")) {
                                        owner.deleteFromDatabase();
                                        users.remove(owner);
                                        System.out.println("Account deleted.");
                                    }
                                    break;

                                case 6:
                                    // Update Hostel Details
                                    System.out.println("1) Update Hostel Name\n2) Update Room Details");
                                    int updateAction = scanner.nextInt();
                                    if (updateAction == 1) {
                                        owner.updateHostelDetails(scanner, hostels);
                                    } else if (updateAction == 2) {
                                        owner.updateRoomDetails(scanner, hostels);
                                    } else {
                                        System.out.println("Invalid choice.");
                                    }
                                    break;

                                case 7:
                                    // Delete Room
                                    owner.deleteRoom(scanner, hostels);
                                    break;

                                default:
                                    System.out.println("Invalid choice.");
                                    break;
                            }
                        } else {
                            System.out.println("Incorrect ID or password.");
                        }

                    } else if (ownerChoice == 2) {
                        // Register
                        System.out.print("Enter ID: ");
                        String ownerID = scanner.next();
                        String name = readValidName(scanner, "Enter Name: ");
                        String email = readValidEmail(scanner, "Enter Email: ");
                        String phoneNumber = readValidPhoneNumber(scanner, "Enter Phone Number: ");
                        System.out.print("Enter Password: ");
                        String password = scanner.next();

                        HostelOwner newOwner = new HostelOwner(ownerID, name, email, phoneNumber, password);
                        newOwner.saveToDatabase();
                        users.add(newOwner);
                        System.out.println("Registration successful.");
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 'B':
                    System.out.println("1) Login\n2) Register");
                    int userChoice = scanner.nextInt();

                    if (userChoice == 1) {
                        // Login
                        System.out.print("Enter ID: ");
                        String userID = scanner.next();
                        System.out.print("Enter Password: ");
                        String password = scanner.next();

                        NormalUser normalUser = (NormalUser) Users.loadFromDatabase(userID);
                        if (normalUser != null && normalUser.login(password)) {
                            System.out.println("Login successful.");

                            // Display Normal User Menu
                            System.out.println("1) Search Hostels\n2) View Bookings\n3) Write a Review\n4) Contact Support\n5) View/Edit Account Details\n6) Delete Review\n7) Delete Account");
                            int userAction = readValidInt(scanner, "Enter action: ", 1, 7);

                            switch (userAction) {
                                case 1:
                                	if (hostels.isEmpty()) {
                                	    System.out.println("No hostels available.");
                                	    break;
                                	}

                                	System.out.println("Available Hostels:");
                                	for (int i = 0; i < hostels.size(); i++) {
                                	    System.out.println((i + 1) + ") " + hostels.get(i).getName());
                                	}

                                	int hostelChoice = readValidInt(scanner, "Enter hostel number: ", 1, hostels.size());
                                	Hostel selectedHostel = hostels.get(hostelChoice - 1);

                                	System.out.println("1) View Reviews\n2) View Rooms");
                                	int viewChoice = readValidInt(scanner, "Enter your choice: ", 1, 2);

                                	if (viewChoice == 1) {
                                	    // View Reviews
                                	    List<Review> reviews = selectedHostel.getReviews();
                                	    if (reviews.isEmpty()) {
                                	        System.out.println("No reviews available for this hostel.");
                                	    } else {
                                	        System.out.println("Reviews for " + selectedHostel.getName() + ":");
                                	        for (Review review : reviews) {
                                	            System.out.println(review);
                                	        }
                                	    }
                                	} else if (viewChoice == 2) {
                                	    // View Rooms
                                	    List<Room> rooms = selectedHostel.getRooms();
                                	    if (rooms.isEmpty()) {
                                	        System.out.println("No rooms available for this hostel.");
                                	        break;
                                	    }

                                	    System.out.println("Available Rooms in " + selectedHostel.getName() + ":");
                                	    for (int i = 0; i < rooms.size(); i++) {
                                	        if (rooms.get(i).isAvailable()) {
                                	            System.out.println((i + 1) + ") " + rooms.get(i).getRoomName() + " - $" + rooms.get(i).getPrice());
                                	        }
                                	    }

                                	    int roomChoice = readValidInt(scanner, "Enter room number: ", 1, rooms.size());
                                	    if (!rooms.get(roomChoice - 1).isAvailable()) {
                                	        System.out.println("Room not available. Choose another.");
                                	        break;
                                	    }

                                	    System.out.print("Enter Check-In Date (yyyy-mm-dd): ");
                                	    String checkInDate = readValidDate(scanner);
                                	    System.out.print("Enter Check-Out Date (yyyy-mm-dd): ");
                                	    String checkOutDate = readValidDate(scanner);

                                	    double cost = rooms.get(roomChoice - 1).getPrice();
                                	    System.out.println("Booking Summary: Total Cost: $" + cost);

                                	    String confirmation = readYesNo(scanner, "Confirm booking? (yes/no): ");
                                	    if (confirmation.equals("yes")) {
                                	        // Generate Booking ID and Payment ID
                                	        String bookingID = "B" + (normalUser.getBookings().size() + 1);
                                	        String paymentID = "P" + (payments.size() + 1);

                                	        Booking booking = new Booking(
                                	            bookingID,
                                	            rooms.get(roomChoice - 1).getRoomID(),
                                	            normalUser.getUserID(),
                                	            checkInDate,
                                	            checkOutDate,
                                	            cost
                                	        );

                                	        Payment payment = new Payment(
                                	            paymentID,
                                	            normalUser.getUserID(),
                                	            bookingID,
                                	            cost,
                                	            java.time.LocalDate.now().toString() // Current date as payment date
                                	        );

                                	        if (payment.processPayment()) {
                                	            normalUser.bookRoom(booking, payment);
                                	            rooms.get(roomChoice - 1).setAvailability(false);
                                	            payments.add(payment);
                                	            System.out.println("Booking successful! Your payment ID is: " + paymentID);
                                	        } else {
                                	            System.out.println("Payment failed. Booking not completed.");
                                	        }
                                	    } else {
                                	        System.out.println("Booking cancelled.");
                                	    }
                                	} else {
                                	    System.out.println("Invalid choice.");
                                	}

                                    break;

                                case 2:
                                    normalUser.viewBookings();
                                    System.out.println("1) Cancel a booking\n2) Go back to main menu");
                                    int bookingAction = scanner.nextInt();
                                    if (bookingAction == 1) {
                                        System.out.print("Enter Booking ID to cancel: ");
                                        String bookingID = scanner.next();
                                        normalUser.cancelBooking(bookingID, hostels);
                                    } else {
                                        System.out.println("Returning to main menu.");
                                    }
                                    break;

                                case 3:
                                    // Write a Review
                                    int hostelID = readValidInt(scanner, "Enter Hostel ID: ", 1, hostels.size());
                                    System.out.print("Enter your review: ");
                                    scanner.nextLine(); // Consume newline
                                    String review = scanner.nextLine();
                                    normalUser.reviewHostel(hostelID, review);
                                    break;

                                case 4:
                                    Support.contactSupport(scanner, normalUser.getUserID());
                                    break;

                                case 5:
                                    // View/Edit Account Details
                                    System.out.println("1) View Account Details\n2) Edit Account Details");
                                    int accountAction = scanner.nextInt();
                                    if (accountAction == 1) {
                                        normalUser.viewAccountDetails();
                                    } else if (accountAction == 2) {
                                        String newName = readValidName(scanner, "Enter Name: ");
                                        String newEmail = readValidEmail(scanner, "Enter Email: ");
                                        normalUser.editAccountDetails(scanner);
                                    }
                                    break;

                                case 6:
                                    // Delete Review
                                    System.out.print("Enter Review ID to delete: ");
                                    String reviewID = scanner.next();
                                    normalUser.deleteReview(reviewID, hostels);
                                    break;

                                case 7:
                                    System.out.println("Are you sure you want to delete your account? (yes/no)");
                                    String confirmDelete = scanner.next().toLowerCase();
                                    if (confirmDelete.equals("yes")) {
                                        normalUser.deleteFromDatabase();
                                        users.remove(normalUser);
                                        System.out.println("Account deleted.");
                                    }
                                    break;

                                default:
                                    System.out.println("Invalid action.");
                            }
                        } else {
                            System.out.println("Incorrect ID or password.");
                        }

                    } else if (userChoice == 2) {
                        // Registration Process
                        System.out.print("Enter ID: ");
                        String userID = scanner.next();

                        // Check if the ID is already in use
                        boolean idExists = users.stream().anyMatch(user -> user.getUserID().equals(userID));
                        if (idExists) {
                            System.out.println("User ID already exists. Please log in.");
                        } else {
                            String name = readAlphabeticString(scanner, "Enter Name: ");
                            String email = readValidEmail(scanner, "Enter Email: ");
                            String phoneNumber = readValidPhoneNumber(scanner, "Enter Phone Number: ");
                            System.out.print("Enter Password: ");
                            String password = scanner.next();
                            String city = readAlphabeticString(scanner, "Enter City: ");

                            NormalUser newUser = new NormalUser(userID, name, email, phoneNumber, password, city);
                            newUser.saveToDatabase();
                            users.add(newUser);
                            System.out.println("Registration successful. Please log in to continue.");
                        }
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 'C':
                    System.out.print("Enter Admin ID: ");
                    String adminID = scanner.next();
                    System.out.print("Enter Password: ");
                    String adminPassword = scanner.next();

                    if (admin.login(adminID, adminPassword)) {
                        System.out.println("Admin Login Successful.");
                        System.out.println("1) Show all normal users\n2) Show all hostel owners\n3) See support messages\n4) Manage payments");

                        int adminAction = readValidInt(scanner, "Enter your choice: ", 1, 4);
                        switch (adminAction) {
                            case 1:
                                // Show all normal users
                                admin.viewNormalUsers();
                                System.out.println("1) Delete a user\n2) Go back to main menu");
                                int normalUserAction = readValidInt(scanner, "Enter your choice: ", 1, 2);

                                if (normalUserAction == 1) {
                                    System.out.print("Enter User ID to delete: ");
                                    String deleteNormalUserID = scanner.next();
                                    admin.deleteNormalUser(deleteNormalUserID);
                                } else {
                                    System.out.println("Returning to main menu.");
                                }
                                break;

                            case 2:
                                // Show all hostel owners
                                admin.viewHostelOwners();
                                System.out.println("1) Delete a hostel owner\n2) Go back to main menu");
                                int hostelOwnerAction = readValidInt(scanner, "Enter your choice: ", 1, 2);

                                if (hostelOwnerAction == 1) {
                                    System.out.print("Enter Hostel Owner ID to delete: ");
                                    String deleteOwnerID = scanner.next();
                                    admin.deleteHostelOwner(deleteOwnerID);
                                } else {
                                    System.out.println("Returning to main menu.");
                                }
                                break;

                            case 3:
                                // See support messages
                                List<SupportMessage> supportMessages = Support.getSupportMessages();
                                if (supportMessages.isEmpty()) {
                                    System.out.println("No support messages found.");
                                } else {
                                    System.out.println("--- Support Messages ---");
                                    for (SupportMessage message : supportMessages) {
                                        System.out.println(message);
                                    }

                                    System.out.print("Enter Ticket ID to resolve or 0 to go back to main menu: ");
                                    int ticketID = scanner.nextInt();
                                    if (ticketID != 0) {
                                        Support.resolveSupportMessage(ticketID);
                                    } else {
                                        System.out.println("Returning to main menu.");
                                    }
                                }
                                break;

                            case 4:
                                // Manage payments
                                admin.managePayments(payments, users, hostels, scanner);
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }
                    } else {
                        System.out.println("Invalid Admin Credentials.");
                    }
                    break;

                case 'D':
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Helper methods for input validation
    private static int readPositiveInt(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value > 0) {
                    break;
                } else {
                    System.out.println("Please enter a positive number.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Clear invalid input
            }
        }
        return value;
    }

    private static double readPositiveDouble(Scanner scanner, String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();
                if (value > 0) {
                    break;
                } else {
                    System.out.println("Please enter a positive number.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Clear invalid input
            }
        }
        return value;
    }

    private static String readValidName(Scanner scanner, String prompt) {
        String name;
        while (true) {
            System.out.print(prompt);
            name = scanner.next();
            if (name.matches("[a-zA-Z]+")) {
                break;
            } else {
                System.out.println("Invalid input. Name must contain only letters.");
            }
        }
        return name;
    }

    private static String readValidEmail(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.next();
    }

    private static String readValidPhoneNumber(Scanner scanner, String prompt) {
        String phoneNumber;
        while (true) {
            System.out.print(prompt);
            phoneNumber = scanner.next();
            if (phoneNumber.matches("\\d+")) { // Accepts any number of digits
                break;
            } else {
                System.out.println("Invalid phone number. It must contain only digits.");
            }
        }
        return phoneNumber;
    }

    // Helper method for yes/no input
    public static String readYesNo(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.next().toLowerCase();
            if (input.equals("yes") || input.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter either 'yes' or 'no'.");
            }
        }
        return input;
    }

    // Helper method for valid date format
    public static String readValidDate(Scanner scanner) {
        String date;
        while (true) {
            date = scanner.next();
            if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                break;
            } else {
                System.out.println("Invalid date format. Enter as yyyy-mm-dd.");
            }
        }
        return date;
    }

    // Method to read alphabetic string only
    public static String readAlphabeticString(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.next();
            if (input.matches("[a-zA-Z]+")) { // Only alphabetic characters
                break;
            } else {
                System.out.println("Invalid input. Please enter alphabetic characters only.");
            }
        }
        return input;
    }

    // Helper method for integer validation
    public static int readValidInt(Scanner scanner, String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear invalid input
            }
        }
        return value;
    }
}
