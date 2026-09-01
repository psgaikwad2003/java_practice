import java.util.*;
import java.util.function.*;


public class GenericsDemo {

    
    static class Pair<K, V> {
        private final K key;
        private final V value;

        Pair(K key, V value) { this.key = key; this.value = value; }
        K getKey() { return key; }
        V getValue() { return value; }

        @Override
        public String toString() { return "(" + key + ", " + value + ")"; }
    }

    
    static <T extends Comparable<T>> T findMax(List<T> list) {
        if (list == null || list.isEmpty())
            throw new IllegalArgumentException("List must not be empty");
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) max = item;
        }
        return max;
    }

    
    interface Printable { void print(); }

    static class PrintableNumber implements Comparable<PrintableNumber>, Printable {
        private final int value;
        PrintableNumber(int value) { this.value = value; }

        @Override public int compareTo(PrintableNumber o) { return Integer.compare(value, o.value); }
        @Override public void print() { System.out.println("  PrintableNumber: " + value); }
        @Override public String toString() { return String.valueOf(value); }
    }

    static <T extends Comparable<T> & Printable> T findAndPrintMax(List<T> list) {
        T max = findMax(list);
        System.out.print("  Max element -> ");
        max.print();
        return max;
    }

    
    @FunctionalInterface
    interface Transformer<I, O> {
        O transform(I input);
    }

    static <I, O> List<O> transformList(List<I> input, Transformer<I, O> transformer) {
        List<O> result = new ArrayList<>();
        for (I item : input) result.add(transformer.transform(item));
        return result;
    }

    
    
    static double sumOfNumbers(List<? extends Number> numbers) {
        double sum = 0;
        for (Number n : numbers) sum += n.doubleValue();
        return sum;
    }

    
    static void addIntegers(List<? super Integer> list, int count) {
        for (int i = 1; i <= count; i++) list.add(i);
    }

    
    @SafeVarargs
    static <T> List<T> listOf(T... items) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, items);
        return list;
    }

    
    static class GenericStack<E> {
        private final List<E> elements = new ArrayList<>();

        void push(E item) { elements.add(item); }

        E pop() {
            if (elements.isEmpty()) throw new EmptyStackException();
            return elements.remove(elements.size() - 1);
        }

        E peek() {
            if (elements.isEmpty()) throw new EmptyStackException();
            return elements.get(elements.size() - 1);
        }

        boolean isEmpty() { return elements.isEmpty(); }
        int size() { return elements.size(); }

        
        void pushAll(Collection<? extends E> items) {
            for (E item : items) push(item);
        }

        @Override
        public String toString() { return elements.toString(); }
    }

    static class EmptyStackException extends RuntimeException {
        EmptyStackException() { super("Stack is empty"); }
    }

    
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("         Java Generics Deep Dive");
        System.out.println("══════════════════════════════════════════\n");

        
        System.out.println("── 1. Generic Pair ─────────────────────");
        Pair<String, Integer> p1 = new Pair<>("Age", 25);
        Pair<Integer, List<String>> p2 = new Pair<>(1, Arrays.asList("a","b"));
        System.out.println("  " + p1);
        System.out.println("  " + p2 + "\n");

        
        System.out.println("── 2. Bounded Type (Comparable) ────────");
        List<Integer> nums = Arrays.asList(3, 7, 1, 9, 4);
        System.out.println("  Max integer: " + findMax(nums));
        List<String> words = Arrays.asList("banana", "apple", "cherry");
        System.out.println("  Max string : " + findMax(words) + "\n");

        
        System.out.println("── 3. Multiple Bounds ──────────────────");
        List<PrintableNumber> pnList = Arrays.asList(
            new PrintableNumber(10), new PrintableNumber(42), new PrintableNumber(7));
        findAndPrintMax(pnList);
        System.out.println();

        
        System.out.println("── 4. Transformer Interface ────────────");
        List<String> names = Arrays.asList("alice", "bob", "carol");
        List<String> upper = transformList(names, s -> s.toUpperCase());
        List<Integer> lengths = transformList(names, String::length);
        System.out.println("  Uppercase: " + upper);
        System.out.println("  Lengths  : " + lengths + "\n");

        
        System.out.println("── 5. PECS Principle ───────────────────");
        List<Integer> ints = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.5, 2.5);
        System.out.println("  Sum of ints   : " + sumOfNumbers(ints));
        System.out.println("  Sum of doubles: " + sumOfNumbers(doubles));
        List<Number> numberList = new ArrayList<>();
        addIntegers(numberList, 3);
        System.out.println("  After addIntegers: " + numberList + "\n");

        
        System.out.println("── 6. Generic Varargs ──────────────────");
        List<String> created = listOf("x", "y", "z");
        System.out.println("  listOf: " + created + "\n");

        
        System.out.println("── 7. Generic Stack ────────────────────");
        GenericStack<Number> stack = new GenericStack<>();
        stack.push(42);
        stack.push(3.14);
        stack.pushAll(Arrays.asList(100, 200)); 
        System.out.println("  Stack  : " + stack);
        System.out.println("  Peek   : " + stack.peek());
        System.out.println("  Pop    : " + stack.pop());
        System.out.println("  After  : " + stack + "\n");

        
        System.out.println("── 8. Type Erasure ─────────────────────");
        List<String> strList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        System.out.println("  List<String>.class == List<Integer>.class ? "
            + (strList.getClass() == intList.getClass()));
        System.out.println("  Both are: " + strList.getClass().getName());
        System.out.println("  Generics are erased at compile time!\n");
    }
}
