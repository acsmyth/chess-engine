package util;

import java.util.Random;

public class HashingUtils {
  public static Random ran = new Random(77);
  public static long[][] table = new long[18][64];

  static {
    for (int i=0;i<table.length;i++) {
      for (int p=0;p<table[0].length;p++) {
        table[i][p] = ran.nextLong();
      }
    }
  }
}
