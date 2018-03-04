package hoppers.com.tamir.hoopers.bluetooth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import bluetooth.BluetoothConnectionHandler;
import hoppers.com.tamir.hoopers.R;
import logic.DIFFICULTY;

/**
 * Created by Tamir on 26/02/2018.
 * A fragment for when there is a bluetooth request to initialize a new game.
 */

public class IncomingRequestFragment extends Fragment implements View.OnClickListener {

    // Time limitation for the user to respond to the request
    private final int RESPONSE_TIMEOUT_MS = 10000;

    // The difficulty the remote player wants to play
    private DIFFICULTY difficulty;
    // The name of the device requested
    private String deviceName;

    private IOnChallengeResponse challengeResponseListener;

    // A timer to wait for the user to respond
    private Timer connectTimer;

    void setChallengeResponseListener(IOnChallengeResponse challengeResponseListener){
        this.challengeResponseListener = challengeResponseListener;
    }

    /**
     * Setting the difficulty of the request
     * @param difficulty the difficulty
     */
    void setRequestDetails(String deviceName, DIFFICULTY difficulty){
        this.deviceName = deviceName;
        this.difficulty = difficulty;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_incoming_request, container, false);

        // Setting the name and the level of the request
        TextView playerNameTextView = convertView.findViewById(R.id.player_name_text);
        TextView levelTextView = convertView.findViewById(R.id.level_text);
        playerNameTextView.setText(deviceName);
        levelTextView.setText(difficulty.toString());

        convertView.findViewById(R.id.accept_image).setOnClickListener(this);
        convertView.findViewById(R.id.reject_image).setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Starting a timer to wait for a response:

        connectTimer = new Timer(true);
        connectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Stopping the timer
                connectTimer.cancel();
                try {
                    if(challengeResponseListener != null){
                        challengeResponseListener.onRejected();
                    }
                    // Closing the connection
                    BluetoothConnectionHandler.getInstance().closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Indicating canceled
                connectTimer = null;
            }
        }, RESPONSE_TIMEOUT_MS);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(connectTimer != null){
            connectTimer.cancel();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.accept_image){
            // Sending accept to the other side indicating starting a game
            // and checking if the send went well
            if(BluetoothConnectionHandler.getInstance().sendAccept()) {
                // TODO: Move to game activity, start the game
                Toast.makeText(getContext(),"Accepted", Toast.LENGTH_SHORT).show();
                if(challengeResponseListener != null){
                    challengeResponseListener.onAccepted();
                }
            }else{
                ConnectionErrorHandler.displayErrorDialog(getActivity());
            }
        }
        else if(view.getId() == R.id.reject_image){
            try {
                // Sending exit and checking if send was successful
                if(!BluetoothConnectionHandler.getInstance().sendExit()){
                    ConnectionErrorHandler.displayErrorDialog(getActivity());
                }else{
                    if(challengeResponseListener != null){
                        challengeResponseListener.onRejected();
                    }
                    // Starting to acceptConnections again
                    BluetoothConnectionHandler.getInstance().acceptConnections();
                }
            } catch (IOException e) {
                e.printStackTrace();
                ConnectionErrorHandler.displayErrorDialog(getActivity());
            }
        }
    }
}
