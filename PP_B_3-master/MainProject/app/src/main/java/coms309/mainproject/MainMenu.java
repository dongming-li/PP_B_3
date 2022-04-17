package coms309.mainproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
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

/**
 * A nexus class which accesses all other main branches of the app.
 * It also shows any events scheduled for the current date.
 */
public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    private StringSplicer splicer = new StringSplicer();
    private DateHandler dateHandler = new DateHandler(Calendar.getInstance());
    private Toaster toaster = new Toaster(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        findViewById(R.id.contacts_button).setOnClickListener(this);
        findViewById(R.id.schedule_button).setOnClickListener(this);
        findViewById(R.id.classes_button).setOnClickListener(this);

        loadEvents();
    }

    @Override
    public void onClick(View v) {

//        if (!checkWifi()) {
//            toaster.wifiToast();
//            return;
//        }

        Intent intent;

        switch(v.getId()) {
            case R.id.contacts_button:
                intent = new Intent(this, ContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.schedule_button:
                intent = new Intent(this, ScheduleView.class);
                startActivity(intent);
                break;
            case R.id.classes_button:
                intent = new Intent(this, ViewAllCoursesActivity.class);
                startActivity(intent);
                break;
            case R.id.groups_button:
                intent = new Intent(this, showAllGroups.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //Check Wi-Fi
    private boolean checkWifi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getActiveNetworkInfo();

        return wifi != null && wifi.getType() == ConnectivityManager.TYPE_WIFI;
    }

    //Load Today's Events
    private void todayEvents(ArrayList<Event> events) {
        LinearLayout eventsList = (LinearLayout) findViewById(R.id.eventsList);

        //Populate list with today's events
        for (Event e : events) {

            //Check if event is relevant to this day
            if (!dateHandler.checkDate(e.dateTime)) continue;

            Button b = new Button(this);
            b.setTransformationMethod(null);
            b.setTextSize(14);

            String text = e.title + "\n";
            if (!e.location.equals("")) text += e.location + "\n";
            text += e.displayTime + "\n";
            if (!e.description.equals("")) text += e.description;

            b.setText(text);

            eventsList.addView(b);
        }

    }

    //Make the GET request to the server to get a day's events
    private void loadEvents() {
        final String url ="http://proj-309-pp-b-3.cs.iastate.edu/schedule/getUserEvents.php";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        final int inputID = getSharedPreferences("UserInfo", MODE_PRIVATE).getInt("userID", 1);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        todayEvents(splicer.spliceEvents(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response:", "ERROR");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String>  params = new HashMap<>();

                params.put("UserID", inputID + "");

                return params;
            }
        };
        queue.add(request);
    }
}
