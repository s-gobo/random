let genPrimes = function(max) {
    if (max < 2) {
        return [];
    }
    let addPrime = function(num) {
        let sqrt = Math.sqrt(num);
        for (let i = 0; a[i] <= sqrt; i++) {
            if (num % a[i] == 0) {
                return;
            }
        }
        a.push(num);
    }
    let a = [2];
    for (let i = 3; i <= max; i++) {
        addPrime(i);
    }
    return a;
}

let simplify = function(num, denom) {
    let divis = num / denom;
    if (Number.isInteger(divis) || denom == 0 || num == Infinity || num == -Infinity) {
        return divis;
    }
    let negative = Math.sign(num) != Math.sign(denom);
    num = Math.abs(num);
    denom = Math.abs(denom);
    let primes = genPrimes(num);
    let thisPrime;
    let newNum = 1;
    let i = 0;
    while (num != 1) {
        if (num % (thisPrime = primes[i]) == 0) {
            num /= thisPrime;
            if (denom % thisPrime == 0) {
                denom /= thisPrime;
            } else {
                newNum *= thisPrime;
            }
        } else {
            i++;
        }
    }
    num *= newNum;
    if (negative) {
        num = -num;
    }
    return num + "/" + denom;
}

console.log(simplify(1000, 2024));
