package util;

import processing.core.PApplet;
import processing.core.PImage;

public class PieceDrawer {
  public final PImage whitePawnImg;
  public final PImage whiteKnightImg;
  public final PImage whiteBishopImg;
  public final PImage whiteRookImg;
  public final PImage whiteQueenImg;
  public final PImage whiteKingImg;
  public final PImage blackPawnImg;
  public final PImage blackKnightImg;
  public final PImage blackBishopImg;
  public final PImage blackRookImg;
  public final PImage blackQueenImg;
  public final PImage blackKingImg;

  public PieceDrawer(PApplet p, int width) {
    float xScl = (float)(((- 1 / 400.0) * (width - 800)) + 4.0);
    System.out.println(xScl);
    float yScl = xScl;
    String r = "resources/";
    whitePawnImg = p.loadImage(r + "white_pawn.PNG");
    whitePawnImg.resize((int)(whitePawnImg.width / xScl), (int)(whitePawnImg.height / yScl));
    whiteKnightImg = p.loadImage(r + "white_knight.PNG");
    whiteKnightImg.resize((int)(whiteKnightImg.width / xScl), (int)(whiteKnightImg.height / yScl));
    whiteBishopImg = p.loadImage(r + "white_bishop.PNG");
    whiteBishopImg.resize((int)(whiteBishopImg.width / xScl), (int)(whiteBishopImg.height / yScl));
    whiteRookImg = p.loadImage(r + "white_rook.PNG");
    whiteRookImg.resize((int)(whiteRookImg.width / xScl), (int)(whiteRookImg.height / yScl));
    whiteQueenImg = p.loadImage(r + "white_queen.PNG");
    whiteQueenImg.resize((int)(whiteQueenImg.width / (xScl - 0.2)),
            (int)(whiteQueenImg.height / (yScl - 0.2)));
    whiteKingImg = p.loadImage(r + "white_king.PNG");
    whiteKingImg.resize((int)(whiteKingImg.width / xScl), (int)(whiteKingImg.height / yScl));
    blackPawnImg = p.loadImage(r + "black_pawn.PNG");
    blackPawnImg.resize((int)(blackPawnImg.width / xScl), (int)(blackPawnImg.height / yScl));
    blackKnightImg = p.loadImage(r + "black_knight.PNG");
    blackKnightImg.resize((int)(blackKnightImg.width / xScl), (int)(blackKnightImg.height / yScl));
    blackBishopImg = p.loadImage(r + "black_bishop.PNG");
    blackBishopImg.resize((int)(blackBishopImg.width / xScl), (int)(blackBishopImg.height / yScl));
    blackRookImg = p.loadImage(r + "black_rook.PNG");
    blackRookImg.resize((int)(blackRookImg.width / xScl), (int)(blackRookImg.height / yScl));
    blackQueenImg = p.loadImage(r + "black_queen.PNG");
    blackQueenImg.resize((int)(blackQueenImg.width / (xScl - 0.2)),
            (int)(blackQueenImg.height / (yScl - 0.2)));
    blackKingImg = p.loadImage(r + "black_king.PNG");
    blackKingImg.resize((int)(blackKingImg.width / xScl), (int)(blackKingImg.height / yScl));
  }
}
