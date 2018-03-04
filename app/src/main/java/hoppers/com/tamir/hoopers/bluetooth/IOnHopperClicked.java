package hoppers.com.tamir.hoopers.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Tamir on 03/03/2018.
 * An event for when the user clicks on one of the hoppers in the bluetooth hoppers listView.
 */

public interface IOnHopperClicked {
    void onHopperClicked(BluetoothDevice deviceClicked);
}
