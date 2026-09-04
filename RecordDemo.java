public class RecordDemo {
    public record Person(String name, int age) {}

    public static void main(String[] args) {
        Person person = new Person("John Doe", 30);
        System.out.println("Name: " + person.name());
        System.out.println("Age: " + person.age());
        System.out.println("Person Record: " + person);
    }
}
