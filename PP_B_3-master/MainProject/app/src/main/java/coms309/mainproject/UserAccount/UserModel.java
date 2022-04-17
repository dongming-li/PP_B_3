package coms309.mainproject.UserAccount;

/**
 * Created by hausten on 11/1/17.
 */

public class UserModel {

    private final int userID;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String password;

    public UserModel(int userID, String fistName, String lastName,
                     String phoneNumber, String password) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public int getID() {
        return userID;
    }

    private String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
