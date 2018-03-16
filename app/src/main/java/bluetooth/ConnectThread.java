package bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

import logic.DIFFICULTY;

/**
 * Created by Tamir on 25/02/2018.
 * A thread connecting to a given bluetooth device
 * and sending the chosen difficulty to play with the device.
 */

public class ConnectThread extends Thread {
    private BluetoothSocket socket;
    private BluetoothAdapter bluetoothAdapter;
    private IOnSocketConnected socketConnectedListener;
    private byte difficultyToPlayCode;
    private byte levelCode;

    ConnectThread(BluetoothAdapter bluetoothAdapter, BluetoothDevice device,
                         IOnSocketConnected socketConnectedListener, DIFFICULTY difficultyToPlay, int level) throws IOException {
        this.bluetoothAdapter = bluetoothAdapter;
        this.difficultyToPlayCode = ConnectionCodes.DifficultyToByteCode(difficultyToPlay);
        this.levelCode = ConnectionCodes.LevelToByteCode(level);

        BluetoothDevice actual = bluetoothAdapter.getRemoteDevice(device.getAddress());

        // Get a BluetoothSocket to connect with the given BluetoothDevice.
        socket = actual.createInsecureRfcommSocketToServiceRecord(BluetoothConnectionHandler.APP_UUID);
        this.socketConnectedListener = socketConnectedListener;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            socket.connect();

            // Sending the difficulty the user choice
            socket.getOutputStream().write(new byte[]{difficultyToPlayCode, levelCode});

            if(socketConnectedListener != null){
                // The connection attempt succeeded.
                // Transferring the socket to the connection handler
                socketConnectedListener.onSocketConnected(socket);
            }
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                Log.e("", "Error!", connectException);
                socket.close();
            } catch (IOException closeException) {
                Log.e("", "Could not close the client socket", closeException);
            }
        }
    }
}
