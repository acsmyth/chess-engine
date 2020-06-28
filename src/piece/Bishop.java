package piece;

public class Bishop extends AbstractChessPiece implements ChessPiece {
  public Bishop(boolean isWhitePiece) {
    super(isWhitePiece);
  }

  @Override
  protected boolean moveFitsLegalMotionPattern(int toRow, int toCol) {
    return false;
  }

  @Override
  public ChessPiece create(boolean isWhitePiece) {
    return new Bishop(isWhitePiece);
  }
}
