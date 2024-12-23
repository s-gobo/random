// Based off 2023 APCSA FRQ #2
// apcentral.collegeboard.org/media/pdf/ap23-frq-comp-sci-a.pdf

"use strict";

const sign = function(msg, width) {
  msg = msg.split(" ");
  let space = 0;
  let re = "";
  for (let word of msg) {
    if (word.length <= width) {
      if (space + word.length > width) {
        re += "\n";
        space = 0;
      }
      re += word;
      space += word.length;
    } else {
      let i = width - space;
      re += word.substring(0, i);
      re += "\n";
      while (i + width < word.length) {
        re += word.substring(i, i + width);
        re += "\n";
        i += width;
      }
      space = word.length - i;
      re += word.substring(i);
    }
    if (space !== width) {
      re += " ";
      space++;
    }
  }
  return re;
}


// Testing script
const test = [10, 15, 20, 40];
for (let x of test) {
  console.log("_".repeat(x));
  console.log(sign("Everything on sale, please come in", x));
}
