package piece;

import java.util.List;

public interface ChessPiece extends Cloneable {
  boolean side();

  int sideAsInt();

  boolean isLegalMove(int toRow, int toCol, ChessPiece[][] board);

  ChessPiece copy();

  ChessPiece create(int r, int c, boolean isWhitePiece);

  void updatePieceMoved(int toR, int toC);

  void updatePieceNotMoved(int toR, int toC);

  List<Move> getLegalMoves();
}
