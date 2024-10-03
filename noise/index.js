//Created with Joe Iddon's tutorial
//joeiddon.github.io/projects/javascript/perlin

//3D

let noise = {
  mem: function() {
    let randomSpherePoint = function() {
      let x, y, z, dist;
      do {
        x = 2 * Math.random() - 1;
        y = 2 * Math.random() - 1;
        z = 2 * Math.random() - 1;
        dist = Math.sqrt(x ** 2 + y ** 2 + z ** 2);
      } while (dist > 1);
      x /= dist;
      y /= dist;
      z /= dist;
      return {x: x, y: y, z: z};
    }
    let vectors = {};
    for (let x = 0; x < 10; x++) {
      for (let y = 0; y < 10; y++) {
        for (let z = 0; z < 10; z++) {
          vectors[[x, y, z]] = randomSpherePoint();
        }
      }
    }
    return vectors;
  }(),
  
  getmem: function(x, y, z) {
    x %= 10;
    y %= 10;
    z %= 10;
    return this.mem[[x, y, z]];
  },
  
  dp: function(v1, v2) {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
  },
  
  in: function(a, b, wt) {
    wt = 6 * wt ** 5 - 15 * wt ** 4 + 10 * wt ** 3;
    return a + (b - a) * wt;
  },
  
  get: function (x, y, z) {
    let d = Math.floor(y);
    let u = d + 1;
    let l = Math.floor(x);
    let r = l + 1;
    let b = Math.floor(z);
    let f = b + 1;
    let pbul = {x: l - x, y: u - y, z: b - z};
    let pbur = {x: r - x, y: u - y, z: b - z};
    let pbdl = {x: l - x, y: d - y, z: b - z};
    let pbdr = {x: r - x, y: d - y, z: b - z};
    let pful = {x: l - x, y: u - y, z: f - z};
    let pfur = {x: r - x, y: u - y, z: f - z};
    let pfdl = {x: l - x, y: d - y, z: f - z};
    let pfdr = {x: r - x, y: d - y, z: f - z};
    let dpbul = this.dp(pbul, this.getmem(l, u, b));
    let dpbur = this.dp(pbur, this.getmem(r, u, b));
    let dpbdl = this.dp(pbdl, this.getmem(l, d, b));
    let dpbdr = this.dp(pbdr, this.getmem(r, d, b));
    let dpful = this.dp(pful, this.getmem(l, u, f));
    let dpfur = this.dp(pfur, this.getmem(r, u, f));
    let dpfdl = this.dp(pfdl, this.getmem(l, d, f));
    let dpfdr = this.dp(pfdr, this.getmem(r, d, f));
    let rebu = this.in(dpbul, dpbur, x - l);
    let rebd = this.in(dpbdl, dpbdr, x - l);
    let refu = this.in(dpful, dpfur, x - l);
    let refd = this.in(dpfdl, dpfdr, x - l);
    let reb = this.in(rebd, rebu, y - d);
    let ref = this.in(refd, refu, y - d);
    let re = this.in(reb, ref, z - b);
    return re;
  },
};

let resolution = 2; //size of render parts
let zoom = 2; //inverse number of render parts
let speed = 1; //amount skipped per frame
let framerate = 2; //skips per second

let renderKey = function(number) {
  return number >= 0.5? "@":
    number >= 0.3? "X":
    number >= 0.1? "+":
    number >= -0.1? "|":
    number >= -0.3? "-":
    number >= -0.5? ".":
    " ";
}
let run = async function() {
  for (let i = 0; true; i += 0.1 * speed) {
    console.clear();
    for (let j = 0; j < 10 / zoom; j += 0.3 / resolution) {
      let string = "";
      for (let k = 0; k < 10 / zoom; k += 0.1 / resolution) {
        string += renderKey(noise.get(j, k, i));
      }
      console.log(string);
    }
    console.log();
    await new Promise(resolve => setTimeout(resolve, 1000 / framerate));
  }
}

run();
