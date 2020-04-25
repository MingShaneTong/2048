import java.util.Arrays;

public class Board {
    /* The board object that stores the board information and changes the board based on input */
    private int[][] boardGrid;
    private int boardSize;

    Board(int size){
        /* Initialises the board */
        boardSize = size;
        boardGrid = new int[size][size];
    }

    private int getBoardSize(){ return this.boardSize; }
    private int[][] getBoardGrid(){ return this.boardGrid; }
    private void setBoardGrid(int[][] newGrid) { this.boardGrid = newGrid; }

    public void add_tile(){
        /* Adds a 2 or 4 tile randomly on empty tile */
        int x = (int) (Math.random() * this.getBoardSize());
        int y = (int) (Math.random() * this.getBoardSize());
        // tile is taken, new coor
        while (!tileIsEmpty(y, x)){
            x = (int) (Math.random() * this.getBoardSize());
            y = (int) (Math.random() * this.getBoardSize());
        }

        int value = Math.random() < 0.9 ? 2 : 4;

        // change the value on grid
        this.getBoardGrid()[y][x] = value;
    }

    private boolean tileIsEmpty(int y, int x){ return this.getTile(y, x) == 0;}
    private int getTile(int y, int x){ return this.getBoardGrid()[y][x]; }

    public String boardBuilder(int cellSize){
        /* displays the board to the screen */
        StringBuilder boardString = new StringBuilder();
        for(int[] row : this.getBoardGrid()){
            boardString.append(" | ");
            for(int cell : row){
                // if the cell is empty
                if(cell == 0){ boardString.append(rjust("", cellSize)); }
                // numbers
                else { boardString.append(rjust(Integer.toString(cell), cellSize)); }
                boardString.append(" | ");
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }
    private String rjust(String str, int length){
        /* Put string on right of a standard sized block of spaces */
        StringBuilder outputString = new StringBuilder();
        outputString.append(" ".repeat(length - str.length()));     // add spaces
        outputString.append(str);           // add string
        return outputString.toString();
    }

    public boolean swipe(String direction, boolean apply){
        /* changes the board based on swipe and returns if the board has changed */
        int[][] outputBoard = new int[this.getBoardSize()][this.getBoardSize()];

        // turn grid
        int[][] turnedGrid = new int[this.getBoardSize()][this.getBoardSize()];
        switch (direction){
            case "W":
                // flip grid along the downwards diagonal
                for(int i = 0; i < this.getBoardSize(); i++){
                    for(int j = 0; j < this.getBoardSize(); j++){
                        turnedGrid[i][j] = this.getBoardGrid()[j][i];
                    }
                }
                break;
            case "A":
                // copy grid to turned grid
                for(int i = 0; i < this.getBoardSize(); i++){
                    System.arraycopy(this.getBoardGrid()[i], 0, turnedGrid[i], 0, this.getBoardSize());
                }
                break;
            case "S":
                // flip along vertical axis
                turnedGrid = new int[this.getBoardSize()][this.getBoardSize()];
                for(int i = 0; i < this.getBoardSize(); i++){
                    for(int j = 0; j < this.getBoardSize(); j++){
                        turnedGrid[this.getBoardSize() - 1 - i][this.getBoardSize() - 1 - j] = this.getBoardGrid()[j][i];
                    }
                }
                break;
            case "D":
                // flip grid along upwards diagonal
                for(int i = 0; i < this.getBoardSize(); i++){
                    turnedGrid[i] = this.reverseRow(this.getBoardGrid()[i].clone());
                }
                break;
        }

        // swipe each row of the turned grid
        int[][] turnedGridSwiped = new int[this.getBoardSize()][this.getBoardSize()];
        for(int i = 0; i < this.getBoardSize(); i++){
            turnedGridSwiped[i] = this.rowSwipe(turnedGrid[i]);
        }

        // switch back if necessary
        switch (direction){
            case "W":
                // switch back
                for(int i = 0; i < this.getBoardSize(); i++){
                    for(int j = 0; j < this.getBoardSize(); j++){
                        outputBoard[i][j] = turnedGridSwiped[j][i];
                    }
                }
                break;
            case "A":
                // switch back
                for(int i = 0; i < this.getBoardSize(); i++){
                    System.arraycopy(turnedGridSwiped[i], 0, outputBoard[i], 0, this.getBoardSize());
                }
                break;
            case "S":
                // switch back
                for(int i = 0; i < this.getBoardSize(); i++){
                    for(int j = 0; j < this.getBoardSize(); j++){
                        outputBoard[this.getBoardSize() - 1 - i][this.getBoardSize() - 1 - j] = turnedGridSwiped[j][i];
                    }
                }
                break;
            case "D":
                // moves right, swipe reversed row and reverse back
                for(int i = 0; i < this.getBoardSize(); i++){
                    outputBoard[i] = this.reverseRow(turnedGridSwiped[i].clone());
                }
                break;
        }

        if(Arrays.deepEquals(outputBoard, this.getBoardGrid())){
            return false;
        } else {
            // apply changes if requested
            if(apply){ this.setBoardGrid(outputBoard.clone()); }
            return true;
        }
    }

    private int[] reverseRow(int[] row){
        int start = 0;
        int end = this.getBoardSize() - 1;
        int[] reverse = new int[this.getBoardSize()];
        while(start < end){
            reverse[start] = row[end];
            reverse[end] = row[start];
            start++;
            end--;
        }
        return reverse;
    }

    private int[] rowSwipe(int[] row){
        int[] outputRow = new int[this.getBoardSize()];
        int outputCell = 0;

        int current = 0;
        int next = 1;

        while(next < this.getBoardSize()){
            // there is no tile on that area of the row
            if(row[current] == 0){
                current++;
                next = current + 1;
            } else if (row[next] == 0){
                next++;
            } else if(row[current] == row[next]){
                // add tiles
                outputRow[outputCell] = row[current] * 2;
                outputCell++;
                current = next + 1;
                next += 2;
            } else {
                // tiles are not the same
                outputRow[outputCell] = row[current];
                outputCell++;
                current = next;
                next = current + 1;
            }
        }
        if(current < this.getBoardSize()){
            if(!(row[current] == 0)){
                outputRow[outputCell] = row[current];
            }
        }
        return outputRow;
    }
}
