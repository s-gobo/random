const debugBox = function(label, output) {
  
  const getBox = function(...objects) {
    let vlen = 0;
    for (let object of objects) {
      // preprocess objects (find type and stringify)
      let output = object.output;
      object.postType = function type(value) {
        const typeraw = typeof value;
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
      } (output);
      if (typeof output === "object") {
        if (circular.has(output)) {
          output = "circular:\n" + circular.get(output);
        } else {
          circular.set(output, object.path);
          let processObject = [];
          for (let property in output) {
            processObject.push({
              label: property,
              output: output[property],
              path: object.path + "/" + property,
            });
          }
          if (processObject.length === 0) {
            // object doesn't have properties
            object.postOutput = [];
            continue;
          } else {
            // call recursively
            output = getBox(...processObject);
          }
        }
      } else {
        // is not an object
        try {
          output = output.toString();
        } catch {
          output = "" + output;
        }
      }
      output = output.replace(/[\r]/, " ");
      output = output.replace(/[\t]/, "    ");
      output = output.split("\n");
      object.postOutput = output;
      // console.log("preout", object);
      
      // find vlen
      if (output.length > vlen) {
        vlen = output.length;
      }
    }
    // return box
    let re1 = re3 = "";
    let re2 = [];
    for (let i = 0; i < vlen; i++) {
      re2.push("");
    }
    for (let i = 0; i < objects.length; i++) {
      // is the ends of the box?
      const isLeft = i === 0;
      const isRight = i === objects.length - 1;
      // find length of box
      const object = objects[i];
      // format label
      let label = object.label
      label = label.replace(/[\n]/, "\\n");
      label = label.replace(/[\t]/, "\\t");
      const type = object.postType;
      const output = object.postOutput;
      // console.log("postout", object);
      
      // get length of output
      const llen = label.length;
      const olen = output.reduce((max, x) => {
        return x.length > max? x.length: max;
      }, "") + 2;
      const tlen = type.length;
      const len = Math.max(llen, olen, tlen);
      
      if (isLeft) {
        re1 += "┌";
        re3 += "└";
      }
      re1 += `${label + "─".repeat(len - llen)}`;
      for (let j = 0; j < vlen; j++) {
        let line;
        if (j < output.length) {
          line = output[j];
        } else {
          line = "";
        }
        let aolen = line.length + 2;
        if (isLeft) {
          re2[j] += "│";
        }
        re2[j] += ` ${line + " ".repeat(len - aolen)} │`;
      }
      re3 +=`${"─".repeat(len - tlen) + type}`;
      if (isRight) {
        re1 += "┐";
        re3 += "┘";
      } else {
        re1 += "┬";
        re3 += "┴";
      }
    }
    if (re2.length === 0) {
      return [re1, re3].join("\n");
    } else {
      re2 = re2.join("\n");
      return [re1, re2, re3].join("\n");
    }
  };
  

  if (output === undefined) {
    // generate output based on label
    if (typeof label === "string") {
      // it could be code!
      try {
        // run the code
        let unusedBufferVariable;
        output = eval("unusedBufferVariable = " + label);
      } catch {
        // it's just a string
        output = label;
      } 
    } else {
      // the label's the output (not a string)
      output = label;
    }
  }
  // stringify the label!
  try {
    label = label.toString();
  } catch {
    label = "" + label;
  }
  
  let circular = new Map();
  console.log(getBox({
    label: label,
    output: output,
    path: "/" + label,
  }));
};

let a = [];
a[0] = a;

debugBox("a",a);
