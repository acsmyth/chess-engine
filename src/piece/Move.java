package piece;

public class Move {
  public final int fromR;
  public final int fromC;
  public final int toR;
  public final int toC;

  public Move(int fromR, int fromC, int toR, int toC) {
    this.fromR = fromR;
    this.fromC = fromC;
    this.toR = toR;
    this.toC = toC;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Move)) return false;
    Move other = (Move)o;
    return fromR == other.fromR && fromC == other.fromC
            && toR == other.toR && toC == other.toC;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(fromR + fromC + toR + toC);
  }
}
