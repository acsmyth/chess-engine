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
  public void makeMove(int fromCol, int fromRow, int toCol, int toRow) {
    if (board.isLegalMove(fromCol, fromRow, toCol, toRow, whiteTurn)) {
      board.makeMove(fromCol, fromRow, toCol, toRow);
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
