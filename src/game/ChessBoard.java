package game;

import java.util.List;

import piece.ChessPiece;
import piece.Move;

public interface ChessBoard {
  boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, boolean whiteTurn);

  boolean kingIsInCheck(boolean side);

  void makeMove(Move move);

  void display();

  List<Move> getLegalMoves(boolean side);

  List<Move> getAttackMoves(boolean side);

  ChessPiece[][] getBoard();
}
