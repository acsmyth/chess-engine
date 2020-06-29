package piece;

public class King extends AbstractChessPiece implements ChessPiece {
  public King(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
  }


  @Override
  protected boolean isLegalMoveIgnoringChecks(int toRow, int toCol, ChessPiece[][] board) {
    return toRow >= r - 1 && toRow <= r + 1
            && toCol >= c - 1 && toCol <= c + 1;
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new King(r, c, isWhitePiece);
  }
}
