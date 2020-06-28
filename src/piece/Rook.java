package piece;

public class Rook extends AbstractChessPiece implements ChessPiece {
  public Rook(boolean isWhitePiece) {
    super(isWhitePiece);
  }

  @Override
  protected boolean moveFitsLegalMotionPattern(int toRow, int toCol) {
    return false;
  }

  @Override
  public ChessPiece create(boolean isWhitePiece) {
    return new Rook(isWhitePiece);
  }
}
