package piece;

import java.util.List;

public class Bishop extends AbstractChessPiece implements ChessPiece {
  public Bishop(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
  }

  @Override
  protected boolean isLegalMoveIgnoringChecks(int toR, int toC, ChessPiece[][] board) {
    return getLegalMoves().contains(new Move(r, c, toR, toC));
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new Bishop(r, c, isWhitePiece);
  }

  @Override
  public List<Move> getLegalMoves() {
    if (cachedLegalMoves != null) return cachedLegalMoves;

  }
}
