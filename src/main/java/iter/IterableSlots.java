package iter;

import java.util.Iterator;

public class IterableSlots implements Iterable<Integer> {

  private Integer[] slots;

  public IterableSlots(Integer[] slots) {
    this.slots = slots;
  }

  public void setSlots(Integer[] slots) {
    this.slots = slots;
  }

  public Integer[] getSlots() {
    return slots;
  }

  @Override
  public Iterator<Integer> iterator() {
    return new Iter();
  }

  private class Iter implements Iterator<Integer> {

    private int cursor;

    public Iter() {
      cursor = 0;
    }

    @Override
    public boolean hasNext() {
      return cursor != slots.length;
    }

    @Override
    public Integer next() {
      return slots[cursor++];
    }
  }

  public static void main(String[] args) {
    IterableSlots iterableSlots = new IterableSlots(new Integer[] {1, 2, 3, 4, 5, 6});
    for (Integer i : iterableSlots) {
      System.out.println("Elements: " + i);
    }

    System.out.println("------------------- 等价于 -------------------");

    for (Iterator<Integer> iter = iterableSlots.iterator(); iter.hasNext();) {
      System.out.println("Elements: " + iter.next());
    }

    System.out.println("------------------- 等价于 -------------------");

    Iterator<Integer> iter = iterableSlots.iterator();
    while (iter.hasNext()) {
      System.out.println("Elements: " + iter.next());
    }
  }
}
