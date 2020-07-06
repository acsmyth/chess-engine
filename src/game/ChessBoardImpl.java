package game;

import java.util.List;
import piece.Bishop;
import piece.ChessPiece;
import piece.King;
import piece.Knight;
import piece.Move;
import piece.Pawn;
import piece.Queen;
import piece.Rook;
import util.Pos;

public class ChessBoardImpl implements ChessBoard {
  private final ChessPiece[][] board;
  private Pos whiteKingPos;
  private Pos blackKingPos;

  public ChessBoardImpl() {
    board = new ChessPiece[8][8];
    initBoard(0, 1, false);
    initBoard(7, -1, true);
    whiteKingPos = getKingPos(true);
    blackKingPos = getKingPos(false);
  }

  public ChessBoardImpl(ChessPiece[][] board) {
    this.board = deepClone(board);
    whiteKingPos = getKingPos(true);
    blackKingPos = getKingPos(false);
  }

  private ChessPiece[][] deepClone(ChessPiece[][] arr) {
    ChessPiece[][] newArr = new ChessPiece[arr.length][arr[0].length];
    for (int i = 0; i < arr.length; i++) {
      for (int p = 0; p < arr[0].length; p++) {
        if (arr[i][p] != null) {
          newArr[i][p] = arr[i][p].copy();
        }
      }
    }
    return newArr;
  }

  private void initBoard(int r, int delta, boolean isWhitePiece) {
    board[r][0] = new Rook(r, 0, isWhitePiece);
    board[r][1] = new Knight(r, 1, isWhitePiece);
    board[r][2] = new Bishop(r, 2, isWhitePiece);
    board[r][3] = new Queen(r, 3, isWhitePiece);
    board[r][4] = new King(r, 4, isWhitePiece);
    board[r][5] = new Bishop(r, 5, isWhitePiece);
    board[r][6] = new Knight(r, 6, isWhitePiece);
    board[r][7] = new Rook(r, 7, isWhitePiece);
    for (int p = 0; p < 8; p++) {
      board[r + delta][p] = new Pawn(r + delta, p, isWhitePiece);
    }
  }

  @Override
  public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, boolean turn) {
    // from and to must be valid positions
    // must be a piece in from, and piece in from must be same color as turn
    // must either be no piece in to, or piece of opposite color
    if (!inBounds(fromRow, fromCol) || !inBounds(toRow, toCol)
            || !isOccupied(fromRow, fromCol) || board[fromRow][fromCol].side() != turn
            || (isOccupied(toRow, toCol) && board[toRow][toCol].side() == turn)) {
      return false;
    }
    // now, delegate to the piece to make sure its a valid move format of the piece
    return board[fromRow][fromCol].isLegalMove(toRow, toCol, board);
  }

  private boolean isOccupied(int fromRow, int fromCol) {
    return board[fromRow][fromCol] != null;
  }

  private boolean inBounds(int r, int c) {
    return r >= 0 && r < 8 && c >= 0 && c < 8;
  }

  @Override
  public boolean kingIsInCheck(boolean side) {
    Pos kingPos = side ? whiteKingPos : blackKingPos;
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (board[r][c] != null && side != board[r][c].side()) {
          List<Move> attackMoves = board[r][c].getAttackMoves(board);
          for (Move move : attackMoves) {
            if (kingPos.r == move.toR && kingPos.c == move.toC) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  private Pos getKingPos(boolean side) {
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (board[r][c] != null && board[r][c] instanceof King && board[r][c].side() == side) {
          return new Pos(r, c);
        }
      }
    }
    return null;
  }

  @Override
  public void makeMove(Move move) {
    move.execute(board);
    updateBoard(move);
  }

  private void updateBoard(Move move) {
    board[move.toR][move.toC].updatePieceMoved(move.toR, move.toC);
    updateKingPos(move);
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (board[r][c] != null && r != move.toR && c != move.toC) {
          board[r][c].updatePieceNotMoved(r, c);
        }
      }
    }
  }

  private void updateKingPos(Move move) {
    if (board[move.toR][move.toC] instanceof King) {
      if (board[move.toR][move.toC].side()) {
        whiteKingPos = new Pos(move.toR, move.toC);
      } else {
        blackKingPos = new Pos(move.toR, move.toC);
      }
    }
  }

  @Override
  public void display() {
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (board[r][c] == null) {
          System.out.print("\u001B[37m" + "☐" + "\u001B[0m");
        } else {
          System.out.print((board[r][c].side() ? "\u001B[34m" : "\u001B[31m")
                  + board[r][c].display() + "\u001B[0m");
        }
        if (c < 7) {
          System.out.print(" ");
        }
      }
      if (r < 7) {
        System.out.print("\n");
      }
    }
  }
}
