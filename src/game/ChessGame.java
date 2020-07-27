package game;

import java.util.List;

import bot.Bot;
import piece.Move;

public interface ChessGame {
  // Returns true if the move was successfully made
  boolean makeMove(int fromR, int fromC, int toR, int toC);

  // Returns true if the move was successfully made
  boolean makeComputerMove();

  void display();

  boolean isOver();

  ChessBoard getBoard();

  Move getPrevMove();

  double getPrevEval();

  boolean turn();

  String pgn();
}
