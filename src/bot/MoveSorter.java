package bot;

import java.util.List;
import game.ChessBoard;
import piece.Move;

public interface MoveSorter {
  void sort(List<Move> moves, ChessBoard board);
}
