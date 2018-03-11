package logic;

/**
 * This class represents a leaf's cell coordinates (row and column)
 */

public class LeafCoordinate{
    private final static String COORDINATE_SEPARATOR = "#";

    private int row;
    private int column;

    void setRow(int row){
        this.row = row;
    }

    void setColumn(int column){
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    LeafCoordinate(){}

    LeafCoordinate(int row, int column){
        this.row = row;
        this.column = column;
    }

    public LeafCoordinate(int cellIndex){
        int indexCopy = cellIndex, currRow = 0;

        // Subtracting the columns in each row until reaching zero
        while (indexCopy >= 0){
            if(currRow %2 == 0){
                indexCopy -= 3;
            }
            else{
                indexCopy -= 2;
            }
            currRow ++;
        }

        // The last increment was wrong
        currRow--;

        // To get the column, we need to add the last row's count:

        if(currRow %2 == 0){
            indexCopy += 3;
        }
        else{
            indexCopy += 2;
        }

        this.row = currRow;
        this.column = indexCopy;
    }

    LeafCoordinate(String coordinateString){
        if(coordinateString != null &&
                coordinateString.length() == 3){
            String[] splitCoordinate = coordinateString.split(COORDINATE_SEPARATOR);
            if(splitCoordinate.length == 2){
                row = Integer.parseInt(splitCoordinate[0]);
                column = Integer.parseInt(splitCoordinate[1]);
            }
        }
    }

    public int getCellIndex(){
        return getCellIndex(row, column);
    }

    /**
     * This method calculates the one dimensional index of the cell
     * in the given row and column
     * @param row The row of the cell
     * @param column The column of the cell
     * @return the one dimension index of the given cell
     */
    public static int getCellIndex(int row, int column){
        int index = 0;

        // Adding all the cells in the previous rows
        for(int i = 0; i < row; i ++){
            // Even rows has 3 columns
            if(i % 2 == 0){
                index += 3;
            }
            // Odd rows have 2 columns
            else{
                index += 2;
            }
        }

        index += column;

        return index;
    }

    @Override
    public String toString() {
        return row + COORDINATE_SEPARATOR + column;
    }
}
