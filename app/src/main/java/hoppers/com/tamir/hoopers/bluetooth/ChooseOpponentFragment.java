package hoppers.com.tamir.hoopers.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import bluetooth.BluetoothConnectionHandler;
import hoppers.com.tamir.hoopers.HomeScreen;
import hoppers.com.tamir.hoopers.R;
import logic.DIFFICULTY;
import logic.GameManager;

/**
 * Created by Tamir on 26/02/2018.
 * A fragment for choosing bluetooth partner.
 */

public class ChooseOpponentFragment extends ListFragment implements IOnHopperClicked, View.OnClickListener {

    private HoppersDevicesAdapter adapter;
    private Spinner difficultiesSpinner;
    private LinearLayout connectingLayout;
    private FrameLayout mainLayout;
    private LinearLayout chooseHopperLayout;
    // The random level to play
    private int levelToPlay;

    public int getLevelToPlay() {
        return levelToPlay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_choose_opponent, container, false);

        difficultiesSpinner = convertView.findViewById(R.id.difficulties_spinner);
        String[] difficultiesArray = getResources().getStringArray(R.array.difficulties_array);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.difficulty_spinner_item,difficultiesArray);
        difficultiesSpinner.setAdapter(spinnerAdapter);

        mainLayout = convertView.findViewById(R.id.main_layout);
        connectingLayout = convertView.findViewById(R.id.connecting_layout);
        chooseHopperLayout = convertView.findViewById(R.id.choose_hopper_layout);

        adapter = new HoppersDevicesAdapter(getActivity());
        ((ListView)convertView.findViewById(android.R.id.list)).setAdapter(adapter);
        // Registering to the event of a hopper clicked
        adapter.setHopperClickedListener(this);

        convertView.findViewById(R.id.refresh).setOnClickListener(this);

        showChooseHopperLayout();

        return convertView;
    }

    private void showChooseHopperLayout(){
        mainLayout.bringChildToFront(chooseHopperLayout);
        chooseHopperLayout.setVisibility(View.VISIBLE);
        connectingLayout.setVisibility(View.GONE);
    }

    private void showConnectingLayout(){
        mainLayout.bringChildToFront(connectingLayout);
        connectingLayout.setVisibility(View.VISIBLE);
        chooseHopperLayout.setVisibility(View.GONE);
    }

    /**
     * Indicating the time has passed and there was no response from the opponent
     */
    void noResponseFromOpponent(){
        // Not in a connecting state anymore
        showChooseHopperLayout();

        // Alerting the user
        Toast.makeText(getContext(),R.string.no_response,Toast.LENGTH_LONG).show();

        // TODO: Call start discovery
    }

    /**
     * Indicating the opponent rejected to the invitation
     */
    void opponentRejected(){
        // Not in a connecting state anymore
        showChooseHopperLayout();

        // Alerting the user
        Toast.makeText(getContext(),R.string.opponent_rejected,Toast.LENGTH_LONG).show();

        // TODO: Call start discovery
    }

    /**
     * Indicating the list of devices has updated
     */
    void hoppersListUpdated(){
        // Updating the list
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onHopperClicked(final BluetoothDevice deviceClicked) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showConnectingLayout();

                try {
                    // Getting the difficulty chosen and getting a random level in this difficulty:
                    DIFFICULTY chosenDifficulty = DIFFICULTY.getTypeByCode(difficultiesSpinner.getSelectedItemPosition());
                    levelToPlay = GameManager.getInstance().getRandomLevel(chosenDifficulty);

                    Log.d(HomeScreen.TAG, "ItemClicked dif = " + chosenDifficulty + " level = " + levelToPlay);

                    BluetoothConnectionHandler.getInstance().requestGame(deviceClicked,
                            chosenDifficulty ,
                            levelToPlay);
                } catch (IOException e) {
                    ConnectionErrorHandler.displayErrorDialog(getActivity());
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.refresh){
            try {
                BluetoothConnectionHandler.getInstance().startDiscovery();
            } catch (IOException e) {
                ConnectionErrorHandler.displayErrorDialog(getActivity());
                e.printStackTrace();
            }
        }
    }
}
