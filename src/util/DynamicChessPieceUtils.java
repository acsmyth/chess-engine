package util;

import java.util.List;

import piece.ChessPiece;
import piece.Move;

public class DynamicChessPieceUtils {
  public static void addMoves(boolean side, int r, int c, int rowInc, int colInc,
                               ChessPiece[][] board, List<Move> newLegalMoves,
                               boolean includeAttackMoves) {
    int i = r + rowInc;
    int p = c + colInc;
    while (Utils.inBounds(i, p) && board[i][p] == null) {
      newLegalMoves.add(new Move(r, c, i, p));
      i += rowInc;
      p += colInc;
    }
    if (Utils.inBounds(i, p) && (includeAttackMoves || side != board[i][p].side())) {
      newLegalMoves.add(new Move(r, c, i, p));
    }
  }
}
