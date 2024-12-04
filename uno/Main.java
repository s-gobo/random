import java.util.Scanner;

public class Main {
  public static Scanner in = new Scanner(System.in);
  
  public static void main(String[] args) {
    Uno game = new Uno();
    game.play();
  }
  
  // INPUT METHODS
  public static String ln() {
    return in.nextLine();
  }
  public static String ln(String or) {
    String ln = ln();
    if (ln.equals("")) {
      System.out.println("No input found. Defaulting to " + or + ".");
      return or;
    }
    return ln;
  }
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
    draw = Card.stdDeck();
    Card.shuffle(draw);
    drawSize = draw.length;
    
    players = new Player[4];
    numPlayers = players.length;
    players[0] = new Player(this, false);
    for (int i = 1; i < numPlayers; i++) {
      players[i] = new Player(this, true);
    }
    for (int i = 0; i < 7; i++) {
      for (Player player : players) {
        player.draw();
      }
    }
    
    discard = new Card[108];
    discardSize = 0;
    
    Card start = draw();
    System.out.println("The starting card is " + start + "!");
    if (start.getScolor() == 'w') {
      start.withMetaSColor('x');
    }
    discard(start);
    
    turn = 0;
    isCW = true;
  }
  
  public Card getLastDiscard() {
    return lastDiscard;
  }
  
  public void play() {
    while (true) {
      lastDiscardTurnsAgo++;
      Player player = players[turn];
      boolean skip = false;
      if (lastDiscard.getPreEffect() && lastDiscardTurnsAgo == 1) {
        if (lastDiscard.getSnumber() == 's') {
          skip = true;
          System.out.println(player.toStringPlural() + " turn is skipped");
        } else if (lastDiscard.getSnumber() == '+') {
          if (lastDiscard.getScolor() == 'w') {
            player.penalty(4);
          } else {
            player.penalty(2);
          }
          skip = true;
        }
      }
      
      if (!skip) {
        player.turn();
        if (player.getNumCards() == 0) {
          System.out.println(player + " wins!");
          break;
        }
        if (lastDiscard.getPostEffect() && lastDiscardTurnsAgo == 0) {
          if (lastDiscard.getSnumber() == 'r') {
            isCW = !isCW;
            if (isCW) {
              System.out.println("Play direction is clockwise!");
            } else {
              System.out.println("Play direction is counterclockwise!");
            }
          }
        }
      }
      
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
    if (drawSize == 0) {
      if (discardSize == 1) {
        return null;
      }
      System.out.println("Discard is reshuffled to draw");
      discardSize--;
      drawSize = discardSize;
      for (int i = 0; i < discardSize; i++) {
        draw[i] = discard[i];
      }
      discard[0] = discard[discardSize];
      discardSize = 1;
      Card.shuffle(draw);
    }
    drawSize--;
    return draw[drawSize];
  }
  public void discard(Card c) {
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
    if (isBot) {
      name = "Bot" + (int) (1000 * Math.random());
    } else {
      name = "You";
    }
    this.game = game;
    hand = new Card[10];
    handSize = 0;
  }
  
  public int getNumCards() {
    return handSize;
  }
  
  // @Override
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
    Card drawnCard = game.draw();
    if (drawnCard == null) {
      return null;
    }
    if (hand.length == handSize) {
      Card[] temp = hand;
      hand = new Card[handSize + 10];
      for (int i = 0; i < temp.length; i++) {
        hand[i] = temp[i];
      }
    }
    hand[handSize] = drawnCard;
    handSize++;
    return drawnCard;
  }
  public void play(int cardI) {
    game.discard(hand[cardI]);
    handSize--;
    for (int i = cardI; i < handSize; i++) {
      hand[i] = hand[i + 1];
    }
  }
  public void penalty(int num) {
    if (name.equals("You")) {
      System.out.println("You draw " + num + " cards");
    } else {
      System.out.println(this + " draws " + num + " cards");
    }
    for (int i = 0; i < num; i++) {
      draw();
    }
  }
  
  public int botUse() {
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
    
    int[] rs = new int[handSize];
    int rsSize = 0;
    int[] bs = new int[handSize];
    int bsSize = 0;
    int[] gs = new int[handSize];
    int gsSize = 0;
    int[] ys = new int[handSize];
    int ysSize = 0;
    int w = -1;
    int wplus = -1;
    
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
        if (hand[i].getSnumber() == '+') {
          wplus = i;
        } else {
          w = i;
        }
      }
    }
    
    // System.out.println(
    //   rsSize + " " +
    //   bsSize + " " +
    //   gsSize + " " +
    //   ysSize + " ");
    
    int[][] colorSort = {rs, bs, gs, ys,};
    int[] colorSortSize = {rsSize, bsSize, gsSize, ysSize,};
    
    // for (int i = 0; i < rsSize; i++) {
    //   System.out.println("r" + hand[rs[i]]);
    // }
    // for (int i = 0; i < bsSize; i++) {
    //   System.out.println("b" + hand[bs[i]]);
    // }
    // for (int i = 0; i < gsSize; i++) {
    //   System.out.println("g" + hand[gs[i]]);
    // }
    // for (int i = 0; i < ysSize; i++) {
    //   System.out.println("y" + hand[ys[i]]);
    // }
      
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
      // System.out.println(
      //   (colorSortSizeSort[0] == -1? "-1": colorSortSize[colorSortSizeSort[0]]) + " " +
      //   (colorSortSizeSort[1] == -1? "-1": colorSortSize[colorSortSizeSort[1]]) + " " +
      //   (colorSortSizeSort[2] == -1? "-1": colorSortSize[colorSortSizeSort[2]]) + " " +
      //   (colorSortSizeSort[3] == -1? "-1": colorSortSize[colorSortSizeSort[3]]));
    }
    
    for (int i = 0; i < 4; i++) {
      int color = colorSortSizeSort[i];
      if (colorSortSize[color] == 0) {
        continue;
      }
      if (lScolor == 'x' || color == lScolorNum) {
        return colorSort[color][(int) (colorSortSize[color] * Math.random())];
      } else {
        for (int j = 0; j < colorSortSize[color]; j++) {
          int card = colorSort[color][j];
          if (lSnumber == hand[card].getSnumber()) {
            return card;
          }
        }
      }
    }
    
    char switchColor = new char[]{'r', 'b', 'g', 'y',}[colorSortSizeSort[0]];
    if (w >= 0) {
      hand[w].setMetaSColor(switchColor);
      return w;
    }
    if (wplus >= 0) {
      hand[wplus].setMetaSColor(switchColor);
      return wplus;
    }
    return -1;
  }
  public char botSwitch() {
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
    
    int[][] colorSort = {rs, bs, gs, ys,};
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
    Card lastDiscard = game.getLastDiscard();
    char lScolor = lastDiscard.getScolor();
    char lSnumber = lastDiscard.getSnumber();
    if (lScolor == 'w') {
      lScolor = lastDiscard.getMetaSColor();
      lSnumber = ' ';
    }
    if (c.getScolor() == 'w') {
      if (c.getSnumber() == '+' && !hand[botUse()].getSname().equals("w+")) {
        return false;
      }
      return true;
    }
    if (lScolor == 'x' || c.getScolor() == lScolor) {
      return true;
    }
    if (c.getSnumber() == lSnumber) {
      return true;
    }
    return false;
  }
  public int cardSuch(char scolor, char snumber) {
    for (int i = 0; i < handSize; i++) {
      Card card = hand[i];
      if (card.getScolor() == scolor && card.getSnumber() == snumber) {
        return i;
      }
    }
    return -1;
  }
  public int pInput() {
    String inputStr = Main.ln().toLowerCase();
    
    if (inputStr.length() == 0) {
      System.out.println("No card entered. Defaulting to draw.");
      return -1;
    }
    char pColor = inputStr.charAt(0);
    if (pColor == 'd') {
      return -1;
    }
    char[] colors = {'r', 'b', 'g', 'y', 'w'};
    boolean isFound = false;
    for (char color : colors) {
      if (color == pColor) {
        isFound = true;
      }
    }
    if (!isFound) {
      System.out.println("Card not recognized. Defaulting to draw.");
      return -1;
    }
    
    char pNumber = ' ';
    if (inputStr.length() > 1) {
      char[] numbers = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', 'r', 's'};
      isFound = false;
      Scanner inputSc = new Scanner(inputStr);
      inputSc.next();
      if (inputSc.hasNext()) {
        pNumber = inputSc.next().charAt(0);
        if (pNumber == 'd') {
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
      if (!isFound) {
        pNumber = inputStr.charAt(1);
        if (pNumber == 'd') {
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
    if (pColor == 'w') {
      if (pNumber != ' ' && pNumber != '+') {
        System.out.println(Card.longifyName(pColor, pNumber) +
          " is not a card. Defaulting to draw.");
        return -1;
      }
    } else if (!isFound) {
      System.out.println("Card not recognized. Defaulting to draw.");
      return -1;
    }
    int re = cardSuch(pColor, pNumber);
    if (re == -1) {
      System.out.println("You don't have a " + Card.longifyName(pColor, pNumber) +
        ". Defualting to draw.");
      return -1;
    }
    if (!playable(hand[re])) {
      System.out.println(Card.longifyName(pColor, pNumber) +
        " can't be played right now. Defaulting to draw.");
      return -1;
    }
    return re;
  }
  public char pSwitch() {
    System.out.print("Switch the color to > ");
    char re = Main.ln("red").toLowerCase().charAt(0);
    if (re != 'r' && re != 'b' && re != 'g' && re != 'y') {
      System.out.println("Not a color. Defaulting to red.");
      re = 'r';
    }
    return re;
  }
  
  public void turn() {
    int playCardI;
    if (isBot) {
      playCardI = botUse();
      
      if (playCardI >= 0) {
        Card playCard = hand[playCardI];
        play(playCardI);
        if (playCard.getScolor() == 'w') {
          String switchColor = playCard.getMetaColor();
          if (handSize == 1) {
            System.out.println(this + " plays " + playCard +
              ", switches the color to " + switchColor + ", and calls UNO!");
          } else {
            System.out.println(this + " plays " + playCard +
              " and switches the color to " + switchColor);
          }
        } else {
          if (handSize == 1) {
            System.out.println(this + " plays " + playCard + " and calls UNO!");
          } else {
            System.out.println(this + " plays " + playCard);
          }
        }
      } else {
        Card drawnCard = draw();
        if (drawnCard == null) {
          System.out.println(this + " passes. There are no more cards to draw.");
        } else {
          if (playable(drawnCard)) {
            Card playCard = hand[handSize - 1];
            play(handSize - 1);
            if (playCard.getScolor() == 'w') {
              playCard.setMetaSColor(botSwitch());
              String switchColor = playCard.getMetaColor();
              if (handSize == 1) {
                System.out.println(this + " draws and plays " + playCard +
                  ", switches the color to " + switchColor + ", and calls UNO!");
              } else {
                System.out.println(this + " draws and plays " + playCard +
                  " and switches the color to " + switchColor);
              }
            } else {
              if (handSize == 1) {
                System.out.println(this + " draws and plays " +
                  playCard + " and calls UNO!");
              } else {
                System.out.println(this + " draws and plays " + playCard);
              }
            }
          } else {
            System.out.println(this + " draws a card");
          }
        }
      }
    } else {
      System.out.println(toStringPlural() + " turn! " +
        toStringPlural() + " cards are:");
      for (int i = 0; i < handSize; i++) {
        System.out.println("  - " + hand[i]);
      }
      System.out.print("> ");
      playCardI = pInput();
      
      if (playCardI >= 0) {
        Card playCard = hand[playCardI];
        play(playCardI);
        if (playCard.getScolor() == 'w') {
          playCard.setMetaSColor(pSwitch());
          String switchColor = playCard.getMetaColor();
          if (handSize == 1) {
            System.out.println(this + " play " + playCard +
              ", switch the color to " + switchColor + ", and call UNO!");
          } else {
            System.out.println(this + " play " + playCard +
              " and switch the color to " + switchColor);
          }
        } else {
          if (handSize == 1) {
            System.out.println(this + " play " + playCard + " and call UNO!");
          } else {
            System.out.println(this + " play " + playCard);
          }
        }
      } else {
        Card drawnCard = draw();
        if (drawnCard == null) {
          System.out.println(this + " pass. There are no more cards to draw.");
        } else {
          if (playable(drawnCard)) {
            System.out.print(this + " draw a " + drawnCard +
              ". Play it? > ");
            if (Main.lnyn()) {
              Card playCard = hand[handSize - 1];
              play(handSize - 1);
              if (playCard.getScolor() == 'w') {
                playCard.setMetaSColor(pSwitch());
                String switchColor = playCard.getMetaColor();
                if (handSize == 1) {
                  System.out.println(this + " draw and play " + playCard +
                    ", switch the color to " + switchColor + ", and call UNO!");
                } else {
                  System.out.println(this + " draw and play " + playCard +
                    " and switch the color to " + switchColor);
                }
              } else {
                if (handSize == 1) {
                  System.out.println(this + " draw and play " +
                    playCard + " and call UNO!");
                } else {
                  System.out.println(this + " draw and play " + playCard);
                }
              }
            } else {
              System.out.println(this + " draw a " + drawnCard);
            }
          } else {
            System.out.println(this + " draw a " + drawnCard);
          }
        }
      }
    }
  }
}

class Card {
  private String name;
  private String sname;
  
  private String color;
  private char scolor;
  private String number;
  private char snumber;
  
  private boolean postEffect;
  private boolean preEffect;
  
  private char metaSColor;
  private String metaColor;
  
  public Card(String s) {
    preEffect = false;
    postEffect = false;
    
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
        preEffect = true;
      } else if (snumber == 'r') {
        number = "reverse";
        postEffect = true;
      } else if (snumber == 's') {
        number = "skip";
        preEffect = true;
      } else {
        number = "" + snumber;
      }
      name = color + " " + number;
    } else {
      snumber = ' ';
      number = "";
      name = color;
    }
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
  
  // @Override
  public String toString() {
    return name;
  }
  
  public static String longifyName(char scolor, char snumber) {
    String color;
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
    if (snumber == ' ') {
      return color;
    }
    String number;
    if (snumber == '+') {
      if (scolor == 'w') {
        number = "draw 4";
      } else {
        number = "draw 2";
      }
    } else if (snumber == 'r') {
      number = "reverse";
    } else if (snumber == 's') {
      number = "skip";
    } else {
      number = "" + snumber;
    }
    return color + " " + number;
  }
  
  public static Card[] stdDeck() {
    Card[] cards = new Card[108];
    int numCards = 0;
    
    String[] colors = {"r", "b", "g", "y"};
    String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "r", "s"};
    for (int i = 0; i < 4; i++) {
      cards[numCards] = new Card(colors[i] + "0");
      numCards++;
      for (int j = 0; j < 12; j++) {
        for (int k = 0; k < 2; k++) {
          cards[numCards] = new Card(colors[i] + numbers[j]);
          numCards++;
        }
      }
    }
    for (int i = 0; i < 4; i++) {
      cards[numCards] = new Card("w");
      numCards++;
      cards[numCards] = new Card("w+");
      numCards++;
    }
    return cards;
  }
  
  public static void shuffle(Card[] deck) {
    for (int i = deck.length - 1; i >= 0; i--) {
      int swapWith = (int) (i * Math.random());
      Card temp = deck[i];
      deck[i] = deck[swapWith];
      deck[swapWith] = temp;
    }
  }
}





