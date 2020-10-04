import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import bot.MinimaxWithABPruningBot;
import game.ChessBoard;
import game.ChessGame;
import game.ChessGameImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import piece.ChessPiece;
import piece.Move;
import processing.core.PApplet;
import processing.sound.SoundFile;
import util.Pair;
import util.PieceDrawer;
import util.Pos;
import util.Settings;
import util.Utils;
import java.io.*;

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
  private Pos drawArrowFrom;
  private List<Pair<Pos, Pos>> arrows;
  private MediaPlayer mediaPlayer;

  // TODO - null move pruning, move ordering, extensions for captures (quiescence search)
  public static void main(String[] args) {
    PApplet.main("VisualRunner");
  }

  public void settings() {
    int screenW = 800;
    size(screenW + sidebarWidth, screenW);
  }

  public void setup() {
    Settings.checkExtensions = false;
    game = new ChessGameImpl();
    drawer = new PieceDrawer(this, width);
    moveFrom = null;
    cellWidth = (width-sidebarWidth) / 8;
    cellHeight = height / 8;
    shouldMakeComputerMove = false;
    editMode = false;
    side = true;
    prevGameStates = new Stack<>();
    futureGameStates = new Stack<>();
    drawArrowFrom = null;
    arrows = new ArrayList<>();
    drawBoard();
    drawPrevMove();
    drawBoardEval();
    Platform.startup(() -> {});
  }

  public void draw() {
    drawBoard();
    drawPrevMove();
    drawBoardEval();
    drawArrows();
    if (((ChessGameImpl)game).shouldPlaySound) {
      ((ChessGameImpl)game).shouldPlaySound = false;
      URL file = VisualRunner.class.getClassLoader().getResource("tick_sound.mp3");
      final Media media = new Media(file.toString());
      mediaPlayer = new MediaPlayer(media);
      mediaPlayer.play();
    }
    if (shouldMakeComputerMove) {
      ChessGame savedCopy = new ChessGameImpl(game);
      if (game.makeComputerMove()) {
        prevGameStates.push(savedCopy);
      }
      shouldMakeComputerMove = false;
    }
  }

  private void drawBoard() {
    ChessPiece[][] brd = game.getBoard().getBoard();
    noStroke();
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        int x = side ? c * cellWidth : (7 - c) * cellHeight;
        int y = side ? r * cellHeight : (7 - r) * cellHeight;

        int light = color(238, 238, 209);
        int dark = color(121, 150, 83);
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
            image(side ? drawer.whitePawnImg : drawer.blackPawnImg, x - 8, y);
            break;
          case "Knight":
            image(side ? drawer.whiteKnightImg : drawer.blackKnightImg, x - 6, y);
            break;
          case "Bishop":
            image(side ? drawer.whiteBishopImg : drawer.blackBishopImg, x - 5, y);
            break;
          case "Rook":
            image(side ? drawer.whiteRookImg : drawer.blackRookImg, x - 5, y);
            break;
          case "Queen":
            image(side ? drawer.whiteQueenImg : drawer.blackQueenImg, x - 8, y);
            break;
          case "King":
            image(side ? drawer.whiteKingImg : drawer.blackKingImg, x - 6, y);
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
      int x1 = side ? prevMove.fromC * cellWidth + cellWidth / 2
              : ((7 - prevMove.fromC) * cellWidth + cellWidth / 2);
      int y1 = side ? prevMove.fromR * cellHeight + cellHeight / 2
              : ((7 - prevMove.fromR) * cellHeight + cellHeight / 2);
      int x2 = side ? prevMove.toC * cellWidth + cellWidth / 2
              : ((7 - prevMove.toC) * cellWidth + cellWidth / 2);
      int y2 = side ? prevMove.toR * cellHeight + cellHeight / 2
              : ((7 - prevMove.toR) * cellHeight + cellHeight / 2);

      stroke(0, 0, 255, 30);
      strokeWeight(10);
      line(x1, y1, x2, y2);
      strokeWeight(1);
      stroke(0, 0, 0, 255);
    }
  }

  private void drawBoardEval() {
    double eval = game.getPrevEval();
    float whiteProportion = (float)(1.0 / (1.0 + Math.pow(1.3, -eval)));
    int white = color(255, 255, 255);
    int black = color(0, 0, 0);
    noStroke();
    fill(side ? white : black);
    rect(width - sidebarWidth, (side ? (1 - whiteProportion) : whiteProportion) * height,
            sidebarWidth, (side ? whiteProportion : (1 - whiteProportion)) * height);
    fill(side ? black : white);
    rect(width - sidebarWidth, 0, sidebarWidth, (side ? (1 - whiteProportion) : whiteProportion)
            * height);
    fill(255, 255, 255);
  }

  private void drawArrows() {
    int half = cellWidth / 2;
    for (Pair<Pos, Pos> arrow : arrows) {
      int r1 = side ? arrow.x.r : (7 - arrow.x.r);
      int c1 = side ? arrow.x.c : (7 - arrow.x.c);
      int r2 = side ? arrow.y.r : (7 - arrow.y.r);
      int c2 = side ? arrow.y.c : (7 - arrow.y.c);

      stroke(0,0,255,150);
      strokeWeight(12);
      line(c1 * cellWidth + half, r1 * cellWidth + half,
              c2 * cellWidth + half, r2 * cellWidth + half);
      fill(0,0,255);
      noStroke();
      pushMatrix();
      translate(c2 * cellWidth + half, r2 * cellWidth + half);
      rotate(atan2(r2 - r1, c2 - c1) + PI/2);
      int dist = cellWidth / 8;
      triangle(-dist, dist, 0, -dist, dist, dist);
      popMatrix();
    }
  }

  public void mousePressed() {
    int r = side ? mouseY / cellHeight : (7 - mouseY / cellHeight);
    int c = side ? mouseX / cellWidth : (7 - mouseX / cellWidth);
    if (Utils.inBounds(r, c) && game.getBoard().getBoard()[r][c] != null && mouseButton == LEFT) {
      moveFrom = new Pos(r, c);
    }
    if (Utils.inBounds(r, c) && mouseButton == RIGHT) {
      drawArrowFrom = new Pos(r, c);
    }
  }

  public void mouseReleased() {
    int r = side ? mouseY / cellHeight : (7 - mouseY / cellHeight);
    int c = side ? mouseX / cellWidth : (7 - mouseX / cellWidth);
    if (!Utils.inBounds(r, c)) return;
    if (mouseButton == LEFT) arrows.clear();
    if (mouseButton == LEFT && moveFrom != null) {
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
    } else if (mouseButton == RIGHT) {
      if (r != drawArrowFrom.r || c != drawArrowFrom.c) {
        arrows.add(new Pair<>(drawArrowFrom, new Pos(r, c)));
      }
      drawArrowFrom = null;
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
        break;
      case 't':
        Settings.checkExtensions = !Settings.checkExtensions;
        System.out.println("Check extensions are now " + (Settings.checkExtensions ? "on" : "off"));
        break;
      case 'q':
        System.out.println(game.getBoard().hashCode());
        break;
      case 'z':
        int r = side ? mouseY / cellHeight : (7 - mouseY / cellHeight);
        int c = side ? mouseX / cellWidth : (7 - mouseX / cellWidth);
        System.out.println(r + "," + c);
        break;
      default:
        // do nothing
    }
    switch (keyCode) {
      case LEFT:
        if (!prevGameStates.isEmpty()) {
          ChessGame prevState = prevGameStates.pop();
          futureGameStates.push(new ChessGameImpl(game));
          game = prevState;
        }
        break;
      case RIGHT:
        if (!futureGameStates.isEmpty()) {
          ChessGame nextState = futureGameStates.pop();
          prevGameStates.push(new ChessGameImpl(game));
          game = nextState;
        }
        break;
      default:
        // do nothing
    }
  }
}
