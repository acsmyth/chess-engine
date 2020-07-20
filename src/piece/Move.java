package piece;

import game.ChessBoard;
import game.ChessBoardImpl;
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
    if (this instanceof NullMove) return;
    board[toR][toC] = board[fromR][fromC];
    board[fromR][fromC] = null;

    if (board[toR][toC] instanceof Pawn
            && board[fromR][toC] instanceof Pawn
            && board[toR][toC].side() != board[fromR][toC].side()
            && ((Pawn)board[fromR][toC]).justAdvancedTwoSquares()) {
      board[fromR][toC] = null;
    } else if (board[toR][toC] instanceof King && Math.abs(toC - fromC) > 1) {
      if (Utils.inBounds(toR, toC + 1) && board[toR][toC + 1] instanceof Rook) {
        board[toR][toC - 1] = board[toR][toC + 1];
        board[toR][toC + 1] = null;
        // update r,c of rook
        board[toR][toC - 1].updatePieceMoved(toR, toC - 1);
      } else {
        board[toR][toC + 1] = board[toR][toC - 2];
        board[toR][toC - 2] = null;
        // update r,c of rook
        board[toR][toC + 1].updatePieceMoved(toR, toC + 1);
      }
    } else if (board[toR][toC] instanceof Pawn && ((board[toR][toC].side() && toR == 0)
            || (!board[toR][toC].side() && toR == 7))) {
      board[toR][toC] = new Queen(toR, toC, board[toR][toC].side());
    }
  }

  @Override
  public String toString() {
    return fromR + " " + fromC + " --> " + toR + " " + toC;
  }

  public boolean isCaptureMove(ChessBoard board) {
    ChessPiece[][] brd = board.getBoard();
    return brd[toR][toC] != null;
  }
}
