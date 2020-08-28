package bot;

import game.ChessBoard;
import game.ChessGame;
import piece.Move;

public interface Bot {
  Move chooseMove(ChessBoard board, boolean turn);

  Move chooseMove(ChessGame game, boolean turn);

  double getPrevEval();
}
