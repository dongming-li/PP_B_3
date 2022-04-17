package coms309.mainproject.schedule.switching_calendars;

/**
 * An alternative Contact class only containing the info essential to the Contacts View for calendar switching.
 * Takes info from the Users table instead of the Contacts table.
 */

public class Contact {

    public String userName;
    public int userID;

    /**
     * Contact constructor
     *
     * @param email - the email associated with this contact from which the uername is taken
     * @param id - user ID for contact
     */
    public Contact(String email, String id) {
        userID = Integer.parseInt(id);

        String split[] = email.split("@");

        userName = split[0];
    }

}
