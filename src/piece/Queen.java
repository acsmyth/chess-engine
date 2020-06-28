package piece;

public class Queen extends AbstractChessPiece implements ChessPiece {
  public Queen(boolean isWhitePiece) {
    super(isWhitePiece);
  }

  @Override
  protected boolean moveFitsLegalMotionPattern(int toRow, int toCol) {
    return false;
  }

  @Override
  public ChessPiece create(boolean isWhitePiece) {
    return new Queen(isWhitePiece);
  }
}
