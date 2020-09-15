package util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utils {
  public static final Map<String, String> masterOpeningBookInfo;
  public static final Map<String, String> lichessOpeningBookInfo;

  static {
    Gson gson = new Gson();
    File masterFile = new File("resources/master_database.json");
    File lichessFile = new File("resources/lichess_database.json");
    BufferedReader br = null;
    BufferedReader br2 = null;
    try {
      br = new BufferedReader(new FileReader(masterFile));
      br2 = new BufferedReader(new FileReader(lichessFile));
    } catch (IOException e) {
      e.printStackTrace();
    }
    masterOpeningBookInfo = gson.fromJson(br, HashMap.class);
    lichessOpeningBookInfo = gson.fromJson(br2, HashMap.class);

    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 *", "Nc6");
    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nc6 4. Nxc6 *", "dxc6");
    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nc6 4. Nxc6 dxc6 5. d3 *", "Bc5");
    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nc6 4. Nxc6 dxc6 5. d3 Bc5 6. Be2 *", "Ng4");
    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nc6 4. Nxc6 dxc6 5. d3 Bc5 6. Be2 Ng4 7. Bxg4 *", "Qh4");
    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nc6 4. Nxc6 dxc6 5. d3 Bc5 6. Be2 Ng4 7. O-O *", "Qh4");
    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nc6 4. Nxc6 dxc6 5. d3 Bc5 6. Be2 Ng4 7. Bxg4 Qh4 8. O-O Bxg4 9. Qe1 *", "O-O");
    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nc6 4. Nxc6 dxc6 5. d3 Bc5 6. Bg5 *", "Nxe4");
    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nc6 4. Nxc6 dxc6 5. d3 Bc5 6. Bg5 Nxe4 7. dxe4 *", "Bxf2+");
    lichessOpeningBookInfo.put("1. e4 e5 2. Nf3 Nf6 3. Nxe5 Nc6 4. Nxc6 dxc6 5. d3 Bc5 6. Bg5 Nxe4 7. dxe4 Bxf2+ 8. Ke2", "Bg4+");
  }

  public static boolean inBounds(int r, int c) {
    return r >= 0 && r < 8 && c >= 0 && c < 8;
  }
}
