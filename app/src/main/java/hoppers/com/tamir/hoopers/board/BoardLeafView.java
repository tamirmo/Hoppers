package hoppers.com.tamir.hoopers.board;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import hoppers.com.tamir.hoopers.R;
import logic.GameManager;
import logic.Leaf;

/**
 * Created by Tamir on 09/03/2018.
 * A class for a view of a leaf on the board.
 */

public class BoardLeafView extends RelativeLayout implements View.OnClickListener {
    private ImageView frog;
    private ImageView leafImage;
    private int index;
    // The angle to rotate the leaf
    private float leafRotation;
    private IOnLeafClicked leafClickedListener;

    void setLeafClickedListener(IOnLeafClicked leafClickedListener){
        this.leafClickedListener = leafClickedListener;
    }

    public BoardLeafView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BoardLeafView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Random an angle to rotate the leaf
        leafRotation = (float) randInt(180);

        inflate(getContext(), R.layout.board_leaf_layout, this);
        this.frog = (ImageView) findViewById(R.id.board_leaf_frog);
        this.leafImage = (ImageView) findViewById(R.id.board_leaf);

        // Registering to the leaf click event
        findViewById(R.id.board_leaf_maim_layout).setOnClickListener(this);

        // Pulling the index of the leaf
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BoardLeafView,
                0, 0);

        try {
            index = a.getInteger(R.styleable.BoardLeafView_index, 0);
        } finally {
            a.recycle();
        }
    }

    void update(){
        Leaf leaf = GameManager.getInstance().getLeaf(index);

        switch (leaf.getType()){
            case EMPTY:
                frog.setVisibility(GONE);
                break;
            case GREEN_FROG:
                frog.setVisibility(VISIBLE);
                frog.setImageResource(R.drawable.green_frog_board);
                break;
            case RED_FROG:
                frog.setVisibility(VISIBLE);
                frog.setImageResource(R.drawable.red_frog_board);
                break;
        }

        if (leaf.isValidHop()){
            leafImage.setImageResource(R.drawable.board_leaf_a_hoppable);
        } else{
            leafImage.setImageResource(R.drawable.board_leaf_a);
        }
    }

    /**
     * Returns a random number between 0 and max, inclusive.
     *
     * @param max Maximum value.
     * @return Integer between 0 and max, inclusive.
     */
    private int randInt(int max) {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max) + 1);
    }

    @Override
    public void onClick(View v) {
        if(leafClickedListener != null){
            leafClickedListener.onLeafClicked(index);
        }
    }
}
