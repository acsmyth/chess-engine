package game;

import piece.Bishop;
import piece.ChessPiece;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Queen;
import piece.Rook;

public class ChessBoardImpl implements ChessBoard {
  ChessPiece[][] board;

  public ChessBoardImpl() {
    board = new ChessPiece[8][8];
    initBoard(0, 1, false);
    initBoard(7, -1, true);
  }

  private void initBoard(int r, int delta, boolean isWhitePiece) {
    board[r][0] = new Rook(isWhitePiece);
    board[r][1] = new Knight(isWhitePiece);
    board[r][2] = new Bishop(isWhitePiece);
    board[r][3] = new Queen(isWhitePiece);
    board[r][4] = new King(isWhitePiece);
    board[r][5] = new Bishop(isWhitePiece);
    board[r][6] = new Knight(isWhitePiece);
    board[r][7] = new Rook(isWhitePiece);
    for (int p=0; p<8; p++) {
      board[r+delta][p] = new Pawn(isWhitePiece);
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
    // finally, check that the move doesn't lead the moving side's king to be in check
  }

  private boolean isOccupied(int fromRow, int fromCol) {
    return board[fromRow][fromCol] != null;
  }

  private boolean inBounds(int r, int c) {
    return r >= 0 && r < 8 && c >= 0 && c < 8;
  }

  @Override
  public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {

  }
}
