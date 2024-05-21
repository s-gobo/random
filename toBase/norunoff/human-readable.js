const remap = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
const elipson = 0.0000000001; //amount of error to allow

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
    
    let currentVal;
    
    //loop through place values
    for (let i = 0; i < 100 /*Max Iterations*/; i++) {
        
        //allow forwards error
        value += elipson;
        
        //add next digit
        currentVal = Math.floor(value / base**place)
        if (remap.length > currentVal) {
            newValue = newValue + remap[currentVal];
        } else {
            newValue = newValue = "?";
        }
        
        //find remainder
        value = value % base**place;
        
        //stop if there is no remainder (or very little)
        if (value < elipson) {
            if (place > 0) {
                //add 0s until decimal point
                for (; place > 0; place--) {
                    newValue = newValue + "0";
                }
            }
            break;
        }
        
        //reset forwards error
        value -= elipson;
        
        //move one place valure down, add decimal point if neccesary
        if (place == 0) {
            newValue = newValue + ".";
        }
        place--;
    }
    
    return newValue;
}
