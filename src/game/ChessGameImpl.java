package game;

import java.util.ArrayList;
import java.util.List;
import bot.Bot;
import bot.MinimaxWithABPruningBot;
import piece.Move;

public class ChessGameImpl implements ChessGame {
  private final ChessBoard board;
  private boolean turn;
  private final Bot engine;
  private Move prevMove;
  private final List<Move> movesMade;
  private final List<ChessBoard> boardStates;
  public boolean shouldPlaySound;

  public ChessGameImpl() {
    board = new ChessBoardImpl();
    turn = true;
    engine = new MinimaxWithABPruningBot();
    prevMove = null;
    movesMade = new ArrayList<>();
    boardStates = new ArrayList<>();
    shouldPlaySound = false;
  }

  public ChessGameImpl(ChessGame gameToCopy) {
    ChessGameImpl copy = (ChessGameImpl)gameToCopy;
    board = new ChessBoardImpl(gameToCopy.getBoard());
    turn = copy.turn;
    engine = copy.engine;
    prevMove = copy.prevMove;
    movesMade = new ArrayList<>(copy.movesMade);
    boardStates = new ArrayList<>(copy.boardStates);
    shouldPlaySound = false;
  }

  @Override
  public boolean makeMove(int fromR, int fromC, int toR, int toC) {
    if (board.isLegalMove(fromR, fromC, toR, toC, turn)) {
      // play sound
      shouldPlaySound = true;

      Move m = new Move(fromR, fromC, toR, toC);
      boardStates.add(new ChessBoardImpl(board));
      movesMade.add(m);
      board.makeMove(m);
      turn = !turn;
      prevMove = m;
      return true;
    }
    return false;
  }

  @Override
  public boolean makeComputerMove() {
    Move computerMove;
    try {
      computerMove = engine.chooseMove(this, turn);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return makeMove(computerMove.fromR, computerMove.fromC,
            computerMove.toR, computerMove.toC);
  }

  @Override
  public void display() {
    board.display();
  }

  @Override
  public boolean isOver() {
    return board.kingIsInCheck(turn) && board.getLegalMoves(!turn).isEmpty();
  }

  @Override
  public ChessBoard getBoard() {
    return board;
  }

  @Override
  public Move getPrevMove() {
    return prevMove;
  }

  @Override
  public double getPrevEval() {
    return engine.getPrevEval();
  }

  @Override
  public boolean turn() {
    return turn;
  }

  @Override
  public String pgn() {
    String res = board.result();
    String pgn = "";
    pgn += "[Event \"Computer Chess\"]\n";
    pgn += "[Site \"Computer\"]\n";
    pgn += "[Date \"0000.00.00\"]\n";
    pgn += "[Round \"0\"]\n";
    pgn += "[White \"Unknown\"]\n";
    pgn += "[Black \"Unknown\"]\n";
    pgn += "[Result \"" + res + "\"]\n\n";
    for (int i=0;i<movesMade.size();i++) {
      if (i % 2 == 0) {
        pgn += (1 + i/2) + ". ";
      }
      Move m = movesMade.get(i);
      pgn += m.notateMove(boardStates.get(i).getBoard(), i % 2 == 0);
      if ((i+1) % 10 == 0) {
        pgn += "\n";
      } else if (i < movesMade.size() - 1) {
        pgn += " ";
      }
    }
    pgn += " " + res;
    return pgn;
  }
}
