import java.util.ArrayList;
import java.util.Random;

//WIP

public class Main {
  public static void main(String[] args) {
    Perlin2D noise = new Perlin2D(10, 10);
    for (double i = 0; i < 10; i += 0.1) {
      System.out.println(noise.get(i, 0));
    }
  }
}

public class Perlin2D {
  final ArrayList<ArrayList<Vector2D>> MEMORY;
  
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
    x %= MEMORY.size();
    y %= MEMORY.get(0).size();
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
    
    System.out.println("" + mem(l, d)+ mem(r, d)+ mem(l, u)+ mem(r, u));
    
    double reD = in(dpLD, dpRD, x - l);
    double reU = in(dpLU, dpRU, x - r);
    
    double re = in(reD, reU, y - r);
    
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
  
  public double dotProduct(Vector2D other) {
    return this.i * other.i + this.j + other.j;
  }
  
  public String toString() {
    String re = "⟨"+
      this.i + ", " + this.j +
      "⟩";
    return re;
  }
}
