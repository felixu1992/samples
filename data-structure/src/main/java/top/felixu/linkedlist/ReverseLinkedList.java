package top.felixu.linkedlist;

/**
 * 反转一个单链表。
 * 示例:
 * 输入: 1->2->3->4->5->NULL
 * 输出: 5->4->3->2->1->NULL
 * 进阶:
 * 你可以迭代或递归地反转链表。你能否用两种方法解决这道题？
 *
 * @author felixu
 * @since 2021.03.22
 */
public class ReverseLinkedList {
    public static ListNode reverseList(ListNode head) {
        /*
         *  1   ->   2   ->   3   ->   4   ->   5   ->   null
         *  ↑        ↑        ↑
         * pre    current   next
         *
         *  null   <-   1   <-   2   ->   3   ->   4   ->   5   ->   null
         *                       ↑        ↑
         *                      pre    current
         *
         *  null   <-   1   <-   2   ->   3   ->   4   ->   5   ->   null
         *                       ↑        ↑        ↑
         *                      pre    current   next
         */
        if (null == head)
            return head;
        ListNode pre = head;
        ListNode current = head.next;
        pre.next = null;
        while (current != null) {
            ListNode next = current.next;
            current.next = pre;
            pre = current;
            current = next;
        }
        return pre;
    }

    public static ListNode reverseList1(ListNode head) {
        /*
         *  null   1   ->   2   ->   3   ->   4   ->   5   ->   null
         *   ↑     ↑
         *  temp  head
         *
         *  null   <-   1   ->   2   ->   3   ->   4   ->   5   ->   null
         *              ↑        ↑
         *            temp     head
         */
        if (null == head)
            return head;
        ListNode temp = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = temp;
            temp = head;
            head = next;
        }
        return temp;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode second = new ListNode(2);
        head.next = second;
        ListNode third = new ListNode(3);
        second.next = third;
        ListNode forth = new ListNode(4);
        third.next = forth;
        ListNode five = new ListNode(5);
        forth.next = five;
        five.next = null;
        ListNode node = reverseList1(head);
        System.out.println(node.val);
        System.out.println(node.next.val);
        System.out.println(node.next.next.val);
        System.out.println(node.next.next.next.val);
        System.out.println(node.next.next.next.next.val);
    }
}

class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}