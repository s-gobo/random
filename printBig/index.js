let printBigNoBreak = function(words) {
  let letterMap = {
    0: [" 000 ", "0  00", "0 0 0", "00  0", " 000 ", ],
    1: ["  1  ", "111  ", "  1  ", "  1  ", "11111", ],
    2: [" 222 ", "2   2", "   2 ", "  2  ", "22222", ],
    3: ["3333 ", "    3", " 333 ", "    3", "3333 ", ],
    4: ["   4 ", "  44 ", " 4 4 ", "44444", "   4 ", ],
    5: ["55555", "5    ", "5555 ", "    5", "5555 ", ],
    6: [" 6666", "6    ", "6666 ", "6   6", " 666 ", ],
    7: ["77777", "    7", "   7 ", "  7  ", " 7   ", ],
    8: [" 888 ", "8   8", " 888 ", "8   8", " 888 ", ],
    9: [" 999 ", "9   9", " 9999", "    9", "9999 ", ],
    A: [" AAA ", "A   A", "AAAAA", "A   A", "A   A", ],
    B: ["BBBB ", "B   B", "BBBB ", "B   B", "BBBB ", ],
    C: [" CCCC", "C    ", "C    ", "C    ", " CCCC", ],
    D: ["DDDD ", "D   D", "D   D", "D   D", "DDDD ", ],
    E: ["EEEEE", "E    ", "EEEE ", "E    ", "EEEEE", ],
    F: ["FFFFF", "F    ", "FFFF ", "F    ", "F    ", ],
    G: [" GGGG", "G    ", "G  GG", "G   G", " GGG ", ],
    H: ["H   H", "H   H", "HHHHH", "H   H", "H   H", ],
    I: ["IIIII", "  I  ", "  I  ", "  I  ", "IIIII", ],
    J: ["JJJJJ", "    J", "    J", "J   J", " JJJ ", ],
    K: ["K   K", "K  K ", "KKK  ", "K  K ", "K   K", ],
    L: ["L    ", "L    ", "L    ", "L    ", "LLLLL", ],
    M: ["M   M", "MM MM", "M M M", "M   M", "M   M", ],
    N: ["N   N", "NN  N", "N N N", "N  NN", "N   N", ],
    O: [" OOO ", "O   O", "O   O", "O   O", " OOO ", ],
    P: ["PPPP ", "P   P", "PPPP ", "P    ", "P    ", ],
    Q: [" QQQ ", "Q   Q", "Q   Q", "Q  Q ", " QQ Q", ],
    R: ["RRRR ", "R   R", "RRRR ", "R  R ", "R   R", ],
    S: [" SSSS", "S    ", " SSS ", "    S", "SSSS ", ],
    T: ["TTTTT", "  T  ", "  T  ", "  T  ", "  T  ", ],
    U: ["U   U", "U   U", "U   U", "U   U", " UUU ", ],
    V: ["V   V", "V   V", " V V ", " V V ", "  V  ", ],
    W: ["W   W", "W   W", "W W W", "WW WW", "W   W", ],
    X: ["X   X", " X X ", "  X  ", " X X ", "X   X", ],
    Y: ["Y   Y", " Y Y ", "  Y  ", " Y   ", "Y    ", ],
    Z: ["ZZZZZ", "   Z ", "  Z  ", " Z   ", "ZZZZZ", ],
    " ": ["     ", "     ", "     ", "     ", "     ", ],
  };
  for (let line = 0; line < 5; line++) {
    console.log(words.split("").map(x => letterMap[x][line]).join("   "));
  }
  console.log();
}
let printBig = function(words) {
  words.toUpperCase().split("\n").forEach(x => printBigNoBreak(x));
}

printBig("abcdef\nghijkl\nmnopqr\nstuvwx\nyz0123\n456789");
