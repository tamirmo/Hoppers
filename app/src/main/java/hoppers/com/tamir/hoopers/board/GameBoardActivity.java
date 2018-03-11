package hoppers.com.tamir.hoopers.board;

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

import hoppers.com.tamir.hoopers.R;
import logic.GameManager;
import logic.Hop;
import logic.IOnGameDurationChanged;
import logic.LeafCoordinate;
import logic.Turn;

public class GameBoardActivity extends AppCompatActivity implements View.OnClickListener, IOnGameDurationChanged, IOnLeafClicked {

    public static final String LEVEL_NUM_KEY = "LEVEL_NUM";
    public static final String IS_BLUETOOTH_GAME_KEY = "IS_BLUETOOTH_GAME_KEY";



    private TextView levelText;
    private ImageButton viewSolutionBtn;
    private TableLayout gameGrid;
    private TextView timePlayed;
    private FrameLayout solutionFrame;
    private RelativeLayout solutionNavigationLayout;
    private LinearLayout viewSolutionLayout;
    private ImageButton undoButton;
    private LinearLayout timePlayedLayout;
    private BoardAdapter boardAdapter;

    private int levelNum;
    private boolean isBluetoothGame;
// Reducing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        // Getting the level number and if the game is a bluetooth game or single
        levelNum = getIntent().getExtras().getInt(LEVEL_NUM_KEY, 0);
        isBluetoothGame = getIntent().getExtras().getBoolean(IS_BLUETOOTH_GAME_KEY, false);

        levelText = (TextView) findViewById(R.id.level_name);
        viewSolutionBtn = (ImageButton) findViewById(R.id.view_solution_btn);
        viewSolutionLayout = (LinearLayout) findViewById(R.id.view_solution_layout);
        gameGrid = (TableLayout) findViewById(R.id.game_board);
        timePlayed = (TextView) findViewById(R.id.time_played_txt);
        solutionFrame = (FrameLayout) findViewById(R.id.solution_frame);
        solutionNavigationLayout = (RelativeLayout) findViewById(R.id.solution_navigation_layout);
        undoButton = (ImageButton) findViewById(R.id.undo_button);
        timePlayedLayout = (LinearLayout) findViewById(R.id.time_played_layout);

        findViewById(R.id.previous_solution_step_btn).setOnClickListener(this);
        findViewById(R.id.next_solution_step_btn).setOnClickListener(this);
        undoButton.setOnClickListener(this);

        // Setting the level number
        levelText.setText(String.valueOf(levelNum));
        viewSolutionBtn.setOnClickListener(this);

        // Registering to the event to display the durations
        GameManager.getInstance().setGameDurationChangedListener(this);
        GameManager.getInstance().startGame(levelNum, isBluetoothGame);

        // Initializing the adapter and refreshing the view according to this level
        boardAdapter = new BoardAdapter(gameGrid, this,
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
        // TODO: Resume game only if not running
        GameManager.getInstance().resumeGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: close the bluetooth game
        GameManager.getInstance().pauseGame();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.view_solution_btn){
            // TODO: Update db of the solution viewed
            GameManager.getInstance().startShowSolution();
            // No need to display time anymore
            timePlayedLayout.setVisibility(View.GONE);

            showSolutionNavigationLayout();

            // When viewing solution,
            // undo button should not be active
            undoButton.setVisibility(View.GONE);

            // Refreshing view cause we are back to the beginning
            boardAdapter.updateBoard();
        } else if(view.getId() == R.id.previous_solution_step_btn){
            Hop hop = GameManager.getInstance().prevSolutionStep();

            // Refreshing view
            boardAdapter.updateBoard();
        }
        else if(view.getId() == R.id.next_solution_step_btn){
            Hop hop = GameManager.getInstance().nextSolutionStep();

            // If there was a step
            if(hop != null) {
                // Refreshing view
                boardAdapter.updateHop(hop);
            }
        }
        else if(view.getId() == R.id.undo_button){
            GameManager.getInstance().undoLastStep();
            // Refreshing view
            boardAdapter.updateBoard();
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
                Toast.makeText(this, "WON!!!!!", Toast.LENGTH_SHORT).show();
                // Refreshing view
                boardAdapter.updateHop(turnResult.getHop());
            }
            else if(turnResult.getResult() == GameManager.TURN_RESULT.INVALID_HOP){
                Toast.makeText(this, "Invalid hop", Toast.LENGTH_SHORT).show();
            }
            else if(turnResult.getResult() == GameManager.TURN_RESULT.HOP){
                // Refreshing view
                boardAdapter.updateHop(turnResult.getHop());
            }
        }else{
            // Setting the clicked leaf as selected and refreshing view
            GameManager.getInstance().setSelectedLeaf(leafIndex);
            boardAdapter.updateBoard();
        }
    }
}
