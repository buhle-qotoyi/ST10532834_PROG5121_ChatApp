/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// An update for part 3
/**
 *
 * @author Buhle
 */
package com.mycompany.chatapppt1;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        // Allows for the user's input for their information
        Scanner input = new Scanner(System.in);

        // Creating an object for the login class so we can call its methods
        Login login = new Login();

        // REGISTRATION SECTION
        System.out.println("===USER REGISTRATION===");

        System.out.print("Enter a username: ");
        String username = input.nextLine();

        System.out.print("Enter a password: ");
        String password = input.nextLine();

        System.out.print("Enter your South African phone number (+27...): ");
        String phone = input.nextLine();

        // Call the registerUser method and store the message it returns
        String response = login.registerUser(username, password, phone);

        // Show the registration message
        System.out.println(response);

        // LOGIN SECTION
        System.out.println("=== USER LOGIN ===");

        System.out.print("Enter a username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = input.nextLine();

        // Call loginUser to check if details match the stored ones
        boolean loggedIn = login.loginUser(loginUsername, loginPassword);

        // Print out the correct login message
        String loginMessage = login.returnLoginStatus(loggedIn);
        System.out.println(loginMessage);

        // MAIN APP SECTION
        if (loggedIn) {
            System.out.println("Welcome to ChatApp.");

            // PART 3 - Load any previously stored messages from JSON into the storedMessages array
            Message.loadStoredMessages();

            boolean running = true;

            while (running) {
                System.out.println("\n1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.println("4) Stored Messages");   // PART 3 - new menu option
                System.out.print("Enter your choice: ");
                int choice = input.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("How many messages would you like to send? ");
                        int numMessages = input.nextInt();
                        input.nextLine(); // clear buffer

                        for (int i = 0; i < numMessages; i++) {
                            int messageNumber = i + 1;
                            System.out.println("--- Message " + messageNumber + " ---");

                            // Validate recipient
                            String recipient = "";
                            while (true) {
                                System.out.print("Enter recipient number (+27...): ");
                                recipient = input.nextLine();
                                Message tempMsg = new Message(messageNumber, recipient, "temp");
                                String recipientCheck = tempMsg.checkRecipientCell(recipient);
                                System.out.println(recipientCheck);
                                if (recipientCheck.contains("successfully")) break;
                            }

                            // Validate message text
                            String messageText = "";
                            while (true) {
                                System.out.print("Enter your message (max 250 chars): ");
                                messageText = input.nextLine();
                                Message tempMsg = new Message(messageNumber, recipient, messageText);
                                String textCheck = tempMsg.validateMessageText(messageText);
                                System.out.println(textCheck);
                                if (textCheck.equals("Message ready to send.")) break;
                            }

                            // Create the final message object
                            Message msg = new Message(messageNumber, recipient, messageText);

                            // Send, Store, Disregard option
                            System.out.println("\n1) Send Message");
                            System.out.println("2) Disregard Message");
                            System.out.println("3) Store Message to send later");
                            System.out.print("Choose an option: ");
                            int sendChoice = input.nextInt();
                            input.nextLine();

                            String sendResult = msg.sentMessage(sendChoice);
                            System.out.println(sendResult);

                            // Show message details
                            System.out.println("MESSAGE DETAILS");
                            System.out.println("Message ID     : " + msg.getMessageID());
                            System.out.println("Message Hash   : " + msg.getMessageHash());
                            System.out.println("Recipient      : " + msg.getRecipient());
                            System.out.println("Message        : " + msg.getMessageText());
                            System.out.println("Status         : " + msg.getSendStatus());
                        }
                        break;

                    case 2:
                        System.out.println(Message.printMessages());
                        break;

                    case 3:
                        running = false;
                        System.out.println("Goodbye!");
                        break;

                    case 4:
                        // PART 3 - Stored Messages sub-menu
                        storedMessagesMenu(input);
                        break;

                    default:
                        System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
                }
            }
        } else {
            System.out.println("Access denied. Please check your username and password.");
        }
    }

    /**
     * Displays and handles the Stored Messages sub-menu for Part 3 features.
     * @param input the Scanner object for reading user input
     */
    private static void storedMessagesMenu(Scanner input) {
        Message msg = new Message();
        boolean running = true;

        while (running) {
            System.out.println("\n=== Stored Messages Menu ===");
            System.out.println("a) Display all stored messages");
            System.out.println("b) Display longest message");
            System.out.println("c) Search by message ID");
            System.out.println("d) Search by recipient");
            System.out.println("e) Delete by message hash");
            System.out.println("f) Display full report");
            System.out.println("q) Return to main menu");
            System.out.print("Enter choice: ");
            String choice = input.next();
            input.nextLine();

            switch (choice.toLowerCase()) {
                case "a":
                    System.out.println(Message.getStoredMessages());
                    break;
                case "b":
                    System.out.println(msg.displayLongestMessage());
                    break;
                case "c":
                    System.out.print("Enter message ID: ");
                    String id = input.nextLine();
                    System.out.println(msg.searchByMessageID(id));
                    break;
                case "d":
                    System.out.print("Enter recipient number: ");
                    String recipient = input.nextLine();
                    System.out.println(msg.searchByRecipient(recipient));
                    break;
                case "e":
                    System.out.print("Enter message hash: ");
                    String hash = input.nextLine();
                    System.out.println(msg.deleteByHash(hash));
                    break;
                case "f":
                    System.out.println(Message.printMessages());
                    break;
                case "q":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose a, b, c, d, e, f, or q.");
            }
        }
    }
}
