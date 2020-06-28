package piece;

import game.ChessBoardImpl;

public abstract class AbstractChessPiece implements ChessPiece {
  private final boolean isWhitePiece;

  public AbstractChessPiece(boolean isWhitePiece) {
    this.isWhitePiece = isWhitePiece;
  }

  public boolean side() {
    return isWhitePiece;
  }

  public boolean isLegalMove(int toRow, int toCol, ChessPiece[][] board) {
    return moveFitsLegalMotionPattern(toRow, toCol)
            && new ChessBoardImpl(board).kingIsInCheck();
  }

  abstract protected boolean moveFitsLegalMotionPattern(int toRow, int toCol);

  public ChessPiece copy() {
    return create(isWhitePiece);
  }
}
