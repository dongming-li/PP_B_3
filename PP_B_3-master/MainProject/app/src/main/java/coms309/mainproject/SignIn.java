package coms309.mainproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import coms309.mainproject.contacts.ContactsActivity;
import coms309.mainproject.courses.ViewAllCoursesActivity;
import coms309.mainproject.groups.showAllGroups;
import coms309.mainproject.schedule.ScheduleView;
import coms309.mainproject.schedule.event.Event;
import coms309.mainproject.schedule.helpers.DateHandler;
import coms309.mainproject.schedule.helpers.StringSplicer;
import coms309.mainproject.schedule.helpers.Toaster;

public class SignIn extends AppCompatActivity  {

//    private final String url ="http://proj-309-pp-b-3.cs.iastate.edu/schedule/getUserEvents.php";
//    private StringSplicer splicer = new StringSplicer();
//    private Toaster toaster = new Toaster(this);

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<String> emails = new ArrayList<>();
    private static String userEmail;
    private static String userPassword;
    private static int userID;

    /**
     * The URL to get all contacts of the current user.
     */
    private static final String addUserURL =
            "http://proj-309-pp-b-3.cs.iastate.edu/add_user.php";

    private static final String PREFS_FILE = "UserInfo";
    private static SharedPreferences.Editor editor;


    /**
     * The URL to get all users.
     */
    private static final String getAllUsersURL =
            "http://proj-309-pp-b-3.cs.iastate.edu/get_all_users.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in);

        editor = getSharedPreferences(PREFS_FILE, MODE_PRIVATE).edit();


        final RequestQueue controller = Volley.newRequestQueue(this);
        final ImageView submitButton = (ImageView) findViewById(R.id.sign_in_submit_button);

        loadUsers();

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userEmail = ((EditText) findViewById(R.id.user_email)).getText().toString();
                userPassword = ((EditText) findViewById(R.id.user_password)).getText().toString();
                if(emails.contains(userEmail)) {
                    User user = getUserFromEmail();
                    if(user != null) {
                        // Make user id available to through shared preferences.
                        editor.putInt("userID", Integer.parseInt(user.userID));
                        editor.apply();
                        goToMainScreen();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "Incorrect password. Please try again.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                } else {
                    // Execute POST request to server.
                    executePost(controller);
                }
            }
        });
    }

    /**
     * Finds the user that has the inputted email and if no user exists, returns null.
     * If the user is found, sets user id instance variable to user's user id.
     *
     * @return the current user, given their email
     */
    private User getUserFromEmail() {
        for (User user: users) {
            if (user.email.equals(userEmail) && user.password.equals(userPassword)) {
                userID = Integer.parseInt(user.userID);
                return user;
            }
        }
        return null;
    }

    /**
     * Starts the main screen activity to load the main menu page.
     */
    public void goToMainScreen() {

//        if (!checkWifi()) {
//            toaster.wifiToast();
//            return;
//        }

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    /**
     * Makes a request to the server to add a user to the database.
     * @param controller the RequestQueue
     */
    private void executePost(RequestQueue controller) {
        userID = emails.size() + 1;

        StringRequest request = new StringRequest(Request.Method.POST, addUserURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Context context = getApplicationContext();
                        CharSequence text = "User created successfully";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        Log.d("Response:", response);

                        // Make user id available to through shared preferences.
                        editor.putInt("userID", userID);
                        editor.commit();
                        goToMainScreen();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // response
                        Context context = getApplicationContext();
                        CharSequence text = "Failed to create user. Try again.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        Log.e("Volley Error", error.toString());
                        // error
                        Log.d("Error.Response:", error == null ? "Null" : "NotNull");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();

                params.put("userID", "" + userID);
                params.put("email", userEmail);
                params.put("password", userPassword);

                return params;
            }
        };
        controller.add(request);
    }

    //Check Wi-Fi
    private boolean checkWifi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getActiveNetworkInfo();

        return wifi != null && wifi.getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * Splits up Users information by row and then by column.
     * Creates new User instances.
     * @param res the String returned by the server's response
     */
    private void spliceUsers(String res)
    {
        // Split by row where each row is one user.
        String[] usersInfo = res.split("\n");

        for (String individualUserInfo : usersInfo) {
            // Split by column where columns are the user's user id, email, and password.
            String[] cols = individualUserInfo.split("\\s");

            User user = new User(cols[0], cols[1], cols[2]);
            emails.add(cols[1]);
            users.add(user);
        }
    }

    /**
     * Makes a request to the server to get all users.
     */
    public void loadUsers() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getAllUsersURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        spliceUsers(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
