package coms309.mainproject.schedule.event_forms;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import coms309.mainproject.schedule.ScheduleView;
import coms309.mainproject.schedule.helpers.Toaster;

/**
 * Superclass for EditEventActivity and AddEventActivity.
 * It contains some methods used by both.
 */
public abstract class EventFormActivity extends AppCompatActivity {

    protected Toaster toaster = new Toaster(this);

    //Check Wi-Fi
    protected boolean checkWifi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifi.isConnected();
    }

    //Title not filled in
    protected boolean isValid(String text) {
        //Empty
        if (text.compareTo("") == 0) return false;

        //Only spaces
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ') {
                return true;
            }
        }
        return false;
    }

    /**
     * Return to the calendar schedule view.
     * Intended to be used by a button in a layout
     *
     * @param view - mandatory parameter
     */
    public void backButton(View view) {
        Intent intent = new Intent(this, ScheduleView.class);
        startActivity(intent);
    }

}
