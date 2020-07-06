import game.ChessGame;
import game.ChessGameImpl;

public class Main {
  public static void main(String[] args) {
    ChessGame game = new ChessGameImpl();
    //System.out.println(game.display());
    game.makeMove(6, 4, 4, 4);
    game.makeMove(1, 0, 2, 0);
    game.makeMove(4, 4, 3, 4);
    game.display();

    // TODO
    // Optimizations to test:


    // hashing for boards and board states

    // use set/hashset/hashmap instead of lists

    // calculate legal moves as a subset of attack moves
  }
}
