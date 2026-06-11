/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapppt1;
/**
 *
 * @author Buhle
 */
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class MessageTest {

    // ===== PART 2 TESTS - DO NOT CHANGE =====

    // Message length valid (under 250 chars)
    @Test
    public void testMessageLengthValid() {
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.validateMessageText("Hi Mike, can you join us for dinner tonight?"));
    }

    // Message length invalid (over 250 chars)
    @Test
    public void testMessageLengthInvalid() {
        Message msg = new Message(1, "+27718693002", "Hi");
        String longText = "A".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", msg.validateMessageText(longText));
    }

    // Recipient number valid
    @Test
    public void testRecipientValid() {
        Message msg = new Message(1, "+27718693002", "Hi");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell("+27718693002"));
    }

    // Recipient number invalid (no international code)
    @Test
    public void testRecipientInvalid() {
        Message msg = new Message(1, "+27718693002", "Hi");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                msg.checkRecipientCell("08575975889"));
    }

    // Message hash correct using POE test data
    @Test
    public void testMessageHash() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        // Hash format: first2OfID:messageNumber:FirstWordLastWord
        // We only check the end part since ID is random
        assertTrue(msg.createMessageHash().endsWith(":HITONIGHT"));
    }

    // Message ID is exactly 10 digits
    @Test
    public void testMessageID() {
        Message msg = new Message(1, "+27718693002", "Hi");
        assertTrue(msg.checkMessageID());
    }

    // sentMessage - Send chosen
    @Test
    public void testSentMessageSend() {
        Message msg = new Message(1, "+27718693002", "Hi");
        assertEquals("Message successfully sent.", msg.sentMessage(1));
    }

    // sentMessage - Disregard chosen
    @Test
    public void testSentMessageDisregard() {
        Message msg = new Message(1, "+27718693002", "Hi");
        assertEquals("Press 0 to delete the message.", msg.sentMessage(2));
    }

    // sentMessage - Store chosen
    @Test
    public void testSentMessageStore() {
        Message msg = new Message(1, "+27718693002", "Hi");
        assertEquals("Message successfully stored.", msg.sentMessage(3));
    }


    // ===== PART 3 TESTS =====

    /**
     * Clears all arrays before each Part 3 test to ensure clean state.
     */
    @Before
    public void clearArraysBeforeEachTest() {
        Message.clearAllArrays();
    }

    /**
     * Test 1: Checks that sent messages are correctly added to the sentMessages array.
     * POE messages 1 and 4 are flagged as Sent.
     */
    @Test
    public void testSentMessagesArray_correctlyPopulated() {
        Message msg1 = new Message(1, "+27834557896", "Did you get the cake?");
        Message msg4 = new Message(4, "0838884567", "It is dinner time!");

        msg1.sentMessage(1); // Send
        msg4.sentMessage(1); // Send

        assertTrue(Message.getSentMessages().contains("Did you get the cake?"));
        assertTrue(Message.getSentMessages().contains("It is dinner time!"));
    }

    /**
     * Test 2: Checks that displayLongestMessage returns the correct longest message.
     * POE expected: "Where are you? You are late! I have asked you to be on time."
     */
    @Test
    public void testDisplayLongestMessage_returnsCorrectMessage() {
        Message.addToStoredMessages("Did you get the cake?");
        Message.addToStoredMessages("Where are you? You are late! I have asked you to be on time.");
        Message.addToStoredMessages("Yohh wait!!! I am coming.");
        Message.addToStoredMessages("It is dinner time!");
        Message.addToStoredMessages("Ok, I am leaving without you.");

        Message msg = new Message();
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                msg.displayLongestMessage());
    }

    /**
     * Test 3: Checks that searchByMessageID returns the correct message for a given ID.
     * POE expected: searching msg4's ID returns "It is dinner time!"
     */
    @Test
    public void testSearchByMessageID_returnsCorrectMessage() {
        Message msg4 = new Message(4, "0838884567", "It is dinner time!");
        msg4.sentMessage(1); // Send - adds to sentMessages and messageIDs

        Message searcher = new Message();
        assertEquals("It is dinner time!", searcher.searchByMessageID(msg4.getMessageID()));
    }

    /**
     * Test 4: Checks that searchByRecipient returns ALL messages for a given recipient.
     * POE expected: +27838884567 returns both message 2 and message 5.
     */
    @Test
    public void testSearchByRecipient_returnsAllMatchingMessages() {
        Message msg2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        Message msg5 = new Message(5, "+27838884567", "Ok, I am leaving without you.");

        msg2.sentMessage(1); // Send
        msg5.sentMessage(1); // Send

        Message searcher = new Message();
        String result = searcher.searchByRecipient("+27838884567");

        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    /**
     * Test 5: Checks that deleteByHash removes the correct message and returns the right success message.
     * POE expected: deleting msg2 by hash returns the success message with the message text.
     */
    @Test
    public void testDeleteByHash_removesCorrectMessage() {
        Message msg2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage(1); // Send - adds hash to messageHashes

        Message searcher = new Message();
        String result = searcher.deleteByHash(msg2.getMessageHash());

        assertEquals("Message: Where are you? You are late! I have asked you to be on time. successfully deleted.",
                result);
    }

    /**
     * Test 6: Checks that the full report contains hash, recipient, and message for sent messages.
     */
    @Test
    public void testDisplayReport_containsRequiredFields() {
        Message msg1 = new Message(1, "+27834557896", "Did you get the cake?");
        msg1.sentMessage(1); // Send

        String report = Message.printMessages();

        assertTrue(report.contains(msg1.getMessageHash()));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
    }
}
