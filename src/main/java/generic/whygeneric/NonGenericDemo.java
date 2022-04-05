package generic.whygeneric;

public class NonGenericDemo {
  private Object name;

  public NonGenericDemo() {}

  public void setName(Object name) {
    this.name = name;
  }

  public Object getName() {
    return name;
  }

  public static void main(String[] args) {
    NonGenericDemo nonGenericDemo = new NonGenericDemo();
    nonGenericDemo.setName("Wendy");
    System.out.println(nonGenericDemo.getName());
  }
}
