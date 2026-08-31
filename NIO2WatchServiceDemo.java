import java.nio.file.*;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates NIO.2 WatchService to monitor a directory for changes.
 */
public class NIO2WatchServiceDemo {
    public static void main(String[] args) throws Exception {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get("."); // Watch current directory
        
        path.register(watchService, 
            StandardWatchEventKinds.ENTRY_CREATE, 
            StandardWatchEventKinds.ENTRY_MODIFY, 
            StandardWatchEventKinds.ENTRY_DELETE);
        
        System.out.println("Watching directory for changes for up to 3 seconds...");
        
        // Using poll to check events without blocking indefinitely
        WatchKey key = watchService.poll(3, TimeUnit.SECONDS);
        
        if (key != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println("Event kind: " + event.kind() + 
                                   " - File affected: " + event.context());
            }
            key.reset();
        } else {
            System.out.println("No file changes detected within 3 seconds. Exiting watch service.");
        }
        
        watchService.close();
    }
}
