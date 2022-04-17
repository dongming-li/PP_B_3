package coms309.mainproject.schedule.event;

/**
 * Very similar to the Event class but holds information about Courses instead.
 * Really it's a convenience class that helps organize info while dealing with event managing classes.
 */

public class Course {

    public String number, name, location, times;

    /**Constructor for the course.
     * Keeps Course data for the input parameters.
     *
     * @param number - corse number
     * @param name - course name
     * @param location - where the class takes place
     * @param times - when class is in session
     * */
    public Course(String number, String name, String location, String times) {
        this.number = number;
        this.name = name;
        this.location = location;
        this.times = times;
    }

    /**String display for main lecture times
     *
     * @return A string containing class number, location, and times*/
    public String getClassDescription() {
        String[] halves = times.split(";"); //Split between class days and lab/recitation
        String[] daysTimes = halves[0].split(":");

        String[] times = daysTimes[2].split(" ");
        String timeString = "";

        for (int i = 0; i < times.length; i++) {
            if (i == 4) {
                timeString += " - ";
            }

            timeString += times[i];
            if (i == 1 || i == 4) timeString += ":";
        }

        return number + "\n" +
                "Location: " + location + "\n" +
                timeString;
    }

    /**String display for lab or recitation times
     *
     * @return A string containing recitation/lab number, location, and times
     * */
    public String getRecLab() {
        String[] halves = times.split(";"); //Split between class days and lab/recitation
        String[] daysTimes = halves[1].split(":");

        String[] times = daysTimes[2].split(" ");
        String timeString = "";

        for (int i = 0; i < times.length; i++) {
            if (i == 4) {
                timeString += " - ";
            }

            timeString += times[i];
            if (i == 1 || i == 4) timeString += ":";
        }

        return number + "\n" +
                "Rec/Lab" + "\n" +
                timeString;
    }
}
