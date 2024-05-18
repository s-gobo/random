//for nonogram solver
function createCombos(input, max) {
    function append(input) {
        let remap = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
        combo = combo + (input <= remap.length? remap[input]: "?");
    }
    let out = [], white = [], i2, combo, count;
    for(let i = 0; i < input.length; i++) {
        white.push(0);
    }
    while(1) {
        combo = "", count = 0;
        for(let i = 0; i < input.length; i++) {
            append(white[i] + (i != 0));
            append(input[i]);
            count+= white[i] + (i != 0) + input[i];
        }
        if(count <= max) {
            append(max - count);
            out.push(combo);
            //console.log(white, combo);
        }
        i2 = 0;
        white[i2]++;
        while (white[i2] == max) {
            white[i2] = 0;
            i2++;
            if(i2 >= white.length) {
                return out;
            }
            white[i2]++;
        }
    }
}
//console.log(createCombos([1, 1, 5], 20));




function splitString(input) {
    let remap = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
    let array = input.split("");
    let out = [];
    for(let i = 0; i < input.length; i++) {
        out.push(remap.indexOf(array[i]));
    }
    return out;
}
//console.log(splitString("643CS"));
