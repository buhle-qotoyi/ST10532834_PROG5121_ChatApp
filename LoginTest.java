/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapppt1;

//Allows us to write @test
import org.junit.Test;

//Allows us to use all assertions
import static org.junit.Assert.*;
/**
 *
 * @author Buhle
 */

//Makes a login object so we can test those methods inside it
public class LoginTest {
    
    Login login = new Login();
    
    
    //User Name test
    @Test
    public void testvalidUsername(){
        //Should be 5 characters long
        assertTrue(login.checkUserName("BU1_1"));
    }
    @Test
    public void testInvalidUsername_noUnderscore(){
        //Checks the username for an underscore
        assertFalse(login.checkUserName("Buhle!!!!!!!"));
    }
    @Test
    public void testInvalidUsername_TooLong(){
        //reason why it is false
        //username should be 5 characters
        
        assertFalse(login.checkUserName("BUUUUHLEEEE"));
    }
    
    //Checks Password
    @Test
    public void testvalidPassword(){
        //Password must be longer than 8 characters and must have special charcters
        assertTrue(login.checkPasswordComplexity("Bu&&man@U22"));
    }
    
    @Test
    public void testInvalidPassword_TooShort(){
        //checks password length
        assertFalse(login.checkPasswordComplexity("password"));
    }
    
    @Test
    public void testInvalidPassword_DoesNotHaveSpecialCharacters() {
        //checks if the password has special characters
        assertFalse(login.checkPasswordComplexity("password"));
    }
    
    //Phone number test
    @Test
    public void testvalidphoneNumber() {
        //phone number must start with +27
        assertTrue(login.checkCellPhoneNumber("+27460295178"));
    }
    
    @Test
    public void testInvalidphoneNumber_DoesNotHaveSouthAfricanCode(){
        //checks if it starts with +27 not 0
        assertFalse(login.checkCellPhoneNumber("07460295178"));
        
    }
    
    @Test
    public void testInvalidphoneNumber_TooShort() {
        //checks if the number is the correct legnth
        assertFalse(login.checkCellPhoneNumber("074602"));
    }
    
    
    //Login Test
    @Test
    public void testLoginSuccess() {
        //First register a user
        login.registerUser("BU1_1","Bu&&man@U22" , "+27460295178");
        
        //Then attempt login with correct details
        boolean result = login.loginUser("BU1_1", "Bu&&man@U22");
        
        
        assertTrue(result);
    }
    
    @Test
    public void testLoginFail() {
        //First register a user
        login.registerUser("BU1_1", "Bu&&man@U22", "+27460295178");
        
        
        //Attempt login with incorrect password
        boolean result = login.loginUser("BU1_1", "PasssW");
        
        assertFalse(result);
    }
            
}
