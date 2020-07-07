import game.ChessBoard;
import game.ChessBoardImpl;
import game.ChessGame;
import game.ChessGameImpl;
import piece.Bishop;
import piece.ChessPiece;
import piece.King;
import piece.Knight;
import piece.Move;
import piece.Pawn;
import piece.Queen;
import piece.Rook;

public class Main {
  public static void main(String[] args) {
    ChessGame game = new ChessGameImpl();
    game.display();
    System.out.println("\n");
    while (!game.isOver()) {
      try {
        Thread.sleep(500);
      } catch (Exception e) {

      }
      game.makeComputerMove();
      game.display();
      System.out.println("\n");
    }

    // TODO
    // Optimizations to test:


    // hashing for boards and board states

    // use set/hashset/hashmap instead of lists

    // calculate legal moves as a subset of attack moves
  }
}
