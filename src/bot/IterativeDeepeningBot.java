package bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import game.ChessBoard;
import game.ChessBoardImpl;
import game.ChessGame;
import piece.Move;
import piece.NullMove;
import util.Pair;

public class IterativeDeepeningBot implements Bot {
  private final Evaluator evaluator;
  private final MoveSorter moveSorter;
  public static int depth;
  private double prevEval;

  static {
    depth = 4;
  }

  public IterativeDeepeningBot() {
    evaluator = new ComplexEvaluator();
    moveSorter = new SimpleMoveSorter();
    prevEval = 0;
  }

  @Override
  public Move chooseMove(ChessBoard board, boolean turn) {
    GameTree gameTree = new GameTree(board, null);
    MoveEvalPair result = null;
    boolean curTurn = turn;
    Pair<Map<Move, Float>, Map<Move, Float>> historyHeuristicTable = new Pair<>(new HashMap<>(), new HashMap<>());
    for (int i=0;i<depth;i++) {
      extendGameTree(curTurn);
      result = minimax(gameTree, turn, new HashMap<>(), historyHeuristicTable, result, -999999999, 999999999);
      /*if (i < depth-1) {
        reorderGameTree(gameTree, result);
      }*/
      curTurn = !curTurn;
    }

    Move chosenMove = result.cameFrom;
    if (chosenMove == null) {
      throw new IllegalArgumentException("No legal moves on the given board");
    }
    prevEval = result.eval;
    return chosenMove;
  }

  @Override
  public Move chooseMove(ChessGame game, boolean turn) {
    return chooseMove(game.getBoard(), turn);
  }

  private void reorderGameTree(GameTree gameTree, MoveEvalPair result) {
    // next chain takes us through the PV moves
    GameTree curTree = gameTree;
    MoveEvalPair node = result;
    while (node != null) {
      MoveEvalPair finalNode = node;
      //moveSorter.sortWithSpecialPriority(curTree.board, curTree.children, finalNode.cameFrom);
      // ascending order - smallest value (one that matches PV) comes first
      node = node.next;
      curTree = curTree.getChildWithMove(finalNode.cameFrom);
    }
  }

  private void extendGameTree(boolean turn) {
    List<GameTree> newLeafNodes = new ArrayList<>();
    for (GameTree tree : GameTree.leafNodes) {
      List<Move> legalMoves = tree.board.getLegalMoves(turn);
      for (Move m : legalMoves) {
        ChessBoard newBoard = new ChessBoardImpl(tree.board);
        newBoard.makeMove(m);
        tree.addChild(newBoard, m);
      }
      newLeafNodes.addAll(tree.children);
    }
    GameTree.leafNodes = newLeafNodes;
  }

  MoveEvalPair minimax(GameTree gameTree, boolean turn, Map<ChessBoard, MoveEvalPair> cachedEvals,
                       Pair<Map<Move, Float>, Map<Move, Float>> historyHeuristicTable,
                       MoveEvalPair pvPath, double alpha, double beta) {
    if (gameTree.children.isEmpty()) {
      double eval = evaluator.evaluate(gameTree.board, turn);
      return new MoveEvalPair(null, eval, null);
    }
    return loopThroughMoves(gameTree, turn, cachedEvals,
            historyHeuristicTable, pvPath, alpha, beta);
  }

  private MoveEvalPair loopThroughMoves(GameTree gameTree, boolean turn,
                                        Map<ChessBoard, MoveEvalPair> cachedEvals,
                                        Pair<Map<Move, Float>, Map<Move, Float>> historyHeuristicTable,
                                        MoveEvalPair pvPath,
                                        double alpha, double beta) {
    Move bestMove = null;
    double bestEval = turn ? -999999999 : 999999999;
    double newAlpha = alpha;
    double newBeta = beta;

    if (gameTree.children.isEmpty()) {
      if (gameTree.board.kingIsInCheck(turn)) {
        return new MoveEvalPair(null, turn ? -999999 + gameTree.depth
                : 999999 - gameTree.depth, null);
      } else {
        return new MoveEvalPair(null, 0, null);
      }
    }

    // sort this layer of the game tree
    if (pvPath != null) {
      moveSorter.sortWithSpecialPriority(gameTree.board, gameTree.children, pvPath.cameFrom,
              historyHeuristicTable, turn);
    }

    MoveEvalPair next = null;
    for (GameTree tree : gameTree.children) {
      MoveEvalPair nextPvPath = null;
      if (pvPath != null) {
        nextPvPath = pvPath.next;
      }
      MoveEvalPair res = minimax(tree, !turn, cachedEvals, historyHeuristicTable,
              nextPvPath, newAlpha, newBeta);
      double eval = res.eval;

      // alpha / beta cutoffs
      if ((!turn && eval < alpha) || (turn && eval > beta)) {
        if (turn) {
          historyHeuristicTable.x.put(tree.moveCameFrom,
                  historyHeuristicTable.x.getOrDefault(tree.moveCameFrom, (float)0) + (5-depth)*(5-depth)); // TODO - dont just add one - based on depth
        } else {
          historyHeuristicTable.y.put(tree.moveCameFrom,
                  historyHeuristicTable.y.getOrDefault(tree.moveCameFrom, (float)0) + (5-depth)*(5-depth));
        }
        MoveEvalPair pair = new MoveEvalPair(tree.moveCameFrom, eval, null);
        return pair;
      }

      if (!(tree.moveCameFrom instanceof NullMove)
              && ((turn && eval > bestEval) || (!turn && eval < bestEval))) {
          bestEval = eval;
          bestMove = tree.moveCameFrom;
          next = res;
      }

      // update alpha and beta
      if (turn) {
        newAlpha = Math.max(eval, newAlpha);
      } else {
        newBeta = Math.min(eval, newBeta);
      }
    }
    return new MoveEvalPair(bestMove, bestEval, next);
  }

  @Override
  public double getPrevEval() {
    return prevEval;
  }

  private static class MoveEvalPair {
    private final Move cameFrom;
    private final double eval;
    private final MoveEvalPair next;

    private MoveEvalPair(Move cameFrom, double eval, MoveEvalPair next) {
      this.cameFrom = cameFrom;
      this.eval = eval;
      this.next = next;
    }
  }

  static class GameTree {
    final ChessBoard board;
    final Move moveCameFrom;
    private final List<GameTree> children;
    private final int depth;
    private static List<GameTree> leafNodes;

    static {
      leafNodes = new ArrayList<>();
    }

    private GameTree(ChessBoard board, Move moveCameFrom) {
      this.board = board;
      this.moveCameFrom = moveCameFrom;
      children = new ArrayList<>();
      depth = 0;
      leafNodes = new ArrayList<>();
      leafNodes.add(this);
    }

    private GameTree(ChessBoard board, Move moveCameFrom, int depth) {
      this.board = board;
      this.moveCameFrom = moveCameFrom;
      children = new ArrayList<>();
      this.depth = depth;
    }

    private void addChild(ChessBoard board, Move moveCameFrom) {
      children.add(new GameTree(board, moveCameFrom, depth + 1));
    }

    public GameTree getChildWithMove(Move cameFrom) {
      for (GameTree tree : children) {
        if (tree.moveCameFrom == cameFrom) {
          return tree;
        }
      }
      return null;
    }
  }
}
