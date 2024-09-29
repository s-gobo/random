/**
 * @param {string} s
 * @return {boolean}
 */

//goal: if the string is a number, return true

var isNumber = function(s) {
    let i = 0;
    if ("+-".includes(s[i])) {
        i++; //leading sign
    }
    let num = false;
    while ("1234567890".includes(s[i])) {
        num = true;
        i++; //predecimal digits
    }
    if (s[i] == ".") {
        i++ //decimal
    }
    while ("1234567890".includes(s[i])) {
        num = true;
        i++; //postdecimal digits
    }
    if (!num) {
        return false; //no digits
    }
    if (i === s.length) {
        return true; //nothing after
    }
    if ("eE".includes(s[i])) {
        i++ //exponent after
    } else {
        return false; //not an exponent after
    }
    
    if ("+-".includes(s[i])) {
        i++; //exponent leading sign
    }
    num = false;
    while ("1234567890".includes(s[i])) {
        num = true;
        i++; //exponent digits
    }
    if (!num) {
        return false; //no exponent digits
    }
    if (i === s.length) {
        return true; //nothing after
    }
    return false; //something after
};
