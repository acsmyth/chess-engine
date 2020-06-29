package piece;

public interface ChessPiece extends Cloneable {
  boolean side();

  int sideAsInt();

  boolean isLegalMove(int toRow, int toCol, ChessPiece[][] board);

  ChessPiece copy();

  ChessPiece create(int r, int c, boolean isWhitePiece);

  void update(int r, int c);
}
