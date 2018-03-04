package bluetooth;

import android.bluetooth.BluetoothSocket;

/**
 * Created by Tamir on 25/02/2018.
 * Event for when a bluetooth socket is connected (while accepting connections or choosing a device).
 */

public interface IOnSocketConnected {
    void onSocketConnected(BluetoothSocket socketAccepted);
}
