const remap = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
function toBase(base, value) {
    let place = Math.floor(Math.log(value) / Math.log(base)), newValue = (place < 0)? "0." + "0".repeat(-1 - place): "";
    for (; place >= -100; place--) {
        newValue = newValue + (remap[Math.floor(value / base**place)]?? "?") + (place == 0? ".": "");
        value = value % base**place;
        if (!value) {
            return newValue + (place > 0? "0".repeat(place): "");
        }
    }
    return newValue;
}
