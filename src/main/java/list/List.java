package list;

import list.node.ListNode;

public interface List extends Iterable<ListNode> {
  /**
   * 在链表末尾添加一个节点
   *
   * @param node 待添加的节点
   */
  void addNode(ListNode node);

  /**
   * 查找节点值等于 value 的节点，并返回找到的第一个节点的引用
   *
   * @param value 查找的目标值
   * @return 找到的第一个节点引用，或 null 表示未找到到
   */
  ListNode findNode(Integer value);

  /**
   * 删除节点值等于 value 的节点，如果有链表中有多个符合条件的节点，仅删除第一个
   *
   * @param value 查找的目标值
   * @return 被删除节点的引用
   */
  ListNode deleteNode(Integer value);
}
