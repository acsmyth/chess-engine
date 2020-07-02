package piece;

import java.util.ArrayList;
import java.util.List;

import game.ChessBoardImpl;

public abstract class AbstractChessPiece implements ChessPiece {
  protected int r;
  protected int c;
  private final boolean isWhitePiece;
  protected List<Move> cachedLegalMoves;

  public AbstractChessPiece(int r, int c, boolean isWhitePiece) {
    this.r = r;
    this.c = c;
    this.isWhitePiece = isWhitePiece;
    cachedLegalMoves = null;
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
  public void updatePieceMoved(int toR, int toC) {
    r = toR;
    c = toC;
    cachedLegalMoves = null;
  }

  @Override
  public void updatePieceNotMoved(int toR, int toC) {
    cachedLegalMoves = null;
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
