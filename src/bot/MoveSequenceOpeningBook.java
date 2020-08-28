package bot;

import game.ChessGame;
import piece.Move;

public interface MoveSequenceOpeningBook {
  boolean hasBookMove(String pgn, boolean side);

  Move getMove(String pgn, ChessGame gameState, boolean side);
}
