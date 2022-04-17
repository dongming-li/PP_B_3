package coms309.demoapp.activities;

import android.graphics.Color;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import coms309.demoapp.R;

/** A class with  fragments in it **/
public class ContactsList extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_view);
        LinearLayout layout = (LinearLayout) findViewById(R.id.contacts_linear);

        for (int i = 0; i < 15; i++) {

            String contactName = "Contact " + (i + 1);

            //Button
            Button button = new Button(this);

            button.setText(contactName);
            button.setTextColor(Color.BLACK);
            button.setTextSize(20);

            layout.addView(button);

            //Divider
            View view = new View(this);
            view.setMinimumHeight(15);
            layout.addView(view);
        }
    }
}
