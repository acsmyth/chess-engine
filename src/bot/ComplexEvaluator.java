package bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.ChessBoard;
import piece.ChessPiece;
import piece.King;
import piece.Knight;
import piece.Move;
import piece.Pawn;
import piece.Queen;
import piece.Rook;
import processing.core.PApplet;
import util.Pos;
import util.Utils;

public class ComplexEvaluator implements Evaluator {
  @Override
  public double evaluate(ChessBoard board, boolean turn) {
    ChessPiece[][] brd = board.getBoard();
    double totalMaterial = 0;
    double totalMaterialExceptPawns = 0;
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
          if (!(brd[r][c] instanceof King) && !(brd[r][c] instanceof Pawn)) {
            totalMaterialExceptPawns += valChange;
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
    double earlyGameScalar = lerp(totalMaterial, 78.4, 0, 1, 0);
    double endGameScalar = 1 - earlyGameScalar;


    // tempo bonus
    eval += turn ? 0.25 : -0.25;

    // if king moves before castling, very bad
    Pos whiteKingPos = board.getKingPos(turn);
    boolean whiteKingHasMoved = ((King)brd[whiteKingPos.r][whiteKingPos.c]).hasMoved();
    Pos blackKingPos = board.getKingPos(turn);
    boolean blackKingHasMoved = ((King)brd[blackKingPos.r][blackKingPos.c]).hasMoved();
    boolean whiteHasCastled = ((King)brd[whiteKingPos.r][whiteKingPos.c]).hasCastled();
    boolean blackHasCastled = ((King)brd[blackKingPos.r][blackKingPos.c]).hasCastled();

    eval += whiteKingHasMoved && !whiteHasCastled ? -1.5 * earlyGameScalar : 0;
    eval += blackKingHasMoved && !blackHasCastled ? 1.5 * earlyGameScalar: 0;

    // however this will make it not want to castle, so need to offset it with
    // a benefit for rook mobility / not being blocked by the king

    // depends on total material - max material is about 78.4
    eval += whiteHasCastled ? 2.0 * earlyGameScalar: 0;
    eval += blackHasCastled ? -2.0 * earlyGameScalar: 0;

    // number of legal moves is slightly good
    eval += (board.getAttackMoves(true).size() / 30.0) * earlyGameScalar;
    eval += (-board.getAttackMoves(false).size() / 30.0) * earlyGameScalar;

    // having knights on the back row is bad
    for (int c=0;c<8;c++) {
      if (brd[7][c] != null && brd[7][c].side() && brd[7][c] instanceof Knight) {
        eval += (-1 / 10.0) * earlyGameScalar;
      }
    }

    for (int c=0;c<8;c++) {
      if (brd[0][c] != null && !brd[0][c].side() && brd[0][c] instanceof Knight) {
        eval += (1 / 10.0) * earlyGameScalar;
      }
    }

    // king safety

    // back row is good
    if (whiteKingPos.r == 7) eval += (1 / 3.0) * earlyGameScalar;
    // but some threshold where central king is better
    if (totalMaterialExceptPawns <= 14) eval += -lerp(totalMaterialExceptPawns, 14, 0.5, 0, 1)
            * PApplet.dist(whiteKingPos.c, whiteKingPos.r, 3.5f, 3.5f) / 5.0;

    // nearby allied pieces or walls is good
    for (Pos p : whiteKingPos.neighbors()) {
      if (!Utils.inBounds(p.r, p.c) || (brd[p.r][p.c] != null && brd[p.r][p.c].side())) {
        eval += (1 / 5.0) * earlyGameScalar;
      }
    }

    // back row is good
    if (blackKingPos.r == 0) eval += -1 / 3.0;
    // but some threshold where central king is better
    if (totalMaterialExceptPawns <= 14) eval += lerp(totalMaterialExceptPawns, 14, 0.5, 0, 1)
            * PApplet.dist(blackKingPos.c, blackKingPos.r, 3.5f, 3.5f) / 5.0;

    // nearby allied pieces or walls is good
    for (Pos p : blackKingPos.neighbors()) {
      if (!Utils.inBounds(p.r, p.c) || (brd[p.r][p.c] != null && !brd[p.r][p.c].side())) {
        eval += (-1 / 5.0) * earlyGameScalar;
      }
    }

    // a lot of enemy attack moves near the king is bad
    Map<Pos, Integer> attackMovesNearWhiteKing = new HashMap<>();
    Map<Pos, Integer> attackMovesNearBlackKing = new HashMap<>();
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (brd[r][c] == null) continue;
        if (!brd[r][c].side()) {
          for (Move m : brd[r][c].getAttackMoves(board.getBoard())) {
            for (Pos p : whiteKingPos.neighbors()) {
              if (m.toR == p.r && m.toC == p.c) {
                attackMovesNearWhiteKing.put(p, attackMovesNearWhiteKing.getOrDefault(p, 0) + 1);
              }
            }
          }
        } else {
          for (Move m : brd[r][c].getAttackMoves(board.getBoard())) {
            for (Pos p : blackKingPos.neighbors()) {
              if (m.toR == p.r && m.toC == p.c) {
                attackMovesNearBlackKing.put(p, attackMovesNearBlackKing.getOrDefault(p, 0) + 1);
              }
            }
          }
        }
      }
    }
    for (Integer n : attackMovesNearWhiteKing.values()) {
      if (n > 1) {
        eval += - n / 5.0;
      }
    }
    for (Integer n : attackMovesNearBlackKing.values()) {
      if (n > 1) {
        eval += n / 5.0;
      }
    }



    // doubled pawns is bad
    for (int c=0;c<8;c++) {
      boolean whiteHasPawn = false;
      boolean blackHasPawn = false;
      for (int r=6;r>=1;r--) {
        if (brd[r][c] instanceof Pawn) {
          if (whiteHasPawn && brd[r][c].side()) {
            eval += -1 / 4.0;
          } else if (blackHasPawn && !brd[r][c].side()) {
            eval += 1 / 4.0;
          }
          if (brd[r][c].side()) {
            whiteHasPawn = true;
          } else {
            blackHasPawn = true;
          }
        }
      }
    }

    // past pawns are better the farther they are pushed
    for (int r=0;r<8;r++) {
      for (int c=0;c<8;c++) {
        if (brd[r][c] == null) continue;
        if (brd[r][c] instanceof Pawn) {
          if (pastPawn(r, c, brd)) {
            if (brd[r][c].side()) {
              eval += lerp(r, 6, 1, 0.5, 3) * endGameScalar;
            } else {
              eval += lerp(r, 1, 6, -0.5, -3) * endGameScalar;
            }
          } else {
            if (brd[r][c].side()) {
              eval += lerp(r, 6, 1, 0, 0.3) * endGameScalar;
            } else {
              eval += lerp(r, 1, 6, -0, -0.3) * endGameScalar;
            }
          }
        }
      }
    }

    // random
    //eval += Math.random() < 0.5 ? Math.random() / 100.0 : -Math.random() / 100.0;

    return eval;
  }

  private boolean pastPawn(int r, int c, ChessPiece[][] brd) {
    List<Integer> adjacentCols = new ArrayList<>();
    if (c-1 >= 0) adjacentCols.add(c-1);
    if (c+1 <= 7) adjacentCols.add(c+1);
    for (int i = r - brd[r][c].sideAsInt(); i >= 0 && i <= 7; i -= brd[r][c].sideAsInt()) {
      for (Integer p : adjacentCols) {
        if (brd[i][p] instanceof Pawn && brd[i][p].side() != brd[r][c].side()) {
          return false;
        }
      }
      if (brd[i][c] instanceof Pawn) {
        return false;
      }
    }
    return true;
  }

  private boolean inCenter(int toR, int toC) {
    return (toR == 3 || toR == 4) && (toC == 3 || toC == 4);
  }

  private boolean inInnerRingOutsideCenter(int toR, int toC) {
    return ((toR == 2 || toR == 5) && (toC >= 2 && toC <= 5))
            || ((toC == 2 || toC == 5) && (toR >= 2 && toR <= 5));
  }

  private static double lerp(double x, double a, double b, double c, double d) {
    return ((d - c) / (b - a)) * (x - a) + c;
  }
}
