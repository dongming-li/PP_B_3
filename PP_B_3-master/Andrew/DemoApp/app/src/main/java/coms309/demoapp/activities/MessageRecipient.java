package coms309.demoapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import coms309.demoapp.R;

public class MessageRecipient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_recipient);

        //Get the intent that started the activity and extract message
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //Find the new text view in this activity and apply the message
        TextView messageDisplay = (TextView) findViewById(R.id.textView);
        messageDisplay.setText(message);
    }
}
