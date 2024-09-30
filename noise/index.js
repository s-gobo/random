//Created with Joe Iddon's tutorial
//joeiddon.github.io/projects/javascript/perlin

//wip

let noise = {
  memory: function() {
    let vectors = {};
    for (let x = 0; x <= 10; x++) {
      for (let y = 0; y <= 10; y++) {
        const theta = 2 * Math.PI * Math.random();
        vectors[[x, y]] = {x: Math.cos(theta), y: Math.sin(theta)};
      }
    }
    return vectors;
  }(),
  
  dp: function(v1, v2) {
    if (v1.x * v2.x + v1.y * v2.y > 1) {
      console.log(v1, v2);
    }
    return v1.x * v2.x + v1.y * v2.y;
  },
  
  in: function(a, b, wt) {
    wt = (wt + 1) / 2;
    wt = 6 * wt ** 5 - 15 * wt ** 4 + 10 * wt ** 3;
    wt = wt * 2 - 1;
    return a + (b - a) * wt;
  },
  
  get: function (x, y) {
    let d = Math.floor(y);
    let u = d + 1;
    let l = Math.floor(x);
    let r = l + 1;
    let pul = {x: l - x, y: u - y};
    let pur = {x: r - x, y: d - y};
    let pdl = {x: l - x, y: u - y};
    let pdr = {x: r - x, y: d - y};
    let dpul = this.dp(pul, this.memory[[u, l]]);
    let dpur = this.dp(pur, this.memory[[u, r]]);
    let dpdl = this.dp(pdl, this.memory[[d, l]]);
    let dpdr = this.dp(pdr, this.memory[[d, r]]);
    //console.log(dpul, dpur, dpdl, dpdr);
    let reu = this.in(dpul, dpur, x - l);
    let red = this.in(dpdl, dpdr, x - l);
    let re = this.in(reu, red, y - d);
    return re;
  },
};

let renderKey = function(number) {
  return number >= 1? "M":
    number >= 0.6? "X":
    number >= 0.2? "+":
    number >= -0.2? "-":
    number >= -0.6? "-":
    number >= -1? ".":
    " ";
}

for (let i = 0; i < 10; i += 0.1) {
  let string = "";
  for (let j = 0; j < 10; j += 0.1) {
    string += renderKey(noise.get(i, j));
  }
  //console.log(string);
}

