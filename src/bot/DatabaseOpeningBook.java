package bot;

import game.ChessGame;
import game.ChessGameImpl;
import piece.Move;
import util.Utils;

public class DatabaseOpeningBook implements MoveSequenceOpeningBook {
  @Override
  public boolean hasBookMove(String pgn, boolean side) {
    // process pgn
    // remove all newlines
    pgn = pgn.replace('\n', ' ').replace("  *", " *");
    //System.out.println("p: " + pgn);
    return Utils.masterOpeningBookInfo.containsKey(pgn)
            || Utils.lichessOpeningBookInfo.containsKey(pgn);
  }

  @Override
  public Move getMove(String pgn, ChessGame gameState, boolean side) {
    pgn = pgn.replace('\n', ' ').replace("  *", " *");

    String bookStr;
    if (Utils.masterOpeningBookInfo.containsKey(pgn)) {
      bookStr = Utils.masterOpeningBookInfo.get(pgn);
    } else if (Utils.lichessOpeningBookInfo.containsKey(pgn)) {
      bookStr = Utils.lichessOpeningBookInfo.get(pgn);
    } else {
      throw new RuntimeException();
    }

    Move move = null;
    for (Move m : gameState.getBoard().getLegalMoves(side)) {
      ChessGameImpl copy = new ChessGameImpl(gameState);
      copy.makeMove(m.fromR, m.fromC, m.toR, m.toC);
      String newMove = copy.pgn();
      newMove = newMove.substring(newMove.indexOf("\n\n") + 1).strip();
      newMove = newMove.substring(0, newMove.length()-2).strip();

      //System.out.println(newMove);
      //System.out.println(newMove);
      //System.out.println("resulting pgn: " + newMove);
      newMove = newMove.substring(newMove.lastIndexOf(" ")+1);
      //System.out.println("newMove: " + newMove);
      //System.out.println("bookStr: " + bookStr);
      //System.out.println("pgn: " + pgn);
      if (newMove.equals(bookStr)) {
        move = m;
        break;
      }
    }
    //if (move == null) throw new RuntimeException(pgn + "\n" + bookStr);
    return move;
  }
}