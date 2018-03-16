package hoppers.com.tamir.hoopers.bluetooth;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    // The level the opponent sent (not showing it to the user)
    private int level;

    private FrameLayout incomingRequestMainFrame;
    private LinearLayout loadingLayout;
    private LinearLayout requestLayout;

    int getLevel(){
        return level;
    }

    void setChallengeResponseListener(IOnChallengeResponse challengeResponseListener){
        this.challengeResponseListener = challengeResponseListener;
    }

    /**
     * Setting the difficulty of the request
     * @param difficulty the difficulty
     */
    void setRequestDetails(String deviceName, DIFFICULTY difficulty, int level){
        this.deviceName = deviceName;
        this.difficulty = difficulty;
        this.level = level;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_incoming_request, container, false);

        // Setting the name and the level of the request
        TextView playerNameTextView = convertView.findViewById(R.id.player_name_text);
        TextView levelTextView = convertView.findViewById(R.id.level_name_text);
        playerNameTextView.setText(deviceName);
        levelTextView.setText(difficulty.toString());
        incomingRequestMainFrame = convertView.findViewById(R.id.incoming_request_main_frame);
        loadingLayout = convertView.findViewById(R.id.loading_layout);
        requestLayout = convertView.findViewById(R.id.request_layout);

        convertView.findViewById(R.id.accept_image).setOnClickListener(this);
        convertView.findViewById(R.id.reject_image).setOnClickListener(this);

        // At first showing the request
        showRequest();

        return convertView;
    }

    private void showLoading(){
        incomingRequestMainFrame.bringChildToFront(loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);
        requestLayout.setVisibility(View.GONE);
    }

    private void showRequest(){
        incomingRequestMainFrame.bringChildToFront(requestLayout);
        loadingLayout.setVisibility(View.GONE);
        requestLayout.setVisibility(View.VISIBLE);
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
            new SendAcceptTask().execute();
            showLoading();
        }
        else if(view.getId() == R.id.reject_image){
            new SendRejectTask().execute();
            showLoading();
        }
    }

    /**
     * Sending accept to the other side indicating starting a game
     */
    private class SendAcceptTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            // Preparing the game
            return BluetoothConnectionHandler.getInstance().sendAccept();
        }

        @Override
        protected void onPostExecute(Boolean isSent) {
            if(isSent) {
                if(challengeResponseListener != null){
                    challengeResponseListener.onAccepted();
                }
            }else{
                ConnectionErrorHandler.displayErrorDialog(getActivity());
            }
        }
    }

    /**
     * A task sending reject to the other user
     */
    private class SendRejectTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                return BluetoothConnectionHandler.getInstance().sendExit();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSent) {
            if(!isSent){
                ConnectionErrorHandler.displayErrorDialog(getActivity());
            }else{
                if(challengeResponseListener != null){
                    challengeResponseListener.onRejected();
                }
                // Starting to acceptConnections again
                try {
                    BluetoothConnectionHandler.getInstance().acceptConnections();
                } catch (IOException e) {
                    e.printStackTrace();
                    ConnectionErrorHandler.displayErrorDialog(getActivity());
                }
            }
        }
    }
}
