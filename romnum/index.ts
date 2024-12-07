const romNum = function(num: number): string {
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

for (let i = 0; i < 4000; i++) {
  console.log(Math.round(i * 100) / 100 + "\t: " + romNum(i));
}
