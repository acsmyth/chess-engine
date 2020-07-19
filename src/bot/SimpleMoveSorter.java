package bot;

import java.util.List;
import game.ChessBoard;
import piece.ChessPiece;
import piece.Move;

public class SimpleMoveSorter implements MoveSorter {
  @Override
  public void sort(List<Move> moves, ChessBoard board) {
    // sorts in ascending order
    // lowest scores are best ones
    // low score means its delta is higher than others
    moves.sort((m1, m2) -> {
      ChessPiece[][] brd = board.getBoard();
      return Math.max(0, (eval(m2.toR, m2.toC, brd) - eval(m2.fromR, m2.fromC, brd)))
              - Math.max(0, (eval(m1.toR, m1.toC, brd) - eval(m1.fromR, m1.fromC, brd)));
    });
  }

  private int eval(int r, int c, ChessPiece[][] brd) {
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
}
