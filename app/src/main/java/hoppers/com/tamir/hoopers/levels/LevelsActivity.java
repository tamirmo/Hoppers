package hoppers.com.tamir.hoopers.levels;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import hoppers.com.tamir.hoopers.HomeScreen;
import hoppers.com.tamir.hoopers.R;
import hoppers.com.tamir.hoopers.board.GameBoardActivity;
import logic.DIFFICULTY;

public class LevelsActivity extends FragmentActivity implements IOnDifficultyChosen, IOnLevelClicked{

    private ChooseDifficultyFragment chooseDifficultyFragment;
    private ChooseLevelFragment chooseLevelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        // Creating the two fragments:
        chooseDifficultyFragment = new ChooseDifficultyFragment();
        chooseLevelFragment = new ChooseLevelFragment();

        // Add the fragment to the 'fragment_container' and adding to back stack to be able to get back to
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_holder, chooseDifficultyFragment)
                .commit();
    }

    @Override
    public void onDifficultyChosen(DIFFICULTY difficultyChosen) {
        // Updating the levels fragment
        chooseLevelFragment.setDifficulty(difficultyChosen);

        // Add the fragment to the 'fragment_container'
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_holder, chooseLevelFragment)
                .addToBackStack(null)
                .commit();
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }

    @Override
    public void onLevelClicked(int levelNum) {
        Intent gameBoardActivity = new Intent(this, GameBoardActivity.class);
        gameBoardActivity.putExtra(GameBoardActivity.LEVEL_NUM_KEY, levelNum);
        startActivity(gameBoardActivity);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }
}
