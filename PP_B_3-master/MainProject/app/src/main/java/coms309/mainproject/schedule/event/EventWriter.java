package coms309.mainproject.schedule.event;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import coms309.mainproject.schedule.event_forms.AddEventActivity;

/**
 * This class is entirely responsible for the creation of new events.
 * It was sectioned off from the AddEventActivity for the sake of modularity.
 */

public class EventWriter {

    private Context context;
    private Activity act;

    /**
     * Constructor for the Event Writer Class.
     *
     * @param context - context for this writer
     * @param act - activity this writer is being used in
     */
    public EventWriter(Context context, Activity act) {
        this.context = context;
        this.act = act;
    }

    /**
     * Add a new row into the User Events Table
     *
     * @param eID - Event ID
     * @param title - Event title
     * @param type - Event type
     * @param memo - Short description of this event or relevant notes
     * @param loc - Event location
     * @param dateTime - Event date and time in string format
     */
    public void addEventPost(final String eID, final String title,
                             final String type, final String memo, final String loc, final String dateTime) {
        RequestQueue controller = Volley.newRequestQueue(context);

        String url = "http://proj-309-pp-b-3.cs.iastate.edu/schedule/addEventPost.php";
        final int userID = 1; //TODO change from storage

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response:", response);

                        ((AddEventActivity) act).backButton(null);
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

                params.put("EventID", eID + "");
                params.put("UserID", userID + "");
                params.put("Title", title + "");
                params.put("Type", type + "");
                params.put("Memo", memo + "");
                params.put("Location", loc + "");
                params.put("DateTime", dateTime + "");

                return params;
            }
        };
        controller.add(request);
    }

}
