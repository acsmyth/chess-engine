package bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import game.ChessBoard;
import game.ChessBoardImpl;
import piece.Move;
import piece.NullMove;

public class MinimaxWithABPruningBot implements Bot {
  private final Evaluator evaluator;
  private final MoveSorter moveSorter;
  public static int depth;
  private double prevEval;

  public MinimaxWithABPruningBot() {
    evaluator = new ComplexEvaluator();
    moveSorter = new SimpleMoveSorter();
    depth = 4;
    prevEval = 0;
  }

  @Override
  public Move chooseMove(ChessBoard board, boolean turn) {
    MoveEvalPair result = minimax(board, turn, depth, new HashMap<>(),
            -999999, 999999);
    Move chosenMove = result.move;
    prevEval = result.eval;
    return chosenMove;
  }

  MoveEvalPair minimax(ChessBoard board, boolean turn, int depthLeft,
                       Map<ChessBoard, MoveEvalPair> cachedEvals,
                       double alpha, double beta) {
    if (depthLeft <= 0 && !board.kingIsInCheck(turn)) {
      double eval = evaluator.evaluate(board, turn);
      MoveEvalPair pair = new MoveEvalPair(null, eval);
      return pair;
    }
    /*
    if (depthLeft <= -1) {
      double eval = evaluator.evaluate(board);
      MoveEvalPair pair = new MoveEvalPair(null, eval);
      return pair;
    }
    if (depthLeft == 0) {
      List<Move> captureMoves = board.getCaptureMoves(turn);
      ChessPiece[][] brd = board.getBoard();
      captureMoves.removeIf(
              m -> moveSorter.eval(m.toR, m.toC, brd)
                      - moveSorter.eval(m.fromR, m.fromC, brd) <= 0);
      if (captureMoves.isEmpty()) { // maybe extensions for checks as well
        double eval = evaluator.evaluate(board);
        MoveEvalPair pair = new MoveEvalPair(null, eval);
        return pair;
      } else {
        return loopThroughMoves(board, captureMoves, turn, depthLeft, cachedEvals, alpha, beta);
      }
    }*/
    /*
    List<Move> legalMoves = board.getLegalMoves(turn);
    if (legalMoves.isEmpty()) {
      if (board.kingIsInCheck(turn)) {
        MoveEvalPair pair = new MoveEvalPair(null, turn ? -999999 : 999999);
        return pair;
      } else {
        MoveEvalPair pair = new MoveEvalPair(null, 0);
        return pair;
      }
    }*/

    // order legal moves
    return loopThroughMoves(board, board.getLegalMoves(turn),
            turn, depthLeft, cachedEvals, alpha, beta);
  }

  private MoveEvalPair loopThroughMoves(ChessBoard board, List<Move> legalMoves, boolean turn,
                                        int depthLeft, Map<ChessBoard, MoveEvalPair> cachedEvals,
                                        double alpha, double beta) {
    Move bestMove = null;
    double bestEval = turn ? -999999 : 999999;
    double newAlpha = alpha;
    double newBeta = beta;

    moveSorter.sort(legalMoves, board);

    // add the null move to beginning only at max depth
    //if (depthLeft == depth || depthLeft == depth-1 || depthLeft == depth-2) {
    //  legalMoves.add(0, new NullMove());
    //}

    for (Move m : legalMoves) {
      ChessBoard newBoard = new ChessBoardImpl(board);
      newBoard.makeMove(m);

      int nextDepth = depthLeft - 1;
      //if (moveSorter.evalDelta(m, board.getBoard()) > 0) {
      //  nextDepth++;
      //}

      double eval = minimax(newBoard, !turn, nextDepth, cachedEvals, newAlpha, newBeta).eval;
      // alpha cutoff
      if (!turn && eval < alpha) {
        return new MoveEvalPair(m, eval);
      }
      // beta cutoff
      if (turn && eval > beta) {
        return new MoveEvalPair(m, eval);
      }

      if (!(m instanceof NullMove)) {
        bestEval = turn ? Math.max(eval, bestEval) : Math.min(eval, bestEval);
        if (eval == bestEval) {
          bestMove = m;
        }
      }

      // update alpha and beta
      if (turn) {
        newAlpha = Math.max(eval, newAlpha);
      } else {
        newBeta = Math.min(eval, newBeta);
      }
    }
    MoveEvalPair pair = new MoveEvalPair(bestMove, bestEval);
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