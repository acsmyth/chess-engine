package piece;

public interface ChessPiece extends Cloneable {
  boolean side();

  boolean isLegalMove(int toRow, int toCol, ChessPiece[][] board);

  ChessPiece copy();

  ChessPiece create(boolean isWhitePiece);
}
