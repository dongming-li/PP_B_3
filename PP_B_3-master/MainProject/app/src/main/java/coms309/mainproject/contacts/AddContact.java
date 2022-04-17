package coms309.mainproject.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
 * Activity class that handles a form which is used to add a new contact to the user's list of
 *  contacts.
 *
 * @author christine
 */
public class AddContact extends FragmentActivity implements View.OnClickListener {

    /**
     * URL to add a contact.
     */
    private static final String addContactURL =
            "http://proj-309-pp-b-3.cs.iastate.edu/add_contact.php";

    /**
     * User ID of the current user.
     */
     private static int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        userID = userID = (getSharedPreferences(
                "UserInfo", MODE_PRIVATE)).getInt("userID",0);;
        final RequestQueue controller = Volley.newRequestQueue(this);
        final ImageView submitButton = (ImageView) findViewById(R.id.submit_button);
        final ImageView backButton = (ImageView) findViewById(R.id.back_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Execute POST request to server.
                executePost(controller);

                // Return to the contacts screen.
                goBackToContacts(v);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Return to the contacts screen.
                goBackToContacts(v);
            }
        });

    }

    /**
     * Makes a request to the server to add a contact to the database.
     * @param controller the RequestQueue
     */
    private void executePost(RequestQueue controller) {
        final String id = userID + "";
        final EditText lastName = (EditText) findViewById(R.id.last_name);
        final EditText firstName = (EditText) findViewById(R.id.first_name);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText phoneNumber = (EditText) findViewById(R.id.phone_number);

        StringRequest request = new StringRequest(Request.Method.POST, addContactURL,
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
                        Log.e("Volley Error", error.toString());
                        // error
                        Log.d("Error.Response:", error == null ? "Null" : "NotNull");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();

                params.put("userID", id);
                params.put("firstName", firstName.getText().toString());
                params.put("lastName", lastName.getText().toString());
                params.put("email", email.getText().toString());
                params.put("phoneNumber", phoneNumber.getText().toString());

                return params;
            }
        };
        controller.add(request);
    }


    /**
     * onClick method for buttons to go back to contacts screen.
     * @param v the current view
     */
    @Override
    public void onClick(View v) {
        goBackToContacts(v);
    }

    /**
     * Starts the Contacts Activity to go back to the contacts screen.
     * @param view the current view
     */
    public void goBackToContacts(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}
