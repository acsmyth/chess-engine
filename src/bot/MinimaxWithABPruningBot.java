package bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import game.ChessBoard;
import game.ChessBoardImpl;
import game.ChessGame;
import piece.Move;
import piece.NullMove;
import util.Settings;

public class MinimaxWithABPruningBot implements Bot {
  private final Evaluator evaluator;
  private final MoveSorter moveSorter;
  private final MoveSequenceOpeningBook openingBook;
  public static int depth;
  private double prevEval;

  static {
    depth = 4;
  }

  public MinimaxWithABPruningBot() {
    evaluator = new ComplexEvaluator();
    moveSorter = new SimpleMoveSorter();
    openingBook = new DatabaseOpeningBook();
    prevEval = 0;
  }

  @Override
  public Move chooseMove(ChessBoard board, boolean turn) {
    MoveEvalPair result = minimax(board, turn, depth, new HashMap<>(),
            -999999999, 999999999, 0);
    Move chosenMove = result.move;
    if (chosenMove == null) {
      throw new IllegalArgumentException("No legal moves on the given board");
    }
    prevEval = result.eval;
    return chosenMove;
  }

  @Override
  public Move chooseMove(ChessGame game, boolean turn) {
    String pgn = game.pgn();
    pgn = pgn.substring(pgn.indexOf("\n\n") + 1).trim();
    if (openingBook.hasBookMove(pgn, turn)) {
      Move m = openingBook.getMove(pgn, game, turn);
      if (m != null) {
        prevEval = 0;
        return m;
      }
    }
    return chooseMove(game.getBoard(), turn);
  }

  MoveEvalPair minimax(ChessBoard board, boolean turn, int depthLeft,
                       Map<ChessBoard, MoveEvalPair> cachedEvals,
                       double alpha, double beta, int extensionDepth) {
    /*if (depthLeft <= -2) {
      double eval = evaluator.evaluate(board, turn);
      MoveEvalPair pair = new MoveEvalPair(null, eval);
      return pair;
    }
    List<Move> captureMoves = board.getCaptureMoves(turn);
    captureMoves.removeIf(m -> moveSorter.evalDelta(m, board.getBoard()) > 0);
    if (depthLeft <= 0 && !captureMoves.isEmpty()) {
      return loopThroughMoves(board, captureMoves, turn, depthLeft, cachedEvals, alpha, beta);
    }*/

    // -2 is hard limit
    if ((depthLeft <= -extensionDepth && depthLeft % 2 == 0) || depthLeft <= -2
            || (!Settings.checkExtensions && depthLeft <= 0)) {
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
            turn, depthLeft, cachedEvals, alpha, beta, extensionDepth);
  }

  private MoveEvalPair loopThroughMoves(ChessBoard board, List<Move> legalMoves, boolean turn,
                                        int depthLeft, Map<ChessBoard, MoveEvalPair> cachedEvals,
                                        double alpha, double beta, int extensionDepth) {
    //if (cachedEvals.containsKey(board)) {
    //  return cachedEvals.get(board);
    //}
    Move bestMove = null;
    double bestEval = turn ? -999999999 : 999999999;
    double newAlpha = alpha;
    double newBeta = beta;

    // add the null move to beginning only at max depth
    //if (depthLeft == depth || depthLeft == depth-1 || depthLeft == depth-2) {
    //  legalMoves.add(0, new NullMove());
    //}

    if (legalMoves.isEmpty()) {
      if (board.kingIsInCheck(turn)) {
        MoveEvalPair pair = new MoveEvalPair(null, turn ? -999999 - depthLeft : 999999 + depthLeft);
        //cachedEvals.put(board, pair);
        return pair;
      } else {
        MoveEvalPair pair = new MoveEvalPair(null, 0);
        //cachedEvals.put(board, pair);
        return pair;
      }
    }

    /*if (board.numBoardStateRepeats() >= 3) {
      return new MoveEvalPair(null, 0);
    }*/

    moveSorter.sort(legalMoves, board);
    for (Move m : legalMoves) {
      ChessBoard newBoard = new ChessBoardImpl(board);

      // TODO - feed the board new hashcode information, since this move lets us just xor whatever
      //  changed only to save time
      newBoard.makeMove(m);

      //if (cachedEvals.containsKey(newBoard)) {
      //  return cachedEvals.get(newBoard);
      //}

      int nextDepth = depthLeft - 1;
      //if (moveSorter.evalDelta(m, board.getBoard()) > 0) {
      //  nextDepth++;
      //}

      // check extension / positive delta extension
      int nextExtensionDepth = extensionDepth;
      if (Settings.checkExtensions
              && (newBoard.kingIsInCheck(true) || newBoard.kingIsInCheck(false))) {
        nextExtensionDepth++;
      }

      double eval = minimax(newBoard, !turn, nextDepth, cachedEvals, newAlpha, newBeta,
              nextExtensionDepth).eval;

      // alpha cutoff
      if (!turn && eval < alpha) {
        MoveEvalPair pair = new MoveEvalPair(m, eval);
        cachedEvals.put(newBoard, pair);
        return pair;
      }
      // beta cutoff
      if (turn && eval > beta) {
        MoveEvalPair pair = new MoveEvalPair(m, eval);
        cachedEvals.put(newBoard, pair);
        return pair;
      }

      if (!(m instanceof NullMove)) {
        if ((turn && eval > bestEval) || (!turn && eval < bestEval)) {
          bestEval = eval;
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