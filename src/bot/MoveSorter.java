package bot;

import java.util.List;
import game.ChessBoard;
import piece.ChessPiece;
import piece.Move;

public interface MoveSorter {
  void sort(List<Move> moves, ChessBoard board);

  int eval(int r, int c, ChessPiece[][] brd);

  int evalDelta(Move m, ChessPiece[][] brd);
}
