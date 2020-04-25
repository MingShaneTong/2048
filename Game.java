import java.util.*;
import javax.swing.JOptionPane;

public class Game {
    /* The 2048 Game, Creates a board and receives user input */
    private boolean gameContinue;
    Board board;

    Game(){
        this.gameContinue = true;
        board = new Board(4);
        // continues until game ends
        while(gameContinue){
            turn();
        }
    }

    private void setGameContinue(boolean val){ this.gameContinue = val; }

    private void turn(){
        // does one turn
        board.add_tile();       // add random tile
        System.out.println(board.boardBuilder(4));      // shows user

        // gets user input
        System.out.println("Move:");
        String move = "";
        String[] movements = {"W", "A", "S", "D"};
        List<String> validMoves = new ArrayList<String>();

        // check valid moves
        for(String direction: movements){
            // add move to validMoves list if valid
            if(board.swipe(direction, false)){ validMoves.add(direction); }
        }

        if(validMoves.isEmpty()){
            // game lost
            System.out.println("GAME OVER");
            this.setGameContinue(false);
        } else {
            // loop until valid
            while(!validMoves.contains(move)){
                Scanner scanner = new Scanner(System.in);
                move = scanner.nextLine().toUpperCase();
                if(!validMoves.contains(move)){
                    System.out.println("The valid moves are: " + String.join(", ", validMoves));
                }
            }
            // apply swipe
            board.swipe(move, true);
        }
    }
}
