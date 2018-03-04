package hoppers.com.tamir.hoopers.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;

import bluetooth.BluetoothConnectionHandler;
import bluetooth.IBluetoothEvents;
import hoppers.com.tamir.hoopers.HomeScreen;
import hoppers.com.tamir.hoopers.R;
import logic.DIFFICULTY;

public class BluetoothActivity extends AppCompatActivity implements IBluetoothEvents, IOnChallengeResponse {
    private static final int DISCOVERABLE_TIME_SEC = 10000;
    private static final int REQUEST_COARSE_LOCATION_PERMISSIONS = 10;

    private BluetoothAdapter bluetoothAdapter;
    private ChooseOpponentFragment chooseOpponentFragment;
    private IncomingRequestFragment incomingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initializing the fragments
        chooseOpponentFragment = new ChooseOpponentFragment();
        incomingFragment = new IncomingRequestFragment();

        // Registering to get the result of the challenge and move to the game activity
        incomingFragment.setChallengeResponseListener(this);

        BluetoothConnectionHandler.getInstance().setBluetoothAdapter(bluetoothAdapter);
        BluetoothConnectionHandler.getInstance().addEventsListener(this);

        if(isBluetoothAbilityDevice()){
            // If no device paired found with the app running
            if(!checkPairedDevices()){
                enableDiscovery();
                registerDiscoveredDeviceReceiver();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBluetoothAbilityDevice()){
            unregisterReceiver(BluetoothConnectionHandler.getInstance());
        }

        BluetoothConnectionHandler.getInstance().removeEventsListener(this);

        try {
            BluetoothConnectionHandler.getInstance().stopAcceptConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // When the user goes out of this activity,
        // closing the connection
        // TODO: Alert the manager that the user has exited the bluetooth activity, next game is not multi player
        try {
            BluetoothConnectionHandler.getInstance().closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveToDevicesListFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_holder_layout, chooseOpponentFragment);
        fragmentTransaction.commit();
    }

    private void moveToIncomingReqFragment(String deviceName, DIFFICULTY difficulty){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        incomingFragment.setRequestDetails(deviceName, difficulty);

        fragmentTransaction.replace(R.id.fragment_holder_layout, incomingFragment);
        fragmentTransaction.commit();
    }

    private void registerDiscoveredDeviceReceiver(){
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(BluetoothConnectionHandler.getInstance(), filter);
    }

    private void enableDiscovery(){
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_TIME_SEC);
        startActivityForResult(discoverableIntent, DISCOVERABLE_TIME_SEC);
    }

    /**
     * Checking if we are already paired with a device running the application
     * @return True if found a paired device running the application, False if not
     */
    private boolean checkPairedDevices(){
        // TODO: Check if paired has my UUID
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d(HomeScreen.TAG, "Paired - " + deviceName);
            }
        }
        return false;
    }

    /**
     * Checking if the current device has bluetooth ability
     * @return True if device has bluetooth ability False otherwise
     */
    private boolean isBluetoothAbilityDevice(){

        boolean isBluetoothAbility = true;

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, R.string.no_bluetooth_support_err, Toast.LENGTH_LONG).show();
            finish();
            isBluetoothAbility = false;
        }

        return isBluetoothAbility;
    }

    public void doDiscovery() {
        int hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            continueDoDiscovery();
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_COARSE_LOCATION_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION_PERMISSIONS: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    continueDoDiscovery();
                } else {
                    ConnectionErrorHandler.displayErrorDialog(this);
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(HomeScreen.TAG, "onActivityResult code = " + requestCode);

        // Checking if this is the discoverable request
        if (requestCode == DISCOVERABLE_TIME_SEC){
            if(resultCode ==  RESULT_CANCELED){
                // Device is not discoverable
                Toast.makeText(this, R.string.not_bluetooth_discoverable, Toast.LENGTH_LONG).show();
                finish();
            }else{
                doDiscovery();
                // TODO: Loading ?
            }
        }
    }

    /**
     * After permissions granted to discover devices, the method starts discovery
     */
    private void continueDoDiscovery(){
        try {
            // Starting discovery (checking if operation was successful)
            if(BluetoothConnectionHandler.getInstance().startDiscovery()){
                // Accepting incoming connections:
                BluetoothConnectionHandler.getInstance().acceptConnections();
            }else{
                Toast.makeText(this, R.string.discoverable_err, Toast.LENGTH_LONG).show();
                finish();
            }
            moveToDevicesListFragment();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.accepting_connections_err, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onGameRequestReceived(final DIFFICULTY difficulty, final String deviceName, final int level) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(HomeScreen.TAG, "activity onGameRequestReceived difficulty = " + difficulty + " level = " + level);
                // Moving to the fragment that displays the request
                moveToIncomingReqFragment(deviceName, difficulty);
            }
        });
    }

    @Override
    public void onOpponentExited() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Alerting the fragment that the other side rejected the invitation
                chooseOpponentFragment.opponentRejected();
            }
        });
    }

    @Override
    public void onOpponentFinished() {}
    @Override
    public void onConnectionChanged(boolean isConnected) {}
    @Override
    public void onGameStarted() {
        onAccepted();
    }
    @Override
    public void onDevicesListUpdated() {
        // Alerting the fragment to update it's list
        chooseOpponentFragment.hoppersListUpdated();
    }

    @Override
    public void onNoOpponentResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Alerting the fragment that there was no response for the game request
                chooseOpponentFragment.noResponseFromOpponent();
            }
        });
    }

    @Override
    public void onAccepted() {
        // TODO: Move to activity game
        finish();
    }

    @Override
    public void onRejected() {
        //finish();
        moveToDevicesListFragment();
    }
}
