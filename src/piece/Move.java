package piece;

import util.Utils;

public class Move {
  public final int fromR;
  public final int fromC;
  public final int toR;
  public final int toC;
  private final boolean enPassantMove;
  private final boolean castleMove;

  public Move(int fromR, int fromC, int toR, int toC) {
    this(fromR, fromC, toR, toC, false, false);
  }

  public Move(int fromR, int fromC, int toR, int toC, boolean enPassantMove, boolean castleMove) {
    this.fromR = fromR;
    this.fromC = fromC;
    this.toR = toR;
    this.toC = toC;
    this.enPassantMove = enPassantMove;
    this.castleMove = castleMove;
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

    if (enPassantMove) {
      board[fromR][toC] = null;
    } else if (castleMove) {
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
