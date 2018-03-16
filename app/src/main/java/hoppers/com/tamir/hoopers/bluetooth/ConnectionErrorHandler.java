package hoppers.com.tamir.hoopers.bluetooth;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import hoppers.com.tamir.hoopers.R;

/**
 * Created by Tamir on 03/03/2018.
 * A static class that handles a bluetooth error
 */

class ConnectionErrorHandler {
    /**
     * Displaying a dialog of an unexpected error
     */
    static void displayErrorDialog(final Activity activity){
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(R.string.unexpected_error)
                .setTitle(R.string.bluetooth_error);

        // Add the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.finish();
            }
        });

        // Get the AlertDialog and show it
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
