import java.util.*;
import javax.swing.JFrame;

public class Game {
    /* The 2048 Game, Creates a board and receives user input */
    private static Board board;
    private static int boardSize = 4;

    public static void main(String[] args){
        board = new Board(boardSize);
        
        JFrame gameFrame = new JFrame("2048");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gameFrame.getContentPane().add(new BoardPanel(board));
        
        gameFrame.pack();
        gameFrame.setVisible(true);
    }
}
