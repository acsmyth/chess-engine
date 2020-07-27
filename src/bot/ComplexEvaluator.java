package bot;

import game.ChessBoard;
import piece.ChessPiece;
import piece.King;
import piece.Move;
import piece.Queen;
import piece.Rook;
import util.Pos;

public class ComplexEvaluator implements Evaluator {
  @Override
  public double evaluate(ChessBoard board, boolean turn) {
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
              valChange = 3.05;
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
                eval += brd[r][c].sideAsInt() / 10.0;
              }
              if (inInnerRingOutsideCenter(m.toR, m.toC)) {
                eval += brd[r][c].sideAsInt() / 20.0;
              }
            }
          }

        }
      }
    }
    // tempo bonus
    eval += turn ? 0.25 : -0.25;

    // ability to castle - king hasn't moved yet
    Pos whiteKingPos = board.getKingPos(turn);
    boolean whiteKingHasMoved = ((King)brd[whiteKingPos.r][whiteKingPos.c]).hasMoved();
    Pos blackKingPos = board.getKingPos(turn);
    boolean blackKingHasMoved = ((King)brd[blackKingPos.r][blackKingPos.c]).hasMoved();

    eval += whiteKingHasMoved ? -0.25 : 0;
    eval += blackKingHasMoved ? 0.25 : 0;

    // however this will make it not want to castle, so need to offset it with
    // a benefit for rook mobility / not being blocked by the king
    boolean whiteHasCastled = ((King)brd[whiteKingPos.r][whiteKingPos.c]).hasCastled();
    boolean blackHasCastled = ((King)brd[blackKingPos.r][blackKingPos.c]).hasCastled();

    eval += whiteHasCastled ? 0.9 : 0;
    eval += blackHasCastled ? -0.9 : 0;

    // number of legal moves is slightly good - maybe excluding queen
    eval += board.getAttackMoves(true).size() / 30.0;
    eval += -board.getAttackMoves(false).size() / 30.0;



    // random
    eval += Math.random() < 0.5 ? Math.random() / 20 : -Math.random() / 20;

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
