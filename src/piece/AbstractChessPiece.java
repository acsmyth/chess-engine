package piece;

import game.ChessBoardImpl;

public abstract class AbstractChessPiece implements ChessPiece {
  protected int r;
  protected int c;
  private final boolean isWhitePiece;

  public AbstractChessPiece(int r, int c, boolean isWhitePiece) {
    this.r = r;
    this.c = c;
    this.isWhitePiece = isWhitePiece;
  }

  @Override
  public boolean side() {
    return isWhitePiece;
  }

  @Override
  public int sideAsInt() {
    return isWhitePiece ? 1 : -1;
  }

  @Override
  public void update(int r, int c) {
    this.r = r;
    this.c = c;
  }

  @Override
  public boolean isLegalMove(int toRow, int toCol, ChessPiece[][] board) {
    return isLegalMoveIgnoringChecks(toRow, toCol, board)
            && new ChessBoardImpl(board).kingIsInCheck();
  }

  abstract protected boolean isLegalMoveIgnoringChecks(int toRow, int toCol, ChessPiece[][] board);

  @Override
  public ChessPiece copy() {
    return create(r, c, isWhitePiece);
  }
}
