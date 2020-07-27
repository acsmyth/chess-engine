import java.util.ArrayList;
import java.util.List;
import game.ChessGame;
import game.ChessGameImpl;

public class TimeTester {
  public static void main(String[] args) {
    ChessGame game = new ChessGameImpl();
    int runs = 10;
    for (int i=0;i<26;i++) {
      game.makeComputerMove();
    }
    game.display();
    long totalTime = 0;
    List<Long> times = new ArrayList<>();
    for (int n=0;n<runs;n++) {
      ChessGame copy = new ChessGameImpl(game);
      long startTime = System.currentTimeMillis();
      copy.makeComputerMove();
      long endTime = System.currentTimeMillis();
      totalTime += (endTime - startTime);
      times.add(endTime - startTime);
    }
    System.out.println("\n" + totalTime/((double)runs) + " ms per run");
    long msPerGetLegalMovesCall = totalTime / ((long)runs);
    //System.out.println(msPerGetLegalMovesCall + "s per 1000 runs");
    for (Long l : times) {
      System.out.print(l + ", ");
    }

    //_______ microseconds
    // NULL MOVES AT EVERY DEPTH
    // 2077, 2095, 2044, 2148

    // NO NULL MOVES
    // 1942, 1659, 1650, 1645

    // 1 NULL MOVE
    // 1855, 1762, 1735, 1717

    // HASHSET INSTEAD OF LIST, NO NULL MOVE PRUNING
  }
}
