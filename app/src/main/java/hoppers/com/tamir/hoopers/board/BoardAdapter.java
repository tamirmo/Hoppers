package hoppers.com.tamir.hoopers.board;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;

import logic.GameManager;
import logic.Hop;
import logic.LEAF_TYPE;
import logic.LeafCoordinate;

/**
 * Created by Tamir on 10/03/2018.
 * This class handles the leaves views according to the game state.
 */

class BoardAdapter implements IOnLeafClicked {
    // The time to animate a hop
    private final int HOP_ANIMATION_DURATION_MS = 600;

    private TableLayout gameTable;
    private BoardLeafView[] leaves;
    private IOnLeafClicked leafClickedListener;
    // Frogs used for the hopping animation:
    private ImageView floatingGreenFrog;
    private ImageView floatingRedFrog;

    BoardAdapter(TableLayout board, IOnLeafClicked leafClickedListener,
                 ImageView floatingGreenFrog, ImageView floatingRedFrog){
        // GameBoardActivity should implement IOnLeafClicked
        this.leafClickedListener = leafClickedListener;

        gameTable = board;
        this.floatingGreenFrog = floatingGreenFrog;
        this.floatingRedFrog = floatingRedFrog;

        // TODO: 13 as const, maybe in coords?
        leaves = new BoardLeafView[13];

        // Gettign all leaves from the table
        LeafCoordinate cord;
        for(int i = 0 ; i < leaves.length; i ++){
            cord = new LeafCoordinate(i);
            leaves[i] =
                    (BoardLeafView) ((ViewGroup)gameTable.getChildAt(cord.getRow())).getChildAt(cord.getColumn());
            leaves[i].setLeafClickedListener(this);
        }
    }

    /**
     * Updating the view of each leaf in the board
     */
    void updateBoard(){
        for (BoardLeafView leave : leaves) {
            leave.update();
        }
    }

    /**
     * Refreshing the board after a hop
     * @param hop The hop that just happened
     */
    void updateHop(Hop hop){
        int origFrogLeafIndex =
            hop.getFrogOriginalLeaf().getCellIndex();
        int destFrogLeafIndex =
            hop.getFrogHoppedLeaf().getCellIndex();
        int eatenFrogLeafIndex =
                hop.getEatenFrogLeaf().getCellIndex();

        BoardLeafView origFrogView = leaves[origFrogLeafIndex];
        BoardLeafView destFrogView = leaves[destFrogLeafIndex];
        BoardLeafView eatenFrogView = leaves[eatenFrogLeafIndex];

        animateHop(hop,
                origFrogView,
                destFrogView,
                eatenFrogView);
    }

    private void animateHop(Hop hop,
                            BoardLeafView origFrogView,
                            BoardLeafView destFrogView,
                            BoardLeafView eatenFrogView){
        // Updating the leaf view to animate the set visibility
        eatenFrogView.setFrogEaten();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // First of all, hiding the frog hopping
            origFrogView.hideFrog();

            // Checking which frog we need to animate:

            View frogToAnimate = floatingGreenFrog;
            // Getting the hopping frog's type
            LEAF_TYPE originalLeafType =
                    GameManager.getInstance().getLeaf(hop.getFrogHoppedLeaf().getCellIndex()).getType();
            // Checking if we need to animate a red frog
            if(originalLeafType == LEAF_TYPE.RED_FROG){
                frogToAnimate = floatingRedFrog;
            }

            // Bringing the animating frog to the front of all layout and displaying it:
            frogToAnimate.bringToFront();
            frogToAnimate.setVisibility(View.VISIBLE);

            // Getting the destination and source leaves coordinates
            int origLocation[] = new int[2];
            int destLocation[] = new int[2];
            origFrogView.getLocationOnScreen(origLocation);
            destFrogView.getLocationOnScreen(destLocation);

            // Getting the path of the animation according to the hop
            Path path = getPath(hop, origFrogView, origLocation, destLocation);

            // Applying the animation to the animating frog for the configured time:
            ObjectAnimator animator = ObjectAnimator.ofFloat(frogToAnimate, View.X, View.Y, path);
            animator.setDuration(HOP_ANIMATION_DURATION_MS);

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    // After finishing,
                    // Updating the board and hiding the frog of the animation
                    updateBoard();
                    floatingGreenFrog.setVisibility(View.GONE);
                    floatingRedFrog.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });

            animator.start();
        }else{
            updateBoard();
        }
    }

    /**
     * This method gets a path animating the given hop
     * @param hop The hop to animate
     * @param origFrogView The original frog's leaf
     * @param origFrogLocation The location of the original frog leaf on screen
     * @param destFrogLocation The location of the destination leaf on screen
     * @return The path of the frog to animate
     */
    @NonNull
    private Path getPath(Hop hop, BoardLeafView origFrogView, int[] origFrogLocation, int[] destFrogLocation) {
        Path path = new Path();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Creating a path according to the hop type:
            switch (hop.getHopType()) {
                case DOWN:
                    path.arcTo(origFrogLocation[0] - (2) * origFrogView.getWidth(),
                            origFrogLocation[1] - origFrogView.getHeight() / 2,
                            destFrogLocation[0] + origFrogView.getWidth() * 2,
                            destFrogLocation[1] - origFrogView.getHeight() / 2,
                            -90,
                            -180f, true);
                    break;
                case LEFT:
                    path.arcTo(destFrogLocation[0],
                            origFrogLocation[1] - (2) * origFrogView.getHeight(),
                            origFrogLocation[0],
                            destFrogLocation[1] + origFrogView.getHeight(),
                            0,
                            -180f, true);
                    break;
                case RIGHT:
                    path.arcTo(origFrogLocation[0],
                            origFrogLocation[1] - (2) * origFrogView.getHeight(),
                            destFrogLocation[0],
                            destFrogLocation[1] + origFrogView.getHeight(),
                            -180f,
                            180f, true);
                    break;
                case UP:
                    path.arcTo(origFrogLocation[0] - origFrogView.getWidth(),
                            destFrogLocation[1] - origFrogView.getHeight() / 2,
                            origFrogLocation[0] + 2 * origFrogView.getWidth(),
                            origFrogLocation[1] - origFrogView.getHeight() / 2,
                            -270,
                            180f, true);
                    break;
                case LEFT_DOWN:
                    path.arcTo(destFrogLocation[0] + origFrogView.getWidth()/2,
                            origFrogLocation[1] - origFrogView.getHeight(),
                            origFrogLocation[0] + origFrogView.getWidth() / 2,
                            destFrogLocation[1],
                            -45f,
                            180f, true);
                    break;
                case LEFT_UP:
                    path.arcTo(destFrogLocation[0] - origFrogView.getWidth() / 2,
                            destFrogLocation[1] - origFrogView.getHeight() ,
                            origFrogLocation[0] + origFrogView.getWidth() / 2,
                            origFrogLocation[1],
                            45f,
                            -180f, true);
                    break;
                case RIGHT_DOWN:
                    path.arcTo(origFrogLocation[0] - origFrogView.getWidth() / 2,
                            origFrogLocation[1] - origFrogView.getHeight(),
                            destFrogLocation[0] + origFrogView.getWidth() / 2,
                            destFrogLocation[1],
                            -135f,
                            180f, true);
                    break;
                case RIGHT_UP:
                    path.arcTo(origFrogLocation[0] + origFrogView.getWidth() / 4,
                            destFrogLocation[1] - origFrogView.getHeight(),
                            destFrogLocation[0] + origFrogView.getWidth() / 4,
                            origFrogLocation[1],
                            -225f,
                            180f, true);
                    break;
            }
        }
        return path;
    }

    @Override
    public void onLeafClicked(int leafIndex) {
        if(leafClickedListener != null){
            leafClickedListener.onLeafClicked(leafIndex);
        }
    }
}
