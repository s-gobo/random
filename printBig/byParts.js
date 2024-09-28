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
    Y: "OOHII",
    Z: "AZISA",
    " ": "     ",
  }
  let result, currentletter;
  for (let line = 0; line < 5; line++) {
    result = "";
    for (let char = 0; char < words.length; char++) {
      currentletter = words[char].toUpperCase();
      result += partKey[letterKey[currentletter].split("")[line]].split("").map(x => x == "X"? currentletter: " ").join("");
      result += "   ";
    }
    console.log(result);
  }
}

printBig("hello there");
