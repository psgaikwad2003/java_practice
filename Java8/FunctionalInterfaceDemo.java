import java.util.*;
import java.util.function.*;
import java.util.stream.*;


public class FunctionalInterfaceDemo {

    
    @FunctionalInterface
    interface StringProcessor {
        String process(String input);

        
        default StringProcessor andThenProcess(StringProcessor after) {
            return input -> after.process(this.process(input));
        }
    }

    @FunctionalInterface
    interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    
    static class Employee {
        String name;
        String dept;
        double salary;

        Employee(String name, String dept, double salary) {
            this.name = name; this.dept = dept; this.salary = salary;
        }

        @Override
        public String toString() {
            return name + "(" + dept + ", $" + String.format("%.0f", salary) + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("     Functional Interface Deep Dive");
        System.out.println("══════════════════════════════════════════\n");

        demoCustomInterface();
        demoPredicateChaining();
        demoFunctionComposition();
        demoConsumers();
        demoSupplier();
        demoMethodReferences();
        demoRealWorldPipeline();
    }

    static void demoCustomInterface() {
        System.out.println("── 1. Custom Functional Interface ──────");
        StringProcessor trim = String::trim;
        StringProcessor upper = String::toUpperCase;
        StringProcessor addBrackets = s -> "[" + s + "]";

        
        StringProcessor pipeline = trim.andThenProcess(upper).andThenProcess(addBrackets);
        System.out.println("  Result: " + pipeline.process("  hello world  "));

        
        TriFunction<String, String, Double, Employee> factory = Employee::new;
        Employee emp = factory.apply("Alice", "Eng", 95000);
        System.out.println("  Created: " + emp + "\n");
    }

    static void demoPredicateChaining() {
        System.out.println("── 2. Predicate Chaining ───────────────");
        List<Employee> team = Arrays.asList(
            new Employee("Alice", "Eng", 95000),
            new Employee("Bob", "Eng", 72000),
            new Employee("Carol", "HR", 88000),
            new Employee("Dave", "HR", 55000),
            new Employee("Eve", "Eng", 110000)
        );

        Predicate<Employee> isEng = e -> "Eng".equals(e.dept);
        Predicate<Employee> highEarner = e -> e.salary > 80000;
        Predicate<Employee> nameStartsA = e -> e.name.startsWith("A");

        
        List<Employee> engHighEarners = team.stream()
            .filter(isEng.and(highEarner))
            .collect(Collectors.toList());
        System.out.println("  Eng & high earners: " + engHighEarners);

        List<Employee> engOrStartsA = team.stream()
            .filter(isEng.or(nameStartsA))
            .collect(Collectors.toList());
        System.out.println("  Eng or starts 'A': " + engOrStartsA);

        long notEng = team.stream().filter(isEng.negate()).count();
        System.out.println("  Not Eng count    : " + notEng + "\n");
    }

    static void demoFunctionComposition() {
        System.out.println("── 3. Function Composition ─────────────");
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;
        Function<Integer, String> toLabel = x -> "Value=" + x;

        
        Function<Integer, String> pipeline = doubleIt.andThen(addTen).andThen(toLabel);
        System.out.println("  andThen(5)  : " + pipeline.apply(5)); 

        
        Function<Integer, Integer> composed = doubleIt.compose(addTen);
        System.out.println("  compose(5)  : " + composed.apply(5)); 

        
        UnaryOperator<String> shout = s -> s.toUpperCase() + "!";
        System.out.println("  shout       : " + shout.apply("hello") + "\n");
    }

    static void demoConsumers() {
        System.out.println("── 4. Consumer & BiConsumer ────────────");
        Consumer<String> print = s -> System.out.print("  " + s);
        Consumer<String> println = s -> System.out.println(" ← logged");

        Consumer<String> logAndPrint = print.andThen(println);
        logAndPrint.accept("Processing order #42");

        
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("Math", 95); scores.put("Physics", 88); scores.put("CS", 97);

        BiConsumer<String, Integer> formatter = (subj, score) ->
            System.out.println("  " + subj + ": " + score + (score >= 90 ? " ★" : ""));
        scores.forEach(formatter);
        System.out.println();
    }

    static void demoSupplier() {
        System.out.println("── 5. Supplier — Lazy Init ─────────────");
        
        Supplier<List<Integer>> lazyPrimes = () -> {
            System.out.println("  (Computing primes...)");
            List<Integer> primes = new ArrayList<>();
            for (int n = 2; primes.size() < 10; n++) {
                boolean isPrime = true;
                for (int d = 2; d * d <= n; d++) {
                    if (n % d == 0) { isPrime = false; break; }
                }
                if (isPrime) primes.add(n);
            }
            return primes;
        };

        System.out.println("  Supplier created (no computation yet)");
        List<Integer> result = lazyPrimes.get(); 
        System.out.println("  First 10 primes: " + result + "\n");
    }

    static void demoMethodReferences() {
        System.out.println("── 6. Method References ────────────────");
        List<String> names = Arrays.asList("carol", "alice", "bob");

        
        names.stream().map(FunctionalInterfaceDemo::capitalize).forEach(s -> System.out.print("  " + s));
        System.out.println();

        
        names.sort(String::compareToIgnoreCase);
        System.out.println("  Sorted: " + names);

        
        List<Employee> emps = Arrays.asList("X", "Y").stream()
            .map(n -> new Employee(n, "Eng", 70000))
            .collect(Collectors.toList());
        System.out.println("  Constructed: " + emps + "\n");
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    static void demoRealWorldPipeline() {
        System.out.println("── 7. Real-World Pipeline ──────────────");
        List<Employee> company = Arrays.asList(
            new Employee("Alice", "Eng", 95000),
            new Employee("Bob", "Sales", 62000),
            new Employee("Carol", "Eng", 88000),
            new Employee("Dave", "Sales", 55000),
            new Employee("Eve", "Eng", 110000),
            new Employee("Frank", "HR", 73000)
        );

        
        Function<List<Employee>, List<Employee>> filterEng =
            list -> list.stream().filter(e -> "Eng".equals(e.dept)).collect(Collectors.toList());

        Function<List<Employee>, List<Employee>> sortBySalary =
            list -> list.stream().sorted(Comparator.comparingDouble(e -> -e.salary)).collect(Collectors.toList());

        Function<List<Employee>, String> formatReport =
            list -> list.stream().map(e -> "    " + e.name + " → $" + String.format("%.0f", e.salary))
                .collect(Collectors.joining("\n"));

        String report = filterEng.andThen(sortBySalary).andThen(formatReport).apply(company);
        System.out.println("  Engineering (by salary desc):\n" + report + "\n");
    }
}
