package hoppers.com.tamir.hoopers;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

import bluetooth.BluetoothConnectionHandler;
import hoppers.com.tamir.hoopers.bluetooth.BluetoothActivity;
import hoppers.com.tamir.hoopers.levels.LevelsActivity;
import logic.GameManager;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    // TODO: Delete
    public static String TAG = "Hoppers";

    private AnimationDrawable greenFrogAnimation;
    private AnimationDrawable redFrogAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // TODO: Move this from here and make an async task
        GameManager.getInstance().initializeManager(this);

        ImageButton multiPlayerBtn = (ImageButton) findViewById(R.id.multiPlayerImage);
        ImageButton singlePlayerBtn = (ImageButton) findViewById(R.id.singlePlayerImage);
        multiPlayerBtn.setOnClickListener(this);
        singlePlayerBtn.setOnClickListener(this);

        // Preparing for the frogs animation:

        ImageView redFrogImage = (ImageView) findViewById(R.id.home_screen_red_frog);
        redFrogImage.setBackgroundResource(R.drawable.red_frog_animation);
        redFrogAnimation = (AnimationDrawable) redFrogImage.getBackground();

        ImageView greenFrogImage = (ImageView) findViewById(R.id.home_screen_green_frog);
        greenFrogImage.setBackgroundResource(R.drawable.green_frog_animation);
        greenFrogAnimation = (AnimationDrawable) greenFrogImage.getBackground();
    }

    @Override
    protected void onResume() {
        super.onResume();
        greenFrogAnimation.start();
        redFrogAnimation.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        greenFrogAnimation.stop();
        redFrogAnimation.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            // Closing connection for a case there is still a connection
            BluetoothConnectionHandler.getInstance().closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.multiPlayerImage) {
            // Moving to the bluetooth activity:

            Intent bluetoothIntent = new Intent(this, BluetoothActivity.class);
            startActivity(bluetoothIntent);
        }else if(view.getId() == R.id.singlePlayerImage){
            // Moving to the levels activity:

            Intent levelsIntent = new Intent(this, LevelsActivity.class);
            startActivity(levelsIntent);
        }
    }
}
