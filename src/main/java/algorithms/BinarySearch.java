package algorithms;

import java.util.Objects;

public class BinarySearch {
  public static Integer findValue(Integer[] numbers, Integer value) {
    if (null == numbers || numbers.length == 0) {
      return -1;
    }
    int low = 0;
    int high = numbers.length - 1;
    while (low <= high) {
      int mid = (low + high) / 2;
      if (Objects.equals(value, numbers[mid])) {
        return mid;
      } else if (value < numbers[mid]) {
        high = mid - 1;
      } else {
        low = mid + 1;
      }
    }
    return -1;
  }

  public static void main(String[] args) {
    Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    System.out.println(String.format("Search value: %d ⇝ at index: %d", 3, findValue(numbers, 3)));
    System.out.println(
        String.format("Search value: %d ⇝ at index: %d", 10, findValue(numbers, 10)));
    System.out.println(String.format("Search value: %d ⇝ at index: %d", 1, findValue(numbers, 1)));
    System.out.println(
        String.format("Search value: %d ⇝ at index: %d", 20, findValue(numbers, 20)));
  }
}
