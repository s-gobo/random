const [remap, elipson] = [["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"], 0.0000000001];
function toBase(base, value) {
    for (let place = Math.floor(Math.log(value) / Math.log(base)), newValue = (place < 0)? "0." + "0".repeat(-1 - place): ""; place >= -100; place--) {
        newValue = newValue + (remap[Math.floor((value + elipson) / base**place)]?? "?") + (place == 0? ".": "");
        value = (value + elipson) % base ** place - elipson;
        if (value < 0) return newValue + (place > 0? "0".repeat(place): "");
    }
    return newValue;
}
