package coms309.mainproject.schedule.switching_calendars;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * Similar to EventReader.
 * Queries data for users and sorts them accordingly for the Schedule classes that need it.
 */

public class ContactReader {

    public ArrayList<Contact> contacts;
    private Context context;

    private StringSplicer splicer;

    /**
     * Constructor; defines context and loads in events relevant to the user
     *
     * @param context - context for this reader
     */
    public ContactReader(Context context) {
        this.context = context;

        splicer = new StringSplicer();
        loadContacts();
    }

    //Make the GET request to the server to get a day's events
    private void loadContacts() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        final String url ="http://proj-309-pp-b-3.cs.iastate.edu/schedule/getUsers.php";
        final int inputID = context.getSharedPreferences("UserInfo", MODE_PRIVATE).getInt("userID", 1);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        contacts = splicer.spliceContacts(response);
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
