package hoppers.com.tamir.hoopers.end;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hoppers.com.tamir.hoopers.R;
import hoppers.com.tamir.hoopers.board.GameBoardActivity;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String NEXT_LEVEL_ID_KEY = "NEXT_LEVEL_ID";
    public static final String IS_WON_KEY = "IS_WON";
    public static final String FINISHED_TIME_KEY = "FINISHED_TIME";
    private static final int NO_NEXT_LEVEL_ID = -1;

    private ImageView gameOverImage;
    private TextView gameOverText;
    private LinearLayout nextLevelLayout;
    private LinearLayout finishedTimeLayout;
    private TextView nextLevelIdText;
    private TextView finishedTimeText;
    private int nextLevelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        gameOverImage = (ImageView) findViewById(R.id.game_over_image);
        gameOverText = (TextView) findViewById(R.id.game_over_text);
        nextLevelLayout = (LinearLayout) findViewById(R.id.next_level_layout);
        finishedTimeLayout = (LinearLayout) findViewById(R.id.finished_time_layout);
        nextLevelIdText = (TextView) findViewById(R.id.next_level_id);
        finishedTimeText = (TextView) findViewById(R.id.finished_time);
        (findViewById(R.id.next_level_button)).setOnClickListener(this);

        nextLevelId = getIntent().getExtras().getInt(NEXT_LEVEL_ID_KEY, NO_NEXT_LEVEL_ID);
        boolean isWon = getIntent().getExtras().getBoolean(IS_WON_KEY, false);
        String finishedTime = getIntent().getExtras().getString(FINISHED_TIME_KEY, "");

        if (isWon){
            setWinningView(finishedTime);
        }else {
            setLosingView();
        }
    }

    private void setLosingView(){
        gameOverImage.setImageResource(R.drawable.crying_green_frog);
        gameOverText.setText(R.string.losing_text);
        nextLevelLayout.setVisibility(View.GONE);
        finishedTimeLayout.setVisibility(View.GONE);
    }

    private void setWinningView(String finishedTime){
        gameOverImage.setImageResource(R.drawable.green_frog_game_over_winner);
        gameOverText.setText(R.string.won_text);

        // Checking if there is a next level
        if(nextLevelId != NO_NEXT_LEVEL_ID) {
            // Showing next level layout with the next level id
            nextLevelLayout.setVisibility(View.VISIBLE);
            nextLevelIdText.setText(String.valueOf(nextLevelId));
        }

        // Setting the finished time for the ended game
        finishedTimeLayout.setVisibility(View.VISIBLE);
        finishedTimeText.setText(finishedTime);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.next_level_button){
            // Move to game activity with next level
            Intent gameBoardActivity = new Intent(this, GameBoardActivity.class);
            gameBoardActivity.putExtra(GameBoardActivity.LEVEL_NUM_KEY, nextLevelId);
            startActivity(gameBoardActivity);
            finish();
        }
    }
}
