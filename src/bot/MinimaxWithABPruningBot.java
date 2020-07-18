package bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import game.ChessBoard;
import game.ChessBoardImpl;
import piece.Move;

public class MinimaxWithABPruningBot implements Bot {
  private final Evaluator evaluator;

  public MinimaxWithABPruningBot() {
    evaluator = new ComplexEvaluator();
  }

  @Override
  public Move chooseMove(ChessBoard board, boolean turn) {
    Move chosenMove = minimax(board, turn, 4, new HashMap<>(),
            -999999, 999999).move;
    return chosenMove;
  }

  MoveEvalPair minimax(ChessBoard board, boolean turn, int depthLeft,
                       Map<ChessBoard, MoveEvalPair> cachedEvals,
                       double alpha, double beta) {
    if (depthLeft <= 0) {
      double eval = evaluator.evaluate(board);
      MoveEvalPair pair = new MoveEvalPair(null, eval);
      return pair;
    }
    List<Move> legalMoves = board.getLegalMoves(turn);
    if (legalMoves.isEmpty()) {
      if (board.kingIsInCheck(turn)) {
        MoveEvalPair pair = new MoveEvalPair(null, turn ? -999999 : 999999);
        return pair;
      } else {
        MoveEvalPair pair = new MoveEvalPair(null, 0);
        return pair;
      }
    }
    Move bestMove = null;
    double bestEval = turn ? -999999 : 999999;
    double newAlpha = alpha;
    double newBeta = beta;
    for (Move m : legalMoves) {
      ChessBoard newBoard = new ChessBoardImpl(board);
      newBoard.makeMove(m);
      double eval = minimax(newBoard, !turn, depthLeft - 1, cachedEvals, newAlpha, newBeta).eval;
      // alpha cutoff
      if (!turn && eval < alpha) {
        return new MoveEvalPair(m, eval);
      }
      // beta cutoff
      if (turn && eval > beta) {
        return new MoveEvalPair(m, eval);
      }

      bestEval = turn ? Math.max(eval, bestEval) : Math.min(eval, bestEval);
      if (eval == bestEval) {
        bestMove = m;
      }

      // update alpha and beta
      if (turn) {
        newAlpha = bestEval;
      } else {
        newBeta = bestEval;
      }
    }
    MoveEvalPair pair = new MoveEvalPair(bestMove, bestEval);
    return pair;
  }

  private static class MoveEvalPair {
    private final Move move;
    private final double eval;

    private MoveEvalPair(Move move, double eval) {
      this.move = move;
      this.eval = eval;
    }
  }
}