package bot;

import java.util.List;
import game.ChessBoard;
import game.ChessBoardImpl;
import piece.Move;

public class SimpleMinimaxBot implements Bot {
  @Override
  public Move chooseMove(ChessBoard board, boolean turn) {
    Move chosenMove = minimax(board, turn, 4).move;
    return chosenMove;
  }

  MoveEvalPair minimax(ChessBoard board, boolean turn, int depthLeft) {
    if (depthLeft <= 0) {
      return new MoveEvalPair(null, new SimpleEvaluator().evaluate(board));
    }
    List<Move> legalMoves = board.getLegalMoves(turn);
    if (legalMoves.isEmpty()) {
      return new MoveEvalPair(null, new SimpleEvaluator().evaluate(board));
    }
    Move bestMove = null;
    double bestEval = turn ? -999999 : 999999;
    for (Move m : legalMoves) {
      ChessBoard newBoard = new ChessBoardImpl(board);
      newBoard.makeMove(m);
      double eval = minimax(newBoard, !turn, depthLeft - 1).eval;
      bestEval = turn ? Math.max(eval, bestEval) : Math.min(eval, bestEval);
      if (eval == bestEval) {
        bestMove = m;
      }
    }
    return new MoveEvalPair(bestMove, bestEval);
  }

  private class MoveEvalPair {
    private final Move move;
    private final double eval;

    private MoveEvalPair(Move move, double eval) {
      this.move = move;
      this.eval = eval;
    }
  }
}
