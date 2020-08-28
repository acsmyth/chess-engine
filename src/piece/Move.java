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
    return Integer.hashCode(fromR*1000 + fromC*100 + toR*9 + toC*7);
  }

  public void execute(ChessPiece[][] board) {
    execute(board, false);
  }

  public void execute(ChessPiece[][] board, boolean editMode) {
    if (this instanceof NullMove) return;
    board[toR][toC] = board[fromR][fromC];
    board[fromR][fromC] = null;
    if (editMode) return;

    if (board[toR][toC] instanceof Pawn
            && board[fromR][toC] instanceof Pawn
            && board[toR][toC].side() != board[fromR][toC].side()
            && ((Pawn)board[fromR][toC]).justAdvancedTwoSquares()) {
      board[fromR][toC] = null;
    } else if (board[toR][toC] instanceof King && Math.abs(toC - fromC) == 2) {
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
      // king castled
      ((King)board[toR][toC]).castle();
    } else if (board[toR][toC] instanceof Pawn && ((board[toR][toC].side() && toR == 0)
            || (!board[toR][toC].side() && toR == 7))) {
      board[toR][toC] = new Queen(toR, toC, board[toR][toC].side());
    }
  }

  public String notateMove(ChessPiece[][] board, boolean side) {
    if (this instanceof NullMove) return "NULL";
    String pgn = "";

    boolean isAmbiguous = false;
    for (Move m : new ChessBoardImpl(board).getLegalMoves(side)) {
      if (!this.equals(m) && m.toR == toR && m.toC == toC
              && board[m.fromR][m.fromC].display().equals(board[fromR][fromC].display())) {
        isAmbiguous = true;
      }
    }

    if (board[fromR][fromC] instanceof Pawn
            && board[fromR][toC] instanceof Pawn
            && board[fromR][fromC].side() != board[fromR][toC].side()
            && ((Pawn) board[fromR][toC]).justAdvancedTwoSquares()) {
      return pgnCol(fromC) + "x" + pgnCol(toC) + pgnRow(toR);
    } else if (board[fromR][fromC] instanceof King && Math.abs(toC - fromC) == 2) {
      if (Utils.inBounds(toR, toC + 1) && board[toR][toC + 1] instanceof Rook) {
        pgn = "O-O";
      } else {
        pgn = "O-O-O";
      }
    } else if (board[fromR][fromC] instanceof Pawn && ((board[fromR][fromC].side() && toR == 0)
            || (!board[fromR][fromC].side() && toR == 7))) {
      if (isCaptureMove(board)) {
        pgn = pgnCol(fromC) + "x" + pgnCol(toC) + pgnRow(toR) + "=Q";
      } else {
        pgn = pgnCol(toC) + pgnRow(toR) + "=Q";
      }
      // if pawn non capture move, just e4
    } else if (board[fromR][fromC] instanceof Pawn && !isCaptureMove(board)) {
      pgn = pgnCol(toC) + pgnRow(toR);
      // if pawn capture move, exd4
    } else if (board[fromR][fromC] instanceof Pawn) {
      pgn = pgnCol(fromC) + "x" + pgnCol(toC) + pgnRow(toR);
      // if non-pawn ambiguous non capture move, Nbc4
    } else if (!isCaptureMove(board) && isAmbiguous) {
      pgn = board[fromR][fromC].display() + pgnCol(fromC) + pgnCol(toC) + pgnRow(toR);
      // if non-pawn non-ambiguous non capture move, Nc4
    } else if (!isCaptureMove(board)) {
      pgn = board[fromR][fromC].display() + pgnCol(toC) + pgnRow(toR);
    // if non-pawn capture move, Nbxc4
    } else if (isCaptureMove(board) && isAmbiguous) {
      pgn = board[fromR][fromC].display() + pgnCol(fromC) + "x" + pgnCol(toC) + pgnRow(toR);
    // if non-pawn capture move, Nxc4
    } else if (isCaptureMove(board)) {
      pgn = board[fromR][fromC].display() + "x" + pgnCol(toC) + pgnRow(toR);
    }

    // additions:
    // check -> +
    // mate --> #
    ChessBoard afterMoveMade = new ChessBoardImpl(board);
    afterMoveMade.makeMove(this);
    if (afterMoveMade.kingIsInCheck(!board[fromR][fromC].side())
            && !afterMoveMade.getLegalMoves(!board[fromR][fromC].side()).isEmpty()) {
      pgn += "+";
    } else if (afterMoveMade.kingIsInCheck(!board[fromR][fromC].side())) {
      pgn += "#";
    }
    return pgn;
  }

  private String pgnRow(int row) {
    return String.valueOf(8 - row);
  }

  private String pgnCol(int col) {
    switch (col) {
      case 0:
        return "a";
      case 1:
        return "b";
      case 2:
        return "c";
      case 3:
        return "d";
      case 4:
        return "e";
      case 5:
        return "f";
      case 6:
        return "g";
      case 7:
        return "h";
      default:
        throw new RuntimeException();
    }
  }

  @Override
  public String toString() {
    return fromR + " " + fromC + " --> " + toR + " " + toC;
  }

  private boolean isCaptureMove(ChessPiece[][] board) {
    return board[toR][toC] != null;
  }

  public boolean isCaptureMove(ChessBoard board) {
    ChessPiece[][] brd = board.getBoard();
    return brd[toR][toC] != null;
  }
}
