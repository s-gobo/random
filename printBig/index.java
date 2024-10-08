import java.util.Scanner;
class index {
  public static String map = (
    " 000 0  000 0 000  0 000 " +
    "  1  111    1    1  11111" +
    " 222 2   2   2   2  22222" +
    "3333     3 333     33333 " +
    "   4   44  4 4 44444   4 " +
    "555555    5555     55555 " +
    " 66666    6666 6   6 666 " +
    "77777    7   7   7   7   " +
    " 888 8   8 888 8   8 888 " +
    " 999 9   9 9999    99999 " +
    " AAA A   AAAAAAA   AA   A" +
    "BBBB B   BBBBB B   BBBBB " +
    " CCCCC    C    C     CCCC" +
    "DDDD D   DD   DD   DDDDD " +
    "EEEEEE    EEEE E    EEEEE" +
    "FFFFFF    FFFF F    F    " +
    " GGGGG    G  GGG   G GGG " +
    "H   HH   HHHHHHH   HH   H" +
    "IIIII  I    I    I  IIIII" +
    "JJJJJ    J    JJ   J JJJ " +
    "K   KK  K KKK  K  K K   K" +
    "L    L    L    L    LLLLL" +
    "M   MMM MMM M MM   MM   M" +
    "N   NNN  NN N NN  NNN   N" +
    " OOO O   OO   OO   O OOO " +
    "PPPP P   PPPPP P    P    " +
    " QQQ Q   QQ   QQ  Q  QQ Q" +
    "RRRR R   RRRRR R  R R   R" +
    " SSSSS     SSS     SSSSS " +
    "TTTTT  T    T    T    T  " +
    "U   UU   UU   UU   U UUU " +
    "V   VV   V V V  V V   V  " +
    "W   WW   WW W WWW WWW   W" +
    "X   X X X   X   X X X   X" +
    "Y   Y Y Y   Y   Y   Y    " +
    "ZZZZZ   Z   Z   Z   ZZZZZ" +
    "                         ");
  public static String key = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
  public static String get(char letter, int layer) {
    int letterID = key.indexOf(letter);
    if (letterID == -1) {
      letterID = key.indexOf(" ");
    }
    int index = letterID * 25 + layer * 5;
    return map.substring(index, index + 5);
  }
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    scanner.useDelimiter("\n");
    while (scanner.hasNext()) {
      char[] input = scanner.next().toUpperCase().toCharArray();
      for (int i = 0; i < 5; i++) {
        String output = "";
        for (char ch : input) {
          output += get(ch, i);
          output += "   ";
        }
        System.out.println(output);
      }
      System.out.println();
    }
  }
}
