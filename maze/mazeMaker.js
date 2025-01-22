class Display {
  constructor(l = 10, w = 10, fill = false) {
    let re = [];
    for (let i = 0; i < w; i++) {
      let temp = [];
      for (let j = 0; j < l; j++) {
        temp.push(fill);
      }
      re.push(temp);
    }
    this.data = re;
  }
  getAt(x, y) {
    if (x >= 0 && x < this.getL() && y >= 0 && y < this.getW()) {
      return this.data[y][x];
    }
    return false;
  }
  setAt(x, y, value) {
    this.data[y][x] = value;
  }
  
  getL() {
    return this.data[0].length;
  }
  getW() {
    return this.data.length;
  }
  
  print(mapperfn = a => a? "██": "  ") {
    let re = "";
    for (let i = 0; i < this.data.length; i++) {
      for (let j = 0; j < this.data[0].length; j++) {
        re += mapperfn(this.data[i][j]);
      }
      re += "\n";
    }
    console.log(re);
  }
  
  printWall() {
    let re = "";
    for (let i = 0; i < this.data.length; i++) {
      for (let j = 0; j < this.data[0].length; j++) {
        if (!this.data[i][j]) {
          re += "  ";
          continue;
        }
        let temp = 0;
        if (this.getAt(j + 1, i)) {
          temp += 1;
        }
        if (this.getAt(j - 1, i)) {
          temp += 2;
        }
        if (this.getAt(j, i + 1)) {
          temp += 4;
        }
        if (this.getAt(j, i - 1)) {
          temp += 8;
        }
        re += ["╶╴", "╶─", "╴ ", "──", "╷ ", "┌─", "┐ ", "┬─", "╵ ", "└─", "┘ ", "┴─",
        "│ ", "├─", "┤ ", "┼─"][temp];
      }
      re += "\n";
    }
    console.log(re);
  }
}

class Maze {
  constructor() {
    this.d = new Display(17, 17, true);
    this.#generate();
    this.d.print();
    this.d.printWall();
  }
  #generate() {
    let x = Math.floor(Math.random() * (this.d.getL() - 2) + 1);
    let y = Math.floor(Math.random() * (this.d.getW() - 2) + 1);
    function mX(x, dir, times = 1) {
      return x + times * [1, 0, -1, 0][dir];
    }
    function mY(y, dir, times = 1) {
      return y + times * [0, 1, 0, -1][dir];
    }
    function mR(x, y, dir, u, r) {
      return [mX(mX(x, dir, u), aw(dir), r), mY(mY(y, dir, u), aw(dir), r)];
    }
    function cw(dir) {
      return (dir + 3) % 4;
    }
    function aw(dir) {
      return (dir + 1) % 4;
    }
    
    let dir = Math.floor(Math.random() * 4);
    this.d.setAt(x, y, false);
    let stack = [[x, y, aw(aw(dir))]];
      stack.push([x, y, aw(dir)]);
      stack.push([x, y, cw(dir)]);
      stack.push([x, y, dir]);
    while (stack.length > 0) {
      if (stack.length > 2 && Math.random() < 0.3) {
        if (Math.random() < 0.5) {
          [x, y, dir] = stack[stack.length - 2];
          stack.splice(stack.length - 2, 1);
        } else {
          [x, y, dir] = stack[stack.length - 3];
          stack.splice(stack.length - 3, 1);
        }
      } else {
        [x, y, dir] = stack.pop();
      }
      if (!this.d.getAt(...mR(x, y, dir, 1,  0)) ||
          !this.d.getAt(...mR(x, y, dir, 2,  0)) ||
          !this.d.getAt(...mR(x, y, dir, 2,  1)) ||
          !this.d.getAt(...mR(x, y, dir, 2, -1)) ||
          !this.d.getAt(...mR(x, y, dir, 1,  1)) ||
          !this.d.getAt(...mR(x, y, dir, 1, -1))) {
        continue;
      }
      x = mX(x, dir);
      y = mY(y, dir);
      this.d.setAt(x, y, false);
      stack.push([x, y, aw(dir)]);
      stack.push([x, y, cw(dir)]);
      stack.push([x, y, dir]);
    }
  }
}

let m = new Maze();
