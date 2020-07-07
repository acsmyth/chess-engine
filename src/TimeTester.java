import game.ChessBoard;
import game.ChessBoardImpl;

public class TimeTester {
  public static void main(String[] args) {
    ChessBoard board = new ChessBoardImpl();
    int runs = 1000000;
    long startTime = System.currentTimeMillis();
    for (int n=0;n<runs;n++) {
      board.getLegalMoves(true);
    }
    long endTime = System.currentTimeMillis();
    System.out.println(endTime - startTime + " ms per " + runs + " runs");
    long totalTime = endTime - startTime;
    long msPerGetLegalMovesCall = 1000 * totalTime / ((long)runs);
    //System.out.println(msPerGetLegalMovesCall + " ms per 1000 runs");
    //0.73 microseconds
  }
}
