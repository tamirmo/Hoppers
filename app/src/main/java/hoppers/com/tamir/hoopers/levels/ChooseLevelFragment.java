package hoppers.com.tamir.hoopers.levels;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

import hoppers.com.tamir.hoopers.R;
import logic.DIFFICULTY;
import logic.GameManager;

/**
 * Created by Tamir on 04/03/2018.
 * A Fragment for choosing a level in a given difficulty.
 */

public class ChooseLevelFragment extends Fragment implements IOnLevelClicked {

    private DIFFICULTY difficulty;
    private LevelsAdapter adapter;
    private IOnLevelClicked levelClickedListener;

    void setDifficulty(DIFFICULTY difficulty){
        this.difficulty = difficulty;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // The listener if the levels activity
        if(context instanceof IOnLevelClicked){
            levelClickedListener = (IOnLevelClicked) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_choose_level, container, false);

        RecyclerView levelsRecyclerView = root.findViewById(R.id.levels_recycler_view);

        // Initializing the adapter with a list of the given difficulty's levels
        //and a click listener
        this.adapter = new LevelsAdapter(getContext(),
                GameManager.getInstance().getLevelsByDifficulty(difficulty),
                this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        levelsRecyclerView.setLayoutManager(mLayoutManager);
        levelsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        levelsRecyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refreshing the levels for when returning from game
        // and we have new records or levels viewed
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLevelClicked(int levelNum) {
        if(levelClickedListener != null){
            levelClickedListener.onLevelClicked(levelNum);
        }
    }
}
