package coms309.mainproject.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coms309.mainproject.R;

import static android.R.layout.simple_spinner_item;


/**
 * An activity that allows users to edit a course they have already
 * added to their schedule.
 * Created by hausten on 11/8/17.
 */

public class EditCourse extends FragmentActivity {

    /**
     * URL to the PHP script.
     */
    private final String url
            = "http://proj-309-pp-b-3.cs.iastate.edu/courses/update_individual_course.php";

    /**
     * This course's name.
     */
    private Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_course_layout);
        course = (Course) getIntent().getSerializableExtra("Course");

        setSpinnerValues();
        setCheckBoxValues();

        final RequestQueue controller = Volley.newRequestQueue(this);


        final Button button = (Button) findViewById(R.id.done_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Execute POST request to server.
                executePost(controller);

                // Return to the main menu.
                goBackToSpecificCourse(null);
            }
        });
    }

    /**
     * Executes the PHP script, effectively saving the data that was changed.
     * @param controller
     *  The controller to send the request.
     */
    private void executePost(RequestQueue controller) {
        final EditText courseNumber = (EditText) findViewById(R.id.course_number_text_input);
        final EditText courseName = (EditText) findViewById(R.id.course_name_text_input);
        final EditText courseLocation = (EditText) findViewById(R.id.course_location_text_input);
        final String[] meetingTimes = getMeetingTimes();
        final String oldCourseNumber = course.getCode();
        final String oldCourseName = course.getName();

        course.setCode(courseNumber.getText().toString());
        course.setName(courseName.getText().toString());
        course.setLocation(courseLocation.getText().toString());
        course.setLectureTimes(meetingTimes[0]);
        course.setLabTimes(meetingTimes[1]);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response:", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response:", error == null ? "Null" : "NotNull");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();

                params.put("userID", course.getUserID() + "");
                params.put("courseNumber", courseNumber.getText().toString() + ";");
                params.put("courseName", courseName.getText().toString().trim() + ";");
                params.put("location", courseLocation.getText().toString().trim() + ";");
                params.put("meetingTime", meetingTimes[0] + "; " + meetingTimes[1] + ";");
                params.put("oldCourseNumber", oldCourseNumber + ";");
                params.put("oldCourseName", oldCourseName + ";");

                return params;
            }
        };
        controller.add(request);
    }

    /**
     * Set the default spinner values based on information that was already saved about the course.
     */
    private void setSpinnerValues() {
        // Lecture hour start/end
        Spinner meetingHours = (Spinner) findViewById(R.id.hourClassStart);
        ArrayAdapter<CharSequence> hoursAdapter = ArrayAdapter.createFromResource(this,
                R.array.meeting_hours, simple_spinner_item);
        hoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meetingHours.setAdapter(hoursAdapter);
        meetingHours = (Spinner) findViewById(R.id.hourClassEnd);
        meetingHours.setAdapter(hoursAdapter);

        // Lecture minutes start/end
        Spinner meetingMinutes = (Spinner) findViewById(R.id.minuteClassStart);
        ArrayAdapter<CharSequence> minutesAdapter = ArrayAdapter.createFromResource(this,
                R.array.meeting_minutes, simple_spinner_item);
        minutesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meetingMinutes.setAdapter(minutesAdapter);
        meetingMinutes = (Spinner) findViewById(R.id.minuteClassEnd);
        meetingMinutes.setAdapter(minutesAdapter);

        // Lecture time of day
        Spinner timeOfDay = (Spinner) findViewById(R.id.timeOfDayClassStart);
        ArrayAdapter<CharSequence> timeOfDayAdapter = ArrayAdapter.createFromResource(this,
                R.array.am_pm, simple_spinner_item);
        timeOfDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeOfDay.setAdapter(timeOfDayAdapter);
        timeOfDay = (Spinner) findViewById(R.id.timeOfDayClassEnd);
        timeOfDay.setAdapter(timeOfDayAdapter);


        // Recitation hour start/end
        meetingHours = (Spinner) findViewById(R.id.hourRecitationStart);
        meetingHours.setAdapter(hoursAdapter);
        meetingHours = (Spinner) findViewById(R.id.hourRecitationEnd);
        meetingHours.setAdapter(hoursAdapter);

        // Recitation minutes start/end
        meetingMinutes = (Spinner) findViewById(R.id.minuteRecitationStart);
        meetingMinutes.setAdapter(minutesAdapter);
        meetingMinutes = (Spinner) findViewById(R.id.minuteRecitationEnd);
        meetingMinutes.setAdapter(minutesAdapter);

        // Recitation time of day
        timeOfDay = (Spinner) findViewById(R.id.timeOfDayRecitationStart);
        timeOfDay.setAdapter(timeOfDayAdapter);
        timeOfDay = (Spinner) findViewById(R.id.timeOfDayRecitationEnd);
        timeOfDay.setAdapter(timeOfDayAdapter);

        EditText courseNumber = (EditText) findViewById(R.id.course_number_text_input);
        EditText courseName = (EditText) findViewById(R.id.course_name_text_input);
        EditText courseLocation = (EditText) findViewById(R.id.course_location_text_input);
        courseNumber.setText(course.getCode());
        courseName.setText(course.getName());
        courseLocation.setText(course.getLocation());

        // Set values for spinners
        if (course.checkLectureTimesForDays()) {
            HashMap<String, String> timeMap = course.getTimes(course.getLectureTimes());

            meetingHours = (Spinner) findViewById(R.id.hourClassStart);
            meetingHours.setSelection(hoursAdapter.getPosition(timeMap.get("hourFrom")));

            meetingMinutes = (Spinner) findViewById(R.id.minuteClassStart);
            meetingMinutes.setSelection(minutesAdapter.getPosition(timeMap.get("minuteFrom")));

            meetingHours = (Spinner) findViewById(R.id.hourClassEnd);
            meetingHours.setSelection(hoursAdapter.getPosition(timeMap.get("hourTo")));

            meetingMinutes = (Spinner) findViewById(R.id.minuteClassEnd);
            meetingMinutes.setSelection(minutesAdapter.getPosition(timeMap.get("minuteTo")));

            timeOfDay = (Spinner) findViewById(R.id.timeOfDayClassStart);
            timeOfDay.setSelection(timeOfDayAdapter.getPosition(timeMap.get("todFrom")));

            timeOfDay = (Spinner) findViewById(R.id.timeOfDayClassEnd);
            timeOfDay.setSelection(timeOfDayAdapter.getPosition(timeMap.get("todTo")));
        }

        if (course.checkLabTimesForDays()) {
            HashMap<String, String> timeMap = course.getTimes(course.getLabTimes());

            meetingHours = (Spinner) findViewById(R.id.hourRecitationStart);
            meetingHours.setSelection(hoursAdapter.getPosition(timeMap.get("hourFrom")));

            meetingMinutes = (Spinner) findViewById(R.id.minuteRecitationStart);
            meetingMinutes.setSelection(minutesAdapter.getPosition(timeMap.get("minuteFrom")));

            meetingHours = (Spinner) findViewById(R.id.hourRecitationEnd);
            meetingHours.setSelection(hoursAdapter.getPosition(timeMap.get("hourTo")));

            meetingMinutes = (Spinner) findViewById(R.id.minuteRecitationEnd);
            meetingMinutes.setSelection(minutesAdapter.getPosition(timeMap.get("minuteTo")));

            timeOfDay = (Spinner) findViewById(R.id.timeOfDayRecitationStart);
            timeOfDay.setSelection(timeOfDayAdapter.getPosition(timeMap.get("todFrom")));

            timeOfDay = (Spinner) findViewById(R.id.timeOfDayRecitationEnd);
            timeOfDay.setSelection(timeOfDayAdapter.getPosition(timeMap.get("todTo")));
        }
    }

    /**
     * Set the default checkbox values based on information that was previously saved about the course.
     */
    private void setCheckBoxValues() {
        if (course.checkLectureTimesForDays()) {
            ArrayList<String> days = course.getDays(course.getLectureTimes());
            CheckBox checkBox;

            for (String day : days) {
                switch (day) {
                    case "M":
                        checkBox = (CheckBox) findViewById(R.id.mondayClass);
                        checkBox.setChecked(true);
                        break;
                    case "T":
                        checkBox = (CheckBox) findViewById(R.id.tuesdayClass);
                        checkBox.setChecked(true);
                        break;
                    case "W":
                        checkBox = (CheckBox) findViewById(R.id.wednesdayClass);
                        checkBox.setChecked(true);
                        break;
                    case "R":
                        checkBox = (CheckBox) findViewById(R.id.thursdayClass);
                        checkBox.setChecked(true);
                        break;
                    case "F":
                        checkBox = (CheckBox) findViewById(R.id.fridayClass);
                        checkBox.setChecked(true);
                        break;
                }
            }
        }

        if (course.checkLabTimesForDays()) {
            ArrayList<String> days = course.getDays(course.getLabTimes());
            CheckBox checkBox;

            for (String day : days) {
                switch (day) {
                    case "M":
                        checkBox = (CheckBox) findViewById(R.id.mondayRecitation);
                        checkBox.setChecked(true);
                        break;
                    case "T":
                        checkBox = (CheckBox) findViewById(R.id.tuesdayRecitation);
                        checkBox.setChecked(true);
                        break;
                    case "W":
                        checkBox = (CheckBox) findViewById(R.id.wednesdayRecitation);
                        checkBox.setChecked(true);
                        break;
                    case "R":
                        checkBox = (CheckBox) findViewById(R.id.thursdayRecitation);
                        checkBox.setChecked(true);
                        break;
                    case "F":
                        checkBox = (CheckBox) findViewById(R.id.fridayRecitation);
                        checkBox.setChecked(true);
                        break;
                }
            }
        }
    }

    /**
     *
     * @return
     *  A formatted string containing all meeting times for a course.
     *  Format:  "Class- Days: ... Time: ...; Recitation- Days: ... Time: ..."
     */
    private String[] getMeetingTimes() {
        String[] meetingTimes = new String[2];

        meetingTimes[0] = "Class- Days: ";
        meetingTimes[1] = "Recitation- Days: ";

        // Adding meeting times for lecture.
        CheckBox checkBox = (CheckBox) findViewById(R.id.mondayClass);
        if (checkBox.isChecked()) {
            meetingTimes[0] += "M ";
        }
        checkBox = (CheckBox) findViewById(R.id.tuesdayClass);
        if (checkBox.isChecked()) {
            meetingTimes[0] += "T ";
        }
        checkBox = (CheckBox) findViewById(R.id.wednesdayClass);
        if (checkBox.isChecked()) {
            meetingTimes[0] += "W ";
        }
        checkBox = (CheckBox) findViewById(R.id.thursdayClass);
        if (checkBox.isChecked()) {
            meetingTimes[0] += "R ";
        }
        checkBox = (CheckBox) findViewById(R.id.fridayClass);
        if (checkBox.isChecked()) {
            meetingTimes[0] += "F ";
        }
        Spinner hours = (Spinner) findViewById(R.id.hourClassStart);
        meetingTimes[0] += "Time: " + hours.getSelectedItem().toString();
        Spinner minutes = (Spinner) findViewById(R.id.minuteClassStart);
        meetingTimes[0] += " " + minutes.getSelectedItem().toString();
        Spinner timeOfDay = (Spinner) findViewById(R.id.timeOfDayClassStart);
        meetingTimes[0] += " " + timeOfDay.getSelectedItem().toString();
        hours = (Spinner) findViewById(R.id.hourClassEnd);
        minutes = (Spinner) findViewById(R.id.minuteClassEnd);
        timeOfDay = (Spinner) findViewById(R.id.timeOfDayClassEnd);
        meetingTimes[0] += " " + hours.getSelectedItem().toString();
        meetingTimes[0] += " " + minutes.getSelectedItem().toString();
        meetingTimes[0] += " " + timeOfDay.getSelectedItem().toString();

        // Adding meeting times for recitation.
        checkBox = (CheckBox) findViewById(R.id.mondayRecitation);
        if (checkBox.isChecked()) {
            meetingTimes[1] += "M ";
        }
        checkBox = (CheckBox) findViewById(R.id.tuesdayRecitation);
        if (checkBox.isChecked()) {
            meetingTimes[1] += "T ";
        }
        checkBox = (CheckBox) findViewById(R.id.wednesdayRecitation);
        if (checkBox.isChecked()) {
            meetingTimes[1] += "W ";
        }
        checkBox = (CheckBox) findViewById(R.id.thursdayRecitation);
        if (checkBox.isChecked()) {
            meetingTimes[1] += "R ";
        }
        checkBox = (CheckBox) findViewById(R.id.fridayRecitation);
        if (checkBox.isChecked()) {
            meetingTimes[1] += "F ";
        }
        hours = (Spinner) findViewById(R.id.hourRecitationStart);
        minutes = (Spinner) findViewById(R.id.minuteRecitationStart);
        timeOfDay = (Spinner) findViewById(R.id.timeOfDayRecitationStart);
        meetingTimes[1] += "Time: " + hours.getSelectedItem().toString();
        meetingTimes[1] += " " + minutes.getSelectedItem().toString();
        meetingTimes[1] += " " + timeOfDay.getSelectedItem().toString();
        hours = (Spinner) findViewById(R.id.hourRecitationEnd);
        minutes = (Spinner) findViewById(R.id.minuteRecitationEnd);
        timeOfDay = (Spinner) findViewById(R.id.timeOfDayRecitationEnd);
        meetingTimes[1] += " " + hours.getSelectedItem().toString();
        meetingTimes[1] += " " + minutes.getSelectedItem().toString();
        meetingTimes[1] += " " + timeOfDay.getSelectedItem().toString();
        meetingTimes[0] = meetingTimes[0].trim();
        meetingTimes[1] = meetingTimes[1].trim();
        return meetingTimes;
    }

    /**
     * Starts the "Specific Course" activity.
     * Only used if the back button is pressed.
     */
    public void goBackToSpecificCourse(View view) {
        Intent intent = new Intent(this, ViewSpecificCourseActivity.class);
        intent.putExtra("Course", course);
        startActivity(intent);
    }
}
