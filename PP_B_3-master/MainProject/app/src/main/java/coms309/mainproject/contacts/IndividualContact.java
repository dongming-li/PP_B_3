package coms309.mainproject.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Activity class for an viewing an one contact's information (name, email, phone number).
 *
 * @author christine
 */
public class IndividualContact extends FragmentActivity implements View.OnClickListener {
    /**
     * The URL to delete a contact.
     */
    private static final String deleteContactURL =
            "http://proj-309-pp-b-3.cs.iastate.edu/delete_contact.php";

    /**
     * The user ID of the current user.
     */
    private static int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_contact);
        final String contactInfo = getIntent().getStringExtra("contactInfo");
        userID = (getSharedPreferences(
                "UserInfo", MODE_PRIVATE)).getInt("userID",0);

        final TextView name = (TextView) findViewById(R.id.individual_contact_name);
        final TextView email = (TextView) findViewById(R.id.individual_contact_email);
        final TextView phoneNumber = (TextView) findViewById(R.id.individual_contact_phone_number);

        // firstName, lastName, phoneNumber, email
        String[] contactInfoArr = contactInfo.split("\\s");
        final Contact contact = new Contact(
                contactInfoArr[0], contactInfoArr[1], contactInfoArr[2], contactInfoArr[3]);

        name.setText(contact.firstName + " " + contact.lastName);
        email.setText(contact.email);
        phoneNumber.setText(contact.phoneNumber);

        final ImageView deleteButton = (ImageView) findViewById(R.id.delete_contact_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact(contact);
                goBackToContacts();
            }
        });
    }


    /**
     * Makes a request to the server to delete this contact from the database.
     *
     * @param contact the contact to delete
     */
    public void deleteContact(final Contact contact) {
        final RequestQueue controller = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, deleteContactURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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

                params.put("userID", "" + userID);
                params.put("firstName", contact.firstName);
                params.put("lastName", contact.lastName);

                return params;
            }
        };
        controller.add(request);
    }

    /**
     * onClick method for back button.
     * @param v the current view
     */
    @Override
    public void onClick(View v) {
        goBackToContacts();
    }

    /**
     * Starts Contacts Activity to go back to contacts screen.
     */
    private void goBackToContacts() {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}
