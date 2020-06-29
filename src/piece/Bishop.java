package piece;

public class Bishop extends AbstractChessPiece implements ChessPiece {
  public Bishop(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
  }

  @Override
  protected boolean isLegalMoveIgnoringChecks(int toRow, int toCol, ChessPiece[][] board) {
    return false;
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new Bishop(r, c, isWhitePiece);
  }
}
