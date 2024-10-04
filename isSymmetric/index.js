/* Determines if a given number is symmetric by
 * rotation or horizontal or vertical reflection
 * by s-gobo, 9/28/2024
 */
let symmetry = {
  rotation: {
    0: 0,
    1: 1,
    2: 2,
    5: 5,
    6: 9,
    8: 8,
    9: 6,
  },
  horizontal: {
    0: 0,
    1: 1,
    2: 5,
    3: 3,
    5: 2,
    8: 8,
  },
  vertical: {
    0: 0,
    1: 1,
    2: 5,
    5: 2,
    8: 8,
  },
};

let isSymmetric = function(number) {
  let digits = number.toString().split("").map(x => +x);
  let horizRefl = true, vertRefl = true, rot = true;
  for (let i = 0; i < digits.length; i++) {
    let digit = digits[i], backDigit = digits[digits.length - 1 - i];
    horizRefl = horizRefl && symmetry.horizontal[digit] === digit;
    vertRefl = vertRefl && symmetry.vertical[digit] === backDigit;
    rot = rot && symmetry.rotation[digit] === backDigit;
    if (!(horizRefl || vertRefl || rot)) {
      return false;
    }
  }
  console.log(`${number}\t${horizRefl}\t${vertRefl}\t${rot}`);
  return true;
}

console.log("#\thoriz\tvert\trot")
for (let i = 0; i <= 1400; i++) {
  isSymmetric(i);
}
