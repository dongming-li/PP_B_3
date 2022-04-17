package coms309.mainproject.schedule.helpers;

import java.util.ArrayList;

import coms309.mainproject.schedule.event.Course;
import coms309.mainproject.schedule.event.Event;
import coms309.mainproject.schedule.switching_calendars.Contact;

/**
 * This class stores the different ways incoming string data from the database needs
 * to be spliced or organized, frequently into arrays to be stored and accessed later.
 */

public class StringSplicer {

    /**Mandatory constructor method.
     * Does nothing else. */
    public StringSplicer() {
        //Empty constructor
    }

    /**
     * Split up all the information by rows and then by column and create new Event instances
     *
     * @param res - source string containing several rows of event data
     * @return a list of Event instances based on the res parameter
     */
    public ArrayList<Event> spliceEvents(String res) {
        ArrayList<Event> events = new ArrayList<>();

        if (res.length() == 0) return events;

        String[] eventInfo = res.split("\n"); //Split by row, each row an event

        for (String s : eventInfo) {
            String[] cols = s.split("~/"); //Split by column, each column a value

            Event e = new Event(cols[0], cols[1], cols[2], cols[3], cols[4], cols[5], cols[6]);
            events.add(e);
        }

        return events;
    }

    /**
     * Split up all classes information by row and then by column
     *
     * @param res - resource string containing several rows of Course data
     * @return a list of Course instances based on the res parameter
     */
    public ArrayList<Course> spliceClasses(String res) {
        ArrayList<Course> courses = new ArrayList<>();

        if (res.length() == 0) return courses;

        String[] courseInfo = res.split("\n");

        for (String s : courseInfo) {
            String[] cols = s.split("~/");

            Course c = new Course(cols[0], cols[1], cols[2], cols[3]);
            courses.add(c);
        }

        return courses;
    }

    /**
     * Split up user information by row and then by column
     *
     * @param res - resource string containing several rows of User data
     * @return a list of User instances based on the rest parameter
     */
    public ArrayList<Contact> spliceContacts(String res) {
        ArrayList<Contact> contacts = new ArrayList<>();

        if (res.length() == 0) return contacts;

        String[] contactInfo = res.split("\n");

        for (String s : contactInfo) {
            String[] cols = s.split("~/");

            Contact c = new Contact(cols[0], cols[1]);
            contacts.add(c);
        }

        return contacts;
    }

}
