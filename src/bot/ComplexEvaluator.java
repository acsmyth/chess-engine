package bot;

import game.ChessBoard;
import piece.ChessPiece;
import piece.King;
import piece.Knight;
import piece.Move;
import piece.Pawn;
import piece.Queen;
import piece.Rook;
import util.Pos;
import util.Utils;

public class ComplexEvaluator implements Evaluator {
  @Override
  public double evaluate(ChessBoard board, boolean turn) {
    ChessPiece[][] brd = board.getBoard();
    double totalMaterial = 0;
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
              valChange = 3.1;
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
          if (!(brd[r][c] instanceof King)) {
            totalMaterial += valChange;
          }


          if (!(brd[r][c] instanceof King) && !(brd[r][c] instanceof Queen)
                  && !(brd[r][c] instanceof Rook)) {
            for (Move m : brd[r][c].getAttackMoves(board.getBoard())) {
              if (inCenter(m.toR, m.toC)) {
                eval += brd[r][c].sideAsInt() / 8.0;
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

    // if king moves before castling, very bad
    Pos whiteKingPos = board.getKingPos(turn);
    boolean whiteKingHasMoved = ((King)brd[whiteKingPos.r][whiteKingPos.c]).hasMoved();
    Pos blackKingPos = board.getKingPos(turn);
    boolean blackKingHasMoved = ((King)brd[blackKingPos.r][blackKingPos.c]).hasMoved();
    boolean whiteHasCastled = ((King)brd[whiteKingPos.r][whiteKingPos.c]).hasCastled();
    boolean blackHasCastled = ((King)brd[blackKingPos.r][blackKingPos.c]).hasCastled();

    eval += whiteKingHasMoved && !whiteHasCastled ? -1.5 : 0;
    eval += blackKingHasMoved && !blackHasCastled ? 1.5 : 0;

    // however this will make it not want to castle, so need to offset it with
    // a benefit for rook mobility / not being blocked by the king

    // depends on total material - max material is about 78.4
    eval += whiteHasCastled ? 2.0 * totalMaterial / 78.4: 0;
    eval += blackHasCastled ? -2.0 * totalMaterial / 78.4: 0;

    // number of legal moves is slightly good
    eval += board.getAttackMoves(true).size() / 30.0;
    eval += -board.getAttackMoves(false).size() / 30.0;

    // having knights on the back row is bad
    for (int c=0;c<8;c++) {
      if (brd[7][c] != null && brd[7][c].side() && brd[7][c] instanceof Knight) {
        eval += -1 / 10.0;
      }
    }

    for (int c=0;c<8;c++) {
      if (brd[0][c] != null && !brd[0][c].side() && brd[0][c] instanceof Knight) {
        eval += 1 / 10.0;
      }
    }

    // king safety

    // back row is good
    if (whiteKingPos.r == 7) eval += 1 / 3.0;
    // nearby allied pieces or walls is good
    for (Pos p : whiteKingPos.neighbors()) {
      if (!Utils.inBounds(p.r, p.c) || (brd[p.r][p.c] != null && brd[p.r][p.c].side())) {
        eval += 1 / 5.0;
      }
    }

    // back row is good
    if (blackKingPos.r == 0) eval += -1 / 3.0;
    // nearby allied pieces or walls is good
    for (Pos p : blackKingPos.neighbors()) {
      if (!Utils.inBounds(p.r, p.c) || (brd[p.r][p.c] != null && !brd[p.r][p.c].side())) {
        eval += -1 / 5.0;
      }
    }

    // the farther up a pawn is in a file without a matching opponent pawn, the better
    // this also scales inversely with material - less material on the board
    // makes having pushed pawns better
    int bestWhitePawnR = 8;
    int bestBlackPawnR = -1;
    for (int c=0;c<8;c++) {
      int whitePawnR = 8;
      int blackPawnR = -1;
      boolean whiteHasPawn = false;
      boolean blackHasPawn = false;
      for (int r=6;r>=1;r--) {
        if (brd[r][c] instanceof Pawn) {
          if (brd[r][c].side()) {
            whiteHasPawn = true;
            whitePawnR = Math.min(r, whitePawnR);
          } else {
            blackHasPawn = true;
            blackPawnR = Math.max(r, blackPawnR);
          }
        }
      }
      if (whiteHasPawn && !blackHasPawn && whitePawnR < bestWhitePawnR) {
        bestWhitePawnR = whitePawnR;
      } else if (!whiteHasPawn && blackHasPawn && blackPawnR > bestBlackPawnR) {
        bestBlackPawnR = blackPawnR;
      }
    }
    eval += 1.5 * ((6 - bestWhitePawnR) / 6.0) * (78.4 - totalMaterial) / 78.4;
    eval += -1.5 * ((bestBlackPawnR - 1) / 6.0) * (78.4 - totalMaterial) / 78.4;


    // random
    eval += Math.random() < 0.5 ? Math.random() / 100.0 : -Math.random() / 100.0;

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
