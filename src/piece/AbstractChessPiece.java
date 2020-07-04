package piece;

import java.util.List;
import game.ChessBoardImpl;

public abstract class AbstractChessPiece implements ChessPiece {
  protected int r;
  protected int c;
  private final boolean isWhitePiece;
  protected List<Move> cachedLegalMoves;
  protected List<Move> cachedAttackMoves;

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

  protected static boolean inBounds(int r, int c) {
    return r >= 0 && r < 8 && c >= 0 && c < 8;
  }

  @Override
  public List<Move> getLegalMoves(ChessPiece[][] board) {
    if (cachedLegalMoves != null) return cachedLegalMoves;
    cachedLegalMoves = calculateLegalMoves(board);
    return cachedLegalMoves;
  }

  public List<Move> getAttackMoves(ChessPiece[][] board) {
    if (cachedAttackMoves != null) return cachedAttackMoves;
    cachedAttackMoves = calculateAttackMoves(board);
    return cachedAttackMoves;
  }

  protected abstract List<Move> calculateLegalMoves(ChessPiece[][] board);

  protected abstract List<Move> calculateAttackMoves(ChessPiece[][] board);
}