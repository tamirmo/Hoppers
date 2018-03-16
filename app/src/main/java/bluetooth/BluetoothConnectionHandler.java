package bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import logic.DIFFICULTY;

/**
 * Created by Tamir on 25/02/2018.
 * A portal to all bluetooth operations.
 */

public class BluetoothConnectionHandler extends BroadcastReceiver implements IOnSocketConnected {
    // The time to wait until raising connection failed
    private final int CONNECT_TIMEOUT_MS = 15000;
    final static String APP_NAME = "Hoppers";
    final static UUID APP_UUID = UUID.fromString("00000000-0000-0002-0000-000000000005");

    private enum EVENT_TYPE{GAME_FINISHED, OPPONENT_EXITED, GAME_STARTED, LIST_UPDATED, NO_OPPONENT_RESPONSE}

    private static BluetoothConnectionHandler instance;

    private BluetoothAdapter bluetoothAdapter;
    private AcceptConnectionThread acceptConnectionThread;

    private BluetoothSocket socket;
    private InputStream inStream;
    private OutputStream outStream;
    private ReaderThread readerThread;
    private List<BluetoothDevice> devicesDiscovered;
    private List<IBluetoothEvents> eventsListeners;

    // A timer to wait for a connection establishment
    private Timer connectTimer;

    public static BluetoothConnectionHandler getInstance(){
        if(instance == null) {
            instance = new BluetoothConnectionHandler();
        }
        return instance;
    }

    private BluetoothConnectionHandler(){
        eventsListeners = new ArrayList<>();
    }

    public void addEventsListener(IBluetoothEvents listener){
        eventsListeners.add(listener);
    }

    public void removeEventsListener(IBluetoothEvents listener){
        eventsListeners.remove(listener);
    }

    public List<BluetoothDevice> getDevicesDiscovered() {
        return devicesDiscovered;
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter){
        this.bluetoothAdapter = bluetoothAdapter;
    }

    /**
     * Starting a thread to accept connections. This method is called when starting discovery.
     */
    public void acceptConnections() throws IOException {
        // Accepting only if not accepting already
        if(acceptConnectionThread == null) {
            acceptConnectionThread = new AcceptConnectionThread(bluetoothAdapter, this);
            acceptConnectionThread.start();
        }
    }

    public void stopAcceptConnections() throws IOException {
        // Accepting only if not accepting already
        if(acceptConnectionThread != null) {
            acceptConnectionThread.cancel();
            acceptConnectionThread = null;
        }
    }

    public boolean startDiscovery() throws IOException {
        // Canceling the last discovery for a case there was one
        bluetoothAdapter.cancelDiscovery();
        // Initializing the list
        devicesDiscovered = new ArrayList<>();

        // Raising an event that the list has changed
        raiseEvent(EVENT_TYPE.LIST_UPDATED);

        return bluetoothAdapter.startDiscovery();
    }

    /**
     * Connects the the given device and asks it to play in the given difficulty
     * @param deviceToConnect The device to connect to
     * @param difficultyToPlay The difficulty to play with the other device
     * @param level a random level in the given difficulty to play with the opponent
     * @throws IOException When there is an error getting a socket
     */
    public void requestGame(BluetoothDevice deviceToConnect, DIFFICULTY difficultyToPlay, int level) throws IOException {
        new ConnectThread(bluetoothAdapter, deviceToConnect, this, difficultyToPlay, level).start();

        // Starting a timer to wait for a response:

        connectTimer = new Timer(true);
        connectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Raising an event to indicate the connection was not established
                raiseEvent(EVENT_TYPE.NO_OPPONENT_RESPONSE);
                // Stopping the timer
                connectTimer.cancel();
                try {
                    closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connectTimer = null;
            }
        }, CONNECT_TIMEOUT_MS);
    }

    @Override
    public void onSocketConnected(BluetoothSocket socketAccepted) {

        try {
            // Closing the old connection if exist
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        socket = socketAccepted;

        // Get the input and output streams to read and write to and from the socket
        try {
            inStream = socket.getInputStream();
        } catch (IOException e) {
            //Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            outStream = socket.getOutputStream();
        } catch (IOException e) {
            //Log.e(TAG, "Error occurred when creating output stream", e);
        }

        readerThread = new ReaderThread();
        readerThread.start();

        raiseConnectionChanged(true);
    }

    /**
     * Write the given bytes to the socket
     * @param bytes The data to write
     * @return True if the write operation went well, False for an error
     */
    public boolean write(byte[] bytes) {
        boolean isWriteSuccessful = true;
        try {
            if(outStream != null) {
                outStream.write(bytes);
            }
        } catch (IOException e) {
            isWriteSuccessful = false;
            return isWriteSuccessful;
        }

        return isWriteSuccessful;
    }

    /**
     * This method closes the current connection. Called when the app is finished or game is finished.
     */
    public void closeConnection() throws IOException {
        if(readerThread != null) {
            readerThread.stopReading();
            readerThread = null;
        }
        if(outStream != null && inStream != null && socket != null) {
            socket.close();
            outStream.close();
            inStream.close();
            socket = null;
        }
    }

    public boolean sendFinished() throws IOException {
        boolean wasSent = write(new byte[]{ConnectionCodes.FINISHED});
        closeConnection();
        return wasSent;
    }

    public boolean sendExit() throws IOException {
        boolean wasSent = write(new byte[]{ConnectionCodes.EXIT});
        closeConnection();
        return wasSent;
    }

    public boolean sendAccept() {
        return write(new byte[]{ConnectionCodes.ACCEPT});
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            // Avoiding adding the same device twice
            if(!this.devicesDiscovered.contains(device)) {
                this.devicesDiscovered.add(device);
            }
            // Raising an event that the list has changed
            raiseEvent(EVENT_TYPE.LIST_UPDATED);
        }
    }

    private synchronized void raiseEvent(EVENT_TYPE type){
        for(IBluetoothEvents listener : eventsListeners){
            switch (type){
                case GAME_STARTED:
                    listener.onGameStarted();
                    break;
                case GAME_FINISHED:
                    listener.onOpponentFinished();
                    break;
                case OPPONENT_EXITED:
                    listener.onOpponentExited();
                    break;
                case LIST_UPDATED:
                    listener.onDevicesListUpdated();
                    break;
                case NO_OPPONENT_RESPONSE:
                    listener.onNoOpponentResponse();
                    break;
            }
        }
    }

    private synchronized void raiseConnectionChanged(boolean isConnected){
        for(IBluetoothEvents listener : eventsListeners){
            listener.onConnectionChanged(isConnected);
        }
    }

    private synchronized void raiseGameRequestReceived(DIFFICULTY difficulty, int level){
        // Getting the remote device's name:

        String deviceName = "";
        if(socket != null && socket.getRemoteDevice() != null){
            deviceName = socket.getRemoteDevice().getName();
        }

        for(IBluetoothEvents listener : eventsListeners){
            listener.onGameRequestReceived(difficulty, deviceName, level);
        }
    }

    /**
     * A thread class for processing incoming data
     */
    private class processReadBytesThread extends Thread{
        private byte[] bytes;
        private int readBytes;

        processReadBytesThread(byte[] bytes, int readBytes){
            this.bytes = bytes;
            this.readBytes = readBytes;
        }

        @Override
        public void run() {
            try {
                if (readBytes > 0) {
                    switch (bytes[0]) {
                        case ConnectionCodes.FINISHED:
                            raiseEvent(EVENT_TYPE.GAME_FINISHED);
                            closeConnection();
                            break;
                        case ConnectionCodes.EXIT:
                            raiseEvent(EVENT_TYPE.OPPONENT_EXITED);
                            // No more waiting for connection, we got a response
                            if(connectTimer != null)
                            {
                                connectTimer.cancel();
                            }
                            closeConnection();
                            break;
                        case ConnectionCodes.ACCEPT:
                            // No more waiting for connection, we got a response
                            if(connectTimer != null)
                            {
                                connectTimer.cancel();
                            }
                            raiseEvent(EVENT_TYPE.GAME_STARTED);
                            break;
                        default:
                            // Checking if we have received a game request
                            if (DIFFICULTY.isValidCode(bytes[0])) {
                                // Translating the bytes to the level and difficulty and raising event
                                raiseGameRequestReceived(ConnectionCodes.ByteToDifficulty(bytes[0]), ConnectionCodes.ByteToLevel(bytes[1]));
                            }
                            break;
                    }
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private class ReaderThread extends Thread{
        private volatile boolean isRunning;

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int numBytes; // bytes returned from read()

            isRunning = true;

            try {
                // Keep listening to the InputStream until an exception occurs.
                while (isRunning) {
                    if (inStream.available() > 0) {
                        // Read from the InputStream.
                        numBytes = inStream.read(buffer);
                        new processReadBytesThread(buffer, numBytes).start();
                    }
                }
            } catch (IOException e) {
                //Log.d(TAG, "Input stream was disconnected", e);
                raiseConnectionChanged(false);
                isRunning = false;
            }
        }

        void stopReading(){
            isRunning = false;
        }
    }
}
