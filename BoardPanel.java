import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import javax.swing.border.*;

public class BoardPanel extends JPanel {
	private JLabel[] tiles;
	private Board board;
	private int boardSize;
	private int[][] boardArray;
	
	public BoardPanel(Board board){
		// sets up grid for tiles
		this.boardSize = board.getBoardSize();
		this.board = board;
		this.boardArray = board.getBoardGrid();
		int totalTiles = this.boardSize * this.boardSize;
		
		tiles = new JLabel[totalTiles];
		this.setLayout(new GridLayout(this.boardSize, this.boardSize));
		setFocusable(true);
		addKeyListener(new DirectionListener());
		
		// adds all tiles
		for(int i = 0; i < totalTiles; i++){
			JLabel tile = new JLabel();
			tile.setPreferredSize(new Dimension(120, 120));
			tile.setBorder(BorderFactory.createEtchedBorder());
			tile.setFont(new Font("Helvetica", Font.BOLD, 30));
			tile.setHorizontalAlignment(SwingConstants.CENTER);
			tile.setOpaque(true);
			tiles[i] = tile;
			add(tile);
		}
		
		updateTiles();
	}
	
	private void updateTiles(){
		// add numbers accordingly
		for(int i = 0; i < board.getBoardSize(); i++){
			for(int j = 0; j < board.getBoardSize(); j++){
				// sets text for each tile
				JLabel tile = tiles[board.getBoardSize() * i + j];
				int tileValue = board.getBoardGrid()[i][j];
				if(tileValue != 0){
					tile.setText(Integer.toString(tileValue));
					
					// decide a color, decreases green so it changes from yellow to red
					int stage = (int)(Math.log(tileValue) / Math.log(2));
					int green = 255 - stage * 20;
					tile.setBackground(new Color(255, green, 0));
				} else {
					// blank it and make white
					tile.setText("");
					tile.setBackground(new Color(255, 255, 255));
				}
			}
		}
	}
	
	// swipes the tiles
	private class DirectionListener implements KeyListener {
		public void keyPressed(KeyEvent event){
			if (event.getKeyCode() == 82)
				board.gameOver();
			
			
			List moves = new ArrayList();
			moves.add(KeyEvent.VK_UP);
			moves.add(KeyEvent.VK_LEFT);
			moves.add(KeyEvent.VK_DOWN);
			moves.add(KeyEvent.VK_RIGHT);
										  
			// types directional key and applies the swipe
			if(moves.contains(event.getKeyCode())){
				board.swipe(event.getKeyCode(), true);
			}
			updateTiles();
			
			// check if move available and the game can be continued
			if(!board.movesAvailable()){
				// game over
				board.gameOver();
			}
		}
		
		public void keyTyped (KeyEvent event) {}
		public void keyReleased (KeyEvent event) {}
	}
}