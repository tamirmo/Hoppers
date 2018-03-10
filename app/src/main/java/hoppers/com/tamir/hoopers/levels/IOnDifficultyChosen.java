package hoppers.com.tamir.hoopers.levels;

import logic.DIFFICULTY;

/**
 * Created by Tamir on 04/03/2018.
 * Event class for when the user chooses a difficulty.
 */

public interface IOnDifficultyChosen {
    void onDifficultyChosen(DIFFICULTY difficultyChosen);
}
