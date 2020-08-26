package game;

import java.util.List;
import piece.ChessPiece;
import piece.Move;
import util.Pos;

public interface ChessBoard {
  boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, boolean whiteTurn);

  boolean kingIsInCheck(boolean side);

  void makeMove(Move move);

  void makeMove(Move move, boolean editMode);

  void display();

  ChessPiece[][] getBoard();

  List<Move> getLegalMoves(boolean side);

  List<Move> getAttackMoves(boolean side);

  List<Move> getCaptureMoves(boolean side);

  Pos getKingPos(boolean side);

  String result();

  int numBoardStateRepeats();
}
