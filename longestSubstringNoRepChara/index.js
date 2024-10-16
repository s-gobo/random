// find the longest substring with no repeating characters
var longestSubstringNoRepChara = function(s) {
    let longest = "";
    let accum = "";
    for (let i = 0; i < s.length; i++) {
        const current = s[i];
        if (accum.includes(current)) {
            accum = accum.substring(accum.indexOf(current) + 1);
        }
        accum += current;
        if (accum.length > longest.length) {
            longest = accum;
        }
    }
    return longest;
};
