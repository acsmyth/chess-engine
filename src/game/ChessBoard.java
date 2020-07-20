package game;

import java.util.List;
import java.util.Map;

import piece.ChessPiece;
import piece.Move;

public interface ChessBoard {
  boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, boolean whiteTurn);

  boolean kingIsInCheck(boolean side);

  void makeMove(Move move);

  void display();

  ChessPiece[][] getBoard();

  List<Move> getLegalMoves(boolean side);

  List<Move> getAttackMoves(boolean side);

  List<Move> getCaptureMoves(boolean side);
}
