package util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utils {
  public static final Map<String, String> openingBookInfo;

  static {
    Gson gson = new Gson();
    File file = new File("opening_book_data_2.json");
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(file));
    } catch (IOException e) {
      e.printStackTrace();
    }
    openingBookInfo = gson.fromJson(br, HashMap.class);
  }

  public static boolean inBounds(int r, int c) {
    return r >= 0 && r < 8 && c >= 0 && c < 8;
  }
}
