package piece;

public class Knight extends AbstractChessPiece implements ChessPiece {
  public Knight(boolean isWhitePiece) {
    super(isWhitePiece);
  }

  @Override
  protected boolean moveFitsLegalMotionPattern(int toRow, int toCol) {
    return false;
  }

  @Override
  public ChessPiece create(boolean isWhitePiece) {
    return new Knight(isWhitePiece);
  }
}
