/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapppt1;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Message.java
 * Stores and handles all message data and operations for ChatApp.
 * Includes Part 3 arrays, search, delete, and report functionality.
 * @author Buhle
 */
public class Message {

    // FIELDS
    private String messageID;       // Random 10-digit auto-generated ID
    private int messageNumber;      // Message number from the loop counter
    private String recipient;       // Validated SA cell number (+27...)
    private String messageText;     // The message content (max 250 chars)
    private String messageHash;     // Auto-generated hash for the message
    private String sendStatus;      // Tracks if message was Sent, Stored, or Disregarded

    // PART 3 - Five static arrays that persist across all Message objects for the session
    // Attribution: Java ArrayList - https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
    private static List<String> sentMessages = new ArrayList<>();
    private static List<String> disregardedMessages = new ArrayList<>();
    private static List<String> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIDs = new ArrayList<>();
    private static List<String> recipientList = new ArrayList<>();  // parallel to sentMessages


    // CONSTRUCTORS

    /**
     * Default no-arg constructor needed for test setup and sub-menu usage.
     */
    public Message() {
        this.messageID = "";
        this.messageNumber = 0;
        this.recipient = "";
        this.messageText = "";
        this.messageHash = "";
        this.sendStatus = "";
    }

    /**
     * Full constructor. Initialises all fields when a new Message object is created.
     * @param messageNumber the loop counter number of this message
     * @param recipient the recipient cell number
     * @param messageText the message content
     */
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();       // Auto-generate ID on creation
        this.messageHash = createMessageHash();     // Auto-generate hash on creation
        this.sendStatus = "";                       // Empty until user chooses Send/Store/Disregard
    }


    // MESSAGE ID

    /**
     * Randomly generates a 10-digit message ID using string manipulation.
     * @return 10-digit ID as a String
     */
    private String generateMessageID() {
        Random random = new Random();
        long id = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    /**
     * Validates that the message ID is exactly 10 digits long.
     * @return true if valid, false if not
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }


    // RECIPIENT VALIDATION

    /**
     * Validates the recipient cell number.
     * Must contain an international code and be max 12 characters total.
     * @param number the cell number to validate
     * @return success or failure message
     */
    public String checkRecipientCell(String number) {
        if (number.startsWith("+") && number.substring(1).length() <= 11) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain " +
                   "an international code. Please correct the number and try again.";
        }
    }


    // MESSAGE TEXT VALIDATION

    /**
     * Validates that the message does not exceed 250 characters.
     * @param text the message text to validate
     * @return success or failure message showing how many chars over the limit
     */
    public String validateMessageText(String text) {
        if (text.length() <= 250) {
            return "Message ready to send.";
        } else {
            int over = text.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
    }


    // MESSAGE HASH

    /**
     * Creates the message hash in the format:
     * First2DigitsOfID:MessageNumber:FirstWordLastWord
     * Example: 00:0:HITONIGHT
     * @return the generated hash as a String
     */
    public String createMessageHash() {
        // Step 1: Get first 2 digits of the message ID
        String first2 = messageID.substring(0, 2);

        // Step 2: Split the message text into words
        String[] words = messageText.trim().split(" ");

        // Step 3: Get the first and last word, convert to uppercase
        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase().replaceAll("[^A-Z]", "");

        // Step 4: Combine everything into the hash format
        return first2 + ":" + messageNumber + ":" + firstWord + lastWord;
    }


    // SEND, STORE, DISREGARD

    /**
     * Handles the user's choice to Send, Discard, or Store the message.
     * Populates the correct arrays based on the choice made.
     * @param choice 1=Send, 2=Discard, 3=Store
     * @return result message string
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                // SEND - add to sent arrays
                sendStatus = "Sent";
                sentMessages.add(this.messageText);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                recipientList.add(this.recipient);
                return "Message successfully sent.";

            case 2:
                // DISCARD - add to disregarded array only
                sendStatus = "Disregarded";
                disregardedMessages.add(this.messageText);
                return "Press 0 to delete the message.";

            case 3:
                // STORE - write to JSON, add to hash and ID arrays
                // Note: storedMessages array is populated by loadStoredMessages(), not here
                sendStatus = "Stored";
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                recipientList.add(this.recipient);
                storeMessage();
                return "Message successfully stored.";

            default:
                return "Invalid option. Please choose 1, 2, or 3.";
        }
    }


    // STORE TO JSON

    /**
     * Writes the message to messages.json as a JSON object on a new line.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     */
    public void storeMessage() {
        JSONObject obj = new JSONObject();
        obj.put("messageID", this.messageID);
        obj.put("messageNumber", this.messageNumber);
        obj.put("recipient", this.recipient);
        obj.put("messageText", this.messageText);   // key is "messageText" for Part 3 reading
        obj.put("messageHash", this.messageHash);

        try (FileWriter fw = new FileWriter("messages.json", true)) {
            fw.write(obj.toString() + "\n");
            System.out.println("Message saved to messages.json");
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }


    // PART 3 - LOAD STORED MESSAGES FROM JSON

    /**
     * Reads messages.json at startup and loads each stored message into the storedMessages array.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     * BufferedReader - https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
     */
    public static void loadStoredMessages() {
        try (BufferedReader reader = new BufferedReader(new FileReader("messages.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    JSONObject obj = new JSONObject(line);
                    String text = obj.getString("messageText");
                    storedMessages.add(text);
                }
            }
            System.out.println("Stored messages loaded: " + storedMessages.size());
        } catch (IOException e) {
            // File does not exist yet - this is fine, continue without crashing
            System.out.println("No stored messages found. Starting fresh.");
        }
    }


    // PART 3 - DISPLAY LONGEST MESSAGE

    /**
     * Searches the storedMessages array and returns the message with the most characters.
     * @return the longest stored message, or a message if none found
     */
    public String displayLongestMessage() {
        String longest = "";
        for (String message : storedMessages) {
            if (message.length() > longest.length()) {
                longest = message;
            }
        }
        if (longest.isEmpty()) {
            return "No stored messages found.";
        }
        return longest;
    }


    // PART 3 - SEARCH BY MESSAGE ID

    /**
     * Searches messageIDs array for a match and returns the corresponding sent message.
     * Uses parallel array indexing - same index in messageIDs maps to same index in sentMessages.
     * @param id the message ID to search for
     * @return the matching message text, or not found message
     */
    public String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                if (i < sentMessages.size()) {
                    return sentMessages.get(i);
                }
            }
        }
        return "Message not found.";
    }


    // PART 3 - SEARCH BY RECIPIENT

    /**
     * Searches recipientList for all messages sent to a given recipient number.
     * Returns all matching messages, not just the first one.
     * @param recipient the cell number to search for
     * @return all matching messages as a formatted string
     */
    public String searchByRecipient(String recipient) {
        StringBuilder results = new StringBuilder();
        for (int i = 0; i < recipientList.size(); i++) {
            if (recipientList.get(i).equals(recipient)) {
                if (i < sentMessages.size()) {
                    results.append(sentMessages.get(i)).append("\n");
                }
            }
        }
        if (results.length() == 0) {
            return "No messages found for recipient: " + recipient;
        }
        return results.toString().trim();
    }


    // PART 3 - DELETE BY HASH

    /**
     * Finds a message by its hash and removes it from all parallel arrays.
     * @param hash the message hash to search for and delete
     * @return success message with deleted text, or not found message
     */
    public String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String deletedMessage = "";
                if (i < sentMessages.size()) {
                    deletedMessage = sentMessages.get(i);
                    sentMessages.remove(i);
                }
                messageHashes.remove(i);
                if (i < messageIDs.size()) messageIDs.remove(i);
                if (i < recipientList.size()) recipientList.remove(i);
                return "Message: " + deletedMessage + " successfully deleted.";
            }
        }
        return "Hash not found.";
    }


    // PART 3 - DISPLAY MESSAGE REPORT

    /**
     * Builds and returns a formatted report of all sent messages.
     * Each entry shows the hash, recipient, and message text.
     * @return the full report as a String
     */
    public static String printMessages() {
        StringBuilder report = new StringBuilder();
        report.append("=== Message Report ===\n");

        if (sentMessages.isEmpty()) {
            return "No sent messages to display.";
        }

        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("---------------------------\n");
            report.append("Hash:      ").append(messageHashes.get(i)).append("\n");
            report.append("Recipient: ").append(recipientList.get(i)).append("\n");
            report.append("Message:   ").append(sentMessages.get(i)).append("\n");
        }
        report.append("---------------------------\n");
        return report.toString();
    }


    // PART 3 - DISPLAY ALL STORED MESSAGES

    /**
     * Returns all stored messages as a formatted string for sub-menu option a.
     * @return all stored messages or a message if none found
     */
    public static String getStoredMessages() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append(i + 1).append(") ").append(storedMessages.get(i)).append("\n");
        }
        return sb.toString().trim();
    }


    // TOTAL MESSAGES

    /**
     * Returns the message number (used for total messages sent count).
     * @return message number
     */
    public int returnTotalMessages() {
        return messageNumber;
    }


    // PART 3 - HELPER METHODS FOR TESTS

    /**
     * Manually adds a message to storedMessages - used in unit tests to set up test data.
     * @param message the message text to add
     */
    public static void addToStoredMessages(String message) {
        storedMessages.add(message);
    }

    /**
     * Clears all static arrays - used in unit tests to reset state between tests.
     */
    public static void clearAllArrays() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        recipientList.clear();
    }

    /**
     * Returns the sentMessages list - used in unit tests.
     * @return list of sent message texts
     */
    public static List<String> getSentMessages() {
        return sentMessages;
    }


    // GETTERS
    // Allows other classes to safely read the private fields

    public String getMessageID()     { return messageID; }
    public int getMessageNumber()    { return messageNumber; }
    public String getRecipient()     { return recipient; }
    public String getMessageText()   { return messageText; }
    public String getMessageHash()   { return messageHash; }
    public String getSendStatus()    { return sendStatus; }


    // SETTERS
    // Allow other classes to safely update fields after validation

    /**
     * Sets the recipient after validating the cell number format.
     * @param recipient the cell number to set
     */
    public void setRecipient(String recipient) {
        if (checkRecipientCell(recipient).contains("successfully")) {
            this.recipient = recipient;
        } else {
            System.out.println(checkRecipientCell(recipient));
        }
    }

    /**
     * Sets the message text and regenerates the hash after validation.
     * @param messageText the new message text
     */
    public void setMessageText(String messageText) {
        if (messageText.length() <= 250) {
            this.messageText = messageText;
            this.messageHash = createMessageHash();
        } else {
            System.out.println(validateMessageText(messageText));
        }
    }
}
