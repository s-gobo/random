public class Main {
  public static void main(String[] args) {
    for (int i = 0; i < 26; i++) {
      System.out.println(caesarCypher("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", i));
    }
  }
  
  public static String caesarCypher(String str, int shift) {
    shift %= 26;
    String re = new String();
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (c > 64 && c <= 90) {
        c += shift;
        c -= c > 90? 26: 0;
      }
      if (c > 96 && c <= 122) {
        c += shift;
        c -= c > 122? 26: 0;
      }
      re += c;
    }
    return re;
  }
}
