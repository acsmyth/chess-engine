package bot;

import java.util.List;
import java.util.Map;

import game.ChessBoard;
import piece.ChessPiece;
import piece.Move;
import util.Pair;

public interface MoveSorter {
  void sort(List<Move> moves, ChessBoard board);

  int eval(int r, int c, ChessPiece[][] brd);

  int evalDelta(Move m, ChessPiece[][] brd);

  void sortWithSpecialPriority(ChessBoard origBoard,
                               List<IterativeDeepeningBot.GameTree> children, Move cameFrom,
                               Pair<Map<Move, Float>, Map<Move, Float>> historyHeuristicTable,
                               boolean turn);
}
