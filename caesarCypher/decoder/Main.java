import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    try {
      final Scanner IN = new Scanner(System.in);
      final File WORD = new File("wordlist.txt");
      
      ArrayList<String> words = new ArrayList<String>(10000);
      Scanner reader = new Scanner(WORD);
      while (reader.hasNextLine()) {
        words.add(reader.nextLine());
      }
      reader.close();
      
      String line = IN.nextLine();
      Scanner input = new Scanner(line);
      ArrayList<Integer> shifts = new ArrayList<Integer>(26);
      for (int i = 0; i < 26; i++) {
        shifts.add(0);
      }
      while (input.hasNext()) {
        String word = input.next();
        ArrayList<Integer> freq = new ArrayList<Integer>(26);
        for (int i = 0; i < 26; i++) {
          freq.add(words.indexOf(caesarCypher(word, i)));
        }
        int min = 10000;
        int minI = 0;
        for (int i = 0; i < 26; i++) {
          int val = freq.get(i);
          if (val == -1) {
            continue;
          }
          if (val < min) {
            min = val;
            minI = i;
          }
        }
        if (minI != 0) {
          shifts.set(minI, shifts.get(minI) + 1);
        }
      }
      int max = 0;
      int maxI = -1;
      for (int i = 0; i < 26; i++) {
        int val = shifts.get(i);
        if (val > max) {
          max = val;
          maxI = i;
        }
      }
      
      if (maxI >= 0) {
        System.out.println(caesarCypher(line, maxI));
      } else {
        System.out.println("Error: Not recognized as English.");
      }
    } catch (FileNotFoundException e) {
      System.out.println("Error: Wordlist is not downloaded.");
    }
    long endTime = System.currentTimeMillis();
    System.out.println("Execution finished in " +
      (endTime - startTime) + "ms");
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
