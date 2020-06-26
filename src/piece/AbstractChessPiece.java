package piece;

public abstract class AbstractChessPiece implements ChessPiece {
  private final boolean isWhitePiece;

  public AbstractChessPiece(boolean isWhitePiece) {
    this.isWhitePiece = isWhitePiece;
  }

  public boolean side() {
    return isWhitePiece;
  }
}
