let genPrimes = function(total) {
    function addPrime(num) {
    }
    let a = [2], isPrime;
    for (let i = 3; a.length < total; i += 2) {
        let sqrt = Math.sqrt(i), isPrime = true;
        for (let j = 0; a[j] <= sqrt; j++) {
            if (i % a[j] == 0) {
                isPrime = false;
                break;
            }
        }
        if (isPrime) {
          a.push(i);
        }
    }
    return a;
}
