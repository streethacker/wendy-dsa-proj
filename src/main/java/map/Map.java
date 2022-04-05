package map;  // 包

// 接口、内部接口
public interface Map<K, V> {
  // 接口的方法签名
  int size();

  boolean isEmpty();

  V get(K key);

  @SuppressWarnings("UnusedReturnValue")
  V put(K key, V value);

  @SuppressWarnings("UnusedReturnValue")
  V remove(K key);

  boolean containsKey(K key);

  interface Entry<K, V> {
    K getKey();

    V getValue();

    @SuppressWarnings("UnusedReturnValue")
    V setValue(V value);

    boolean equals(Object o);

    int hashCode();
  }
}
