package hoppers.com.tamir.hoopers.levels;

import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hoppers.com.tamir.hoopers.R;
import hoppers.com.tamir.hoopers.databinding.LevelItemBinding;
import logic.DIFFICULTY;
import logic.Level;

/**
 * Created by Tamir on 04/03/2018.
 * An adapter for the levels displayed after choosing difficulty.
 */

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelViewHolder> implements View.OnClickListener {
    private List<Level> levels;
    private IOnLevelClicked levelClickedListener;

    LevelsAdapter(List<Level> levels, IOnLevelClicked levelClickedListener){
        this.levels = levels;
        this.levelClickedListener = levelClickedListener;
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        //View itemView = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        //View itemView = LayoutInflater.from(parent.getContext())
        //        .inflate(R.layout.level_item, parent, false);

        LevelItemBinding levelItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.level_item, parent, false);

        return new LevelViewHolder(levelItemBinding.getRoot(), levelItemBinding);
    }

    public static int GetDifficultyBackgroundResourceId(DIFFICULTY difficulty)
    {
        int levelBackground = R.drawable.beginner_background;
        switch (difficulty){
            case ADVANCED:
                levelBackground = R.drawable.advanced_background;
                break;
            case EXPERT:
                levelBackground = R.drawable.expert_background;
                break;
            case INTERMEDIATE:
                levelBackground = R.drawable.intermediate_background;
                break;
        }

        return levelBackground;
    }

    @Override
    public void onBindViewHolder(LevelViewHolder levelViewHolder, int position) {
        // Getting the level in this position
        Level level = levels.get(position);

        levelViewHolder.bind(level);

        levelViewHolder.mainLayout.setOnClickListener(this);
        levelViewHolder.mainLayout.setTag(level.getId());
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    @Override
    public void onClick(View v) {
        if(this.levelClickedListener != null){
            levelClickedListener.onLevelClicked((int)v.getTag());
        }
    }

    class LevelViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout mainLayout;
        final LevelItemBinding levelItemBinding;

        LevelViewHolder(View view, LevelItemBinding levelItemBinding) {
            super(view);
            this.mainLayout = view.findViewById(R.id.level_item_main_layout);
            this.levelItemBinding = levelItemBinding;
        }

        void bind(Level level) {
            levelItemBinding.setLevel(level);
            levelItemBinding.executePendingBindings();
        }
    }
}

