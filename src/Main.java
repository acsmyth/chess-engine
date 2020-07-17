import java.util.Scanner;

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
    Scanner sc = new Scanner(System.in);
    while (!game.isOver()) {
      try {
        Thread.sleep(500);
      } catch (Exception e) {

      }
      int r1 = sc.nextInt();
      int c1 = sc.nextInt();
      int r2 = sc.nextInt();
      int c2 = sc.nextInt();
      game.makeMove(r1, c1, r2, c2);
      game.display();
      System.out.println("\n");

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
