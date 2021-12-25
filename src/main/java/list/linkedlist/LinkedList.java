package list.linkedlist;

import list.List;
import list.node.ListNode;

import java.util.Iterator;
import java.util.Objects;
import java.util.StringJoiner;

public class LinkedList implements List {

  private ListNode head;
  private ListNode tail;
  private int size;

  public LinkedList() {
    head = null;
    tail = null;
    size = 0;
  }

  public LinkedList(ListNode node) {
    head = tail = node;
    size = 1;
  }

  public LinkedList(Integer value) {
    head = tail = new ListNode(value);
    size = 1;
  }

  @Override
  public void addNode(ListNode node) {
    if (head == null) {
      head = tail = node;
      size = 1;
      return;
    }
    tail.setNext(node);
    tail = tail.getNext();
    size++;
  }

  @Override
  public ListNode findNode(Integer value) {
    ListNode lp = head;
    while (lp != null) {
      if (Objects.equals(lp.getValue(), value)) {
        return lp;
      }
      lp = lp.getNext();
    }
    return null;
  }

  @Override
  public ListNode deleteNode(Integer value) {
    ListNode slowPtr, fastPtr;
    slowPtr = fastPtr = head;
    while (fastPtr != null) {
      if (Objects.equals(fastPtr.getValue(), value)) {
        slowPtr.setNext(fastPtr.getNext());
        fastPtr.setNext(null);
        size--;
        return fastPtr;
      }
      slowPtr = fastPtr;
      fastPtr = fastPtr.getNext();
    }
    return null;
  }

  @Override
  public Iterator<ListNode> iterator() {
    return new LinkedListIterator(this);
  }

  static class LinkedListIterator implements Iterator<ListNode> {

    private ListNode lp;

    public LinkedListIterator(LinkedList linkedList) {
      this.lp = linkedList.head;
    }

    @Override
    public boolean hasNext() {
      return lp != null;
    }

    @Override
    public ListNode next() {
      ListNode n = lp;
      lp = lp.getNext();
      return n;
    }
  }

  public static void main(String[] args) {
    List linkedList = new LinkedList();
    linkedList.addNode(new ListNode(1));
    linkedList.addNode(new ListNode(3));
    linkedList.addNode(new ListNode(2));
    linkedList.addNode(new ListNode(4));
    linkedList.addNode(new ListNode(3));
    linkedList.addNode(new ListNode(5));
    linkedList.addNode(new ListNode(6));

    // 查找 value = 3 的节点
    ListNode targetNode = linkedList.findNode(3);
    if (targetNode != null) {
      System.out.println(String.format("查找到的节点：%s", targetNode.getValue()));
      // 后一个节点应该是2，而不是5
      if (targetNode.getNext() != null) {
        System.out.println(String.format("该节点的后续节点为： %s", targetNode.getNext().getValue()));
      }
    }

    // 删除 value = 4 的节点
    ListNode deletedNode = linkedList.deleteNode(4);
    if (deletedNode != null) {
      System.out.println(String.format("被删除的节点为: %s", deletedNode.getValue()));
    }
    StringJoiner joiner = new StringJoiner(" ⟶ ");
    for (ListNode node : linkedList) {
      joiner.add(String.valueOf(node.getValue()));
    }
    System.out.println("删除后的链表如下：" + joiner.toString());
  }
}
