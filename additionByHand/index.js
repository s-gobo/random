// two numbers are represented as linked lists in reverse order
// find the sum and return it as a linked list

/** linked lists are objects with the format
    {
        val: the digit
        next: the linked list of the next place values (acending, eg ones to tens)
    }

    the constructor ListNode(val, next) returns a linked list
*/

/**
 * Definition for singly-linked list.
 * function ListNode(val, next) {
 *     this.val = (val===undefined ? 0 : val)
 *     this.next = (next===undefined ? null : next)
 * }
 */
/**
 * @param {ListNode} l1
 * @param {ListNode} l2
 * @return {ListNode}
 */
var addTwoNumbers = function(l1, l2) {
    // make nodes if nonexistent
    l1 ||= new ListNode();
    l2 ||= new ListNode();
    let sum = l1.val + l2.val;
    // carryover
    if (sum >= 10) {
        sum -= 10;
        l1.next ||= new ListNode();
        l1.next.val++;
    }
    if (l1.next === null && l2.next === null) {
        return new ListNode(sum);
    }
    return new ListNode(sum, addTwoNumbers(l1.next, l2.next));
};
