package game;

public interface ChessGame {
  void makeMove(int fromCol, int fromRow, int toCol, int toRow);

  void makeComputerMove();
}
