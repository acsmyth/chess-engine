package piece;

import java.util.List;

public class Pawn extends AbstractChessPiece implements ChessPiece {
  private boolean hasMoved;
  private boolean justAdvancedTwoSquares;

  public Pawn(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
    hasMoved = false;
    justAdvancedTwoSquares = false;
  }

  @Override
  protected boolean isLegalMoveIgnoringChecks(int toRow, int toCol, ChessPiece[][] board) {
    ChessPiece toPiece = board[toRow][toCol];
    if (c == toCol) {
      // if moving forward, must be no piece in either spot
      return (r - toRow == sideAsInt() && toPiece == null)
              ||
              (r - toRow == 2 * sideAsInt() && toPiece == null
              && board[toRow - sideAsInt()][toCol] == null);
    } else {
      // opposite side piece must be there
      return (r - toRow == sideAsInt() && Math.abs(c - toCol) == 1
              && toPiece != null && side() != toPiece.side())
              ||
              (board[r][toCol] != null && side() != board[r][toCol].side()
                      && board[r][toCol] instanceof Pawn
                      && ((Pawn)board[r][toCol]).justAdvancedTwoSquares);
    }
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new Pawn(r, c, isWhitePiece);
  }

  @Override
  public void updatePieceMoved(int toR, int toC) {
    hasMoved = true;
    justAdvancedTwoSquares = r - toR == 2 * sideAsInt();
    super.updatePieceMoved(r, c);
  }

  @Override
  public List<Move> getLegalMoves() {
    return null;
  }
}
