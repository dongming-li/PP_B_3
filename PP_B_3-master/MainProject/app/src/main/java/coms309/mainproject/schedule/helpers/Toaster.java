package coms309.mainproject.schedule.helpers;

import android.content.Context;
import android.widget.Toast;

/**
 * A helper class which handles toasts made to notify the user usually of errors or faults
 */

public class Toaster {

    private Context context;

    /**
     * Constructor for the toaster.
     * Assigns some variables.
     *
     * @param context - context for this Toaster
     */
    public Toaster(Context context) {
        this.context = context;
    }

    /**Toast for not being connected to wifi*/
    public void wifiToast() {
        String text = "Not connected to wifi";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Toast for telling the user an entered field is invalid
     *
     * @param field - the string to be checked
     */
    public void invalidToast(String field) {
        String text = field + " must be filled in";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Toast anything put into the function.
     * Mainly used for testing purposes
     *
     * @param text
     */
    public void toast(String text) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
