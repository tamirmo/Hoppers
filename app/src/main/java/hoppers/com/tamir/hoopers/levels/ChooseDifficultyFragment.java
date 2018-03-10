package hoppers.com.tamir.hoopers.levels;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hoppers.com.tamir.hoopers.R;
import logic.DIFFICULTY;

/**
 * Created by Tamir on 04/03/2018.
 * A fragment for choosing the difficulty before moving on choosing level.
 */

public class ChooseDifficultyFragment extends Fragment implements View.OnClickListener {

    private IOnDifficultyChosen difficultyChosenListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // The activity (LevelsActivity) is the listener
        if(context instanceof IOnDifficultyChosen){
            this.difficultyChosenListener = (IOnDifficultyChosen)context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_choose_difficulty, container, false);

        root.findViewById(R.id.beginner_layout).setOnClickListener(this);
        root.findViewById(R.id.intermediate_layout).setOnClickListener(this);
        root.findViewById(R.id.advanced_layout).setOnClickListener(this);
        root.findViewById(R.id.expert_layout).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        DIFFICULTY difficultyChosen = DIFFICULTY.BEGINNER;
        switch (v.getId()){
            case R.id.beginner_layout:
                difficultyChosen = DIFFICULTY.BEGINNER;
                break;
            case R.id.intermediate_layout:
                difficultyChosen = DIFFICULTY.INTERMEDIATE;
                break;
            case R.id.advanced_layout:
                difficultyChosen = DIFFICULTY.ADVANCED;
                break;
            case R.id.expert_layout:
                difficultyChosen = DIFFICULTY.EXPERT;
                break;
        }

        // Alerting the activity of the difficulty chosen
        if(difficultyChosenListener != null){
            difficultyChosenListener.onDifficultyChosen(difficultyChosen);
        }
    }
}
