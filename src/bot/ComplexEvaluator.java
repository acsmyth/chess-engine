package bot;

import game.ChessBoard;
import piece.ChessPiece;
import piece.King;
import piece.Move;
import piece.Queen;
import piece.Rook;

public class ComplexEvaluator implements Evaluator {
  @Override
  public double evaluate(ChessBoard board) {
    ChessPiece[][] brd = board.getBoard();
    double eval = 0;
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (brd[r][c] != null) {
          double valChange = 0;
          switch (brd[r][c].getClass().getSimpleName()) {
            case "Pawn":
              valChange = 1;
              break;
            case "Knight":
              valChange = 3;
              break;
            case "Bishop":
              valChange = 3;
              break;
            case "Rook":
              valChange = 5;
              break;
            case "Queen":
              valChange = 9;
              break;
            case "King":
              valChange = 10000;
              break;
          }
          eval += valChange * brd[r][c].sideAsInt();

          if (!(brd[r][c] instanceof King) && !(brd[r][c] instanceof Queen) && !(brd[r][c] instanceof Rook)) {
            for (Move m : brd[r][c].getAttackMoves(board.getBoard())) {
              if (inCenter(m.toR, m.toC)) {
                eval += brd[r][c].sideAsInt() / 15.0;
              }
              if (inInnerRingOutsideCenter(m.toR, m.toC)) {
                eval += brd[r][c].sideAsInt() / 30.0;
              }
            }
          }

        }
      }
    }
    return eval;
  }

  private boolean inCenter(int toR, int toC) {
    return (toR == 3 || toR == 4) && (toC == 3 || toC == 4);
  }

  private boolean inInnerRingOutsideCenter(int toR, int toC) {
    return ((toR == 2 || toR == 5) && (toC >= 2 && toC <= 5))
            || ((toC == 2 || toC == 5) && (toR >= 2 && toR <= 5));
  }
}
