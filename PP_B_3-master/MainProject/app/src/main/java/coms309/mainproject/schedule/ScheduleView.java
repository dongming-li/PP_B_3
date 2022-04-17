package coms309.mainproject.schedule;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import coms309.mainproject.MainMenu;
import coms309.mainproject.R;
import coms309.mainproject.schedule.event.Course;
import coms309.mainproject.schedule.event.Event;
import coms309.mainproject.schedule.event.EventReader;
import coms309.mainproject.schedule.event_forms.AddEventActivity;
import coms309.mainproject.schedule.event_forms.EditEventActivity;
import coms309.mainproject.schedule.switching_calendars.Contact;
import coms309.mainproject.schedule.switching_calendars.ContactReader;
import coms309.mainproject.schedule.switching_calendars.ContactsCell;

/**
 * This class displays all information about a user's schedule in a dense package.
 * Certain event views can be toggled on and off, including friends' entire schedules.
 * The calendar is primarily made of a collection of calendar cells which contain each day's events.
 */

public class ScheduleView extends FragmentActivity implements CalendarCell.CellListener, ContactsCell.ContactCellListener {

    public static final String EVENT_DATA = "coms309.mainproject.schedule.scheduleView.STRING_ARRAY";

    private boolean laC = false, cellOpen = false;

    private ArrayList<Button> daysOfMonth; //List for all calendar cells

    private int firstDay, daysInMonth, month, year;
    private int fragmentDay = 1, fragmentDayOfWeek = 1;
    private String[] months = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private EventReader loader;
    private ContactReader cLoader;

    private Calendar currentDate;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);

        int userID = getSharedPreferences("ShortTerm", 0).getInt("nextID", 1);

        loader = new EventReader(this);
        loader.loadAll(userID);

        cLoader = new ContactReader(this);

        TextView dateText = (TextView) findViewById(R.id.dateText);
        dateText.setText(new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(new Date()));

        //Calendar
        currentDate = Calendar.getInstance(); //does not change
        cal = Calendar.getInstance();
        daysOfMonth = new ArrayList<>();
        setLists();
        weekSetup();
        setDates();
    }

    //Fill daysOfMonth with the cells from a particular layout
    private ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<>();
        final int childCount = root.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                if (getViewsByTag((ViewGroup) child, tag) != null) {
                    views.addAll(getViewsByTag((ViewGroup) child, tag));
                }
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }
        }

        for (int i = 0; i < views.size(); i++) {
            daysOfMonth.add((Button) views.get(i));
        }

        return null;
    }

    //Populate daysOfMonth; and assign date numbers to each calendar cell
    private void setDates() {

        int last = 0;
        float fontSize = 11;

        //Empty cells before
        for (int i = 0; i < firstDay; i++) {
            String text = "\n\n\n";

            daysOfMonth.get(i).setText(text);
            daysOfMonth.get(i).setTextSize(fontSize);

            daysOfMonth.get(i).setBackgroundResource(android.R.drawable.btn_default);
        }

        //Active cells
        for (int i = 0; i < daysInMonth; i++) {

            String text = "" + (i + 1);
            text += "\n\n\n";
            /*
            text += "\n" + 99; //Class events quantity
            text += "\n" + 99; //Group events quantity
            text += "\n" + 99; //Other events quantity
            */

            daysOfMonth.get(firstDay + i).setTextSize(fontSize);
            daysOfMonth.get(firstDay + i).setText(text);

            last = firstDay + i + 1;

            //Check if this button correlates to current date
            if (i + 1 == currentDate.get(Calendar.DAY_OF_MONTH)
                    && cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
                    && cal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
                daysOfMonth.get(firstDay + i).setBackgroundColor(Color.RED);
            }
            else daysOfMonth.get(firstDay + i).setBackgroundResource(android.R.drawable.btn_default);

            final int day = i + 1;
            final int dayOfWeek = (firstDay + i + 1) % 7;

            daysOfMonth.get(firstDay + i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    fragmentDay = day;
                    fragmentDayOfWeek = dayOfWeek;
                    loadFragment();
                }
            });
        }

        //Empty cells after
        for (int i = last; i < daysOfMonth.size(); i++) {
            String text = "\n\n\n";

            daysOfMonth.get(i).setTextSize(fontSize);
            daysOfMonth.get(i).setText(text);

            daysOfMonth.get(i).setBackgroundResource(android.R.drawable.btn_default);
        }

    }

    //Populate the array-lists which represent the weeks of the month
    private void setLists() {
        LinearLayout week;

        week = (LinearLayout) findViewById(R.id.week1);
        getViewsByTag(week, "dayOfWeek");

        week = (LinearLayout) findViewById(R.id.week2);
        getViewsByTag(week, "dayOfWeek");

        week = (LinearLayout) findViewById(R.id.week3);
        getViewsByTag(week, "dayOfWeek");

        week = (LinearLayout) findViewById(R.id.week4);
        getViewsByTag(week, "dayOfWeek");

        week = (LinearLayout) findViewById(R.id.week5);
        getViewsByTag(week, "dayOfWeek");

        week = (LinearLayout) findViewById(R.id.week6);
        getViewsByTag(week, "dayOfWeek");
    }

    //Get first day and number of days in month
    private void weekSetup() {
        cal.set(Calendar.DAY_OF_MONTH, 1); //To get the first day of the month

        daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        firstDay = fragmentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        determineMonths();
    }

    /**
     * Loads the Add Event activity.
     * Intended to be used by a button.
     *
     * @param view - mandatory parameter
     */
    public void editEvent(View view) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }

    /**Loads a fragment containing scheduled events for a specific day**/
    public void loadFragment() {
        if (laC || cellOpen) return; //Don't open cell if contacts window is already open

        if (findViewById(R.id.fragmentContainer) != null) {
            CalendarCell cell = CalendarCell.newInstance();

            //Add to layout
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragmentContainer, cell).commit();

            cellOpen = true;
        }
    }

    /**
     * Loads a fragment containing contact information to switch to contacts' calendars
     */
    public void loadContacts(View view) {
        if (cellOpen) { //Remove an open calendar cell before opening contacts cell
            backToCalendar(null);
            cellOpen = false;
        }

        if (laC) {
            backToCalendar(null);

            laC = false;
        }

        else if (findViewById(R.id.fragmentContainer) != null) {
            ContactsCell cell = ContactsCell.newInstance();

            //Add to layout
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragmentContainer, cell).commit();

            laC = true;
        }
    }

    /**
     * Remove the currently loaded fragment.
     * Intended to be used by a button.
     *
     * @param view - mandatory parameter
     */
    public void backToCalendar(View view) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        getSupportFragmentManager().beginTransaction().remove(f).commit();

        cellOpen = false;
    }

    /**
     * Go back to Main Menu.
     * Intended to be used by a button.
     *
     * @param view - mandatory parameter
     */
    public void backToMenu(View view) {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    /**
     * Load calendar for previous month.
     * Intended to be used by a button.
     *
     * @param view - mandatory parameter
     * **/
    public void lastMonth(View view) {
        int curMonth = cal.get(Calendar.MONTH);

        if (curMonth == 0)  {
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        }
        else cal.set(Calendar.MONTH, curMonth - 1);

        weekSetup();
        setDates();
    }

    /**
     * Load calendar for next month.
     * Inteded to be used by a button.
     *
     * @param view - mandatory parameter
     */
    public void nextMonth(View view) {
        int curMonth = cal.get(Calendar.MONTH);

        if (curMonth == 11) {
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        }
        else cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);

        weekSetup();
        setDates();
    }

    //Set appropriate months to layout fields
    private void determineMonths() {
        Button lastMonth = (Button) findViewById(R.id.prevMonth);
        Button nextMonth = (Button) findViewById(R.id.nextMonth);
        TextView curMonth = (TextView) findViewById(R.id.currentMonth);

        String text = months[month] + " " + year;
        curMonth.setText(text);

        if (month == 11) text = months[0];
        else text = months[month];
        text += " -->";
        nextMonth.setText(text);

        text = "<-- ";
        if (month == 0) text += months[11];
        else text += months[month-1];
        lastMonth.setText(text);
    }

    //**CELL FRAGMENT INTERFACE**//

    /**
     * Change to Edit Event Activity with appropriate event info
     *
     * @param index - index of event as found in the events list
     */
    public void loadEventEditor(int index) {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra(EVENT_DATA, loader.events.get(index).toStringArray());

        startActivity(intent);
    }

    /**
     * Pass in events to fragment
     *
     * @param events - Empty events list for some Calendar Cell
     */
    public void loadEvents(ArrayList<Event> events) {
        events.addAll(loader.events);
    }

    /**
     * Pass in courses to fragment
     *
     * @param courses - Empty courses list for some Calendar Cell
     */
    public void loadCourses(ArrayList<Course> courses) {
        courses.addAll(loader.courses);
    }

    /**Pass in current date to fragment
     *
     * @param cal - Calendar storing date/time for some Calendar Cell
     * @return the numerical day of the week
     **/
    public int setDate(Calendar cal) {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, fragmentDay);

        Log.d("***DEBUG***", "" + fragmentDayOfWeek);

        return fragmentDayOfWeek;
    }

    //**CONTACT FRAGMENT INTERFACE**//

    /**
     * Pass in contact list to fragment
     *
     * @param contacts - list from the fragment
     */
    public void loadContacts(ArrayList<Contact> contacts) {
        contacts.addAll(cLoader.contacts);
    }

    /**
     * Reloads the calendar with a different user's ID
     *
     * @param id - user id
     */
    public void switchCalendar(int id) {
        SharedPreferences.Editor editor = getSharedPreferences("ShortTerm", 0).edit();
        editor.putInt("nextID", id);
        editor.apply();

        startActivity(getIntent());
    }
}
