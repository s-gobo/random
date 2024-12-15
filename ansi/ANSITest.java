/**
 * Test ANSI escape codes
 * Using Select Graphic Rendition
 * See https://en.wikipedia.org/wiki/ANSI_escape_code
 */
public class ANSITest {
    static final String START = "\u001B[";
    static final String END = "m";
    
    public static String format(String str, int format) {
        return START + format + END + str + START + END;
    }
    public static void main(String[] args) {
        for (int i = 0; i <= 107; i++) {
            System.out.println(format("test: " + i, i));
        }
    }
}
