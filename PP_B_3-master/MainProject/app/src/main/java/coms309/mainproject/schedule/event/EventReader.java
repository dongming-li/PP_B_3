package coms309.mainproject.schedule.event;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coms309.mainproject.schedule.helpers.StringSplicer;

/**
 * This class keeps the data for events shown in a Calendar Cell.
 * It's all sectioned off in this class to keep things simpler and more modular.
 * The Event Loader should get all events relevant to the user so they can immediately accessed for any calendar cell fragment.
 * This means getting events is a one-time load operation.
 */

public class EventReader {

    public ArrayList<Event> events;
    public ArrayList<Course> courses;
    private Context context;
    private int newID = -1;

    private StringSplicer splicer;

    /**
     * Constructor; defines context and loads in events relevant to the user
     *
     * @param context - context for this reader
     */
    public EventReader(Context context) {
        this.context = context;
    }

    /**
     * Read all relevant events and classes from the database
     */
    public void loadAll(int userID) {
        events = new ArrayList<>();
        courses = new ArrayList<>();

        splicer = new StringSplicer();

        loadEvents(userID);
        loadClasses(userID);
    }

    /**Make a GET request to find the highest Event ID in existing events**/
    public void getHighestID() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        final String urlEventID ="http://proj-309-pp-b-3.cs.iastate.edu/schedule/getHighestEventID.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlEventID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String res[] = response.split("~/");

                        if (res[0].compareTo("") == 0) newID = 0;
                        else newID = Integer.parseInt(res[0]) + 1;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /** Creates a new ID one higher than the highest currently in the database
     *
     * @return A valid, unoccupied Event ID
     */
    public int getNewID() {
        if (newID == -1) return 0;

        int temp = newID;
        newID = -1;

        return temp;
    }

    //Make the POST request to the server to get a day's events
    private void loadEvents(int userID) {
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        final String url ="http://proj-309-pp-b-3.cs.iastate.edu/schedule/getUserEvents.php";
        final int inputID = userID;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        events = splicer.spliceEvents(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response:", error.getMessage());
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

    //Make a request to get class information to be displayed alongside daily events
    private void loadClasses(int userID) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        final String url2 ="http://proj-309-pp-b-3.cs.iastate.edu/schedule/getClasses.php";
        final int inputID = userID;

        StringRequest request = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        courses = splicer.spliceClasses(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response:", error.getMessage());
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
