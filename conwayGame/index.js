class LifeGame {
  constructor() {
    this.alive = [];
  }
  
  getState(x, y) {
    return this.alive.some(coords => coords[0] === x && coords[1] === y);
  }
  
  setState(x, y, state) {
    state = !!state;
    if (this.getState(x, y) === state) {
      return;
    }
    if (state) {
      this.alive.push([x, y]);
    } else {
      this.alive.splice(this.alive.indexOf([x, y]), 1);
    }
  }
  
  flipState(x, y) {
    if (this.getState(x, y)) {
      this.alive.splice(this.alive.indexOf([x, y]), 1);
    } else {
      this.alive.push([x, y]);
    }
  }
  
  tick(times = 1) {
    if (times > 1) {
      for (let i = 0; i < times; i++) {
        tick();
      }
    }
    
    let count = new Map();
    const countAdd = function(x, y) {
      if (!count.has([x, y])) {
        count.set([x, y], 0);
      }
      count.set(count.get([x, y]) + 1);
    }
    
    for (let coord of this.alive) {
      countAdd(coord[0] + 1, coord[1] + 1);
      countAdd(coord[0] + 1, coord[1]    );
      countAdd(coord[0] + 1, coord[1] - 1);
      countAdd(coord[0]    , coord[1] + 1);
      countAdd(coord[0]    , coord[1] - 1);
      countAdd(coord[0] - 1, coord[1] + 1);
      countAdd(coord[0] - 1, coord[1]    );
      countAdd(coord[0] - 1, coord[1] - 1);
    }
    
    let nextAlive = [];
    for (let [coord, num] in count) {
      if (num === 3 || num === 2 && this.getState(coord[0], coord[1]))
      {
        nextAlive.push(key);
      }
    }
    
    this.alive = nextAlive;
  }
  
  bound(condition, start) {
    let re = start;
    for (let coord in this.alive) {
      if (condition(coord, re)) {
        re = coord[0];
      }
    }
    return re;
  }
  
  leftBound() {
    return this.bound((coord, re) => coord[0] < re, this.alive[0][0]);
  }
  rightBound() {
    return this.bound((coord, re) => coord[0] > re, this.alive[0][0]);
  }
  upBound() {
    return this.bound((coord, re) => coord[1] > re, this.alive[0][1]);
  }
  downBound() {
    return this.bound((coord, re) => coord[1] < re, this.alive[0][1]);
  }
  
  display(margins = 1) {
    let l = this.leftBound()  - margins;
    let r = this.rightBound() + margins;
    let u = this.upBound()    + margins;
    let d = this.downBound()  - margins;
    
    for (let i = u; i >= d; i--) {
      let re = "";
      for (let j = l; j <= r; j++) {
        re += this.getState(i, j)? "X": " ";
      }
      console.log(re);
    }
  }
}

let conway = new LifeGame();
conway.setState(0, 0, true);
console.log(conway.alive);
conway.display();
