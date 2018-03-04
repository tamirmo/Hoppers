package bluetooth;

/**
 * Created by Tamir on 25/02/2018.
 * Event raised when bluetooth connection changed (connected/disconnected)
 */

public interface IOnConnectionStatusChanged {
    void onConnectionChanged(boolean isConnected);
}
