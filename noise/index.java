import java.util.ArrayList;
import java.util.Random;

public class Main {
  public static void main(String[] args) {
    Perlin2D noise = new Perlin2D(10, 10);
    for (double i = 0; i < 10; i += 0.2) {
      for (double j = 0; j < 10; j += 0.1) {
        double number = noise.get(i, j);
        System.out.print(number >= 0.5? "@":
          number >= 0.3? "X":
          number >= 0.1? "+":
          number >= -0.1? "|":
          number >= -0.3? "-":
          number >= -0.5? ".":
          " ");
      }
      System.out.println();
    }
  }
}

public class Perlin2D {
  private final ArrayList<ArrayList<Vector2D>> MEMORY;
  
  public ArrayList<ArrayList<Vector2D>> getMemory() {
    return MEMORY;
  }
  
  public Perlin2D(int x, int y, long seed) {
    Random rand = new Random(seed);
    ArrayList<ArrayList<Vector2D>> re = new ArrayList<ArrayList<Vector2D>>();
    for (int i = 0; i < x; i++) {
      ArrayList<Vector2D> re2 = new ArrayList<Vector2D>();
      for (int j = 0; j < y; j++) {
        double theta = 2 * Math.PI * rand.nextDouble();
        Vector2D gradient = new Vector2D(Math.sin(theta), Math.cos(theta));
        re2.add(gradient);
      }
      re.add(re2);
    }
    
    MEMORY = re;
  }
  
  public Perlin2D(int x, int y) {
    ArrayList<ArrayList<Vector2D>> re = new ArrayList<ArrayList<Vector2D>>();
    for (int i = 0; i < x; i++) {
      ArrayList<Vector2D> re2 = new ArrayList<Vector2D>();
      for (int j = 0; j < y; j++) {
        double theta = 2 * Math.PI * Math.random();
        Vector2D gradient = new Vector2D(Math.sin(theta), Math.cos(theta));
        re2.add(gradient);
      }
      re.add(re2);
    }
    
    MEMORY = re;
  }
  
  public Perlin2D(ArrayList<ArrayList<Vector2D>> gradients) {
    MEMORY = gradients;
  }
  
  public Perlin2D(Vector2D[][] gradients) {
    ArrayList<ArrayList<Vector2D>> re = new ArrayList<ArrayList<Vector2D>>();
    for (Vector2D[] row: gradients) {
      ArrayList<Vector2D> re2 = new ArrayList<Vector2D>();
      for (Vector2D gradient: row) {
        re2.add(gradient);
      }
      re.add(re2);
    }
    MEMORY = re;
  }
  
  private Vector2D mem(int x, int y) {
    x %= MEMORY.get(0).size();
    y %= MEMORY.size();
    return MEMORY.get(x).get(y);
  }
  
  private double in(double a, double b, double wt) {
    wt = 6 * Math.pow(wt, 5) - 15 * Math.pow(wt, 4) + 10 * Math.pow(wt, 3);
    return a + (b - a) * wt;
  }
  
  public double get(double x, double y) {
    int l = (int) Math.floor(x);
    int r = l + 1;
    int d = (int) Math.floor(y);
    int u = d + 1;
    
    Vector2D toLD = new Vector2D(x - l, y - d);
    Vector2D toRD = new Vector2D(x - r, y - d);
    Vector2D toLU = new Vector2D(x - l, y - u);
    Vector2D toRU = new Vector2D(x - r, y - u);
    
    double dpLD = toLD.dotProduct(mem(l, d));
    double dpRD = toRD.dotProduct(mem(r, d));
    double dpLU = toLU.dotProduct(mem(l, u));
    double dpRU = toRU.dotProduct(mem(r, u));
    
    double reD = in(dpLD, dpRD, x - l);
    double reU = in(dpLU, dpRU, x - l);
    
    double re = in(reD, reU, y - d);
    
    return re;
  }
}

public class Vector2D {
  public double i;
  public double j;
  
  public Vector2D(double i, double j) {
    this.i = i;
    this.j = j;
  }
  
  public Vector2D() {
    this.i = 0;
    this.j = 0;
  }
  
  public double dotProduct(Vector2D that) {
    return this.i * that.i + this.j + that.j;
  }
  
  public static double dotProduct(Vector2D a, Vector2D b) {
    return a.i * b.i + a.j * b.j;
  }
  
  public double magnitude() {
    return Math.sqrt(i * i + j * j);
  }
  
  public String toString() {
    String re = "⟨"+
      this.i + ", " + this.j +
      "⟩";
    return re;
  }
}
