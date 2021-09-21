package bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import game.ChessBoard;
import game.ChessBoardImpl;
import game.ChessGame;
import piece.Move;

public class SimpleMinimaxBot implements Bot {
  private final Evaluator evaluator;
  private double prevEval;

  public SimpleMinimaxBot() {
    evaluator = new ComplexEvaluator();
    prevEval = 0;
  }

  @Override
  public Move chooseMove(ChessBoard board, boolean turn) {
    MoveEvalPair result = minimax(board, turn, 4, new HashMap<>());
    Move chosenMove = result.move;
    prevEval = result.eval;
    return chosenMove;
  }

  @Override
  public Move chooseMove(ChessGame game, boolean turn) {
    return chooseMove(game.getBoard(), turn);
  }

  MoveEvalPair minimax(ChessBoard board, boolean turn, int depthLeft,
                       Map<ChessBoard, MoveEvalPair> cachedEvals) {
    //if (cachedEvals.containsKey(board)) {
    //  return cachedEvals.get(board);
    //}
    if (depthLeft <= 0) {
      double eval = evaluator.evaluate(board, turn);
      MoveEvalPair pair = new MoveEvalPair(null, eval);
      //cachedEvals.put(board, pair);
      return pair;
    }
    List<Move> legalMoves = board.getLegalMoves(turn);
    if (legalMoves.isEmpty()) {
      if (board.kingIsInCheck(turn)) {
        MoveEvalPair pair = new MoveEvalPair(null, turn ? -999999 : 999999);
        //cachedEvals.put(board, pair);
        return pair;
      } else {
        MoveEvalPair pair = new MoveEvalPair(null, 0);
        //cachedEvals.put(board, pair);
        return pair;
      }
    }
    Move bestMove = null;
    double bestEval = turn ? -999999 : 999999;
    for (Move m : legalMoves) {
      ChessBoard newBoard = new ChessBoardImpl(board);
      newBoard.makeMove(m);
      double eval = minimax(newBoard, !turn, depthLeft - 1, cachedEvals).eval;
      bestEval = turn ? Math.max(eval, bestEval) : Math.min(eval, bestEval);
      if (eval == bestEval) {
        bestMove = m;
      }
    }
    MoveEvalPair pair = new MoveEvalPair(bestMove, bestEval);
    //cachedEvals.put(board, pair);
    return pair;
  }

  @Override
  public double getPrevEval() {
    return prevEval;
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
