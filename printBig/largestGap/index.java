public class index {
  public static void main(String[] args) {
    String s = "chingyeung";
    int maxIndex = -1, maxLen = 0;
    char thisLetter = s.charAt(0);
    for (int i = 0; i < s.length() - 1; i++) {
      char nextLetter = s.charAt(i + 1);
      int thisLen = Math.abs(thisLetter - nextLetter); 
      if (thisLen > maxLen) {
        maxIndex = i;
        maxLen = thisLen;
      }
      thisLetter = nextLetter;
    }
    System.out.println("The string \"" + s +
    "\" has the highest gap between letters at index " + maxIndex +
    ", between '" + s.charAt(maxIndex) + "' and '" +
    s.charAt(maxIndex + 1) + "'");
  }
}
