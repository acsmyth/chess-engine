package game;

import bot.Bot;
import bot.MinimaxWithABPruningBot;
import piece.Move;

public class ChessGameImpl implements ChessGame {
  private final ChessBoard board;
  private boolean turn;
  private final Bot engine;
  private Move prevMove;

  public ChessGameImpl() {
    board = new ChessBoardImpl();
    turn = true;
    engine = new MinimaxWithABPruningBot();
    prevMove = null;
  }

  @Override
  public boolean makeMove(int fromR, int fromC, int toR, int toC) {
    if (board.isLegalMove(fromR, fromC, toR, toC, turn)) {
      Move m = new Move(fromR, fromC, toR, toC);
      board.makeMove(m);
      turn = !turn;
      prevMove = m;
      return true;
    }
    return false;
  }

  @Override
  public void makeComputerMove() {
    Move computerMove = engine.chooseMove(board, turn);
    makeMove(computerMove.fromR, computerMove.fromC,
            computerMove.toR, computerMove.toC);
  }

  @Override
  public void display() {
    board.display();
  }

  @Override
  public boolean isOver() {
    return board.kingIsInCheck(turn) && board.getLegalMoves(!turn).isEmpty();
  }

  @Override
  public ChessBoard getBoard() {
    return board;
  }

  @Override
  public Move getPrevMove() {
    return prevMove;
  }

  @Override
  public double getPrevEval() {
    return engine.getPrevEval();
  }
}
