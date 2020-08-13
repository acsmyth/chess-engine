package bot;

import java.util.List;
import java.util.Map;

import game.ChessBoard;
import piece.ChessPiece;
import piece.Move;
import util.Pair;

public class SimpleMoveSorter implements MoveSorter {
  @Override
  public void sort(List<Move> moves, ChessBoard board) {
    // sorts in ascending order
    // lowest scores are best ones
    // low score means its delta is higher than others
    moves.sort((m1, m2) -> {
      ChessPiece[][] brd = board.getBoard();
      return Math.max(0, evalDelta(m2, brd)) - Math.max(0, evalDelta(m1, brd));
    });
  }

  @Override
  public int eval(int r, int c, ChessPiece[][] brd) {
    if (brd[r][c] == null) return 0;
    switch (brd[r][c].getClass().getSimpleName()) {
      case "Pawn":
        return 1;
      case "Knight":
        return 3;
      case "Bishop":
        return 3;
      case "Rook":
        return 5;
      case "Queen":
        return 9;
      case "King":
        return 10000;
      default:
        throw new IllegalStateException("Invalid piece");
    }
  }

  @Override
  public int evalDelta(Move m, ChessPiece[][] brd) {
    return eval(m.toR, m.toC, brd) - eval(m.fromR, m.fromC, brd);
  }

  @Override
  public void sortWithSpecialPriority(ChessBoard origBoard,
                                      List<IterativeDeepeningBot.GameTree> children,
                                      Move pvMove,
                                      Pair<Map<Move, Float>, Map<Move, Float>> historyHeuristicTable,
                                      boolean turn) {
    Map<Move, Float> historyTable = turn ? historyHeuristicTable.x : historyHeuristicTable.y;
    children.sort((t1, t2) -> {
      ChessPiece[][] brd = origBoard.getBoard();
      return Math.max(0, evalDelta(t2.moveCameFrom, brd))
              - Math.max(0, evalDelta(t1.moveCameFrom, brd))
              + (t2.moveCameFrom == pvMove ? 100 : 0) + (t1.moveCameFrom == pvMove ? -100 : 0)
              + (int)(Math.sqrt(historyTable.getOrDefault(t2.moveCameFrom, (float)0) / 100.0 + 0.99))
              - (int)(Math.sqrt(historyTable.getOrDefault(t1.moveCameFrom, (float)0) / 100.0 + 0.99));
    });
  }
}
