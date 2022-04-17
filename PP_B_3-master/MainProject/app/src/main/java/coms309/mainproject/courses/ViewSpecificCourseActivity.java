package coms309.mainproject.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import coms309.mainproject.R;

/**
 * An activity shown when a user wishes to display a more detailed representation
 * of a course they have previously added to their schedule.
 * Created by hausten on 11/1/17.
 */

public class ViewSpecificCourseActivity extends FragmentActivity {

    private RequestQueue controller;

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_individual_course_layout);
        controller = Volley.newRequestQueue(this);
        course = (Course) getIntent().getSerializableExtra("Course");
        createLayout(course);
    }

    /**
     * Creates the layout for displaying a course's information.
     * @param c
     *  The course to be displayed.
     */
    private void createLayout(Course c) {
        TextView courseName = (TextView) findViewById(R.id.individual_course_name);
        TextView courseCode = (TextView) findViewById(R.id.individual_course_code);
        TextView courseLocation = (TextView) findViewById(R.id.individual_course_location);
        TextView courseClassHours = (TextView) findViewById(R.id.individual_course_class_time);
        TextView courseRecitationHours = (TextView) findViewById(R.id.individual_course_recitation_time);

        if (c.checkLectureTimesForDays()) {
            courseClassHours.setText(c.getLectureTimes());
        } else {
            courseClassHours.setText("No Class Times Were Saved");
        }

        if (c.checkLabTimesForDays()) {
            courseRecitationHours.setText(c.getLabTimes());
        } else {
            courseRecitationHours.setText("No Recitation/Lab Times Were Saved");
        }

        courseName.setText(c.getName());
        courseCode.setText(c.getCode());
        courseLocation.setText(c.getLocation());

    }

    /**
     * Starts the {@link EditCourse} activity.
     * @param view
     *  View to be edited.
     */
    public void startEditCourse(View view) {
        Intent intent = new Intent(this, EditCourse.class);
        intent.putExtra("Course", course);
        startActivity(intent);
    }

    public void startDeleteCourse(View view) {
        String url = "http://proj-309-pp-b-3.cs.iastate.edu/courses/delete_specific_course.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("DELETED", response);
                        startViewAllCourses(null);
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
                params.put("courseNumber", course.getCode() + ";");
                params.put("courseName", course.getName() + ";");

                return params;
            }
        };
        controller.add(request);
    }

    /**
     * Starts the {@link ViewAllCoursesActivity} activity.
     * @param view
     *  View to be edited.
     */
    public void startViewAllCourses(View view) {
        Intent intent = new Intent(this, ViewAllCoursesActivity.class);
        startActivity(intent);
    }
}
