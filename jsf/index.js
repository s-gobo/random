//stackoverflow.com/questions/63673610/alternative-way-to-get-c-letter-in-js****/63713987#63713987

const num = function(x) {
  // +[] makes 0
  if (x == 0) {
    return "+[]";
  }
  // +!![] makes 1
  if (x < 10) {
    return "+!![]".repeat(x);
  }
  x = x.toString();
  let re = `[${num(x[0])}]`;
  for (let i = 1; i < x.length; i++) {
    re += `+[${num(x[i])}]`;
  }
  return re;
};

const le = function (x) {
  if (x.length == 1) {
    if (x in map) {
      return map[x];
    } else {
      return fun(`return '\\u${x.charCodeAt().toString(16).padStart(4, 0)}'`)
    }
  }
  let re = map[x[0]];
  for (let i = 1; i < x.length; i++) {
    re += "+" + le(x[i]);
  }
  return re;
};

let map = {};

map[0] = `(${num(0)}+[])`;
map[1] = `(${num(1)}+[])`;
map[2] = `(${num(2)}+[])`;
map[3] = `(${num(3)}+[])`;
map[4] = `(${num(4)}+[])`;
map[5] = `(${num(5)}+[])`;
map[6] = `(${num(6)}+[])`;
map[7] = `(${num(7)}+[])`;
map[8] = `(${num(8)}+[])`;
map[9] = `(${num(9)}+[])`;

// ![] makes false
map.f = `(![]+[])[${num(0)}]`;
map.a = `(![]+[])[${num(1)}]`;
map.l = `(![]+[])[${num(2)}]`;
map.s = `(![]+[])[${num(3)}]`;

// ![] makes true
map.t = `(!![]+[])[${num(0)}]`;
map.r = `(!![]+[])[${num(1)}]`;
map.e = `(!![]+[])[${num(3)}]`;

// [][[]] makes undefined
map.u = `([][[]]+[])[${num(0)}]`;
map.n = `([][[]]+[])[${num(1)}]`;
map.d = `([][[]]+[])[${num(2)}]`;
map.i = `([][[]]+[])[${num(5)}]`;

// +[![]] makes NaN
map.N = `(+[![]]+[])[${num(0)}]`;

// 11e100 makes 1.1e+101
map["."] = `(+(${le("11e100")})+[])[${num(1)}]`;
map["+"] = `(+(${le("11e100")})+[])[${num(4)}]`;

// 1e1000 makes Infinity
map.I = `(+(${le("1e1000")})+[])[${num(0)}]`;
map.y = `(+(${le("1e1000")})+[])[${num(7)}]`;

// [].flat makes function flat() {
map.c = `([][${le("flat")}]+[])[${num(3)}]`;
map.o = `([][${le("flat")}]+[])[${num(6)}]`;
map[" "] = `([][${le("flat")}]+[])[${num(8)}]`;
map["("] = `([][${le("flat")}]+[])[${num(13)}]`;
map[")"] = `([][${le("flat")}]+[])[${num(14)}]`;
map["{"] = `([][${le("flat")}]+[])[${num(16)}]`;

// [].entries makes [object Array Iterator]
map["["] = `([][${le("entries")}]()+[])[${num(0)}]`;
map.j= `([][${le("entries")}]()+[])[${num(3)}]`;
map.A = `([][${le("entries")}]()+[])[${num(8)}]`;
map["]"] = `([][${le("entries")}]()+[])[${num(22)}]`;

// .0000001 makes 1e-7
map["-"] = `(+(${le(".0000001")})+[])[${num(2)}]`;

// false.constructor makes function Boolean() {
map.B = `((![])[${le("constructor")}]+[])[${num(9)}]`;

// 0.constructor makes function Number() {
map.m = `((${num(0)})[${le("constructor")}]+[])[${num(11)}]`;
map.b = `((${num(0)})[${le("constructor")}]+[])[${num(12)}]`;

// "".constructor makes function String() {
map.S = `(([]+[])[${le("constructor")}]+[])[${num(9)}]`;
map.g = `(([]+[])[${le("constructor")}]+[])[${num(14)}]`;

// [].flat.constructor makes function Function() {
map.F = `([][${le("flat")}][${le("constructor")}]+[])[${num(9)}]`;
const fun = function(x) {
  return `[][${le("flat")}][${le("constructor")}](${le(x)})()`;
};

// [].flat last digit is }
map["}"] = `([][${le("flat")}]+[])[${le("slice")}]((${le("-")})+(${num(1)}))`;

// 35.toString(36) = z
map.h = `(+(${num(17)}))[${le("to")}+([]+[])[${le("constructor")}][${le("name")}]](${num(36)})`;
map.k = `(+(${num(20)}))[${le("to")}+([]+[])[${le("constructor")}][${le("name")}]](${num(36)})`;
map.p = `(+(${num(25)}))[${le("to")}+([]+[])[${le("constructor")}][${le("name")}]](${num(36)})`;
map.q = `(+(${num(26)}))[${le("to")}+([]+[])[${le("constructor")}][${le("name")}]](${num(36)})`;
map.v = `(+(${num(31)}))[${le("to")}+([]+[])[${le("constructor")}][${le("name")}]](${num(36)})`;
map.w = `(+(${num(32)}))[${le("to")}+([]+[])[${le("constructor")}][${le("name")}]](${num(36)})`;
map.x = `(+(${num(33)}))[${le("to")}+([]+[])[${le("constructor")}][${le("name")}]](${num(36)})`;
map.z = `(+(${num(35)}))[${le("to")}+([]+[])[${le("constructor")}][${le("name")}]](${num(36)})`;

// "".normalize(false) throws RangeError
map.R = `(${fun("try{String().normalize(false)}catch(f){return f}")}+[])[${num(0)}]`;
map.E = `(${fun("try{String().normalize(false)}catch(f){return f}")}+[])[${num(5)}]`;

// RegExp function
const reg = function(x = "") {
  return `${fun("return RegExp")}(${le(x)})`;
}

// RegExp() makes /(?:)/
map["/"] = `(${reg()}+[])[${num(0)}]`;
map["?"] = `(${reg()}+[])[${num(2)}]`;
map[":"] = `(${reg()}+[])[${num(3)}]`;

// RegExp("/") makes /\\//
map["\\"] = `(${reg("/")}+[])[${num(1)}]`;

// [[],[]] makes , throws SyntaxError
map[","] = `[[]][${le("concat")}]([[]])+[]`;
let synerr = fun(`try{Function([]+[[]].concat([[]]))()}catch(f){return f}`);
map["'"] = `(${reg("[\\u0027]")})[${le("exec")}](${synerr})[${le("0")}]`

let test = fun(`console.log("hello, world");`);
// console.log(test);
console.log(eval(test));




