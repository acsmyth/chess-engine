package piece;

public class Rook extends AbstractChessPiece implements ChessPiece {
  public Rook(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
  }

  @Override
  protected boolean moveFitsLegalMotionPattern(int toRow, int toCol) {
    return false;
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new Rook(r, c, isWhitePiece);
  }
}
