package hoppers.com.tamir.hoopers.board;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import bluetooth.BluetoothConnectionHandler;
import bluetooth.IBluetoothEvents;
import hoppers.com.tamir.hoopers.R;
import hoppers.com.tamir.hoopers.end.GameOverActivity;
import logic.DIFFICULTY;
import logic.GameManager;
import logic.Hop;
import logic.IOnGameDurationChanged;
import logic.LeafCoordinate;
import logic.Turn;

public class GameBoardActivity extends AppCompatActivity implements View.OnClickListener, IOnGameDurationChanged, IOnLeafClicked, IBluetoothEvents, IOnHopAnimationEnded {

    public static final String LEVEL_NUM_KEY = "LEVEL_NUM";
    public static final String IS_BLUETOOTH_GAME_KEY = "IS_BLUETOOTH_GAME";

    private TextView timePlayedHeader;
    private TextView timePlayed;
    private FrameLayout solutionFrame;
    private RelativeLayout solutionNavigationLayout;
    private LinearLayout viewSolutionLayout;
    private ImageButton undoButton;
    private BoardAdapter boardAdapter;
    private MediaPlayer hopPlayer;
    private int levelNum;
    private boolean isGameOver;

    private boolean isBluetoothGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        // Getting the level number and if the game is a bluetooth game or single
        levelNum = getIntent().getExtras().getInt(LEVEL_NUM_KEY, 0);
        isBluetoothGame = getIntent().getExtras().getBoolean(IS_BLUETOOTH_GAME_KEY, false);

        TextView levelText = (TextView) findViewById(R.id.level_name);
        viewSolutionLayout = (LinearLayout) findViewById(R.id.view_solution_layout);
        TableLayout gameGrid = (TableLayout) findViewById(R.id.game_board);
        timePlayed = (TextView) findViewById(R.id.time_played_txt);
        solutionFrame = (FrameLayout) findViewById(R.id.solution_frame);
        solutionNavigationLayout = (RelativeLayout) findViewById(R.id.solution_navigation_layout);
        undoButton = (ImageButton) findViewById(R.id.undo_button);
        timePlayedHeader = (TextView) findViewById(R.id.time_played_header);

        findViewById(R.id.previous_solution_step_btn).setOnClickListener(this);
        findViewById(R.id.next_solution_step_btn).setOnClickListener(this);
        findViewById(R.id.view_solution_btn).setOnClickListener(this);
        undoButton.setOnClickListener(this);

        // Setting the level number
        levelText.setText(String.valueOf(levelNum));

        // Registering to the event to display the durations
        GameManager.getInstance().setGameDurationChangedListener(this);
        GameManager.getInstance().startGame(levelNum);

        // Initializing the adapter and refreshing the view according to this level
        boardAdapter = new BoardAdapter(gameGrid, this, this,
                (ImageView)findViewById(R.id.floating_green_frog),
                (ImageView)findViewById(R.id.floating_red_frog));
        boardAdapter.updateBoard();

        if(isBluetoothGame) {
            // When in remote game, No solution viewing is allowed
            hideSolutionFrame();
        } else {
            // At first letting the user click "show solution"
            showViewSolutionLayout();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameManager.getInstance().resumeGame();
        if(isBluetoothGame) {
            // When in remote game, No solution viewing is allowed
            BluetoothConnectionHandler.getInstance().addEventsListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameManager.getInstance().pauseGame();
        if(this.isBluetoothGame){
            // Sending exit to the opponent
            new BluetoothExitSenderThread().start();
            BluetoothConnectionHandler.getInstance().removeEventsListener(this);
            finish();
        }
        if(hopPlayer != null){
            hopPlayer.stop();
            hopPlayer.release();
            hopPlayer = null;
        }
    }

    @Override
    public void onClick(View view) {
        // When game is over, not letting the user click on anything
        // until moving to the game over activity
        if(!isGameOver) {
            if (view.getId() == R.id.view_solution_btn) {
                GameManager.getInstance().startShowSolution();
                // No need to display time anymore
                timePlayedHeader.setVisibility(View.GONE);
                timePlayed.setVisibility(View.GONE);
                // When viewing solution,
                // undo button should not be active
                undoButton.setVisibility(View.GONE);

                showSolutionNavigationLayout();

                // Refreshing view cause we are back to the beginning
                boardAdapter.updateBoard();
            } else if (view.getId() == R.id.previous_solution_step_btn) {
                Hop hop = GameManager.getInstance().prevSolutionStep();

                // Refreshing view
                boardAdapter.updateBoard();
                if (hop == null) {
                    // Alerting the user the solution is already at the beginning
                    Toast.makeText(this, R.string.first_solution_step_msg, Toast.LENGTH_SHORT).show();
                }
            } else if (view.getId() == R.id.next_solution_step_btn) {
                Hop hop = GameManager.getInstance().nextSolutionStep();

                // If there was a step
                if (hop != null) {
                    // Refreshing view and playing a hop sound
                    boardAdapter.updateHop(hop);
                    playSound(hopPlayer, R.raw.hop);
                } else {
                    // Alerting the user the solution is over
                    Toast.makeText(this, R.string.end_solution_msg, Toast.LENGTH_SHORT).show();
                }
            } else if (view.getId() == R.id.undo_button) {
                if (GameManager.getInstance().undoLastStep()) {
                    // Refreshing view
                    boardAdapter.updateBoard();
                } else {
                    // Alerting the user there are no previous steps
                    Toast.makeText(this, R.string.no_more_undo_msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showViewSolutionLayout(){
        solutionFrame.bringChildToFront(viewSolutionLayout);
        viewSolutionLayout.setVisibility(View.VISIBLE);
        solutionNavigationLayout.setVisibility(View.GONE);
    }

    private void showSolutionNavigationLayout(){
        solutionFrame.bringChildToFront(solutionNavigationLayout);
        viewSolutionLayout.setVisibility(View.GONE);
        solutionNavigationLayout.setVisibility(View.VISIBLE);
    }

    private void hideSolutionFrame(){
        solutionFrame.setVisibility(View.GONE);
        viewSolutionLayout.setVisibility(View.GONE);
        solutionNavigationLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGameDurationChanged(final String duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timePlayed.setText(duration);
            }
        });
    }

    @Override
    public void onLeafClicked(int leafIndex) {
        if(GameManager.getInstance().getLeaf(leafIndex).isValidHop()){
            Turn turnResult = GameManager.getInstance().hop(new LeafCoordinate(leafIndex));

            if(turnResult.getResult() == GameManager.TURN_RESULT.GAME_WON){
                // Indicating the game has won
                // (waiting until hop animation is over to move to game over activity)
                isGameOver = true;
                // Refreshing view
                boardAdapter.updateHop(turnResult.getHop());
                // Playing a losing sound
                playSound(hopPlayer, R.raw.winning);
                
                if(isBluetoothGame){
                    // Alerting the other user
                    new BluetoothFinishedSenderThread().start();
                }
            }
            else if(turnResult.getResult() == GameManager.TURN_RESULT.INVALID_HOP){
                Toast.makeText(this, R.string.invalid_hop_text, Toast.LENGTH_SHORT).show();
            }
            else if(turnResult.getResult() == GameManager.TURN_RESULT.HOP){
                // Refreshing view
                boardAdapter.updateHop(turnResult.getHop());
                // Playing a hop sound
                playSound(hopPlayer, R.raw.hop);
            }
        }else{
            // Setting the clicked leaf as selected and refreshing view
            GameManager.getInstance().setSelectedLeaf(leafIndex);
            boardAdapter.updateBoard();
        }
    }

    private void playSound(MediaPlayer player, int soundResId){
        // Checking if we need to stop last player
        if(player != null){
            player.stop();
            player.release();
        }

        player = MediaPlayer.create(this, soundResId);

        // When the player is prepared, playing the sound:

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mMediaPlayer) {
                mMediaPlayer.start();
            }
        });

        // When the player has finished, releasing it
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mMediaPlayer) {
                mMediaPlayer.release();
            }
        });
    }

    // Bluetooth events:

    @Override
    public void onDevicesListUpdated() {}
    @Override
    public void onNoOpponentResponse() {}
    @Override
    public void onGameStarted() {}
    @Override
    public void onGameRequestReceived(DIFFICULTY difficulty, String deviceName, int level) {}

    @Override
    public void onOpponentExited() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Displaying an error and pausing game
                displayBluetoothGameOverDialog(R.string.bluetooth_opponent_exited);
            }
        });
    }

    @Override
    public void onOpponentFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GameManager.getInstance().pauseGame();
                // Playing a losing sound
                playSound(hopPlayer, R.raw.losing_sound);

                // Moving to game over activity
                Intent gameOverActivity = new Intent(GameBoardActivity.this, GameOverActivity.class);
                gameOverActivity.putExtra(GameOverActivity.IS_WON_KEY, false);
                startActivity(gameOverActivity);
                finish();
            }
        });
    }

    @Override
    public void onConnectionChanged(final boolean isConnected) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isConnected && isBluetoothGame) {
                    // Displaying an error and pausing game
                    displayBluetoothGameOverDialog(R.string.unexpected_error);
                    GameManager.getInstance().pauseGame();
                }
            }
        });
    }

    private void displayBluetoothGameOverDialog(int dialogMessageId){
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.bluetooth_game_over_msg)
                .setTitle(dialogMessageId);

        // Add the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        // Get the AlertDialog and show it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onHopAnimationEnded() {
        // If the game is won
        if(isGameOver) {
            // Moving to game over activity with all the details of the winning
            Intent gameOverActivity = new Intent(GameBoardActivity.this, GameOverActivity.class);
            gameOverActivity.putExtra(GameOverActivity.IS_WON_KEY, true);
            // Checking if there is a next level to go to
            if (levelNum + 1 <= GameManager.getInstance().getMaxLevelId()) {
                gameOverActivity.putExtra(GameOverActivity.NEXT_LEVEL_ID_KEY, levelNum + 1);
            }
            gameOverActivity.putExtra(GameOverActivity.FINISHED_TIME_KEY, timePlayed.getText());
            startActivity(gameOverActivity);
            finish();
        }
    }

    /**
     * Sending exit cause the user has left the activity,
     * doing it in a separated thread to avoid doing bluetooth actions in UI thread
     */
    private class BluetoothExitSenderThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                BluetoothConnectionHandler.getInstance().sendExit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sending finished cause the user has finished the level,
     * doing it in a separated thread to avoid doing bluetooth actions in UI thread
     */
    private class BluetoothFinishedSenderThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                BluetoothConnectionHandler.getInstance().sendFinished();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
