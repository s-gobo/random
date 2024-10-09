var isNumber = function(s) {
    let i = +"+-".includes(s[0]);
    let pass = function(sym = "1234567890") {
        let re = sym.includes(s[i]); //does it pass
        i += re;
        return re;
    }
    let passNums = function() {
        let re = false;
        while (pass()) {
            re = true;
        }
        return re;
    }
    let passed = passNums(); //predecimal digits
    pass("."); //decimal
    if (!(passNums() || passed)) { //postdecimal digits
        return false; //no digits
    }
    if (i === s.length) {
        return true; //nothing after
    }
    if (!pass("eE")) {
        return false; //not an exponent after
    }
    pass("+-"); //exponent sign
    if (!passNums()) { //exponent digits
        return false; //no exponent digits
    }
    if (i === s.length) {
        return true; //nothing after
    }
    return false; //something after
};
