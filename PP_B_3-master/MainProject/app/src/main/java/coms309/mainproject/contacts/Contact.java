package coms309.mainproject.contacts;


import android.support.annotation.NonNull;

/**
 * Class for constructing a new contact with first name, last name, phone number, and email
 * information.
 *
 * @author christine
 */
public class Contact implements Comparable<Contact> {
    /**
     * The first name, last name, phone number, and email of the contact.
     */
    public String firstName, lastName, phoneNumber, email;

    /**
     * Constructs a new Contact.
     * @param firstName the contact's first name
     * @param lastName the contact's last name
     * @param phoneNumber the contact's phone number
     * @param email the contact's email
     */
    public Contact(String firstName, String lastName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * Compares the last name of this contact lexicographically to another contact.
     * @param other
     * @return negative integer if this contact's last name lexicographically
     *  precedes the other contact's last name, positive integer if this contact's last name
     *  lexicographically follows the other contact's last name, zero if this contact's last name
     *  and the other contact's last name are equal.
     */
    @Override
    public int compareTo(@NonNull Contact other) {
        return this.lastName.compareTo(other.lastName);
    }

    /**
     * String concatenation of the contact's information.
     * @return String in the form of the contact's first name, last name, phone number, and email.
     */
    public String toString() {
        return firstName + " " + lastName + " " + phoneNumber + " " + email + " ";
    }

}

