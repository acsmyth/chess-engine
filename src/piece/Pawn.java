package piece;

import java.util.ArrayList;
import java.util.Arrays;
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
  public List<Move> calculateLegalMovesIgnoringChecks(ChessPiece[][] board) {
    List<Move> legalMoves = new ArrayList<>();
    if (board[r - sideAsInt()][c] == null) {
      legalMoves.add(new Move(r, c, r - sideAsInt(), c));
    }
    if (board[r - sideAsInt()][c] == null && board[r - 2 * sideAsInt()][c] == null
            && !hasMoved) {
      legalMoves.add(new Move(r, c, r - 2 * sideAsInt(), c));
    }
    if ((board[r - sideAsInt()][c - 1] != null && side() != board[r - sideAsInt()][c - 1].side())
            || (board[r][c - 1] != null && side() != board[r][c - 1].side()
            && board[r][c - 1] instanceof Pawn && ((Pawn)board[r][c - 1]).justAdvancedTwoSquares)) {
      legalMoves.add(new Move(r, c, r - sideAsInt(), c - 1));
    }
    if ((board[r - sideAsInt()][c + 1] != null && side() != board[r - sideAsInt()][c + 1].side())
            || (board[r][c + 1] != null && side() != board[r][c + 1].side()
            && board[r][c + 1] instanceof Pawn && ((Pawn)board[r][c + 1]).justAdvancedTwoSquares)) {
      legalMoves.add(new Move(r, c, r - sideAsInt(), c + 1));
    }
    return legalMoves;
  }

  @Override
  protected List<Move> calculateAttackMoves(ChessPiece[][] board) {
    return Arrays.asList(
            new Move(r, c, r - sideAsInt(), c - 1),
            new Move(r, c, r - sideAsInt(), c + 1));
  }
}
