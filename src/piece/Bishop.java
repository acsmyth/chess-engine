package piece;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends AbstractChessPiece implements ChessPiece {
  public Bishop(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
  }

  @Override
  protected boolean isLegalMoveIgnoringChecks(int toR, int toC, ChessPiece[][] board) {
    return getLegalMoves(board).contains(new Move(r, c, toR, toC));
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new Bishop(r, c, isWhitePiece);
  }

  @Override
  public List<Move> calculateLegalMovesIgnoringChecks(ChessPiece[][] board) {
    if (cachedLegalMoves != null) return cachedLegalMoves;
    List<Move> newLegalMoves = new ArrayList<>();
    addMoves(-1, -1, board, newLegalMoves);
    addMoves(1, -1, board, newLegalMoves);
    addMoves(-1, 1, board, newLegalMoves);
    addMoves(1, 1, board, newLegalMoves);
    cachedLegalMoves = newLegalMoves;
    return newLegalMoves;
  }

  private void addMoves(int rowInc, int colInc, ChessPiece[][] board, List<Move> newLegalMoves) {
    int i = r + rowInc;
    int p = c + colInc;
    while (inBounds(i, p) && board[i][i] == null) {
      newLegalMoves.add(new Move(r, c, i, p));
      i += rowInc;
      p += colInc;
    }
    if (inBounds(i, p) && side() != board[i][i].side()) {
      newLegalMoves.add(new Move(r, c, i, p));
    }
  }

  @Override
  protected List<Move> calculateAttackMoves(ChessPiece[][] board) {
    return null;
  }
}
