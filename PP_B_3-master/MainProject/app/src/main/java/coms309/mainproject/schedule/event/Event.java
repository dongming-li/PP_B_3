package coms309.mainproject.schedule.event;

import java.util.Calendar;

/**
 * This class stores all the data for an individual event in a compact way to make getting that data simple and easy
 */

public class Event {

    public String eventID, title, userID, location, description, type, displayTime, timeFormatted;

    /**A calendar to keep the date and time for this specific event*/
    public Calendar dateTime = Calendar.getInstance();

    /**Constructor for the Event class.
     * Keeps Event data for the input parameters.
     *
     * @param id - This event's unique ID
     * @param t - Title of the event
     * @param uID - User ID of the person who created this event
     * @param loc - Location the event takes place
     * @param d - Description for this event
     * @param ty - Type of event
     * @param dt - The datetime of this event in string format
     * **/
    public Event(String id, String t, String uID, String loc, String d, String ty, String dt) {
        eventID = id;
        title = t;
        userID = uID;
        location = loc;
        description = d;
        type = ty;
        timeFormatted = dt;

        String[] dtInfo = dt.split(" ");
        String[] date = dtInfo[0].split("-"); //Year, Month, Day
        String[] time = dtInfo[1].split(":"); //Hour, Minute, Seconds.milliseconds

        dateTime.set(Calendar.YEAR, Integer.parseInt(date[0]));
        dateTime.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
        dateTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));

        dateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        dateTime.set(Calendar.MINUTE, Integer.parseInt(time[1]));

        if (dateTime.get(Calendar.HOUR) == 0) displayTime = "12"; //Special Case: 12 am/pm
        else displayTime = dateTime.get(Calendar.HOUR) + "";

        //Minutes
        if (dateTime.get(Calendar.MINUTE) < 10) displayTime += ":0" + dateTime.get(Calendar.MINUTE);
        else displayTime += ":" + dateTime.get(Calendar.MINUTE);

        //am pm
        if (dateTime.get(Calendar.HOUR_OF_DAY) < 12) displayTime += " am";
        else displayTime += " pm";
    }

    @Override
    public String toString() {

        String ret = "";

        ret += "Title: " + title + "\n";

        if (!location.equals("")) ret += location + "\n";
        ret += displayTime + "\n";
        if (!description.equals("")) ret += description + "\n";

        ret += type;

        return ret;
    }

    /**Create and return a string array version of the event's data
     *
     * @return An array containing event ID, title, type, location, description, and date/time
     * */
    public String[] toStringArray() {
        return new String[] {eventID, title, type, location, description, timeFormatted};
    }
}
