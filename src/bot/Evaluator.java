package bot;

import game.ChessBoard;

public interface Evaluator {
  double evaluate(ChessBoard board);
}
