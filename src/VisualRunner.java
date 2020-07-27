import java.util.Stack;

import bot.MinimaxWithABPruningBot;
import game.ChessBoard;
import game.ChessGame;
import game.ChessGameImpl;
import piece.ChessPiece;
import piece.Move;
import processing.core.PApplet;
import util.PieceDrawer;
import util.Pos;

public class VisualRunner extends PApplet {
  private ChessGame game;
  private PieceDrawer drawer;
  private Pos moveFrom;
  private int cellWidth;
  private int cellHeight;
  private boolean shouldMakeComputerMove;
  private boolean editMode;
  private boolean side;
  private static final int sidebarWidth = 30;
  private Stack<ChessGame> prevGameStates;
  private Stack<ChessGame> futureGameStates;

  // TODO - null move pruning, move ordering, extensions for captures (quiescence search)
  public static void main(String[] args) {
    PApplet.main("VisualRunner");
  }

  public void settings() {
    size(1200 + sidebarWidth, 1200);
  }

  public void setup() {
    game = new ChessGameImpl();
    drawer = new PieceDrawer(this);
    moveFrom = null;
    cellWidth = (width-sidebarWidth) / 8;
    cellHeight = height / 8;
    shouldMakeComputerMove = true;
    editMode = false;
    side = true;
    prevGameStates = new Stack<>();
    futureGameStates = new Stack<>();
    drawBoard();
    drawPrevMove();
    drawBoardEval();
  }

  public void draw() {
    drawBoard();
    drawPrevMove();
    drawBoardEval();
    if (shouldMakeComputerMove) {
      prevGameStates.push(new ChessGameImpl(game));
      game.makeComputerMove();
      shouldMakeComputerMove = false;
    }
  }

  private void drawBoard() {
    ChessPiece[][] brd = game.getBoard().getBoard();
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        int x = side ? c * cellWidth : (7 - c) * cellHeight;
        int y = side ? r * cellHeight : (7 - r) * cellHeight;

        int light = color(255, 255, 255);
        int dark = color(150, 220, 150);
        if ((r % 2 == 0 && c % 2 == 0) || (r % 2 == 1 && c % 2 == 1)) {
          fill(light);
        } else {
          fill(dark);
        }

        rect(x, y, cellWidth, cellHeight);
        if (brd[r][c] == null) continue;

        x += cellWidth / 4;
        y += cellHeight / 6;

        boolean side = brd[r][c].side();
        switch (brd[r][c].getClass().getSimpleName()) {
          case "Pawn":
            image(side ? drawer.whitePawnImg : drawer.blackPawnImg, x + (side ? 0 : 5), y);
            break;
          case "Knight":
            image(side ? drawer.whiteKnightImg : drawer.blackKnightImg, x, y);
            break;
          case "Bishop":
            image(side ? drawer.whiteBishopImg : drawer.blackBishopImg, x, y);
            break;
          case "Rook":
            image(side ? drawer.whiteRookImg : drawer.blackRookImg, x, y);
            break;
          case "Queen":
            image(side ? drawer.whiteQueenImg : drawer.blackQueenImg, x, y);
            break;
          case "King":
            image(side ? drawer.whiteKingImg : drawer.blackKingImg, x, y);
            break;
          default:
            throw new IllegalStateException("Invalid piece");
        }
      }
    }
  }

  private void drawPrevMove() {
    Move prevMove = game.getPrevMove();
    if (prevMove != null) {
      int x1 = side ? prevMove.fromC * cellWidth + cellWidth / 2 : ((7 - prevMove.fromC) * cellWidth + cellWidth / 2);
      int y1 = side ? prevMove.fromR * cellHeight + cellHeight / 2 : ((7 - prevMove.fromR) * cellHeight + cellHeight / 2);
      int x2 = side ? prevMove.toC * cellWidth + cellWidth / 2 : ((7 - prevMove.toC) * cellWidth + cellWidth / 2);
      int y2 = side ? prevMove.toR * cellHeight + cellHeight / 2 : ((7 - prevMove.toR) * cellHeight + cellHeight / 2);

      stroke(0, 0, 255, 30);
      strokeWeight(10);
      line(x1, y1, x2, y2);
      strokeWeight(1);
      stroke(0, 0, 0, 255);
    }
  }

  private void drawBoardEval() {
    double eval = game.getPrevEval();
    float whiteProportion = (float)(1.0 / (1.0 + Math.pow(1.5, -eval)));
    int white = color(255, 255, 255);
    int black = color(0, 0, 0);
    fill(side ? white : black);
    rect(width - sidebarWidth, (side ? (1 - whiteProportion) : whiteProportion) * height,
            sidebarWidth, (side ? whiteProportion : (1 - whiteProportion)) * height);
    fill(side ? black : white);
    rect(width - sidebarWidth, 0, sidebarWidth, (side ? (1 - whiteProportion) : whiteProportion)
            * height);
    fill(255, 255, 255);
  }

  public void mousePressed() {
    int r = side ? mouseY / cellHeight : (7 - mouseY / cellHeight);
    int c = side ? mouseX / cellWidth : (7 - mouseX / cellWidth);
    if (game.getBoard().getBoard()[r][c] != null) {
      moveFrom = new Pos(r, c);
    }
  }

  public void mouseReleased() {
    int r = side ? mouseY / cellHeight : (7 - mouseY / cellHeight);
    int c = side ? mouseX / cellWidth : (7 - mouseX / cellWidth);
    if (moveFrom == null) return;
    if (r == moveFrom.r && c == moveFrom.c) {
      moveFrom = null;
    } else {
      if (editMode) {
        prevGameStates.push(new ChessGameImpl(game));
        ChessBoard board = game.getBoard();
        if (board.getBoard()[moveFrom.r][moveFrom.c] != null) {
          board.makeMove(new Move(moveFrom.r, moveFrom.c, r, c), true);
        }
      } else {
        ChessGame prevGameCopy = new ChessGameImpl(game);
        boolean moveSuccessful = game.makeMove(moveFrom.r, moveFrom.c, r, c);
        if (moveSuccessful) {
          prevGameStates.push(prevGameCopy);
          shouldMakeComputerMove = true;
          drawBoard();
          drawPrevMove();
        }
      }
    }
  }

  public void keyPressed() {
    switch (key) {
      case 'e':
        editMode = !editMode;
        break;
      case ' ':
        shouldMakeComputerMove = true;
        break;
      case 's':
        side = !side;
        break;
      case '[':
        MinimaxWithABPruningBot.depth--;
        System.out.println(MinimaxWithABPruningBot.depth);
        break;
      case ']':
        MinimaxWithABPruningBot.depth++;
        System.out.println(MinimaxWithABPruningBot.depth);
        break;
      case 'x':
        System.out.println(game.pgn());
      default:
        // do nothing
    }
    switch (keyCode) {
      case LEFT:
        if (!prevGameStates.isEmpty()) {
          ChessGame prevState = prevGameStates.pop();
          futureGameStates.push(game);
          game = prevState;
        }
        break;
      case RIGHT:
        if (!futureGameStates.isEmpty()) {
          ChessGame nextState = futureGameStates.pop();
          prevGameStates.push(game);
          game = nextState;
        }
        break;
      default:
        // do nothing
    }
  }
}
