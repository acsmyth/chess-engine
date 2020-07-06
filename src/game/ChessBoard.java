package game;

import piece.Move;

public interface ChessBoard {
  boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, boolean whiteTurn);

  void makeMove(int fromRow, int fromCol, int toRow, int toCol);

  boolean kingIsInCheck(boolean side);

  void makeMove(Move move);

  void display();
}
