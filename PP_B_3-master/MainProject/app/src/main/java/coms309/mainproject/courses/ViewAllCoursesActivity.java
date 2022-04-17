package coms309.mainproject.courses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import coms309.mainproject.MainMenu;
import coms309.mainproject.R;

/**
 * Activity to view all courses a user has added to their schedule.
 * Created by hausten on 11/1/17.
 */

public class ViewAllCoursesActivity extends FragmentActivity{

    /**
     * Url to the PHP script.
     */
    private String url = "http://proj-309-pp-b-3.cs.iastate.edu/courses/view_all_courses.php";

    /**
     * Object used to get the current user's id to use in the PHP script.
     */
    private static SharedPreferences editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.view_all_courses_layout));

        editor = getSharedPreferences("UserInfo", MODE_PRIVATE);

        RequestQueue controller = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Course> list = getCourses(response);


                createLayout(list);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", editor.getInt("userID", 0) + "");

                return params;
            }
        };

        controller.add(request);
    }


    /**
     * Creates the initial layout with a corresponding
     * text view for each course, containing its course name & code
     * @param courses
     *  A list of all course codes in this user's schedule
     */
    private void createLayout(final ArrayList<Course> courses) {
        LinearLayout cv = (LinearLayout) findViewById(R.id.view_all_courses_list);
        for (int i = 0; i < courses.size(); i +=1) {

            final Course course = courses.get(i);
            String courseCode = course.getCode();
            String courseName = course.getName();

            LinearLayout individualCourseLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.setMargins(0, 15, 0, 0);
            }
            individualCourseLayout.setLayoutParams(params);
            individualCourseLayout.setOrientation(LinearLayout.VERTICAL);

            TextView courseCodeView = new TextView(this);
            courseCodeView.setText(courseCode);
            TextView courseNameView = new TextView(this);
            courseNameView.setText(courseName);

            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            // setMargins: left top right bottom
            params.setMargins(10, 10, 0, 10);
            params.gravity = Gravity.CENTER;
            courseCodeView.setLayoutParams(params);
            courseNameView.setLayoutParams(params);

            individualCourseLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    startSpecificCourse(course);
                }
            });

            individualCourseLayout.setBackgroundResource(R.drawable.customborder);

            individualCourseLayout.addView(courseNameView);
            individualCourseLayout.addView(courseCodeView);
            cv.addView(individualCourseLayout);
        }
    }

    /**
     * Parses the string returned from the PHP script
     * to seperate each course name and course code.
     * @param s
     *  String response from server running the PHP script.
     * @return
     *  A list containing a list of course names and a list of course codes
     */
    private ArrayList<Course> getCourses(String s) {
        ArrayList<Course> courses = new ArrayList<>();
        Scanner scanner = new Scanner(s);
        scanner.useDelimiter(";");

        while (scanner.hasNext()) {
            String name = scanner.next().trim();
            String code = scanner.next().trim();
            String location = scanner.next().trim();
            String lectureTimes = scanner.next().trim();
            String labTimes = scanner.next().trim();
            Course course = new Course(code, name, location, lectureTimes, labTimes, editor.getInt("userID", 0));
            Log.d("COURSE", course.toString());
            courses.add(course);
        }

        scanner.close();

        return courses;
    }

    /**
     * Setup "add course" button that takes user to the add course activity.
     */
    public void startAddCourse(View view) {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }

    /**
     * Returns user to the main menu.
     */
    public void backToMainMenu(View view) {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    /**
     * Starts specific course activity.
     * @param c
     *  A course currently in this user's schedule
     */
    public void startSpecificCourse(Course c) {
        Intent intent = new Intent(this, ViewSpecificCourseActivity.class);
        intent.putExtra("Course", c);
        startActivity(intent);
    }

}
