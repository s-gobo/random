let printBig = function(words) {
  let partKey = {
    A: "XXXXX", //all
    B: "XXXX ", //B
    P: "XXX  ", //K mid
    J: "XX XX", //not I
    N: "XX  X", //N mid top
    X: "X X X", //checker
    G: "X  XX", //G
    K: "X  X ", //K
    O: "X   X", //outside
    L: "X    ", //left
    C: " XXXX", //C top
    M: " XXX ", //middle
    Q: " XX X", //Q, bottom
    H: " X X ", //two I's
    S: " X   ", //Z bottom
    4: "  XX ", //4
    I: "  X  ", //I
    Z: "   X ", //Z top
    R: "    X", //right
    " ": "     ", //empty
  }
  let letterKey = {
    A: "MOAOO",
    B: "BOBOB",
    C: "CLLLC",
    D: "BOOOB",
    E: "ALBLA",
    F: "ALBLL",
    G: "CLGOM",
    H: "OOAOO",
    I: "AIIIA",
    J: "ARROM",
    K: "OKPKO",
    L: "LLLLA",
    M: "OJXOO",
    N: "ONXGO",
    O: "MOOOM",
    P: "BOBLL",
    Q: "MOOKQ",
    R: "BOBKO",
    S: "CLMRB",
    T: "AIIII",
    U: "OOOOM",
    V: "OOHHI",
    W: "OOXJO",
    X: "OHIHO",
    Y: "OHISL",
    Z: "AZISA",
    " ": "     ",
    
    1: "IPIIA",
    2: "MOZIA",
    3: "BRMRB",
    4: "Z4HAZ",
    5: "ALBRB",
    6: "CLBOM",
    7: "ARZIS",
    8: "MOMOM",
    9: "MOCRB",
    0: "MGXNM",
  }
  let result;
  for (currentLetter in letterKey) {
    if (currentLetter === " ") {
      result = "\" \"";
    } else {
      result = currentLetter;
    }
    result += ": [";
    for (let line = 0; line < 5; line++) {
      result += "\"";
      result += partKey[letterKey[currentLetter].split("")[line]].split("").map(x => x == "X"? currentLetter: " ").join("");
      result += "\", ";
    }
    result += "],";
    console.log(result);
  }
}

printBig();
