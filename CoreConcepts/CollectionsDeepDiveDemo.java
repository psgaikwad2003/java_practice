import java.util.*;
import java.util.stream.*;

public class CollectionsDeepDiveDemo {

    record Student(String name, int age, String major, double gpa) {}

    public static void main(String[] args) {
        System.out.println("=== Collections Deep Dive Demo ===\n");

        
        System.out.println("--- ArrayList Operations ---");
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) arrayList.add(i * 10);
        System.out.println("Initial list: " + arrayList);
        arrayList.add(2, 25);          
        System.out.println("After insert at index 2: " + arrayList);
        arrayList.remove(Integer.valueOf(60));  
        System.out.println("After removing 60: " + arrayList);
        Collections.shuffle(arrayList);
        System.out.println("After shuffle: " + arrayList);
        Collections.sort(arrayList);
        System.out.println("After sort: " + arrayList);

        
        System.out.println("\n--- Stack (Deque) ---");
        Deque<String> stack = new ArrayDeque<>();
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        System.out.println("Stack top: " + stack.peek());
        System.out.println("Popped: " + stack.pop());
        System.out.println("Stack now: " + stack);

        
        System.out.println("\n--- Queue (LinkedList) ---");
        Queue<String> queue = new LinkedList<>(Arrays.asList("A", "B", "C", "D"));
        System.out.println("Queue front: " + queue.peek());
        System.out.println("Polled: " + queue.poll());
        queue.offer("E");
        System.out.println("Queue now: " + queue);

        
        System.out.println("\n--- Priority Queue (Min-Heap) ---");
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.addAll(Arrays.asList(30, 10, 50, 20, 40));
        System.out.print("Polling in order: ");
        while (!pq.isEmpty()) System.out.print(pq.poll() + " ");
        System.out.println();

        
        PriorityQueue<Integer> maxPQ = new PriorityQueue<>(Collections.reverseOrder());
        maxPQ.addAll(Arrays.asList(30, 10, 50, 20, 40));
        System.out.print("Max-heap poll: ");
        while (!maxPQ.isEmpty()) System.out.print(maxPQ.poll() + " ");
        System.out.println();

        
        System.out.println("\n--- TreeMap (Sorted) ---");
        TreeMap<String, Integer> scores = new TreeMap<>();
        scores.put("Charlie", 88);
        scores.put("Alice", 95);
        scores.put("Bob", 72);
        scores.put("Dave", 81);
        System.out.println("TreeMap (sorted): " + scores);
        System.out.println("First key: " + scores.firstKey());
        System.out.println("Last key: " + scores.lastKey());
        System.out.println("Head map (before Bob): " + scores.headMap("Bob"));
        System.out.println("Tail map (from Bob): " + scores.tailMap("Bob"));

        
        System.out.println("\n--- LinkedHashMap (Insertion Order) ---");
        Map<String, String> capitals = new LinkedHashMap<>();
        capitals.put("India", "New Delhi");
        capitals.put("USA", "Washington D.C.");
        capitals.put("France", "Paris");
        capitals.put("Japan", "Tokyo");
        capitals.forEach((k, v) -> System.out.println("  " + k + " -> " + v));

        
        System.out.println("\n--- Set Implementations ---");
        Set<String> hashSet = new HashSet<>(Arrays.asList("Banana", "Apple", "Cherry", "Apple", "Date"));
        Set<String> linkedHashSet = new LinkedHashSet<>(Arrays.asList("Banana", "Apple", "Cherry", "Apple", "Date"));
        Set<String> treeSet = new TreeSet<>(hashSet);
        System.out.println("HashSet (unordered): " + hashSet);
        System.out.println("LinkedHashSet (insertion): " + linkedHashSet);
        System.out.println("TreeSet (sorted): " + treeSet);

        
        System.out.println("\n--- Stream Operations on Student Records ---");
        List<Student> students = List.of(
            new Student("Alice", 22, "CS", 3.9),
            new Student("Bob", 21, "Math", 3.2),
            new Student("Carol", 23, "CS", 3.7),
            new Student("Dave", 22, "Physics", 2.8),
            new Student("Eve", 24, "CS", 3.5)
        );

        System.out.println("CS students sorted by GPA:");
        students.stream()
                .filter(s -> s.major().equals("CS"))
                .sorted(Comparator.comparingDouble(Student::gpa).reversed())
                .forEach(s -> System.out.printf("  %-8s GPA: %.1f%n", s.name(), s.gpa()));

        double avgGpa = students.stream()
                .mapToDouble(Student::gpa)
                .average()
                .orElse(0);
        System.out.printf("Overall average GPA: %.2f%n", avgGpa);

        Map<String, List<Student>> byMajor = students.stream()
                .collect(Collectors.groupingBy(Student::major));
        System.out.println("Students grouped by major:");
        byMajor.forEach((major, list) -> {
            String names = list.stream().map(Student::name).collect(Collectors.joining(", "));
            System.out.println("  " + major + ": " + names);
        });

        System.out.println("\n=== Demo Complete ===");
    }
}
