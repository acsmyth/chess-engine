package piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.Utils;

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
    return getLegalMoves(board).contains(new Move(r, c, toRow, toCol));
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new Pawn(r, c, isWhitePiece);
  }

  @Override
  public void updatePieceMoved(int toR, int toC) {
    hasMoved = true;
    justAdvancedTwoSquares = r - toR == 2 * sideAsInt();
    super.updatePieceMoved(toR, toC);
  }

  @Override
  public String display() {
    return "P";
  }

  @Override
  public List<Move> calculateLegalMovesIgnoringChecks(ChessPiece[][] board) {
    List<Move> legalMoves = new ArrayList<>();
    if (Utils.inBounds(r - sideAsInt(), c)
            && board[r - sideAsInt()][c] == null) {
      legalMoves.add(new Move(r, c, r - sideAsInt(), c));
    }
    if (Utils.inBounds(r - sideAsInt(), c) && Utils.inBounds(r - 2 * sideAsInt(), c)
            && board[r - sideAsInt()][c] == null && board[r - 2 * sideAsInt()][c] == null
            && !hasMoved) {
      legalMoves.add(new Move(r, c, r - 2 * sideAsInt(), c));
    }
    if (Utils.inBounds(r - sideAsInt(), c - 1)
            && ((board[r - sideAsInt()][c - 1] != null
            && side() != board[r - sideAsInt()][c - 1].side())
            || (board[r][c - 1] != null && side() != board[r][c - 1].side()
            && board[r][c - 1] instanceof Pawn && ((Pawn)board[r][c - 1]).justAdvancedTwoSquares))) {
      legalMoves.add(new Move(r, c, r - sideAsInt(), c - 1));
    }
    if (Utils.inBounds(r - sideAsInt(), c - 1)
            && ((board[r - sideAsInt()][c + 1] != null
            && side() != board[r - sideAsInt()][c + 1].side())
            || (board[r][c + 1] != null && side() != board[r][c + 1].side()
            && board[r][c + 1] instanceof Pawn && ((Pawn)board[r][c + 1]).justAdvancedTwoSquares))) {
      legalMoves.add(new Move(r, c, r - sideAsInt(), c + 1));
    }
    return legalMoves;
  }

  @Override
  protected List<Move> calculateAttackMoves(ChessPiece[][] board) {
    List<Move> potentialAttackMoves = new ArrayList<>(Arrays.asList(
            new Move(r, c, r - sideAsInt(), c - 1),
            new Move(r, c, r - sideAsInt(), c + 1)));
    potentialAttackMoves.removeIf(move -> !Utils.inBounds(move.toR, move.toC));
    return potentialAttackMoves;
  }

  public boolean justAdvancedTwoSquares() {
    return justAdvancedTwoSquares;
  }
}
