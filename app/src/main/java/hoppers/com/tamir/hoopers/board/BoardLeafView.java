package hoppers.com.tamir.hoopers.board;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import hoppers.com.tamir.hoopers.R;
import logic.GameManager;
import logic.Leaf;

/**
 * Created by Tamir on 09/03/2018.
 * A class for a view of a leaf on the board.
 */

public class BoardLeafView extends RelativeLayout implements View.OnClickListener {
    private static final int FADE_OUT_ANIM_DURATION = 200;

    private ImageView frog;
    private ImageView leafImage;
    private int index;
    private IOnLeafClicked leafClickedListener;
    // Indicating if the frog was just eaten
    private boolean isFrogEaten;

    /**
     * Called in a case when the frog in this leaf has been eaten
     * (to display an animation)
     */
    void setFrogEaten(){
        isFrogEaten = true;
    }

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
        inflate(getContext(), R.layout.board_leaf_layout, this);
        this.frog = (ImageView) findViewById(R.id.board_leaf_frog);
        this.leafImage = (ImageView) findViewById(R.id.board_leaf);

        // Registering to the leaf click event
        findViewById(R.id.board_leaf_maim_layout).setOnClickListener(this);

        // Pulling the index of the leaf:

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
                updateEmptyLeaf();
                break;
            case GREEN_FROG:
                frog.setVisibility(VISIBLE);
                if(leaf.isSelected()){
                    frog.setImageResource(R.drawable.green_frog_board_selected);
                }else {
                    frog.setImageResource(R.drawable.green_frog_board);
                }
                break;
            case RED_FROG:
                frog.setVisibility(VISIBLE);
                if(leaf.isSelected()){
                    frog.setImageResource(R.drawable.red_frog_board_selected);
                }else {
                    frog.setImageResource(R.drawable.red_frog_board);
                }
                break;
        }

        if (leaf.isValidHop()){
            leafImage.setImageResource(R.drawable.board_leaf_a_hoppable);
        } else{
            leafImage.setImageResource(R.drawable.board_leaf_a);
        }
    }

    private void updateEmptyLeaf() {
        if(frog.getVisibility() != GONE){
            // Checking if we need to animate an eating frog
            if(isFrogEaten) {
                // Animate the frog to 0% opacity. After the animation ends,
                // set its visibility to GONE and back the alpha for next time.
                frog.animate()
                        .alpha(0f)
                        .setDuration(FADE_OUT_ANIM_DURATION)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                frog.setVisibility(View.GONE);
                                // Setting back the alpha for next time
                                frog.setAlpha(1f);
                            }
                        });
            } else{
                // No animation needed
                frog.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Hiding the frog before updating the leaf when stat
     */
    void hideFrog(){
        frog.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        if(leafClickedListener != null){
            leafClickedListener.onLeafClicked(index);
        }
    }
}
