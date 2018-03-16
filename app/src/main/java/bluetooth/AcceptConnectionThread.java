package bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

/**
 * Created by Tamir on 25/02/2018.
 * A thread for accepting bluetooth connections
 */

public class AcceptConnectionThread extends Thread{
    private BluetoothServerSocket mmServerSocket;

    private IOnSocketConnected socketAcceptedListener;

    AcceptConnectionThread(BluetoothAdapter bluetoothAdapter, IOnSocketConnected socketAcceptedListener) throws IOException{
        mmServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(BluetoothConnectionHandler.APP_NAME, BluetoothConnectionHandler.APP_UUID);

        this.socketAcceptedListener = socketAcceptedListener;
    }

    public void run() {
        BluetoothSocket socket;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();


                if (socket != null) {
                    // A connection was accepted.
                    // Transferring the socket to the connection handler.
                    if(socketAcceptedListener != null){
                        socketAcceptedListener.onSocketConnected(socket);
                    }

                    // No more accepting
                    mmServerSocket.close();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    void cancel() throws IOException {
        mmServerSocket.close();
    }
}
