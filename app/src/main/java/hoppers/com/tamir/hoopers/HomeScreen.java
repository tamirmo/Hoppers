package hoppers.com.tamir.hoopers;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.lang.ref.WeakReference;

import bluetooth.BluetoothConnectionHandler;
import hoppers.com.tamir.hoopers.bluetooth.BluetoothActivity;
import hoppers.com.tamir.hoopers.levels.LevelsActivity;
import logic.GameManager;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private AnimationDrawable greenFrogAnimation;
    private AnimationDrawable redFrogAnimation;
    private FrameLayout loadingFrame;
    private ConstraintLayout buttonsLayout;
    private LinearLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        loadingFrame = (FrameLayout) findViewById(R.id.home_screen_loading_frame);
        buttonsLayout = (ConstraintLayout) findViewById(R.id.home_screen_buttons_layout);
        loadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
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

        // At first loading the game:

        showLoading();
        new InitializeGameTask(this).execute();
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

    private void showLoading(){
        loadingFrame.bringChildToFront(loadingLayout);
        buttonsLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void showButtonsLayout(){
        loadingFrame.bringChildToFront(buttonsLayout);
        buttonsLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
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

    /**
     * A task initializing the game and moving to the next activity
     */
    private static class InitializeGameTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<HomeScreen> homeScreen;

        InitializeGameTask(HomeScreen homeScreen){
            this.homeScreen = new WeakReference<>(homeScreen);
        }

        @Override
        protected Void doInBackground(Void... params) {

            // Preparing the game
            GameManager.getInstance().initializeManager(homeScreen.get());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Showing the buttons, all ready
            homeScreen.get().showButtonsLayout();
        }
    }
}
