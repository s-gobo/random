/*******************************************************\
|*         TAKES A REALLY LONG TIME TO RUN             *|
|*         YOU HAVE BEEN WARNED!                       *|
\*******************************************************/

let board = [
  [0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0],
];

let cages = [
  [[0, 0], [0, 1]],
  [[0, 2], [0, 3]],
  [[0, 4], [1, 3], [1, 4]],
  [[0, 5], [0, 6], [1, 5]],
  [[0, 7], [1, 6], [1, 7], [2, 6]],
  [[0, 8], [1, 8], [2, 8]],
  [[1, 0], [2, 0], [3, 0]],
  [[1, 1], [1, 2], [2, 1], [2, 2], [3, 2]],
  [[2, 3], [2, 4], [2, 5]],
  [[2, 7], [3, 6], [3, 7]],
  [[3, 8], [4, 7], [4, 8]],
  [[4, 0], [4, 1], [4, 2], [5, 0]],
  [[4, 6], [5, 6], [5, 7]],
  [[5, 8], [6, 8]],
  [[6, 0], [7, 0]],
  [[6, 1], [6, 2]],
  [[6, 3], [6, 4], [7, 3], [7, 4]],
  [[6, 5], [7, 5], [8, 5]],
  [[6, 6], [6, 7]],
  [[7, 1], [7, 2]],
  [[7, 6], [7, 7], [7, 8]],
  [[8, 0], [8, 1], [8, 2]],
  [[8, 3], [8, 4]],
  [[8, 6], [8, 7], [8, 8]],
];

let i = 0, j = 0;

main: while (i != 9 && j != 9) {
  // increment value
  board[i][j]++;
  
  // value overflow
  if (board[i][j] > 9) {
    board[i][j] = 0;
    // prev
    j--;
    if (j < 0) {
      j = 8;
      i--;
    }
    continue main;
  }
  
  // match prev row
  for (let k = 0; k < j; k++) {
    if (board[i][k] === board[i][j]) {
      continue main;
    }
  }
  
  // match prev clmn
  for (let k = 0; k < i; k++) {
    if (board[k][j] === board[i][j]) {
      continue main;
    }
  }
  
  // match prev box
  for (let k = 0; k < i % 3 * 3 + j % 3; k++) {
    if (board[Math.floor(i / 3) * 3 + Math.floor(k / 3)]
        [Math.floor(j / 3) * 3 + k % 3] === board[i][j]) {
      continue main;
    }
  }
  
  // get cage
  let cageNum = 0, cageCell, cage;
  search: while (cageNum < cages.length) {
    cage = cages[cageNum];
    cageCell = 0;
    while (cageCell < cage.length) {
      cagePoint = cage[cageCell];
      if (cagePoint[0] === i && cagePoint[1] === j) {
        break search;
      }
      cageCell++;
    }
    cageNum++;
  }
  
  // match cage
  if (cageNum !== cages.length) {
    for (let k = 0; k < cageCell; k++) {
      if (board[cage[k][0]][cage[k][1]] === board[i, j]) {
        continue main;
      }
    }
  }
  
  // sum cage
  let sum = 0;
  if (cageNum !== cages.length && cageCell === cage.length - 1) {
    for (let [k, l] of cage) {
      sum += board[k][l];
    }
    if (Math.sqrt(sum) % 1 !== 0) {
      continue main;
    }
  }
  
  // next
  j++;
  if (j >= 9) {
    j = 0;
    i++;
  }
}

console.log(board);
