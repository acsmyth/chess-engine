package piece;

public class Queen extends AbstractChessPiece implements ChessPiece {
  public Queen(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
  }

  @Override
  protected boolean moveFitsLegalMotionPattern(int toRow, int toCol) {
    return false;
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new Queen(r, c, isWhitePiece);
  }
}
