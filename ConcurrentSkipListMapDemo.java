import java.util.concurrent.ConcurrentSkipListMap;


public class ConcurrentSkipListMapDemo {
    public static void main(String[] args) {
        System.out.println("Initializing ConcurrentSkipListMap...");
        ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>();
        
        
        map.put(3, "Three");
        map.put(1, "One");
        map.put(2, "Two");
        map.put(4, "Four");

        System.out.println("First entry: " + map.firstEntry());
        System.out.println("Last entry: " + map.lastEntry());
        
        System.out.println("Iterating through the map:");
        map.forEach((k, v) -> System.out.println("Key: " + k + ", Value: " + v));
    }
}
