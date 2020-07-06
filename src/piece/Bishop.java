package piece;

import java.util.ArrayList;
import java.util.List;
import util.DynamicChessPieceUtils;

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
  public String display() {
    return "B";
  }

  @Override
  public List<Move> calculateLegalMovesIgnoringChecks(ChessPiece[][] board) {
    List<Move> newLegalMoves = new ArrayList<>();
    addMoves(board, newLegalMoves, false);
    return newLegalMoves;
  }

  private void addMoves(ChessPiece[][] board, List<Move> moves, boolean includeAttackMoves) {
    DynamicChessPieceUtils.addMoves(side(), r, c, -1, -1, board, moves, includeAttackMoves);
    DynamicChessPieceUtils.addMoves(side(), r, c, 1, -1, board, moves, includeAttackMoves);
    DynamicChessPieceUtils.addMoves(side(), r, c, -1, 1, board, moves, includeAttackMoves);
    DynamicChessPieceUtils.addMoves(side(), r, c, 1, 1, board, moves, includeAttackMoves);
  }

  @Override
  protected List<Move> calculateAttackMoves(ChessPiece[][] board) {
    List<Move> newAttackMoves = new ArrayList<>();
    addMoves(board, newAttackMoves, true);
    return newAttackMoves;
  }
}
