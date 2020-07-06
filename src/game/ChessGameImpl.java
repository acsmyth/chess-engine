package game;

import bot.Bot;
import bot.SimpleMinimaxBot;
import piece.Move;

public class ChessGameImpl implements ChessGame {
  private final ChessBoard board;
  private boolean whiteTurn;
  private final Bot engine;

  public ChessGameImpl() {
    board = new ChessBoardImpl();
    whiteTurn = true;
    engine = new SimpleMinimaxBot();
  }

  @Override
  public void makeMove(int fromR, int fromC, int toR, int toC) {
    if (board.isLegalMove(fromR, fromC, toR, toC, whiteTurn)) {
      board.makeMove(new Move(fromR, fromC, toR, toC));
      whiteTurn = !whiteTurn;
    }
  }

  @Override
  public void makeComputerMove() {
    Move computerMove = engine.chooseMove(board, whiteTurn);
    board.makeMove(computerMove);
  }

  @Override
  public void display() {
    board.display();
  }
}
