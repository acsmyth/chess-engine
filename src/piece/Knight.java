package piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knight extends AbstractChessPiece implements ChessPiece {
  public Knight(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
  }

  @Override
  protected boolean isLegalMoveIgnoringChecks(int toRow, int toCol, ChessPiece[][] board) {
    return (Math.abs(r - toRow) == 2 && Math.abs(c - toCol) == 1)
            || (Math.abs(r - toRow) == 1 && Math.abs(c - toCol) == 2);
  }

  @Override
  protected List<Move> calculateLegalMovesIgnoringChecks(ChessPiece[][] board) {
    List<Move> potentialLegalMoves = getAttackMoves(board);
    potentialLegalMoves.removeIf(move -> !inBounds(move.toR, move.toC)
            || (board[move.toR][move.toC] != null
            && side() == board[move.toR][move.toC].side()));
    return potentialLegalMoves;
  }

  @Override
  protected List<Move> calculateAttackMoves(ChessPiece[][] board) {
    List<Move> potentialAttackMoves = new ArrayList<>(Arrays.asList(new Move(r, c, r-1, c-2),
            new Move(r, c, r+1, c-2), new Move(r, c, r-1, c+2), new Move(r, c, r+1, c+2),
            new Move(r, c, r-2, c+1), new Move(r, c, r+2, c+1), new Move(r, c, r-2, c-1),
            new Move(r, c, r+2, c-1)));
    potentialAttackMoves.removeIf(move -> !inBounds(move.toR, move.toC));
    return potentialAttackMoves;
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new Knight(r, c, isWhitePiece);
  }

  @Override
  public String display() {
    return "N";
  }
}
