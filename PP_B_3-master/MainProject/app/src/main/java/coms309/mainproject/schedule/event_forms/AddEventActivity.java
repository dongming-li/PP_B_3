package coms309.mainproject.schedule.event_forms;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import coms309.mainproject.R;
import coms309.mainproject.schedule.event.EventReader;
import coms309.mainproject.schedule.event.EventWriter;

/**
 * This class handles a form which is used to create / edit individual schedule events
 */
public class AddEventActivity extends EventFormActivity {

    private EventWriter writer;
    private EventReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_event);

        writer = new EventWriter(this, this);
        reader = new EventReader(this);
        reader.getHighestID();

        ((TimePicker) findViewById(R.id.timePicker)).setIs24HourView(true);

        //Type Spinner
        Spinner type = (Spinner) findViewById(R.id.type_select);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.event_form_types,
                android.R.layout.simple_spinner_dropdown_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        type.setAdapter(adapter1);

        //Change unassigned button function to add
        Button b = (Button) findViewById(R.id.button_create);

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkWifi()) {

                    //Legit
                    if (isValid( ((EditText) findViewById(R.id.title_edit)).getText().toString()) ) {
                        addEvent();
                    }

                    else { //Title not filled in
                        toaster.invalidToast("Title");
                    }

                }
                else { //No wifi
                    toaster.wifiToast();
                }
            }
        });
    }

    /**
     * Take values entered in the form attached to this activity and
     * call the Event Writer to make a new Event in the database.
     */
    public void addEvent() {
        EditText titleEdit = (EditText) findViewById(R.id.title_edit);
        Spinner typeSpinner = (Spinner) findViewById(R.id.type_select);
        EditText memoEdit = (EditText) findViewById(R.id.memo_edit);
        EditText locEdit = (EditText) findViewById(R.id.location_edit);

        // Get data and execute POST request to server.
        final String eventID = reader.getNewID() + "";
        final String title = titleEdit.getText().toString();
        final String type = typeSpinner.getSelectedItem().toString();
        final String memo = memoEdit.getText().toString();
        final String location = locEdit.getText().toString();

        //Time
        DatePicker date = (DatePicker) findViewById(R.id.datePicker);
        int day = date.getDayOfMonth();
        int month = date.getMonth() + 1;
        int year = date.getYear();
        String dateText = year + "-" + month + "-" + day;

        TimePicker time = (TimePicker) findViewById(R.id.timePicker);
        int hour = time.getHour();
        int min = time.getMinute();
        String timeText = hour + ":" + min;

        final String dateTime = dateText + " " + timeText;

        writer.addEventPost(eventID, title, type, memo, location, dateTime);
    }
}
