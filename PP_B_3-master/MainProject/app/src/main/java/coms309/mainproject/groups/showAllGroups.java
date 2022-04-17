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
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import coms309.mainproject.R;

import static android.R.layout.simple_spinner_item;


/**
 * Created by ammorris on 10/7/17.
 */

public class showAllGroups extends AppCompatActivity {

    /**
     * The URL to get all groups of the current user.
     */
    private static final String getAllGroupsURL =
            "http://proj-309-pp-b-3.cs.iastate.edu/get_all_groups.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);
        loadGroups();
    }

    /**
     * Loads groups, which are then processed by other methods to create an xml doc dynamically
     */
    public void loadGroups() {
        final RequestQueue controller = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, getAllGroupsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] ids = splitGroups(response);
                        createLayout(ids);
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

                //params.put("userID", userID);

                return params;
            }
        };
        controller.add(request);
    }
    /**
     * Helper method used to make an array of
     * group ids
     */
    private String[] splitGroups(String res) {
        // Split by row where each row is one group id.
        String[] contactsInfo = res.split("\n");
        return contactsInfo;
    }
    /**
     * Helper method used to dynamically make layouts of groups
     */
    private void createLayout(String[] groups) {
        LinearLayout cv1 = (LinearLayout) findViewById(R.id.view_all_groups_list);
        for (int i = 0; i < groups.length; i++) {
            final String groupID = groups[i];
            TextView courseName = new TextView(this);
            courseName.setText("Group #" + groupID);
            courseName.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //startSpecificCourse(name, code);
                }
            });
            cv1.addView(courseName);
        }
    }




    public void AddGroup(View view) {
        Intent intent = new Intent(this, AddGroup.class);
        startActivity(intent);
    }
}
