package piece;

import util.Utils;

public class Move {
  public final int fromR;
  public final int fromC;
  public final int toR;
  public final int toC;

  public Move(int fromR, int fromC, int toR, int toC) {
    this.fromR = fromR;
    this.fromC = fromC;
    this.toR = toR;
    this.toC = toC;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Move)) return false;
    Move other = (Move)o;
    return fromR == other.fromR && fromC == other.fromC
            && toR == other.toR && toC == other.toC;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(fromR + fromC + toR + toC);
  }

  public void execute(ChessPiece[][] board) {
    board[toR][toC] = board[fromR][fromC];
    board[fromR][fromC] = null;

    if (board[toR][toC] instanceof Pawn
            && board[fromR][toC] instanceof Pawn
            && board[toR][toC].side() != board[fromR][toC].side()
            && ((Pawn)board[fromR][toC]).justAdvancedTwoSquares()) {
      board[fromR][toC] = null;
    } else if (board[toR][toC] instanceof King && Math.abs(toR - fromR) > 1) {
      if (Utils.inBounds(toR, toC + 1) && board[toR][toC + 1] instanceof Rook) {
        board[toR][toC - 1] = board[toR][toC + 1];
        board[toR][toC + 1] = null;
      } else {
        board[toR][toC + 1] = board[toR][toC - 2];
        board[toR][toC - 2] = null;
      }
    }
  }
}
