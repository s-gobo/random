const remap = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
function toBase(base, value) {
    //setup place value
    let place = Math.floor(Math.log(value) / Math.log(base));
    let newValue = "";
    //add preceding 0's
    if (place < 0) {
        newValue = "0."
        for (let i = -1; i > place; i--) {
            newValue = newValue + "0"
        }
    }
    //loop through place values
    for (let i = 0; i < 100 /*Max Iterations*/; i++) {
        newValue = newValue + remap[Math.floor(value / base**place)];
        value = value % base**place;
        if (!value) {
            if (place > 0) {
                for (; place > 0; place--) {
                    newValue = newValue + "0";
                }
            }
            break;
        }
        if (place == 0) {
            newValue = newValue + ".";
        }
        place--;
    }
    
    return newValue;
}
