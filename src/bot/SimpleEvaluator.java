package bot;

import game.ChessBoard;
import piece.ChessPiece;

public class SimpleEvaluator implements Evaluator {
  @Override
  public double evaluate(ChessBoard board) {
    ChessPiece[][] brd = board.getBoard();
    double eval = 0;
    for (int r=0;r<8;r++) {
      for (int c=0;c<8;c++) {
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
        }
      }
    }
    return eval;
  }
}
