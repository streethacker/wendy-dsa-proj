package generic.whygeneric;

public class GenericDemo<T> {
  private T name;

  public GenericDemo() {}

  public void setName(T name) {
    this.name = name;
  }

  public T getName() {
    return name;
  }

  public static void main(String[] args) {
    GenericDemo<String> genericDemo = new GenericDemo<>();
    genericDemo.setName("Wendy");
    System.out.println(genericDemo.getName());
  }
}
