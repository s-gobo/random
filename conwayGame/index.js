class LifeGame {
  constructor() {
    this.alive = [];
    this.time = 0;
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
    
    this.time++;
    
    let count = {};
    const countAdd = function(x, y) {
      if (count[[x, y]] === undefined) {
        count[[x, y]] = 0;
      }
      count[[x, y]]++;
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
    for (let coord in count) {
      let num = count[coord];
      coord = coord.split(",").map(x => +x);
      if (num === 3 || num === 2 && this.getState(coord[0], coord[1]))
      {
        nextAlive.push(coord);
      }
    }
    
    this.alive = nextAlive;
  }
  
  leftBound() {
    return Math.min(...this.alive.map(x => x[0]));
  }
  rightBound() {
    return Math.max(...this.alive.map(x => x[0]));
  }
  upBound() {
    return Math.max(...this.alive.map(x => x[1]));
  }
  downBound() {
    return Math.min(...this.alive.map(x => x[1]));
  }
  
  displayThumb(margins = 1) {
    let l = this.leftBound()  - margins;
    let r = this.rightBound() + margins;
    let u = this.upBound()    + margins;
    let d = this.downBound()  - margins;
    
    this.display(l, r, d, u);
  }
  
  display(l, r, d, u) {
    console.log("Tick "+ this.time + ": ");
    for (let i = u; i >= d; i--) {
      let re = "";
      for (let j = l; j <= r; j++) {
        re += this.getState(j, i)? "█ ":
          i === 0 && j == 0? "┼ ":
          i === 0? "─ ": j === 0? "│ ": "· ";
      }
      console.log(re);
    }
  }
}

let conway = new LifeGame();
conway.setState(0, 0, true);
conway.setState(0, 1, true);
conway.setState(1, 1, true);
conway.setState(0, -1, true);
conway.setState(-1, 0, true);

async function run() {
  while (true) {
    console.clear();
    conway.display(-10, 10, -5, 5);
    conway.tick();
    
    await new Promise(resolve => setTimeout(resolve, 100));   
  }
}

run();

