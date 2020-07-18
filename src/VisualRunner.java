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

  public static void main(String[] args) {
    PApplet.main("VisualRunner");
  }

  public void settings() {
    size(1200, 1200);
  }

  public void setup() {
    game = new ChessGameImpl();
    drawer = new PieceDrawer(this);
    moveFrom = null;
    cellWidth = width / 8;
    cellHeight = height / 8;
    shouldMakeComputerMove = true;
    editMode = false;
  }

  public void draw() {
    drawBoard();
    if (shouldMakeComputerMove) {
      game.makeComputerMove();
      shouldMakeComputerMove = false;
    }
  }

  private void drawBoard() {
    ChessPiece[][] brd = game.getBoard().getBoard();
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        int x = c * cellWidth;
        int y = r * cellHeight;

        rect(x, y, cellWidth, cellHeight);
        if (brd[r][c] == null) continue;

        x += cellWidth / 4;
        y += cellHeight / 6;

        boolean side = brd[r][c].side();
        switch (brd[r][c].getClass().getSimpleName()) {
          case "Pawn":
            image(side ? drawer.whitePawnImg : drawer.blackPawnImg, x, y);
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

  public void mousePressed() {
    int r = mouseY / cellHeight;
    int c = mouseX / cellWidth;
    if (game.getBoard().getBoard()[r][c] != null) {
      moveFrom = new Pos(r, c);
    }
  }

  public void mouseReleased() {
    int r = mouseY / cellHeight;
    int c = mouseX / cellWidth;
    if (moveFrom == null) return;
    if (r == moveFrom.r && c == moveFrom.c) {
      moveFrom = null;
    } else {
      if (editMode) {
        game.getBoard().makeMove(new Move(moveFrom.r, moveFrom.c, r, c));
      } else {
        boolean moveSuccessful = game.makeMove(moveFrom.r, moveFrom.c, r, c);
        if (moveSuccessful) {
          shouldMakeComputerMove = true;
          drawBoard();
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
      default:
        // do nothing
    }
  }
}
