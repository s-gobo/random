/**
 * @param {number[][]} intervals
 * @return {number[][]}
 */
var merge = function(intervals) {
    let nonIntervals = [[-Infinity, Infinity]];
    for (let i = 0; i < intervals.length; i++) {
        
        console.log(nonIntervals);
        //find interval start
        let j = 0;
        while (intervals[i][0] >= (nonIntervals[j]??[])[0]) {
            //Interval starts outside X[]
            j++;
        }
        //find interval end
        j--;
        let start = nonIntervals[j][0] != intervals[i][0];
        let startInt = [nonIntervals[j][0], intervals[i][0]];
        let end = intervals[i][1] != nonIntervals[j][1];
        let endInt = [intervals[i][1], nonIntervals[j][1]];
        if (intervals[i][1] <= nonIntervals[j][1]) {
            //Interval ends inside [X]
            if (start && end) {
                nonIntervals.splice(j, 1, startInt, endInt);
            } else if (start) {
                nonIntervals.splice(j, 1, startInt);
            } else if (end) {
                nonIntervals.splice(j, 1, endInt);
            } else {
                nonIntervals.splice(j, 1);
            }
            continue;
        }
        //Interval ends outside []X
        if (intervals[i][0] < nonIntervals[j][1]) {
            //Interval starts inside [X]X
            if (start) {
                nonIntervals.splice(j, 1, startInt);
            } else {
                nonIntervals.splice(j, 1);
                j--;
            }
        }
        j++;
        //delete everything in between
        while (intervals[i][1] > nonIntervals[j][1]) {
            nonIntervals.splice(j, 1);
        }
        
        if (intervals[i][1] > nonIntervals[j][0]) {
            //Interval ends inside X[X]
            let end = intervals[i][1] != nonIntervals[j][1];
            let endInt = [intervals[i][1], nonIntervals[j][1]];
            if (end) {
                nonIntervals.splice(j, 1, endInt);
            } else {
                nonIntervals.splice(j, 1);
                j--;
            }
        }
    }

    console.log(nonIntervals);
    intervals = [];
    for (let i = 0; i < nonIntervals.length - 1; i++) {
        intervals.push([nonIntervals[i][1], nonIntervals[i + 1][0]]);
    }
    return intervals;
};
