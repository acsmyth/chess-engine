package piece;

public class Pawn extends AbstractChessPiece implements ChessPiece {
  public Pawn(boolean isWhitePiece) {
    super(isWhitePiece);
  }

  @Override
  protected boolean moveFitsLegalMotionPattern(int toRow, int toCol) {
    return false;
  }

  @Override
  public ChessPiece create(boolean isWhitePiece) {
    return new Pawn(isWhitePiece);
  }
}
