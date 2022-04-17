package coms309.mainproject.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;

import coms309.mainproject.R;
import coms309.mainproject.schedule.event.Course;
import coms309.mainproject.schedule.event.Event;
import coms309.mainproject.schedule.helpers.DateHandler;

import static android.content.Context.MODE_PRIVATE;

/**
 * This class is a fragment which is invoked when CalendarCell is pressed on the schedule view.
 * It represents a day on the calendar and contains all events associated with that day
 * in greater detail than in the condensed Calendar Cell.
 */

public class CalendarCell extends Fragment {

    private Calendar cal = Calendar.getInstance();
    private DateHandler dh;
    private int dayOfWeek = 1;

    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();

    private static CellListener mListener;

    /**Interface to communicate with parent Activity**/
    public interface CellListener {
        void loadEventEditor(int index);

        void loadEvents(ArrayList<Event> events);

        void loadCourses(ArrayList<Course> courses);

        int setDate(Calendar cal);
    }

    public CalendarCell() {
        //Mandatory default constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static CalendarCell newInstance() {
        CalendarCell fragment = new CalendarCell();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cell, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Populate Views
        LinearLayout classes = (LinearLayout) getView().findViewById(R.id.classesLayout);
        LinearLayout homework = (LinearLayout) getView().findViewById(R.id.hwkLayout);
        LinearLayout others = (LinearLayout) getView().findViewById(R.id.othersLayout);
        LinearLayout groupEvents = (LinearLayout) getView().findViewById(R.id.groupsLayout);

        //Alternate - events array list
        int index = 0;

        for (Event e : events) {

            final int i = index;
            index++;

            //Check if event is relevant to this day
            if (!dh.checkDate(e.dateTime)) {
                continue;
            }

            Button b = new Button(getContext());
            b.setTransformationMethod(null);
            b.setTextSize(11);

            String text = e.title + "\n";
            if (!e.location.equals("")) text += "Loc: " + e.location + "\n";
            text += "Start: " + e.displayTime + "\n";
            if (!e.description.equals("")) text += "Memo: " + e.description;

            b.setText(text);

            //Sort into appropriate window
            switch(e.type) {
                case "Class": homework.addView(b); break;
                case "Homework": homework.addView(b); break;
                case "Other": others.addView(b); break;
                case "Group": groupEvents.addView(b); break;
                default: break;
            }

            int userID = getContext().getSharedPreferences("ShortTerm", 0).getInt("nextID", 1);

            if (userID == getContext().getSharedPreferences("UserInfo", MODE_PRIVATE).getInt("userID", 1)) {
                //Button listener
                b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        mListener.loadEventEditor(i);
                    }
                });
            }
        }

        //Classes
        for (Course c : courses) {
            String dayOfWeekString = dh.getDay(dayOfWeek);

            if (!dh.checkClassTime(c.times, dayOfWeekString)) continue;

            Button b = new Button(getContext());
            b.setTransformationMethod(null);
            b.setTextSize(12);
            b.setText(c.getClassDescription());

            classes.addView(b);
        }

        //Lab and Recitation
        for (Course c : courses) {

            String dayOfWeek = dh.getDay(cal.get(Calendar.DAY_OF_WEEK));

            if (!dh.checkLabTime(c.times, dayOfWeek)) continue;

            Button b = new Button(getContext());
            b.setTransformationMethod(null);
            b.setTextSize(12);
            b.setText(c.getRecLab());

            classes.addView(b);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CellListener) {
            mListener = (CellListener) context;

            mListener.loadEvents(events);
            mListener.loadCourses(courses);
            dayOfWeek = mListener.setDate(cal);
            dh = new DateHandler(cal);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ContactCellListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
