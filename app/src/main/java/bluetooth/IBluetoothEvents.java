package bluetooth;

import logic.DIFFICULTY;

/**
 * Created by Tamir on 01/03/2018.
 * An interface for bluetooth events (game won, opponent existed...)
 */

public interface IBluetoothEvents {
    // An event indicating the start of the game
    void onGameStarted();

    // An event for a new opponent connected wanting to play with the given difficulty, name and level
    void onGameRequestReceived(DIFFICULTY difficulty, String deviceName, int level);

    // An event indicating the opponent chose to exit the game
    void onOpponentExited();

    // An event indicating the opponent has won
    void onOpponentFinished();

    // Indicating when there is a successful connection or a disconnection
    void onConnectionChanged(boolean isConnected);

    // Indicating a new device found or removed from the list of devices
    void onDevicesListUpdated();

    // Indicating the other side did not respond to the request
    void onNoOpponentResponse();
}
