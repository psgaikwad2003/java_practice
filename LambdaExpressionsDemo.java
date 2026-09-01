import java.util.*;
import java.util.function.*;

public class LambdaExpressionsDemo {

    @FunctionalInterface
    interface MathOperation {
        int operate(int a, int b);
    }

    @FunctionalInterface
    interface Greeting {
        String greet(String name);
    }

    static void printResult(String op, int a, int b, MathOperation operation) {
        System.out.println(op + " of " + a + " and " + b + " = " + operation.operate(a, b));
    }

    static void sayHello(String name, Greeting greeting) {
        System.out.println(greeting.greet(name));
    }

    public static void main(String[] args) {
        System.out.println("=== Lambda Expressions Demo ===\n");

        
        MathOperation add = (a, b) -> a + b;
        MathOperation multiply = (a, b) -> a * b;
        MathOperation power = (a, b) -> (int) Math.pow(a, b);

        printResult("Addition", 10, 5, add);
        printResult("Multiply", 10, 5, multiply);
        printResult("Power", 2, 8, power);

        
        Greeting formal = name -> "Good morning, " + name + "!";
        Greeting casual = name -> "Hey, " + name + "!";
        sayHello("Alice", formal);
        sayHello("Bob", casual);

        System.out.println();

        
        System.out.println("--- Runnable Lambda ---");
        Runnable task = () -> System.out.println("Task executed on thread: " + Thread.currentThread().getName());
        task.run();

        
        System.out.println("\n--- Sorting with Comparator Lambda ---");
        List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Eve", "Bob", "Dave"));
        System.out.println("Before: " + names);
        names.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        System.out.println("After (A-Z): " + names);
        names.sort((s1, s2) -> s2.compareToIgnoreCase(s1));
        System.out.println("After (Z-A): " + names);

        
        System.out.println("\n--- Predicate Lambda ---");
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);

        List<Integer> numbers = Arrays.asList(-4, -1, 0, 3, 6, 8, 11, 14);
        System.out.print("Even numbers: ");
        numbers.stream().filter(isEven).forEach(n -> System.out.print(n + " "));
        System.out.println();
        System.out.print("Even & positive: ");
        numbers.stream().filter(isEvenAndPositive).forEach(n -> System.out.print(n + " "));
        System.out.println();

        
        System.out.println("\n--- Function and BiFunction ---");
        Function<String, Integer> strLength = String::length;
        Function<Integer, String> intToStr = num -> "Number: " + num;
        Function<String, String> combined = strLength.andThen(intToStr);

        System.out.println(combined.apply("Lambda"));
        System.out.println(combined.apply("Java Programming"));

        BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
        System.out.println(repeat.apply("Java! ", 3));

        
        System.out.println("\n--- Method References ---");
        List<String> words = Arrays.asList("lambda", "stream", "java", "functional");
        words.stream()
             .map(String::toUpperCase)
             .forEach(System.out::println);

        
        System.out.println("\n--- Supplier and Consumer ---");
        Supplier<List<String>> listSupplier = ArrayList::new;
        List<String> freshList = listSupplier.get();
        freshList.add("Supplier");
        freshList.add("provides value");
        System.out.println("Supplied list: " + freshList);

        Consumer<String> printer = s -> System.out.println("[LOG] " + s);
        Consumer<String> upperPrinter = s -> System.out.println("[LOG-UPPER] " + s.toUpperCase());
        Consumer<String> bothConsumers = printer.andThen(upperPrinter);
        bothConsumers.accept("hello consumer");

        System.out.println("\n=== Demo Complete ===");
    }
}
