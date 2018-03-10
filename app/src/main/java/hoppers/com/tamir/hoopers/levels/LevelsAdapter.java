package hoppers.com.tamir.hoopers.levels;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import hoppers.com.tamir.hoopers.R;
import logic.Level;

/**
 * Created by Tamir on 04/03/2018.
 * An adapter for the levels displayed after choosing difficulty.
 */

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelViewHolder> implements View.OnClickListener {
    private List<Level> levels;
    private IOnLevelClicked levelClickedListener;
    private Context context;

    public LevelsAdapter(Context context, List<Level> levels, IOnLevelClicked levelClickedListener){
        this.levels = levels;
        this.levelClickedListener = levelClickedListener;
        this.context = context;
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_item, parent, false);

        return new LevelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LevelViewHolder levelViewHolder, int position) {
        // Getting the level in this position
        Level level = levels.get(position);

        // Setting the level number
        levelViewHolder.number.setText(String.valueOf(level.getId()));

        // If the solution is viewed, displaying the icon
        if(level.isSolutionViewed()) {
            levelViewHolder.solutionViewed.setVisibility(View.VISIBLE);
        }else{
            levelViewHolder.solutionViewed.setVisibility(View.INVISIBLE);
        }

        // Setting frog icon according to the level solved indicator
        if(level.isSolved()){
            levelViewHolder.frog.setImageResource(R.drawable.green_frog_levels_winner);
            levelViewHolder.record.setVisibility(View.VISIBLE);
            levelViewHolder.record.setText(context.getString(R.string.record) +
                    level.getRecordHours() + ":" + level.getRecordMinutes() + ":" + level.getRecordSeconds());
        }else{
            levelViewHolder.frog.setImageResource(R.drawable.green_frog_levels);
            levelViewHolder.record.setVisibility(View.INVISIBLE);

        }

        // Setting the background of the main layout according to the level:

        int levelBackground = R.drawable.beginner_background;
        switch (level.getDifficulty()){
            case ADVANCED:
                levelBackground = R.drawable.advanced_background;
                break;
            case BEGINNER:
                levelBackground = R.drawable.beginner_background;
                break;
            case EXPERT:
                levelBackground = R.drawable.expert_background;
                break;
            case INTERMEDIATE:
                levelBackground = R.drawable.intermediate_background;
                break;
        }
        levelViewHolder.mainLayout.setBackgroundResource(levelBackground);
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

    public class LevelViewHolder extends RecyclerView.ViewHolder {
        public TextView number, record;
        public ImageView solutionViewed, frog;
        public RelativeLayout mainLayout;

        public LevelViewHolder(View view) {
            super(view);
            solutionViewed = view.findViewById(R.id.solution_viewed_image);
            number = view.findViewById(R.id.level_name);
            frog = view.findViewById(R.id.level_frog);
            record = view.findViewById(R.id.level_record);
            mainLayout = view.findViewById(R.id.level_item_main_layout);

        }
    }
}

