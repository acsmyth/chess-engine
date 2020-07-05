package piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.Pos;

public class King extends AbstractChessPiece implements ChessPiece {
  public King(int r, int c, boolean isWhitePiece) {
    super(r, c, isWhitePiece);
  }

  @Override
  protected boolean isLegalMoveIgnoringChecks(int toRow, int toCol, ChessPiece[][] board) {
    return toRow >= r - 1 && toRow <= r + 1
            && toCol >= c - 1 && toCol <= c + 1;
  }

  @Override
  protected List<Move> calculateLegalMovesIgnoringChecks(ChessPiece[][] board) {
    List<Move> legalMovesIgnoringChecks = new ArrayList<>();
    Pos[] neighbors = {new Pos(r+1, c), new Pos(r-1, c), new Pos(r, c+1), new Pos(r, c-1),
            new Pos(r+1, c+1), new Pos(r-1, c-1), new Pos(r+1, c-1), new Pos(r-1, c+1)};
    for (Pos pos : neighbors) {
      if (board[pos.r][pos.c] == null || side() != board[pos.r][pos.c].side()) {
        legalMovesIgnoringChecks.add(new Move(r, c, pos.r, pos.c));
      }
    }
    return legalMovesIgnoringChecks;
  }

  @Override
  protected List<Move> calculateAttackMoves(ChessPiece[][] board) {
    return Arrays.asList(new Move(r, c, r+1, c), new Move(r, c, r-1, c), new Move(r, c, r, c+1),
            new Move(r, c, r, c-1), new Move(r, c, r+1, c+1), new Move(r, c, r-1, c-1),
            new Move(r, c, r+1, c-1), new Move(r, c, r-1, c+1));
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new King(r, c, isWhitePiece);
  }
}
