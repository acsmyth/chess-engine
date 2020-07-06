package game;

public interface ChessGame {
  void makeMove(int fromR, int fromC, int toR, int toC);

  void makeComputerMove();

  void display();
}
