package coms309.mainproject.groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import coms309.mainproject.R;
import coms309.mainproject.contacts.AddContact;
import coms309.mainproject.contacts.ContactsActivity;

import static android.R.layout.simple_spinner_item;


/**
 * Created by ammorris on 11/3/17.
 */

public class AddGroup extends FragmentActivity
        implements View.OnClickListener {


    private static final String addGroupURL =
            "http://proj-309-pp-b-3.cs.iastate.edu/add_group.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        final RequestQueue controller = Volley.newRequestQueue(this);
        final ImageView submitButton = (ImageView) findViewById(R.id.submit_button);
        final ImageView backButton = (ImageView) findViewById(R.id.back_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Execute POST request to server.
                executePost(controller);

                // Return to the contacts screen.
                goBackToGroups(v);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Return to the contacts screen.
                goBackToGroups(v);
            }
        });

    }


    private void executePost(RequestQueue controller) {
        final EditText groupID = (EditText) findViewById(R.id.userID);
        final EditText userID = (EditText) findViewById(R.id.group_id);

        StringRequest request = new StringRequest(Request.Method.POST, addGroupURL,
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

                params.put("userID", userID.getText().toString());
                params.put("groupID", groupID.getText().toString());

                return params;
            }
        };
        controller.add(request);
    }


    @Override
    public void onClick(View v) {
        goBackToGroups(v);
    }


    public void goBackToGroups(View view) {
        Intent intent = new Intent(this, showAllGroups.class);
        startActivity(intent);
    }
}
