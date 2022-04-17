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

import coms309.mainproject.schedule.event_forms.EditEventActivity;

/**
 * This class is entirely responsible for editing existing rows in the Events database.
 * It is very similar to the EventWriter class.
 */

public class EventEditor {

    final String url = "http://proj-309-pp-b-3.cs.iastate.edu/schedule/editUserEvent.php";
    final String urlDelete = "http://proj-309-pp-b-3.cs.iastate.edu/schedule/deleteUserEvent.php";

    private Context context;
    private Activity act;

    /**Constructor for the Event Editor.
     *
     * @param context - context for this class; necessary for some methods
     * @param act - the activity this class is found in; necessary for some methods
     * */
    public EventEditor(Context context, Activity act) {
        this.context = context;
        this.act = act;
    }

    /** Edit an existing row in the User Events Table
     *
     * @param eID - Event ID
     * @param newTitle - Edited title (may be unchanged)
     * @param newType - Edited type (may be unchanged)
     * @param newMemo - Edited memo (may be unchanged)
     * @param newLocation - Edited location (may be unchanged)
     * @param newTime - Edited time and date (may be unchanged)
     */
    public void editEvent(final String eID, final String newTitle, final String newType, final String newMemo,
                          final String newLocation, final String newTime) {

        RequestQueue controller = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response:", response);

                        ((EditEventActivity) act).backButton(null);
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

                params.put("EventID", eID);
                params.put("Title", newTitle);
                params.put("Type", newType);
                params.put("Memo", newMemo);
                params.put("Location", newLocation);
                params.put("DateTime", newTime);

                return params;
            }
        };
        controller.add(request);
    }

    /** Remove an event from the database
     *
     * @param eID - The event ID of the event that will be deleted
     */
    public void deleteEvent(final String eID) {
        RequestQueue controller = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, urlDelete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response:", response);
                        ((EditEventActivity) act).backButton(null);
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

                params.put("EventID", eID);

                return params;
            }
        };
        controller.add(request);
    }

}
