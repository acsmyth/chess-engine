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

  public PieceDrawer(PApplet p) {
    int yScl = 3;
    String r = "resources/";
    whitePawnImg = p.loadImage(r + "white_pawn.PNG");
    whitePawnImg.resize(whitePawnImg.width / 3, whitePawnImg.height / yScl);
    whiteKnightImg = p.loadImage(r + "white_knight.PNG");
    whiteKnightImg.resize(whiteKnightImg.width / 3, whiteKnightImg.height / yScl);
    whiteBishopImg = p.loadImage(r + "white_bishop.PNG");
    whiteBishopImg.resize(whiteBishopImg.width / 3, whiteBishopImg.height / yScl);
    whiteRookImg = p.loadImage(r + "white_rook.PNG");
    whiteRookImg.resize(whiteRookImg.width / 3, whiteRookImg.height / yScl);
    whiteQueenImg = p.loadImage(r + "white_queen.PNG");
    whiteQueenImg.resize((int)(whiteQueenImg.width / 2.8),
            (int)(whiteQueenImg.height / (yScl - 0.2)));
    whiteKingImg = p.loadImage(r + "white_king.PNG");
    whiteKingImg.resize(whiteKingImg.width / 3, whiteKingImg.height / yScl);
    blackPawnImg = p.loadImage(r + "black_pawn.PNG");
    blackPawnImg.resize(blackPawnImg.width / 3, blackPawnImg.height / yScl);
    blackKnightImg = p.loadImage(r + "black_knight.PNG");
    blackKnightImg.resize(blackKnightImg.width / 3, blackKnightImg.height / yScl);
    blackBishopImg = p.loadImage(r + "black_bishop.PNG");
    blackBishopImg.resize(blackBishopImg.width / 3, blackBishopImg.height / yScl);
    blackRookImg = p.loadImage(r + "black_rook.PNG");
    blackRookImg.resize(blackRookImg.width / 3, blackRookImg.height / yScl);
    blackQueenImg = p.loadImage(r + "black_queen.PNG");
    blackQueenImg.resize((int)(blackQueenImg.width / 2.8),
            (int)(blackQueenImg.height / (yScl - 0.2)));
    blackKingImg = p.loadImage(r + "black_king.PNG");
    blackKingImg.resize(blackKingImg.width / 3, blackKingImg.height / yScl);
  }
}
