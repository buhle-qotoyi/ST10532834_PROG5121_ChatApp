/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Buhle
 */
package com.mycompany.chatapppt1;
//These imports writes text to files
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Login {
        
//Variables used to store user information
private String username;
private String password;
private String phoneNumber;

//Makes sure the username contains "_" and must be 5 characters
public boolean checkUserName(String username) {
return username.contains("_") && username.length() <=5;

}
//Checks the password complexity
public boolean checkPasswordComplexity(String password) {
    
    boolean hasCapital = false;
    boolean hasNumber = false;
    boolean hasSpecial = false;
    
    //Loop through each character
    for (int i = 0; i < password.length(); i++) {
        char c = password.charAt(i);
        
        if (Character.isUpperCase(c)) {
            hasCapital = true;
        } else if (Character.isDigit(c)) {
            hasNumber = true;
        } else if (!Character.isLetterOrDigit(c)) {
            hasSpecial =true;
        }
    }
    
    return password.length() >=8 && hasCapital && hasNumber && hasSpecial;
}
//Checks if it is in South African phone number format
public boolean checkCellPhoneNumber(String phone) {
    return phone.startsWith("+27") && phone.length() <=12;
}

//Register user
public String registerUser(String username, String password, String phoneNumber){
    
    //validates user name
    if(!checkUserName(username)){
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";          
    }
    
    //Validates the password
    if(!checkPasswordComplexity(password)){
        return"Password id not correctly formatted;please ensure that the password contains at least eight charcters, a capital letter,a number, and a special character.";
    }
    
    //Validates the phone number
    if(!checkCellPhoneNumber(phoneNumber)){
        return"Cell phone number incorrectly formatted or does not contain international code.";
    }
    
    //Stores the validated values
    this.username = username;
    this.password = password;
    this.phoneNumber = phoneNumber;
    
    //Save to file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt"))) {
        writer.write(username + "'" + password + "," + phoneNumber);
    } catch (IOException e ) {
        return "Error saving user.";
    }
    
    return"User registered successfully.";
}

//Login user
public boolean loginUser(String username, String password) {
    return this.username.equals(username) && this.password.equals(password);
}

//Return login status message
public String returnLoginStatus(boolean success) {
    if(success) {
        return "Welcome " + username + " it is great to see you again.";
    } else {
        return "Username or password incorrect, please try again.";
        
    }
}
}