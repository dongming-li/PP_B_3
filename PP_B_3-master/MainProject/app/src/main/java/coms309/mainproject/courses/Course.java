package coms309.mainproject.courses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * An object representing a course that can be added to a user's schedule.
 * Created by hausten on 11/7/17.
 */
public class Course implements Serializable {

    /**
     * The name of this course.
     */
    private String name;

    /**
     * The code of this course.
     */
    private String code;

    /**
     * The location of this course.
     */
    private String location;

    /**
     * The lecture times of this course.
     */
    private String lectureTimes;

    /**
     * The lab/recitation times of this course.
     */
    private String labTimes;

    /**
     * The user this course belongs to.
     */
    private int userID;

    public Course(String name, String code, String location, String lectureTimes, String labTimes, int userID) {
        this.name = name;
        this.code = code;
        this.location = location;
        this.lectureTimes = lectureTimes;
        this.labTimes = labTimes;
        this.userID = userID;
    }

    /**
     * Sets this course's name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets this course's code.
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets this course's location.
     * @param location
     */
    public void setLocation(String location) { this.location = location;}

    /**
     * Sets this course's lecture times.
     * @param lectureTimes
     */
    public void setLectureTimes(String lectureTimes) { this.lectureTimes = lectureTimes; }

    /**
     * Sets this course's lab/recitation times.
     * @param labTimes
     */
    public void setLabTimes(String labTimes) { this.labTimes = labTimes; }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getLocation() { return location; }

    public String getLectureTimes() { return lectureTimes; }

    public String getLabTimes() { return labTimes; }

    public int getUserID() { return userID; }

    /**
     * Checks if this course's lecture times has any days saved.
     * Used to determine if times should attempt to be parsed & shown.
     * @return
     *  True if this course has days saved in its lecture times, false otherwise.
     */
    public boolean checkLectureTimesForDays() {
        Scanner s = new Scanner(lectureTimes);

        while (s.hasNext()) {
            String t = s.next();
            if (t.equals("M") || t.equals("T") || t.equals("W") || t.equals("R") || t.equals("F")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this course's lab/recitation times has any days saved.
     * Used to determine if times should attempt to be parsed & shown.
     * @return
     *  True if this course has days saved in its lab/recitation times, false otherwise.
     */
    public boolean checkLabTimesForDays() {
        Scanner s = new Scanner(labTimes);

        while (s.hasNext()) {
            String t = s.next();
            if (t.equals("M") || t.equals("T") || t.equals("W") || t.equals("R") || t.equals("F")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() { return code + "-" + name;}

    /**
     * Get the minutes & hours of a course's time.
     * @param t
     *  Lab/recitation or lecture times for this course.
     * @return
     *  A mapping of meeting times for the string t.
     */
    public HashMap<String, String> getTimes(String t) {
        HashMap<String, String> timeMap = new HashMap<>();
        Scanner s = new Scanner(t);

        // First move past days
        while (s.hasNext()) {
            String p = s.next();
            if (p.startsWith("Time:")) {
                break;
            }
        }

        timeMap.put("hourFrom", s.next());
        timeMap.put("minuteFrom", s.next());
        timeMap.put("todFrom", s.next());
        timeMap.put("hourTo", s.next());
        timeMap.put("minuteTo", s.next());
        timeMap.put("todTo", s.next());

        return timeMap;
    }

    public ArrayList<String> getDays(String t) {
        ArrayList<String> days = new ArrayList<>();

        Scanner s = new Scanner(t);

        while (s.hasNext()) {
            String p = s.next();
            if (p.equals("M") || p.equals("T") || p.equals("W") || p.equals("R") || p.equals("F")) {
                days.add(p);
            }
        }
        return days;
    }
}
