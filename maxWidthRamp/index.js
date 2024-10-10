/**
 * @param {number[]} nums
 * @return {number}
 */

// Find the largest difference between i and j
// such that i < j and nums[i] <= nums[j]

// And also O(n^2) is too slow :(

//WIP

var maxWidthRamp = function(nums) {
    let passed = [], max = 0;
    for (let i = 0; i < nums.length; i++) {
        passed.push(nums[i]);
        const find = passed.findIndex(x => x <= nums[i]);
        if (i - find > max) {
            max = i - find;
        }
    }
    return max;
};
