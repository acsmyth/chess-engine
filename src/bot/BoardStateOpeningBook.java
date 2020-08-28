package bot;

import game.ChessBoard;
import piece.Move;

public interface BoardStateOpeningBook {
  boolean hasBoardState(ChessBoard board, boolean side);
  Move getMove(ChessBoard board, boolean side);
}
