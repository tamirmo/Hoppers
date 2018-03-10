package hoppers.com.tamir.hoopers.board;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TableLayout;

import logic.GameManager;
import logic.Hop;
import logic.LEAF_TYPE;
import logic.LeafCoordinate;

/**
 * Created by Tamir on 10/03/2018.
 * This class handles the leaves views according to the game state.
 */

public class BoardAdapter implements IOnLeafClicked {
    private TableLayout gameTable;
    private BoardLeafView[] leaves;
    private Context context;
    private IOnLeafClicked leafClickedListener;

    BoardAdapter(TableLayout board, Context context){
        this.context = context;
        // GameBoardActivity should implement IOnLeafClicked
        if(context instanceof IOnLeafClicked){
            this.leafClickedListener = (IOnLeafClicked)context;
        }

        gameTable = board;

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

    void updateHop(Hop hop){
        updateBoard();
    }

    @Override
    public void onLeafClicked(int leafIndex) {
        if(leafClickedListener != null){
            leafClickedListener.onLeafClicked(leafIndex);
        }
    }
}
