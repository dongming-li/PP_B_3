package coms309.mainproject.schedule.event_forms;

import android.content.Intent;
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
import coms309.mainproject.schedule.ScheduleView;
import coms309.mainproject.schedule.event.EventEditor;

/**
 * This class uses the exact same layout as AddEventActivity but is for editing existing events instead of making new ones.
 * The class loads in the currently-existing info for the event it will edit so that the edit-text boxes are already filled with some value when the activity loads.
 */

public class EditEventActivity extends EventFormActivity {

    private EventEditor editor;

    private String[] event_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_event);

        editor = new EventEditor(this, this);

        //Type Spinner
        Spinner type = (Spinner) findViewById(R.id.type_select);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.event_form_types,
                android.R.layout.simple_spinner_dropdown_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        type.setAdapter(adapter1);

        ((TimePicker) findViewById(R.id.timePicker)).setIs24HourView(true);

        //Get message from cell which started this activity
        Intent intent = getIntent();
        event_data = intent.getStringArrayExtra(ScheduleView.EVENT_DATA);
        loadPrevious();

        //Change button function from add to edit
        Button b = (Button) findViewById(R.id.button_edit);

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkWifi()) {

                    //Legit
                    if (isValid( ((EditText) findViewById(R.id.title_edit)).getText().toString()) ) {
                        callEditor();
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

    /**Delete the event being edited**/
    public void deleteEvent(View view) {
        editor.deleteEvent(event_data[0]);
    }

    //Edit event with new entered values (old values are preserved if unchanged)
    private void callEditor() {
        //Views
        EditText titleEdit = (EditText) findViewById(R.id.title_edit);
        Spinner typeSpinner = (Spinner) findViewById(R.id.type_select);
        EditText memoEdit = (EditText) findViewById(R.id.memo_edit);
        EditText locEdit = (EditText) findViewById(R.id.location_edit);

        // Get data and execute POST request to server.
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
        String timeText = hour + ":" + min + ":00";

        final String dateTime = dateText + " " + timeText;

        editor.editEvent(event_data[0], title, type, memo, location, dateTime);
    }

    //Load in previously existing event values
    private void loadPrevious() {

        //Text edit
        EditText titleEdit = (EditText) findViewById(R.id.title_edit);
        EditText memoEdit = (EditText) findViewById(R.id.memo_edit);
        EditText locEdit = (EditText) findViewById(R.id.location_edit);

        titleEdit.setText(event_data[1]);
        memoEdit.setText(event_data[4]);
        locEdit.setText(event_data[3]);

        //Type spinner
        Spinner typeSpinner = (Spinner) findViewById(R.id.type_select);
        if (event_data[2].equals("Homework")) typeSpinner.setSelection(0);
        else typeSpinner.setSelection(1);

        //Date and Time
        DatePicker dateModule = (DatePicker) findViewById(R.id.datePicker);
        TimePicker timeModule = (TimePicker) findViewById(R.id.timePicker);

        String[] dateTime = event_data[5].split(" ");
        String[] date = dateTime[0].split("-");
        String[] time = dateTime[1].split(":");

        dateModule.updateDate(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));
        timeModule.setHour(Integer.parseInt(time[0]));
        timeModule.setMinute(Integer.parseInt(time[1]));
    }

}
