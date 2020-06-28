package game;

public class ChessGameImpl implements ChessGame {
  private final ChessBoard board;
  private boolean whiteTurn;

  public ChessGameImpl() {
    board = new ChessBoardImpl();
    whiteTurn = true;
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
    // TODO
  }
}
