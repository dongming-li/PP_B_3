package coms309.demoapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import coms309.demoapp.R;

/** Home Screen of app **/
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "coms309.demoapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Start Contacts List activity **/
    public void goToContacts(View view) {
        Intent intent = new Intent(this, ContactsList.class);

        startActivity(intent);
    }

    public void goToCalendar(View view) {
        Intent intent = new Intent(this, Calendar.class);

        startActivity(intent);
    }

    /** Send message from this activity to another **/
    public void sendMessage(View vew) {
        Intent intent = new Intent(this, MessageRecipient.class);

        //Get message from activity
        EditText editText = (EditText) findViewById(R.id.edit_text);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message); //Attach message to intent

        startActivity(intent);
    }
}
