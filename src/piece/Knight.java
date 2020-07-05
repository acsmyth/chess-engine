package piece;

import java.util.Arrays;
import java.util.List;

public class Knight extends AbstractChessPiece implements ChessPiece {
  public Knight(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
  }

  @Override
  protected boolean isLegalMoveIgnoringChecks(int toRow, int toCol, ChessPiece[][] board) {
    return (Math.abs(r - toRow) == 2 && Math.abs(c - toCol) == 1)
            || (Math.abs(r - toRow) == 1 && Math.abs(c - toCol) == 2);
  }

  @Override
  protected List<Move> calculateLegalMovesIgnoringChecks(ChessPiece[][] board) {
    return null;
  }

  @Override
  protected List<Move> calculateAttackMoves(ChessPiece[][] board) {
    return Arrays.asList(new Move(r, c, r-1, c-2));
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new Knight(r, c, isWhitePiece);
  }
}
