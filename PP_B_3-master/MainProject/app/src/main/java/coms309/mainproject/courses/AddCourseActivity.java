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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import coms309.mainproject.MainMenu;
import coms309.mainproject.R;

import static android.R.layout.simple_spinner_item;


/**
 * Created by hausten on 10/7/17.
 * An activity that is displayed when a user wants to add a new course to the database.
 */

public class AddCourseActivity extends FragmentActivity {

    private final String url = "http://proj-309-pp-b-3.cs.iastate.edu/courses/add_course_form.php";

    private static final String PREFS_FILE = "UserInfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_layout);

        final RequestQueue controller = Volley.newRequestQueue(this);

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

        final Button button = (Button) findViewById(R.id.done_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Execute POST request to server.
                executePost(controller);

            }
        });
    }

    private void executePost(RequestQueue controller) {
        final int id = getSharedPreferences(PREFS_FILE, MODE_PRIVATE).getInt("userID", 0);
        final EditText courseNumber = (EditText) findViewById(R.id.course_number_text_input);
        final EditText courseName = (EditText) findViewById(R.id.course_name_text_input);
        final EditText courseLocation = (EditText) findViewById(R.id.course_location_text_input);
        final String meetingTimes = getMeetingTimes();

        String c = courseNumber.getText().toString();
        String n = courseName.getText().toString();
        String l = courseLocation.getText().toString();
        if (c.length() < 1 || n.length() < 1 || l.length() < 1) {
            Toast toast = Toast.makeText(this, "Please fill out the entire form", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (c.length() > 0 && n.length() > 0 && l.length() > 0) {
            Log.d("courseNumber", courseNumber.getText().toString());
            Log.d("courseName", courseName.getText().toString());
            Log.d("Location", courseLocation.getText().toString());
            Log.d("meetingTime", meetingTimes);

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
                    Map<String, String> params = new HashMap<>();

                    params.put("userID", id + "");
                    params.put("courseNumber", courseNumber.getText().toString().trim() + ";");
                    params.put("courseName", courseName.getText().toString().trim() + ";");
                    params.put("location", courseLocation.getText().toString().trim() + ";");
                    params.put("meetingTime", meetingTimes + ";");

                    return params;
                }
            };
            controller.add(request);
            // Return to the main menu.
            goBackToViewAllCourses(null);
        }
    }

    private String getMeetingTimes() {
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

        return meetingTimes[0].trim() + "; " + meetingTimes[1].trim();
    }

    public void goBackToViewAllCourses(View view) {
        Intent intent = new Intent(this, ViewAllCoursesActivity.class);
        startActivity(intent);
    }
}
