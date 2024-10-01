//Created with Joe Iddon's tutorial
//joeiddon.github.io/projects/javascript/perlin

//1D

let noise = {
  memory: function() {
    let vectors = {};
    for (let x = 0; x <= 10; x++) {
      vectors[[x]] = {x: Math.random()};
    }
    return vectors;
  }(),
  
  dp: function(v1, v2) {
    return v1.x * v2.x;
  },
  
  in: function(a, b, wt) {
    wt = 6 * wt ** 5 - 15 * wt ** 4 + 10 * wt ** 3;
    return a + (b - a) * wt;
  },
  
  get: function (x, y) {
    let l = Math.floor(x);
    let r = l + 1;
    let pl = {x: l - x};
    let pr = {x: r - x};
    let dpl = this.dp(pl, this.memory[[l]]);
    let dpr = this.dp(pr, this.memory[[r]]);
    let re = this.in(dpl, dpr, x - l);
    return re;
  },
};

for (let i = 0; i < 10; i += 0.05) {
  let count = 40 + Math.floor(noise.get(i, 0) * 200)
  try {
    console.log("X".repeat(count));
  } catch {
    console.log(count);
  }
}
