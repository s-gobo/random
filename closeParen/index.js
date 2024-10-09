// Take a string of "(" and ")" characters
// Return how many additions of "(" or ")" characters
// are needed to match every "(" with a ")" and
// vice versa

/**
 * @param {string} s
 * @return {number}
 */
var minAddToMakeValid = function(s) {
    let num = 0;
    let nest = 0;
    for (let i = 0; i < s.length; i++) {
        if (s[i] === "(") {
            nest++;
        } else {
            if (nest === 0) {
                num++;
            } else {
                nest--;
            }
        }
    }
    num += nest;
    return num;
};
