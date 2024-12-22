const romToNum = function(rom) {
  if (rom === "") {
    return -1;
  }
  rom = rom.toUpperCase();
  const ones = ["I", "X", "C", "M"];
  const fives = ["V", "L", "D"];
  let re = 0;
  let i = 0;
  
  for (let place = ones.length - 1; i < rom.length; place--) {
    if (place < 0) {
      return -1;
    } else if (ones.length > place + 1 && rom.slice(i, i + 2) === ones[place] + ones[place + 1]) { // IX
      re += 9 * 10 ** place;
      i += 2;
      continue;
    } else if (fives.length > place && rom[i] === fives[place]) { // V, VI, VII, VIII
      re += 5 * 10 ** place;
      i++;
    } else if (fives.length > place && rom.slice(i, i + 2) === ones[place] + fives[place]){ // IV
      re += 4 * 10 ** place;
      i += 2;
      continue;
    }
    for (let j = 0; j < 3 && rom[i] === ones[place]; j++) { //III
      re += 10 ** place;
      i++;
    }
  }
  
  return re;
}


// Testing

const romNum = function(num) {
  const ones = ["I", "X", "C", "M"];
  const halves = ["S", "V", "L", "D"];
  
  let re = "";
  for (let i = 3; i >= 0; i--) {
    // straight digits (XXX)
    while (num >= 10 ** i) {
      re += ones[i];
      num -= 10 ** i;
    }
    
    // one till ten (IX)
    if (num >= 10 ** i * .9 && i !== 0) {
      re += ones[i - 1] + ones[i];
      num -= 10 ** i * .9;
    }
    // five (V)
    else if (num >= 10 ** i * .5) {
      re += halves[i];
      num -= 10 ** i * .5;
    }
    // one till five (IV)
    else if (num >= 10 ** i * .4 && i !== 0) {
      re += ones[i - 1] + halves[i];
      num -= 10 ** i * .4;
    }
  }
  
  // zero check
  if (re === "") {
    re = "N";
  }
  return re;
}

for (let i = 1; i < 4000; i++) {
  console.log(Math.round(i * 100) / 100 +
    "\t" + romToNum(romNum(i)) +
    "\t: " + romNum(i));
  console.assert(romToNum(romNum(i)) === i)
}


