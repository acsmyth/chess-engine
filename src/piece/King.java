package piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.ChessBoardImpl;
import util.Pos;
import util.Utils;

public class King extends AbstractChessPiece implements ChessPiece {
  private boolean hasMoved;
  private boolean hasCastled;

  public King(int r, int c, boolean isWhitePiece) {
    this(r, c, isWhitePiece, false, false);
  }

  public King(int r, int c, boolean isWhitePiece, boolean hasMoved, boolean hasCastled) {
    super(r, c, isWhitePiece);
    this.hasMoved = hasMoved;
    this.hasCastled = hasCastled;
  }

  @Override
  protected boolean isLegalMoveIgnoringChecks(int toRow, int toCol, ChessPiece[][] board) {
    return calculateLegalMovesIgnoringChecks(board).contains(new Move(r, c, toRow, toCol));
  }

  @Override
  protected List<Move> calculateLegalMovesIgnoringChecks(ChessPiece[][] board) {
    List<Move> legalMovesIgnoringChecks = new ArrayList<>();
    Pos[] neighbors = {new Pos(r+1, c), new Pos(r-1, c), new Pos(r, c+1), new Pos(r, c-1),
            new Pos(r+1, c+1), new Pos(r-1, c-1), new Pos(r+1, c-1), new Pos(r-1, c+1)};
    for (Pos pos : neighbors) {
      if (Utils.inBounds(pos.r, pos.c) && (board[pos.r][pos.c] == null
              || side() != board[pos.r][pos.c].side())) {
        legalMovesIgnoringChecks.add(new Move(r, c, pos.r, pos.c));
      }
    }
    List<Move> attackMoves = new ChessBoardImpl(board).getAttackMoves(!side());
    List<Pos> passThroughSquaresShortCastle = Arrays.asList(new Pos(r, 4),
            new Pos(r, 5), new Pos(r, 6));
    List<Pos> squaresBetweenShortCastle = Arrays.asList(new Pos(r, 5), new Pos(r, 6));
    // short castle

    if (!hasMoved && board[r][7] instanceof Rook && !((Rook)board[r][7]).hasMoved()
            && !beingAttacked(passThroughSquaresShortCastle, attackMoves)
            && !piecesOccupying(squaresBetweenShortCastle, board)) {
      legalMovesIgnoringChecks.add(new Move(r, c, r, 6));
    }
    // long castle
    List<Pos> passThroughSquaresLongCastle = Arrays.asList(new Pos(r, 2),
            new Pos(r, 3), new Pos(r, 4));
    List<Pos> squaresBetweenLongCastle = Arrays.asList(new Pos(r, 1),
            new Pos(r, 2), new Pos(r, 3));
    if (!hasMoved && board[r][0] instanceof Rook && !((Rook)board[r][0]).hasMoved()
            && !beingAttacked(passThroughSquaresLongCastle, attackMoves)
            && !piecesOccupying(squaresBetweenLongCastle, board)) {
      legalMovesIgnoringChecks.add(new Move(r, c, r, 2));
    }
    return legalMovesIgnoringChecks;
  }

  private boolean beingAttacked(List<Pos> passThroughSquares, List<Move> attackMoves) {
    for (Move m : attackMoves) {
      for (Pos p : passThroughSquares) {
        if (p.r == m.toR && p.c == m.toC) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean piecesOccupying(List<Pos> passThroughSquaresLongCastle, ChessPiece[][] board) {
    for (Pos p : passThroughSquaresLongCastle) {
      if (board[p.r][p.c] != null) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected List<Move> calculateAttackMoves(ChessPiece[][] board) {
    List<Move> potentialAttackMoves = new ArrayList<>(Arrays.asList(new Move(r, c, r+1, c),
            new Move(r, c, r-1, c), new Move(r, c, r, c+1),
            new Move(r, c, r, c-1), new Move(r, c, r+1, c+1), new Move(r, c, r-1, c-1),
            new Move(r, c, r+1, c-1), new Move(r, c, r-1, c+1)));
    potentialAttackMoves.removeIf(move -> !Utils.inBounds(move.toR, move.toC));
    return potentialAttackMoves;
  }

  @Override
  public ChessPiece create(int r, int c, boolean isWhitePiece) {
    return new King(r, c, isWhitePiece, hasMoved, hasCastled);
  }

  @Override
  public String display() {
    return "K";
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(hasMoved ? 10 : -10);
  }

  @Override
  public void updatePieceMoved(int toR, int toC) {
    hasMoved = true;
    super.updatePieceMoved(toR, toC);
  }

  public boolean hasMoved() {
    return hasMoved;
  }

  public void castle() {
    hasCastled = true;
  }

  public boolean hasCastled() {
    return hasCastled;
  }
}
