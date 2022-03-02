package map;

import java.util.Objects;

// 类、类实现接口
// 泛型
public class HashMap<K, V> implements Map<K, V> {

  // 静态属性
  // 访问修饰符
  // final关键字
  private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

  private static final int MAXIMUM_CAPACITY = 1 << 30;

  private static final float DEFAULT_LOAD_FACTOR = 0.75f;

  // 属性
  private int size = 0;
  private int capacity = 0;
  private final float loadFactor;
  private int threshold;

  // 泛型数组
  private Node<K, V>[] table;

  // 静态内部类
  // 和实例无关
  static class Node<K, V> implements Map.Entry<K, V> {
    // final 是防止key被修改 -> 常量
    final K key;
    V value;
    Node<K, V> next;

    /*
     * 无参构造方法就不能用了
     */
    Node(K key, V value, Node<K, V> next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public V setValue(V newValue) {
      V oldValue = value;
      value = newValue;
      return oldValue;
    }

    public Node<K, V> getNext() {
      return next;
    }

    public void setNext(Node<K, V> next) {
      this.next = next;
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Map.Entry) {
        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
        return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(key) ^ Objects.hashCode(value);
    }
  }

  // 构造方法、构造方法重载
  public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0) {
      // 异常机制
      throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    }
    if (initialCapacity > MAXIMUM_CAPACITY) {
      initialCapacity = MAXIMUM_CAPACITY;
    }
    if (loadFactor <= 0) {
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
    }
    this.capacity = initialCapacity;
    this.loadFactor = loadFactor;
    this.threshold = resizeThreshold();
    //noinspection unchecked
    this.table = (Node<K, V>[]) new Node[capacity];
  }

  public HashMap() {
    this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  // 方法覆写
  @Override
  public V get(K key) {
    Node<K, V> node = getNode(key);
    if (node != null) {
      return node.getValue();
    }
    return null;
  }

  @Override
  public boolean containsKey(K key) {
    return getNode(key) != null;
  }

  @Override
  public V put(K key, V value) {
    return putVal(resize(), key, value, false);
  }

  @Override
  public V remove(K key) {
    if (table == null || table.length == 0) {
      return null;
    }
    int i = getSlot(key);
    Node<K, V> lo, hi;
    lo = table[i];
    if (Objects.equals(key, lo.getKey())) {
      table[i] = lo.getNext();
      lo.setNext(null);
      size--;
      return lo.getValue();
    }
    hi = lo.getNext();
    while (hi != null) {
      if (Objects.equals(key, hi.getKey())) {
        lo.setNext(hi.getNext());
        hi.setNext(null);
        size--;
        return hi.getValue();
      }
      lo = hi;
      hi = hi.getNext();
    }
    return null;
  }

  private Node<K, V>[] resize() {
    if (size < threshold) {
      return table;
    }
    int oldCap = capacity;
    int newCap = oldCap << 1;
    if (newCap > MAXIMUM_CAPACITY) {
      throw new RuntimeException(
          "Unable to increase capacity: current = " + oldCap + ", request = " + newCap);
    }
    @SuppressWarnings("unchecked")
    Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
    for (int j = 0; j < oldCap; j++) {
      Node<K, V> p = table[j];
      while (p != null) {
        putVal(newTab, p.getKey(), p.getValue(), true);
        p = p.getNext();
      }
    }
    table = newTab;
    capacity = newCap;
    threshold = resizeThreshold();
    return newTab;
  }

  // 链表遍历、插入、删除
  private V putVal(Node<K, V>[] table, K key, V value, boolean trans) {
    int i = getSlot(key, table.length);
    Node<K, V> p = table[i];
    while (p != null) {
      if (Objects.equals(key, p.getKey())) {
        V oldValue = p.getValue();
        p.setValue(value);
        return oldValue;
      }
      p = p.getNext();
    }
    table[i] = newNode(key, value, table[i]);
    if (!trans) {
      size++;
    }
    return value;
  }

  private Node<K, V> getNode(K key) {
    if (table == null || table.length == 0) {
      return null;
    }
    int i = getSlot(key);
    Node<K, V> p = table[i];
    while (p != null) {
      if (Objects.equals(key, p.getKey())) {
        return p;
      }
      p = p.getNext();
    }
    return null;
  }

  private int getSlot(K key, int capacity) {
    return key.hashCode() % capacity;
  }

  private int getSlot(K key) {
    return getSlot(key, this.capacity);
  }

  private Node<K, V> newNode(K key, V value, Node<K, V> next) {
    return new Node<>(key, value, next);
  }

  private int resizeThreshold() {
    return (int) (capacity * loadFactor);
  }

  public static void main(String[] args) {
    HashMap<String, Integer> m = new HashMap<>();
    m.put("Alice", 10);
    System.out.println("Add Alice, expect 10, actual: " + m.get("Alice"));
    m.put("Bob", 22);
    System.out.println("Add Bob, expect 22, actual: " + m.get("Bob"));
    m.put("Jack", 30);
    System.out.println("Add Jack, expect 30, actual: " + m.get("Jack"));

    m.remove("Jack");
    System.out.println("Jack is removed, expect null, actual:  " + m.get("Jack"));

    m.put("Jack", 30);
    m.put("Jack", 40);
    m.put("Jack", 50);
    System.out.println(
        "Jack is add back, expect value is 50(last put), actual: "
            + m.get("Jack")
            + ", expect size is 3(the key Jack is override with size remains 3), actual: "
            + m.size());

    m.put("Liu", 50);
    m.put("Son", 50);
    m.put("Jimmy", 50);
    m.put("Tom", 50);
    m.put("Hunter", 50);
    m.put("Pot", 50);
    m.put("Smith", 50);
    m.put("John", 50);
    m.put("Kimi", 50);
    m.put("Wendy", 50);
    System.out.println("Size keeps going, expect 13, actual: " + m.size());
    System.out.println("Capacity is now double initial capacity(16 → 32), actual: " + m.capacity);
  }
}
