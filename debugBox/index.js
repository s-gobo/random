const debugBox = function(label) {
  let outputraw, typeraw, type;
  // run the label code, output the error if it doesn't work
  try {
    outputraw = eval(label);
    typeraw = typeof outputraw;
    type = function type(value) {
      if (value === null) {
        return "null"; //null
      }
      if (!["object", "function"].includes(typeraw)) {
        return typeraw; // primitives
      }
      const tag = value[Symbol.toStringTag];
      if (typeof tag === "string") {
        return tag; // Symbol.toStringTag
      }
      if (typeraw === "function" &&
        Function.prototype.toString.call(value).startsWith("class")
      ) {
        return "class"; //classes
      }
      const className = value.constructor.name;
      if (typeof className === "string" && className !== "") {
        return className; //constructors
      }
      return typeraw;
    } (outputraw);
  } catch (e) {
    outputraw = e;
    type = "undefined";
  }
  // format the output into a string
  let output;
  try {
    output = outputraw.toString();
  } catch {
    output = "" + outputraw;
  }
  // format objects as JSON
  if (typeraw === "object") {
    output = JSON.stringify(outputraw);
  }
  output = output.split("\n");
  const llen = label.length;
  const olen = Math.max(...output.map(x => x.length)) + 2;
  const tlen = type.length;
  let len = Math.max(llen, olen, tlen);
  console.log(`┌${label + "─".repeat(len - llen)}┐`);
  for (x of output) {
    let aolen = x.length + 2;
    console.log(`│ ${x + " ".repeat(len - aolen)} │`);
  }
  console.log(`└${"─".repeat(len - tlen) + type}┘`);
};

let a = [0, 1, null, 5, 19, null, () => {}, [0, 3, {ok: "yes!"}], "really long string here yay"];
debugBox("a");
