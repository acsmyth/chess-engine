package util;

import java.util.Random;

public class HashingUtils {
  public static Random ran = new Random(50);
  public static int ranStartInt = ran.nextInt();
  public static int[] keys = new int[7];

  static {
    for (int i=0;i<7;i++) {
      keys[i] = ran.nextInt();
    }
  }
}
