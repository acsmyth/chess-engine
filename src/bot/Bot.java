package bot;

import game.ChessBoard;
import piece.Move;

public interface Bot {
  Move chooseMove(ChessBoard board, boolean turn);

  double getPrevEval();
}
