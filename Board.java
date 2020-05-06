import javax.swing.JOptionPane;
import java.awt.event.*;
import java.util.*;

public class Board {
    /* The board object that stores the board information and changes the board based on input */
    private int[][] boardGrid;
    private int boardSize;

    public Board(int size){
        this.resetBoard(size);
    }
	
	private void resetBoard(int size){
		/* Initialises the board */
        boardSize = size;
        boardGrid = new int[size][size];
		add_tile();
	}

    public int getBoardSize(){ return this.boardSize; }
    public int[][] getBoardGrid(){ return this.boardGrid; }
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

    public boolean swipe(int direction, boolean apply){
        /* changes the board based on swipe and returns if the board has changed */
        int[][] outputBoard = new int[this.getBoardSize()][this.getBoardSize()];

        // turn grid
        int[][] turnedGrid = new int[this.getBoardSize()][this.getBoardSize()];
		
        switch (direction){
            case KeyEvent.VK_UP:
                turnedGrid = upTurn(this.getBoardGrid());
                break;
            case KeyEvent.VK_LEFT:
                turnedGrid = leftTurn(this.getBoardGrid());
                break;
            case KeyEvent.VK_DOWN:
                turnedGrid = downTurn(this.getBoardGrid());
                break;
            case KeyEvent.VK_RIGHT:
                turnedGrid = rightTurn(this.getBoardGrid());
                break;
        }

        // swipe each row of the turned grid
        int[][] turnedGridSwiped = new int[this.getBoardSize()][this.getBoardSize()];
        for(int i = 0; i < this.getBoardSize(); i++){
            turnedGridSwiped[i] = this.rowSwipe(turnedGrid[i]);
        }

        // switch back if necessary
        switch (direction){
            case KeyEvent.VK_UP:
                outputBoard = upTurn(turnedGridSwiped);
                break;
            case KeyEvent.VK_LEFT:
                outputBoard = leftTurn(turnedGridSwiped);
                break;
            case KeyEvent.VK_DOWN:
                outputBoard = downTurn(turnedGridSwiped);
                break;
            case KeyEvent.VK_RIGHT:
                outputBoard = rightTurn(turnedGridSwiped);
                break;
        }
		
		// if swiped changed anything, add new tile
		if(!Arrays.deepEquals(outputBoard, this.getBoardGrid())){
			if(apply){
				this.setBoardGrid(outputBoard.clone());
				this.add_tile();
			}
			return true;
		} else
			return false;
    }
	
	public void gameOver(){
		// displays game over dialog box
		int option = JOptionPane.showConfirmDialog(null, 
												   "No more moves. Play Again?", 
												   "Game Over",
												   JOptionPane.YES_NO_OPTION);
		if(option == JOptionPane.YES_OPTION)
			this.resetBoard(this.getBoardSize());
	}
	
	// -----------------TURNING FUNCTIONS ------------------------
	
	private int[][] upTurn(int[][] toTurn){
		int[][] turned = new int[this.getBoardSize()][this.getBoardSize()];
		// flip grid along the downwards diagonal
        for(int i = 0; i < this.getBoardSize(); i++){
			for(int j = 0; j < this.getBoardSize(); j++){
				turned[i][j] = toTurn[j][i];
			}
        }
		return turned;
	}
	
	private int[][] leftTurn(int[][] toTurn){
		int[][] turned = new int[this.getBoardSize()][this.getBoardSize()];
		// no change copy 2d array
		for(int i = 0; i < this.getBoardSize(); i++){
			System.arraycopy(toTurn[i], 0, turned[i], 0, this.getBoardSize());
        }
		return turned;
	}
	
	private int[][] downTurn(int[][] toTurn){
		// flip along vertical axis
		int[][] turned = new int[this.getBoardSize()][this.getBoardSize()];
		for(int i = 0; i < this.getBoardSize(); i++){
			for(int j = 0; j < this.getBoardSize(); j++){
				turned[this.getBoardSize() - 1 - i][this.getBoardSize() - 1 - j] = toTurn[j][i];
            }
		}
		return turned;
	}
	
	private int[][] rightTurn(int[][] toTurn){
		// flip grid along upwards diagonal
		int[][] turned = new int[this.getBoardSize()][this.getBoardSize()];
        for(int i = 0; i < this.getBoardSize(); i++){
			turned[i] = this.reverseRow(toTurn[i].clone());
		}
		return turned;
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
		// swipes the row to the left
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
	
	public boolean movesAvailable(){
		// check if any more moves can be done
		int[] moves = {KeyEvent.VK_UP,
					   KeyEvent.VK_LEFT,
					   KeyEvent.VK_DOWN,
					   KeyEvent.VK_RIGHT};
					   
		boolean available = false;
		// checks finds one valid move
		for(int move : moves){
			if (!available){
				available = swipe(move, false);
			}
		}
		return available;
	}
}
