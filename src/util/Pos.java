package util;

public class Pos {
  public final int r;
  public final int c;

  public Pos(int r, int c) {
    this.r = r;
    this.c = c;
  }

  public Pos[] neighbors() {
    return new Pos[]{new Pos(r+1, c), new Pos(r-1, c), new Pos(r, c+1), new Pos(r, c-1),
            new Pos(r-1, c-1), new Pos(r-1, c+1), new Pos(r+1, c-1), new Pos(r+1, c+1)};
  }
}