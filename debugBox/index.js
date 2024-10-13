const debugBox = function(label) {
  let outputraw, type;
  try {
    outputraw = eval(label);
    type = typeof outputraw;
  } catch (e) {
    outputraw = e;
    type = "undefined";
  }
  let output;
  try {
    output = outputraw.toString();
  } catch {
    output = "" + outputraw;
  }
  if (type === "object") {
    type = Object.prototype.toString.call(outputraw);
    output = JSON.stringify(outputraw);
  }
  const llen = label.length;
  const olen = output.length + 2;
  const tlen = type.length;
  let len = Math.max(llen, olen, tlen);
  console.log(`┌${label + "─".repeat(len - llen)}┐`);
  console.log(`│ ${output + " ".repeat(len - olen)} │`);
  console.log(`└${"─".repeat(len - tlen) + type}┘`);
};

let variable = {
  cool: "stuff",
  thing: () => {
    console.log("yay");
  }
}
debugBox("variable");
