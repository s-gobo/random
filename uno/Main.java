import java.util.Scanner;

public class Main {
  private static final boolean ANSI = true;
  
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
          out += Card.longifyColor(play.getMetaSColor()).toLowerCase();
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
    System.out.println(out);
  }
  
  /**
   * @param msg - system message
   */
  public static void log(String msg) {
    String out = "";
    if (ANSI) { out += ANSI_DIM; }
    out += msg;
    if (ANSI) { out += ANSI_NORM; }
    System.out.println(out);
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
    System.out.println(out);
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
    System.out.print(out);
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
    System.out.print(out);
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
    System.out.println(out);
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
    System.out.println(out);
  }
}

class Uno {
  private Player[] players;
  private int numPlayers;
  
  private Card[] draw;
  private int drawSize;
  
  private Card[] discard;
  private Card lastDiscard;
  private int lastDiscardTurnsAgo;
  private int discardSize;
  
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
    
    // discard pile
    discard = new Card[108];
    discardSize = 0;
    
    // starting card
    Card start = draw();
    Main.logStart(start);
    // starting wild cards have any color
    if (start.getScolor() == 'w') {
      start.withMetaSColor('x');
    }
    discard(start);
    
    // start position and rotation
    turn = 0;
    isCW = true;
  }
  
  public Card getLastDiscard() {
    return lastDiscard;
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
  public void discard(Card c) {
    // discard the card
    lastDiscard = c;
    lastDiscardTurnsAgo = 0;
    discard[discardSize] = c;
    discardSize++;
  }
}

class Player {
  private boolean isBot;
  private String name;
  private Uno game;
  
  private Card[] hand;
  private int handSize;
  
  public Player(Uno game, boolean bot) {
    isBot = bot;
    
    // name bots and players
    if (isBot) {
      name = "Bot" + (int) (1000 * Math.random());
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
  public String toStringPlural() {
    String toString = toString();
    if (toString.equals("You")) {
      toString += "r";
    } else if (toString.charAt(toString.length() - 1) == 's') {
      toString += "\'";
    } else {
      toString += "\'s";
    }
    return toString;
  }
  
  public Card draw() {
    // draw a card
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
  public void play(int cardI) {
    // discards a card, based on index in hand
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
    Card lastDiscard = game.getLastDiscard();
    char lSnumber = lastDiscard.getSnumber();
    char lScolor = lastDiscard.getScolor();
    if (lScolor == 'w') {
      lScolor = lastDiscard.getMetaSColor();
      lSnumber = ' ';
    }
    int lScolorNum =
      lScolor == 'r'? 0:
      lScolor == 'b'? 1:
      lScolor == 'g'? 2:
      lScolor == 'y'? 3: 4;
    
    // arrays that sort the bot's hand
    int[] rs = new int[handSize];
    int rsSize = 0;
    int[] bs = new int[handSize];
    int bsSize = 0;
    int[] gs = new int[handSize];
    int gsSize = 0;
    int[] ys = new int[handSize];
    int ysSize = 0;
    // indexes of wild cards
    int w = -1;
    int wplus = -1;
    
    // sort cards into arrays
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
      } else if (hand[i].getScolor() == 'w') {
        // set wild indexes
        if (hand[i].getSnumber() == '+') {
          wplus = i;
        } else {
          w = i;
        }
      }
    }
    
    // new array for all sorted cards
    int[][] colorSort = {rs, bs, gs, ys,};
    int[] colorSortSize = {rsSize, bsSize, gsSize, ysSize,};
    
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
    char switchColor = new char[]{'r', 'b', 'g', 'y',}[colorSortSizeSort[0]];
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
    Card lastDiscard = game.getLastDiscard();
    char lScolor = lastDiscard.getScolor();
    char lSnumber = lastDiscard.getSnumber();
    if (lScolor == 'w') {
      lScolor = lastDiscard.getMetaSColor();
      lSnumber = ' ';
    }
    
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
}

class Card {
  private final String name;
  private final String sname;

  private final String color;
  private final char scolor;
  private final String number;
  private final char snumber;

  private final boolean postEffect;
  private final boolean preEffect;

  private char metaSColor;
  private String metaColor;

  private static final char[] scolors = {'r', 'g', 'b', 'y',};
  private static final String[] colors = {"Red", "Green", "Blue", "Yellow"};
  
  private static final char[] snumbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', 'r', 's'};
  private static final String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Draw 2", "Reverse", "Skip"};
  
  /**
   * @param s - a string with the card's short name
   */
  public Card(String s) {
    boolean repreEffect = false;
    boolean repostEffect = false;
    
    sname = s;
    scolor = s.charAt(0);
    if (scolor == 'r') {
      color = "red";
    } else if (scolor == 'b') {
      color = "blue";
    } else if (scolor == 'g') {
      color = "green";
    } else if (scolor == 'y') {
      color = "yellow";
    } else if (scolor == 'w') {
      color = "wild";
    } else {
      color = "" + scolor;
    }
    if (s.length() > 1) {
      snumber = s.charAt(1);
      if (snumber == '+') {
        if (scolor == 'w') {
          number = "draw 4";
        } else {
          number = "draw 2";
        }
        repreEffect = true;
      } else if (snumber == 'r') {
        number = "reverse";
        repostEffect = true;
      } else if (snumber == 's') {
        number = "skip";
        repreEffect = true;
      } else {
        number = "" + snumber;
      }
      name = color + " " + number;
    } else {
      snumber = ' ';
      number = "";
      name = color;
    }

    postEffect = repostEffect;
    preEffect = repreEffect;
  }
  
  /**
   * @param c - color
   * @param n - number
   */
  public Card(char c, char n) {
    this.sname = "" + c + n;
    this.scolor = c;
    this.snumber = n;

    String recolor = "";
    for (int i = 0; i < scolors.length; i++) {
      if (scolors[i] == c) {
        recolor = colors[i];
        break;
      }
    }

    String renumber = "";
    for (int i = 0; i < snumbers.length; i++) {
      if (snumbers[i] == n) {
        renumber = numbers[i];
        break;
      }
    }

    if (c == 'w') {
      recolor = "Wild";
      if (n == '+') {
        renumber = "Draw 4";
      }
    }

    color = recolor;
    number = renumber;

    if (renumber.equals("")) {
      name = recolor;
    } else {
      name = recolor + " " + renumber;
    }

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
  
  public String getMetaColor() {
    return metaColor;
  }
  public char getMetaSColor() {
    return metaSColor;
  }
  public Card withMetaSColor(char metaSColor) {
    this.metaSColor = metaSColor;
    metaColor =
      metaSColor == 'r'? "red":
      metaSColor == 'b'? "blue":
      metaSColor == 'g'? "green":
      metaSColor == 'y'? "yellow":
      "" + metaSColor;
    return this;
  }
  public void setMetaSColor(char metaSColor) {
    this.withMetaSColor(metaSColor);
  }
  
  public boolean getPreEffect() {
    return preEffect;
  }
  public boolean getPostEffect() {
    return postEffect;
  }
  
  @Override
  public String toString() {
    return name;
  }
  
  public static String longifyName(char scolor, char snumber) {
    String recolor = "";
    for (int i = 0; i < scolors.length; i++) {
      if (scolors[i] == scolor) {
        recolor = colors[i];
        break;
      }
    }

    String renumber = "";
    for (int i = 0; i < snumbers.length; i++) {
      if (snumbers[i] == snumber) {
        renumber = numbers[i];
        break;
      }
    }

    if (scolor == 'w') {
      recolor = "wild";
      if (snumber == '+') {
        renumber = "Draw 4";
      }
    }

    if (renumber.equals("")) {
      return recolor;
    }
    return recolor + " " + renumber;
  }
  
  /**
   * @return a standard deck of 108 Uno cards
   * (2 of each card per color, 0 of each color, 4 wilds, 4 wild +4s)
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
  
  public static String longifyColor(char c) {
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
  
  public static String longifyNumber(char n) {
    switch (n) {
      case '+': return "Draw 2";
      case 'r': return "Reverse";
      case 's': return "Skip";
      case ' ': return null; // is a normal wild
      default : return "" + n;
    }
  }
}
