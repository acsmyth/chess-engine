package bot;

import java.util.HashMap;
import java.util.Map;
import game.ChessBoard;
import piece.Move;

public class SimpleOpeningBook implements OpeningBook {
  private final Map<Integer, Move> whiteBook;
  private final Map<Integer, Move> blackBook;

  public SimpleOpeningBook() {
    whiteBook = new HashMap<>();
    whiteBook.put(-1209864316, new Move(6, 6, 5, 6));
    whiteBook.put(1354875219, new Move(7, 6, 5, 5));
    whiteBook.put(1396110340, new Move(7, 5, 6, 6));
    whiteBook.put(1085951128, new Move(6, 3, 4, 3));
    whiteBook.put(2139966697, new Move(6, 2, 4, 2));
    whiteBook.put(-643571404, new Move(7, 5, 6, 6));
    whiteBook.put(1797286848, new Move(7, 1, 5, 2));
    whiteBook.put(772830324, new Move(7, 6, 5, 5));
    whiteBook.put(-649362703, new Move(6, 2, 4, 2));
    whiteBook.put(-308525339, new Move(6, 3, 4, 3));
    whiteBook.put(-947061016, new Move(7, 5, 6, 6));
    whiteBook.put(-1053845441, new Move(6, 2, 4, 2));
    whiteBook.put(1170001348, new Move(6, 3, 4, 3));
    whiteBook.put(469690412, new Move(6, 2, 4, 2));
    whiteBook.put(-1620197929, new Move(6, 3, 4, 3));
    whiteBook.put(2129183494, new Move(7, 6, 5, 5));
    whiteBook.put(1310073797, new Move(7, 5, 6, 6));
    whiteBook.put(1570413401, new Move(6, 4, 4, 4));
    whiteBook.put(378091560, new Move(6, 3, 4, 3));
    whiteBook.put(546763375, new Move(7, 6, 6, 4));
    whiteBook.put(1154025794, new Move(7, 3, 4, 0));
    whiteBook.put(1101750703, new Move(7, 5, 6, 6));
    whiteBook.put(146853606, new Move(7, 6, 5, 5));
    whiteBook.put(1639863235, new Move(7, 5, 6, 6));
    whiteBook.put(1507905167, new Move(7, 6, 5, 5));
    whiteBook.put(1210113403, new Move(7, 4, 7, 6));
    whiteBook.put(-1520681734, new Move(6, 2, 4, 2));

    blackBook = new HashMap<>();
    // STAFFORD GAMBIT
    blackBook.put(-1809849165, new Move(1, 4, 3, 4));
    blackBook.put(372814146, new Move(0, 6, 2, 5));
    blackBook.put(-1536554049, new Move(0, 1, 2, 2));
    blackBook.put(870098043, new Move(1, 3, 2, 2));
    blackBook.put(1989226395, new Move(0, 5, 3, 2));
    blackBook.put(1327478796, new Move(2, 5, 4, 6));
    blackBook.put(-1581872783, new Move(0, 3, 4, 7));
    blackBook.put(1595619353, new Move(0, 2, 4, 6));
    blackBook.put(-1881952274, new Move(0, 4, 0, 6));
    blackBook.put(-732115390, new Move(0, 5, 3, 2));
    blackBook.put(1294558954, new Move(2, 5, 4, 6));
    blackBook.put(-2035996006, new Move(0, 3, 4, 7));
    blackBook.put(-2037205481, new Move(2, 5, 4, 4));
    blackBook.put(-305940762, new Move(0, 5, 3, 2));
    blackBook.put(204984825, new Move(3, 2, 5, 4));
    blackBook.put(-1049146985, new Move(0, 3, 4, 7));
    blackBook.put(1360672721, new Move(3, 2, 2, 3));
    blackBook.put(857073932, new Move(4, 7, 3, 7));
    blackBook.put(-1498790457, new Move(4, 6, 5, 5));
    blackBook.put(-300818890, new Move(3, 7, 4, 6));

    // vs e4 / Nf3
    blackBook.put(-1567154577, new Move(1, 4, 2, 4));
    blackBook.put(-868322534, new Move(1, 3, 3, 3));
    blackBook.put(-1818945007, new Move(0, 5, 1, 4));
    blackBook.put(247646024, new Move(1, 2, 3, 2));
    blackBook.put(-1897057361, new Move(0, 1, 2, 2));
    blackBook.put(73902897, new Move(0, 5, 4, 1));
    blackBook.put(708118855, new Move(0, 5, 4, 1));
    blackBook.put(-1385990322, new Move(0, 4, 0, 6));
    blackBook.put(-1464720725, new Move(0, 1, 2, 2));

    // vs d4
    blackBook.put(-396242836, new Move(0, 6, 2, 5));
    blackBook.put(316128453, new Move(1, 2, 2, 2));
    blackBook.put(-1131221586, new Move(1, 3, 3, 3));
    blackBook.put(673769282, new Move(1, 4, 2, 4));
    blackBook.put(547923354, new Move(0, 5, 1, 4));
    blackBook.put(1604747812, new Move(0, 4, 0, 6));
    blackBook.put(-1418358973, new Move(2, 4, 3, 3));
    blackBook.put(-1716580389, new Move(2, 5, 4, 4));
  }

  @Override
  public boolean hasBoardState(ChessBoard board, boolean side) {
    Map<Integer, Move> book = side ? whiteBook : blackBook;
    return book.containsKey(board.hashCode());
  }

  @Override
  public Move getMove(ChessBoard board, boolean side) {
    Map<Integer, Move> book = side ? whiteBook : blackBook;
    return book.get(board.hashCode());
  }
}