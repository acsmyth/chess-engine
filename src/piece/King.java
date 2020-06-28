package piece;

public class King extends AbstractChessPiece implements ChessPiece {
  public King(boolean isWhitePiece) {
    super(isWhitePiece);
  }

  @Override
  protected boolean moveFitsLegalMotionPattern(int toRow, int toCol) {
    return false;
  }

  @Override
  public ChessPiece create(boolean isWhitePiece) {
    return new King(isWhitePiece);
  }
}
