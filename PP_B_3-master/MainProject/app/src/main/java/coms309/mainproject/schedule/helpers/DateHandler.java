package coms309.mainproject.schedule.helpers;

import java.util.Calendar;

/**
 * This is a helper class for date comparisons
 */

public class DateHandler {

    private Calendar today;

    /**Mandatory constructor.
     * Does nothing in particular.
     *
     * @param today - the date to be compared to
     */
    public DateHandler(Calendar today) {
        this.today = today;
    }

    /**
     * Check if the input date is today's date
     *
     * @param eventTime - The Calendar instance of some event
     * @return True if the class Calendar and the Event Calendar are the same day; False otherwise
     */
    public boolean checkDate(Calendar eventTime) {
        return today.get(Calendar.YEAR) == eventTime.get(Calendar.YEAR)
                && today.get(Calendar.MONTH) == eventTime.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) == eventTime.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Alternate date checking method which uses the date string instead of calendar format
     *
     * @param eventDate - a string version of some Event's date/time
     * @return True if the class Calendar and the eventDate address the same day; False otherwise
     */
    public boolean checkDate(String eventDate) {
        String[] dateTime = eventDate.split(" ");
        String[] date = dateTime[0].split("-");

        return
                Integer.parseInt(date[0]) == today.get(Calendar.YEAR)
                        && Integer.parseInt(date[1]) == (today.get(Calendar.MONTH) + 1)
                        && Integer.parseInt(date[2]) == today.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Scan the days and times string from a course to tell if there is class today
     *
     * @param classTime - all times for the lectures of some Course
     * @param day - the weekday for comparison
     * @return True if lecture is held on the day given by the parameter; False otherwise
     */
    public boolean checkClassTime(String classTime, String day) {
        String[] halves = classTime.split(";"); //Split between class days and lab/recitation
        String[] daysTimes = halves[0].split(":");

        //Lecture
        String[] days = daysTimes[1].split("Time");

        return days[0].contains(day);
    }

    /**
     * Scan the days and times string from a course to tell if there is lab or recitation today
     *
     * @param classTime - all times for the labs/recitations of some some course
     * @param day - the weekday for comparison
     * @return True if lab/recitation is held on the day given by the parameter; False otherwise
     */
    public boolean checkLabTime(String classTime, String day) {
        String[] halves = classTime.split(";"); //Split between class days and lab/recitation
        String[] daysTimes = halves[1].split(":");

        //Lab / Recitation
        String[] days = daysTimes[1].split("Time");

        return days[0].contains(day);
    }

    /**
     * Get the day of the week based on a given index.
     *
     * @param num - index corresponding to a day of the week
     * @return A string containing a single character representing the day of the week
     */
    public String getDay(int num) {

        if (num == 7 || num == 0) return "S";

        String[] days = {"S", "M", "T", "W", "R", "F", "S"};

        return days[num-1];
    }

    /**
     * Externally set the date being compared against
     *
     * @param year - year
     * @param month - months 0 - 11
     * @param day - day of month
     */
    public void setDate(int year, int month, int day) {
        today.set(Calendar.YEAR, year);
        today.set(Calendar.MONTH, month);
        today.set(Calendar.DAY_OF_MONTH, day);
    }

}
