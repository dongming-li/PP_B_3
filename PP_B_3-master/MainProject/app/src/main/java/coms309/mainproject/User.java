package coms309.mainproject;


import android.support.annotation.NonNull;

/**
 * Class for constructing a new User with a user id, email, and password.
 * information.
 *
 * @author christine
 */
public class User {
    /**
     * The user id, email, and password of the User.
     */
    public String userID, email, password;

    /**
     * Constructs a new Contact.
     * @param userID the user's user id
     * @param email the user's email
     * @param password the user's password
     */
    public User(String userID, String email, String password) {
        this.userID = userID;
        this.email = email;
        this.password = password;
    }

    /**
     * String concatenation of the contact's information.
     * @return String in the form of the contact's first name, last name, phone number, and email.
     */
    public String toString() {
        return userID + " " + email + " " + password + " " + email + " ";
    }

}

