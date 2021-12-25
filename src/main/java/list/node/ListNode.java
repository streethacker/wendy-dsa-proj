package list.node;

import java.util.Objects;

public class ListNode {
  /** 节点存储的值 */
  private Integer value;

  /** 指向下一个节点的指针/引用 */
  private ListNode next;

  public ListNode(Integer value) {
    this.value = value;
    this.next = null;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public ListNode getNext() {
    return next;
  }

  public void setNext(ListNode next) {
    this.next = next;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ListNode listNode = (ListNode) o;
    return Objects.equals(value, listNode.value);
  }
}
