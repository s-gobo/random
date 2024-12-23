import java.util.Scanner;

public class Main {
  private static final boolean ANSI = true; // color text with ANSI
  private static final boolean WAIT = true; // delay output (typewriter-style)
  
  // ANSI settings
  private static final String ANSI_NORM = "\u001B[m";
  // private static final String ANSI_DIM = "\u001B[39;2m";
  private static final String ANSI_DIM = "\u001B[38;5;8m";
  private static final String ANSI_BOLD = "\u001B[39;1m";
  private static final String ANSI_SIDE = "\u001B[39;3m";
  private static final String ANSI_COL(char c) {
    switch (c) {
      case 'r': return "\u001B[31m"; // red
      case 'b': return "\u001B[34m"; // blue
      case 'g': return "\u001B[32m"; // green
      case 'y': return "\u001B[33m"; // yellow
      case 'w': return "\u001B[35m"; // purple
      default : return "";
    }
  }
  
  // WAIT settings
  private static final int WAIT_RATE = 0_030; // amount of delay between letters
  private static final int WAIT_END = 0_500; // amount of delay after
  
  public static Scanner in = new Scanner(System.in);
  
  public static void main(String[] args) {
    Uno game = new Uno();
    game.play();
    in.close();
  }
  
  // INPUT METHODS
  
  // ln - line: get input line
  public static String ln() {
    return in.nextLine();
  }
  
  // ln with default for empty input
  public static String ln(String or) {
    String ln = ln();
    if (ln.equals("")) {
      System.out.println("No input found. Defaulting to " + or + ".");
      return or;
    }
    return ln;
  }
  
  // yes or no input
  public static boolean lnyn() {
    char re = ln("no").toLowerCase().charAt(0);
    if (re == 'y') {
      return true;
    }
    if (re != 'n') {
      System.out.println("Input is not yes or no. Defaulting to no.");
    }
    return false;
  }
  
  // OUTPUT METHODS
  
  private static void waitOutln(String out) {
    waitOut(out + "\n");
  }
  
  private static void waitOut(String out) {
    if (!WAIT) {
      System.out.print(out);
      return;
    }
    
    // number of '\n's at the end of the string
    int lines = 0;
    while (out.charAt(out.length() - 1 - lines) == '\n') {
      lines++;
    }
    out = out.substring(0, out.length() - lines);
    
    for (int i = 0; i < out.length(); i++) {
      char c = out.charAt(i);
      if (c == '\u001B') {
        String ansi = "\u001B";
        while (c != 'm') {
          i++;
          if (i >= out.length()) {
            break;
          }
          c = out.charAt(i);
          ansi += c;
        }
        System.out.print(ansi);
      } else {
        System.out.print(c);
        try {
          Thread.sleep(WAIT_RATE);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    }
    try {
      Thread.sleep(WAIT_END);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    for (int i = 0; i < lines; i++) {
      System.out.println();
    }
  }
  
  /**
   * Formats a card to be colorful if ANSI is on
   */
  private static String colorCard(Card card) {
    String out = "";
    if (ANSI) { out += ANSI_COL(card.getScolor()); }
    out += card;
    return out;
  }
  
  /**
   * @param from - player who's logging turn
   * @param draw - 0: no draw; 1: drawn; 2: failed draw
   * @param play - card played
   */
  public static void logTurn(Player from, int draw, Card play) {
    String out = "";
    out += from.getIsBot()? from: "You";
    if (ANSI) { out += ANSI_DIM; }
    out += " ";
    if (draw == 2) {
      out += from.getIsBot()? "passes": "pass";
      out += ". There are no more cards to draw.";
    } else {
      if (draw == 1) {
        out += from.getIsBot()? "draws": "draw";
        if (play == null) {
          out += " a card";
        } else {
          out += " and ";
        }
      }
      if (play != null) {
        out += from.getIsBot()? "plays": "play";
        out += " ";
        if (ANSI) { out += ANSI_NORM; }
        out += colorCard(play);
        if (ANSI) {
          out += ANSI_DIM;
        }
        if (play.getScolor() == 'w') {
          out += ", and ";
          out += from.getIsBot()? "changes": "change";
          out += " the color to ";
          if (ANSI) {
            out += ANSI_NORM;
            out += ANSI_COL(play.getMetaSColor());
          }
          out += Card.stringifyColor(play.getMetaSColor()).toLowerCase();
          if (ANSI) {
            out += ANSI_DIM;
          }
        }
      }
      if (from.getHandSize() == 1) {
        out += ", and ";
        out += from.getIsBot()? "calls": "call";
        out += " ";
        if (ANSI) {
          out += ANSI_BOLD;
        }
        out += "UNO!";
      } else {
        if (ANSI) { out += ANSI_DIM;}
        out += ".";
      }
    }
    if (ANSI) { out += ANSI_NORM; }
    waitOutln(out);
  }
  
  /**
   * @param msg - system message
   */
  public static void log(String msg) {
    String out = "";
    if (ANSI) { out += ANSI_DIM; }
    out += msg;
    if (ANSI) { out += ANSI_NORM; }
    waitOutln(out);
  }
  
  /**
   * Log the start of an Uno game
   * @param first - starting card
   */
  public static void logStart(Card first) {
    String out = "";
    if (ANSI) { out += ANSI_DIM; }
    out += "The starting card is ";
    if (ANSI) { out += ANSI_NORM; }
    out += colorCard(first);
    if (ANSI) {
      out += ANSI_NORM;
      out += ANSI_DIM;
    }
    out += "!";
    if (ANSI) {out += ANSI_NORM; }
    waitOutln(out);
  }
  
  /**
   * Logs the cards in hand (on your turn)
   * @param hand - hand
   */
  public static void logHand(Card[] hand, int handSize) {
    String out = "";
    if (ANSI) { out += ANSI_SIDE; }
    out += "Your turn! Your cards are:\n";
    for (int i = 0; i < handSize; i++) {
      out += "  - ";
      out += colorCard(hand[i]);
      if (ANSI) { out += ANSI_SIDE; }
      out += "\n";
    }
    if (ANSI) { out += ANSI_NORM; }
    out += "> ";
    waitOut(out);
  }
  
  /**
   * Ask if player wants to play a drawn card
   * @param c - drawn card
   */
  public static void logPlayDrawn(Card c) {
    String out = "";
    if (ANSI) { out += ANSI_SIDE; }
    out += "You draw a ";
    out += colorCard(c);
    if (ANSI) { out += ANSI_SIDE; }
    out += ". Play it?";
    if (ANSI) { out += ANSI_NORM; }
    out += " > ";
    waitOut(out);
  }
  
  /**
   * log when a player is affected by a draw or skip card
   * @param victim - person's turn who is skipped
   * @param pain - how many cards they need to draw
   */
  public static void logSkip(Player victim, int pain) {
    String out = "";
    out += victim.getIsBot()? victim: "You";
    if (pain == 0) {
      if (ANSI) { out += ANSI_DIM; }
      out += victim.getIsBot()? "'s": "r";
      out += " turn is ";
      if (ANSI) { out += ANSI_NORM; }
      out += "skipped";
      if (ANSI) { out += ANSI_DIM; }
      out += ".";
    } else {
      out += " ";
      out += victim.getIsBot()? "draws": "draw";
      out += " ";
      out += "" + pain;
      if (ANSI) { out += ANSI_DIM; }
      out += " cards.";
    }
    if (ANSI) { out += ANSI_NORM; }
    waitOutln(out);
  }
  
  /**
   * Logs the end of an Uno game
   * @param winner - who won the game
   */
  public static void logWin(Player winner) {
    String out = "";
    if (ANSI) { out += ANSI_BOLD; }
    out += winner;
    out += " ";
    out += winner.getIsBot()? "wins": "win";
    out += "!";
    if (ANSI) { out += ANSI_NORM; }
    waitOutln(out);
  }
}

class Uno {
  private Player[] players;
  private int numPlayers;
  
  private Card[] draw;
  private int drawSize;
  
  private Card[] discard;
  private int discardSize;
  private Card lastDiscard;
  private int lastDiscardTurnsAgo;
  private char lastDiscardScolor;
  private char lastDiscardSnumber;
  
  private int turn;
  private boolean isCW;
  
  public Uno() {
    // card deck setup
    draw = Card.stdDeck();
    Card.shuffle(draw);
    drawSize = draw.length;
    
    // player setup
    players = new Player[4];
    numPlayers = players.length;
    // you
    players[0] = new Player(this, false);
    // bots
    for (int i = 1; i < numPlayers; i++) {
      players[i] = new Player(this, true);
    }
    // everyone's starting cards
    for (int i = 0; i < 7; i++) {
      for (Player player : players) {
        player.draw();
      }
    }
    
    // start position and rotation
    turn = (int) (Math.random() * numPlayers);
    isCW = true;
    
    // print play order
    String out = "";
    for (int i = 0; i < numPlayers; i++) {
      out += players[(i + turn) % numPlayers];
      if (i != numPlayers - 1) {
        out += " -> ";
      }
    }
    Main.log("Play order is: " + out);
    
    // discard pile
    discard = new Card[108];
    discardSize = 0;
    
    // starting card
    Card start = draw();
    Main.logStart(start);
    // starting wild cards have any color
    if (start.getScolor() == 'w') {
      start.setMetaSColor('x');
    }
    discard(start);
    
    // the starting card has an effect
    if (lastDiscard.getPostEffect() && lastDiscardTurnsAgo == 0) {
      // reverse card
      if (lastDiscard.getSnumber() == 'r') {
        isCW = !isCW;
        if (isCW) {
          Main.log("Play direction is clockwise!");
        } else {
          Main.log("Play direction is counterclockwise!");
        }
      }
    }
  }
  
  public Card getLastDiscard() {
    return lastDiscard;
  }
  public char getLastDiscardScolor() {
    return lastDiscardScolor;
  }
  public char getLastDiscardSnumber() {
    return lastDiscardSnumber;
  }
  
  public void play() {
    // main game loop
    while (true) {
      lastDiscardTurnsAgo++;

      // current turn's Player
      Player player = players[turn];
      
      // does the last played card skip their turn?
      boolean skip = false;
      if (lastDiscard.getPreEffect() && lastDiscardTurnsAgo == 1) {
        if (lastDiscard.getSnumber() == 's') {
          skip = true;
          Main.logSkip(player, 0);
        } else if (lastDiscard.getSnumber() == '+') {
          if (lastDiscard.getScolor() == 'w') {
            player.penalty(4);
            Main.logSkip(player, 4);
          } else {
            player.penalty(2);
            Main.logSkip(player, 2);
          }
          skip = true;
        }
      }
      
      if (!skip) {
        // the player gets a turn
        player.turn();
        
        // check if the player won
        if (player.getHandSize() == 0) {
          Main.logWin(player);
          break;
        }
        
        // the played card has an effect
        if (lastDiscard.getPostEffect() && lastDiscardTurnsAgo == 0) {
          // reverse card
          if (lastDiscard.getSnumber() == 'r') {
            isCW = !isCW;
            if (isCW) {
              Main.log("Play direction is clockwise!");
            } else {
              Main.log("Play direction is counterclockwise!");
            }
          }
        }
      }
      
      // increment turn according to play direction
      if (isCW) {
        turn++;
        turn %= numPlayers;
      } else {
        turn--;
        if (turn < 0) {
          turn += numPlayers;
        }
      }
    }
  }
  
  public Card draw() {
    // reshuffle discard to draw if no more cards to take
    if (drawSize == 0) {
      // check if the discard pile only has one card
      if (discardSize == 1) {
        return null;
      }
      Main.log("Discard is reshuffled to draw");
      
      // the last discarded card is kept; the other cards are reshuffled
      discardSize--;
      drawSize = discardSize;
      // move cards between piles
      for (int i = 0; i < discardSize; i++) {
        draw[i] = discard[i];
      }
      // move last discarded card as new starting card
      discard[0] = discard[discardSize];
      discardSize = 1;
      // shuffle draw pile
      Card.shuffle(draw);
    }
    
    // remove the last card and return it
    drawSize--;
    return draw[drawSize];
  }
  
  /**
   * Discard a card
   * @param c - card to be discarded
   */
  public void discard(Card c) {
    discard[discardSize] = c;
    discardSize++;
    
    // update lastDiscard variables
    lastDiscard = c;
    lastDiscardTurnsAgo = 0;
    lastDiscardScolor = c.getScolor();
    lastDiscardSnumber = c.getSnumber();
    if (lastDiscardScolor == 'w') {
      lastDiscardScolor = c.getMetaSColor();
      lastDiscardSnumber = ' ';
    }
  }
}

class Player {
  private static final boolean COMPUTER_ALIAS = true;
  
  private boolean isBot;
  private String name;
  private Uno game;
  
  private Card[] hand;
  private int handSize;
  
  private static final String[] BOT_NAMES = {"James", "Mary", "Michael", "Patricia", "Robert", "Jennifer", "John", "Linda", "David", "Elizabeth", "William", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Karen", "Christopher", "Sarah", "Charles", "Lisa", "Daniel", "Nancy", "Matthew", "Sandra", "Anthony", "Betty", "Mark", "Ashley", "Donald", "Emily", "Steven", "Kimberly", "Andrew", "Margaret", "Paul", "Donna", "Joshua", "Michelle", "Kenneth", "Carol", "Kevin", "Amanda", "Brian", "Melissa", "Timothy", "Deborah", "Ronald", "Stephanie", "George", "Rebecca", "Jason", "Sharon", "Edward", "Laura", "Jeffrey", "Cynthia", "Ryan", "Dorothy", "Jacob", "Amy", "Nicholas", "Kathleen", "Gary", "Angela", "Eric", "Shirley", "Jonathan", "Emma", "Stephen", "Brenda", "Larry", "Pamela", "Justin", "Nicole", "Scott", "Anna", "Brandon", "Samantha", "Benjamin", "Katherine", "Samuel", "Christine", "Gregory", "Debra", "Alexander", "Rachel", "Patrick", "Carolyn", "Frank", "Janet", "Raymond", "Maria", "Jack", "Olivia", "Dennis", "Heather", "Jerry", "Helen", "Tyler", "Catherine", "Aaron", "Diane", "Jose", "Julie", "Adam", "Victoria", "Nathan", "Joyce", "Henry", "Lauren", "Zachary", "Kelly", "Douglas", "Christina", "Peter", "Ruth", "Kyle", "Joan", "Noah", "Virginia", "Ethan", "Judith", "Jeremy", "Evelyn", "Christian", "Hannah", "Walter", "Andrea", "Keith", "Megan", "Austin", "Cheryl", "Roger", "Jacqueline", "Terry", "Madison", "Sean", "Teresa", "Gerald", "Abigail", "Carl", "Sophia", "Dylan", "Martha", "Harold", "Sara", "Jordan", "Gloria", "Jesse", "Janice", "Bryan", "Kathryn", "Lawrence", "Ann", "Arthur", "Isabella", "Gabriel", "Judy", "Bruce", "Charlotte", "Logan", "Julia", "Billy", "Grace", "Joe", "Amber", "Alan", "Alice", "Juan", "Jean", "Elijah", "Denise", "Willie", "Frances", "Albert", "Danielle", "Wayne", "Marilyn", "Randy", "Natalie", "Mason", "Beverly", "Vincent", "Diana", "Liam", "Brittany", "Roy", "Theresa", "Bobby", "Kayla", "Caleb", "Alexis", "Bradley", "Doris", "Russell", "Lori", "Lucas", "Tiffany"};
  private static int[] usedBotNames = new int[BOT_NAMES.length];
  private static int usedBotNamesSize = 0;
  
  public Player(Uno game, boolean bot) {
    isBot = bot;
    
    // name bots and players
    if (isBot) {
      name = nameBot();
    } else {
      name = "You";
    }
    
    // game that player belongs to
    this.game = game;
    
    // making hand space
    hand = new Card[10];
    handSize = 0;
  }
  
  public int getHandSize() {
    return handSize;
  }
  
  public boolean getIsBot() {
    return isBot;
  }
  
  @Override
  public String toString() {
    return name;
  }
  
  /**
   * Draw a card
   * @return the card drawn, <code>null</code> if a card could not be drawn
   */
  public Card draw() {
    Card drawnCard = game.draw();
    // check if a card could not be drawn
    if (drawnCard == null) {
      return null;
    }
    // increase hand size if it couldn't fit the card
    if (hand.length == handSize) {
      Card[] temp = hand;
      // hand size increases by 10
      hand = new Card[handSize + 10];
      for (int i = 0; i < temp.length; i++) {
        hand[i] = temp[i];
      }
    }
    // add card to hand and return it
    hand[handSize] = drawnCard;
    handSize++;
    return drawnCard;
  }
  /**
   * Discard a card
   * @param cardI - index of card in hand
   */
  public void play(int cardI) {
    game.discard(hand[cardI]);
    handSize--;
    // shifts cards in hand to remove selected card
    for (int i = cardI; i < handSize; i++) {
      hand[i] = hand[i + 1];
    }
  }
  public void penalty(int num) {
    // player draws num cards (from +2 or +4 card)
    for (int i = 0; i < num; i++) {
      draw();
    }
  }
  
  public int botUse() {
    // the bot chooses a card to play
    
    // info about the last card
    char lSnumber = game.getLastDiscardSnumber();
    char lScolor = game.getLastDiscardScolor();
    int lScolorNum = Card.intifyColor(lScolor);
    
    // new array sorting the bot's hand by color
    int[][] colorSort = new int[4][handSize];
    int[] colorSortSize = {0, 0, 0, 0,};
    
    // indexes of wild cards
    int w = -1;
    int wplus = -1;
    
    // sort cards into arrays
    for (int i = 0; i < handSize; i++) {
      for (int j = 0; j < Card.scolors.length; j++) {
        if (hand[i].getScolor() == Card.scolors[j]) {
          colorSort[j][colorSortSize[j]] = i;
          colorSortSize[j]++;
          break;
        }
      }
      if (hand[i].getScolor() == 'w') {
        // set wild indexes
        if (hand[i].getSnumber() == '+') {
          wplus = i;
        } else {
          w = i;
        }
      }
    }
    
    
    // sort the colors based on amount of cards of that color
    int[] colorSortSizeSort = {-1, -1, -1, -1};
    for (int i = 0; i < 4; i++) {
      int j = 0;
      while (colorSortSizeSort[j] != -1 &&
            colorSortSize[colorSortSizeSort[j]] > colorSortSize[i]) {
        j++;
      }
      for (int k = i; k > j; k--) {
        colorSortSizeSort[k] = colorSortSizeSort[k - 1];
      }
      colorSortSizeSort[j] = i;
    }
    
    // find a card from the best color, if possible
    for (int i = 0; i < 4; i++) {
      int color = colorSortSizeSort[i];
      // there are no cards from the color
      if (colorSortSize[color] == 0) {
        continue;
      }
      // the color can be placed
      if (lScolor == 'x' || color == lScolorNum) {
        // return a random card of the color
        return colorSort[color][(int) (colorSortSize[color] * Math.random())];
      } else {
        // can a card be placed matching number?
        for (int j = 0; j < colorSortSize[color]; j++) {
          int card = colorSort[color][j];
          // card with desired color matches number
          if (lSnumber == hand[card].getSnumber()) {
            return card;
          }
        }
      }
    }
    
    // preferred color (for switching)
    char switchColor = Card.scolors[colorSortSizeSort[0]];
    // use a wild, if possible
    if (w >= 0) {
      hand[w].setMetaSColor(switchColor);
      return w;
    }
    // use a wild draw 4, if possible
    if (wplus >= 0) {
      hand[wplus].setMetaSColor(switchColor);
      return wplus;
    }
    
    // no card possible, draw a card
    return -1;
  }
  public char botSwitch() {
    // get the bot's preferred color (copy of portion of above method)
    
    int[] rs = new int[handSize];
    int rsSize = 0;
    int[] bs = new int[handSize];
    int bsSize = 0;
    int[] gs = new int[handSize];
    int gsSize = 0;
    int[] ys = new int[handSize];
    int ysSize = 0;
    
    for (int i = 0; i < handSize; i++) {
      if (hand[i].getScolor() == 'r') {
        rs[rsSize] = i;
        rsSize++;
      } else if (hand[i].getScolor() == 'b') {
        bs[bsSize] = i;
        bsSize++;
      } else if (hand[i].getScolor() == 'g') {
        gs[gsSize] = i;
        gsSize++;
      } else if (hand[i].getScolor() == 'y') {
        ys[ysSize] = i;
        ysSize++;
      }
    }
    
    int[] colorSortSize = {rsSize, bsSize, gsSize, ysSize,};
      
    int[] colorSortSizeSort = {-1, -1, -1, -1};
    for (int i = 0; i < 4; i++) {
      int j = 0;
      while (colorSortSizeSort[j] != -1 &&
        colorSortSize[colorSortSizeSort[j]] > colorSortSize[i]) {
        j++;
      }
      for (int k = i; k > j; k--) {
        colorSortSizeSort[k] = colorSortSizeSort[k - 1];
      }
      colorSortSizeSort[j] = i;
    }
    
    return new char[]{'r', 'b', 'g', 'y',}[colorSortSizeSort[0]];
  }
  public boolean playable(Card c) {
    // is a card playable?
    
    // get last card info
    char lSnumber = game.getLastDiscardSnumber();
    char lScolor = game.getLastDiscardScolor();
    
    // wilds can be played
    if (c.getScolor() == 'w') {
      /* wild draw fours cannot be played if another card can
       * (bot would pick the other card)
       */
      if (c.getSnumber() == '+' && !hand[botUse()].getSname().equals("w+")) {
        return false;
      }
      return true;
    }
    // matches color
    if (lScolor == 'x' || c.getScolor() == lScolor) {
      return true;
    }
    // matches number
    if (c.getSnumber() == lSnumber) {
      return true;
    }
    
    // can't be played
    return false;
  }
  public int cardSuch(char scolor, char snumber) {
    // there's a card in the hand with the color and number
    for (int i = 0; i < handSize; i++) {
      Card card = hand[i];
      if (card.getScolor() == scolor && card.getSnumber() == snumber) {
        // yes, and it's at index i
        return i;
      }
    }
    // no
    return -1;
  }
  public int pInput() {
    // get player input on their turn
    String inputStr = Main.ln().toLowerCase();
    
    // nothing is entered
    if (inputStr.length() == 0) {
      System.out.println("No card entered. Defaulting to draw.");
      return -1;
    }
    
    char pColor = inputStr.charAt(0);
    if (pColor == 'd') {
      // "draw" -> draw a card
      return -1;
    }
    // find color
    char[] colors = {'r', 'b', 'g', 'y', 'w'};
    boolean isFound = false;
    for (char color : colors) {
      if (color == pColor) {
        isFound = true;
      }
    }
    // no color found
    if (!isFound) {
      System.out.println("Card not recognized. Defaulting to draw.");
      return -1;
    }
    
    // find number
    char pNumber = ' ';
    if (inputStr.length() > 1) {
      char[] numbers = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', 'r', 's'};
      isFound = false;
      // long form check (eg "yellow 3")
      Scanner inputSc = new Scanner(inputStr);
      inputSc.next();
      if (inputSc.hasNext()) {
        pNumber = inputSc.next().charAt(0);
        if (pNumber == 'd') {
          // "draw" -> "+"
          isFound = true;
          pNumber = '+';
        } else {
          for (char number : numbers) {
            if (number == pNumber) {
              isFound = true;
              break;
            }
          }
        }
      }
      inputSc.close();
      if (!isFound) {
        // short form check (eg "y3")
        pNumber = inputStr.charAt(1);
        if (pNumber == 'd') {
          // "draw" -> "+"
          isFound = true;
          pNumber = '+';
        } else {
          for (char number : numbers) {
            if (number == pNumber) {
              isFound = true;
              break;
            }
          }
        }
      }
    }
    if (!isFound) {
      pNumber = ' ';
    }
    // wild card
    if (pColor == 'w') {
      if (pNumber != ' ' && pNumber != '+') {
        System.out.println(Card.longifyName(pColor, pNumber) +
          " is not a card. Defaulting to draw.");
        return -1;
      }
    } else if (!isFound) {
      // unrecognized number
      System.out.println("Card not recognized. Defaulting to draw.");
      return -1;
    }
    
    // get that card's index in hand
    int re = cardSuch(pColor, pNumber);
    if (re == -1) {
      System.out.println("You don't have a " + Card.longifyName(pColor, pNumber) +
        ". Defualting to draw.");
      return -1;
    }
    // can you play that card?
    if (!playable(hand[re])) {
      System.out.println(Card.longifyName(pColor, pNumber) +
        " can't be played right now. Defaulting to draw.");
      return -1;
    }
    return re;
  }
  public char pSwitch() {
    // ask the player to switch the color
    System.out.print("Switch the color to > ");
    char re = Main.ln("red").toLowerCase().charAt(0);
    if (re != 'r' && re != 'b' && re != 'g' && re != 'y') {
      System.out.println("Not a color. Defaulting to red.");
      re = 'r';
    }
    return re;
  }
  
  public void turn() {
    // on a turn
    int playCardI;
    if (isBot) {
      playCardI = botUse();
    } else {
      Main.logHand(hand, handSize);
      
      playCardI = pInput();
      if (playCardI >= 0) {
        Card playCard = hand[playCardI];
        if (playCard.getScolor() == 'w') {
          playCard.setMetaSColor(pSwitch());
        }
      }
    }
    if (playCardI >= 0) {
      Card playCard = hand[playCardI];
      play(playCardI);
      Main.logTurn(this, 0, playCard);
    } else {
      Card drawnCard = draw();
      if (drawnCard == null) {
        Main.logTurn(this, 2, null);
      } else {
        boolean doPlayIt = false;
        if (playable(drawnCard)) {
          doPlayIt = true;
          if (!isBot) {
            Main.logPlayDrawn(drawnCard);
            if (!Main.lnyn()) {
              doPlayIt = false;
            }
          }
        }
        if (doPlayIt) {
          Card playCard = hand[handSize - 1];
          if (playCard.getScolor() == 'w') {
            if (isBot) {
              playCard.setMetaSColor(botSwitch());
            } else {
              playCard.setMetaSColor(pSwitch());
            }
          }
          play(handSize - 1);
          Main.logTurn(this, 1, playCard);
        } else {
          Main.logTurn(this, 1, null);
        }
      }
    }
  }
  
  private static String nameBot() {
    if (!COMPUTER_ALIAS) {
      return "Bot" + (int) (1000 * Math.random());
    }
    // if all 200 names are somehow used up, repeat names.
    if (BOT_NAMES.length == usedBotNamesSize) {
      usedBotNamesSize = 0;
    }
    genName: while (true) {
      int choice = (int) (BOT_NAMES.length * Math.random());
      
      // if the name's taken, try again
      for (int i = 0; i < usedBotNamesSize; i++) {
        if (usedBotNames[i] == choice) {
          continue genName;
        }
      }
      
      // the name is found
      usedBotNames[usedBotNamesSize] = choice;
      usedBotNamesSize++;
      return BOT_NAMES[choice];
    }
  }
}

class Card {
  private final String sname;

  private final char scolor;
  private final char snumber;

  private final boolean postEffect;
  private final boolean preEffect;

  private char metaSColor;

  static final char[] scolors = {'r', 'b', 'g', 'y',};
  static final char[] snumbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', 'r', 's'};
  
  /**
   * @param c - color
   * @param n - number
   */
  public Card(char c, char n) {
    this.sname = "" + c + n;
    this.scolor = c;
    this.snumber = n;
    preEffect = n == '+' || n == 's';
    postEffect = n == 'r';
  }

  public String getSname() {
    return sname;
  }
  public char getScolor() {
    return scolor;
  }
  public char getSnumber() {
    return snumber;
  }
  
  public char getMetaSColor() {
    return metaSColor;
  }
  public void setMetaSColor(char metaSColor) {
    this.metaSColor = metaSColor;
  }
  
  public boolean getPreEffect() {
    return preEffect;
  }
  public boolean getPostEffect() {
    return postEffect;
  }
  
  @Override
  public String toString() {
    return longifyName(scolor, snumber);
  }
  
  public static String longifyName(char scolor, char snumber) {
    if (scolor == 'w') {
      if (snumber == '+') {
        return "Wild Draw 4";
      } else {
        return "Wild";
      }
    }
    return stringifyColor(scolor) + " " + stringifyNumber(snumber);
  }
  
  /**
   * @return a standard deck of 108 Uno cards
   * <p>(2 of each card per color, 0 of each color, 4 wilds, 4 wild +4s)
   */
  public static Card[] stdDeck() {
    Card[] cards = new Card[108];
    int numCards = 0;

    for (int i = 0; i < 4; i++) {
      cards[numCards] = new Card(scolors[i], '0');
      numCards++;
      for (int j = 1; j < 13; j++) {
        for (int k = 0; k < 2; k++) {
          cards[numCards] = new Card(scolors[i], snumbers[j]);
          numCards++;
        }
      }
    }
    for (int i = 0; i < 4; i++) {
      cards[numCards] = new Card('w', ' ');
      numCards++;
      cards[numCards] = new Card('w', '+');
      numCards++;
    }
    return cards;
  }
  
  /**
   * Shuffles an array of cards
   * @param deck - the cards to shuffle
   */
  public static void shuffle(Card[] deck) {
    for (int i = deck.length - 1; i >= 0; i--) {
      int swapWith = (int) (i * Math.random());
      Card temp = deck[i];
      deck[i] = deck[swapWith];
      deck[swapWith] = temp;
    }
  }
  
  /** Turn an char color into a string */
  public static String stringifyColor(char c) {
    switch (c) {
      case 'r': return "Red";
      case 'b': return "Blue";
      case 'g': return "Green";
      case 'y': return "Yellow";
      case 'w': return "Wild";
      case 'x': return "Any"; // is a starting wild
      default : return "Unknown";
    }
  }
  
  /** Turn a char color into a int */
  public static int intifyColor(char c) {
    switch (c) {
      case 'r': return 0;
      case 'b': return 1;
      case 'g': return 2;
      case 'y': return 3;
      case 'w': return 4;
      case 'x': return 4; // is a starting wild
      default : return -1;
    }
  }
  
  /** Turn an int color into a char */
  public static char charifyColor(int c) {
    switch (c) {
      case 0: return 'r';
      case 1: return 'b';
      case 2: return 'g';
      case 3: return 'y';
      case 4: return 'w';
      default: return '\0'; //unset
    }
  }
  
  /** Turn a char number into a string */
  public static String stringifyNumber(char n) {
    switch (n) {
      case '+': return "Draw 2";
      case 'r': return "Reverse";
      case 's': return "Skip";
      case ' ': return null; // is a normal wild
      default : return "" + n;
    }
  }
}
