import java.util.ArrayList;
import java.util.List;

public class ObserverPatternDemo {
    interface Observer {
        void update(String message);
    }

    static class Subject {
        private List<Observer> observers = new ArrayList<>();
        public void addObserver(Observer observer) { observers.add(observer); }
        public void removeObserver(Observer observer) { observers.remove(observer); }
        public void notifyObservers(String message) {
            for (Observer observer : observers) { observer.update(message); }
        }
    }

    static class User implements Observer {
        private String name;
        public User(String name) { this.name = name; }
        @Override
        public void update(String message) {
            System.out.println(name + " received: " + message);
        }
    }

    public static void main(String[] args) {
        System.out.println("Observer Pattern Implementation");
        Subject newsAgency = new Subject();
        Observer user1 = new User("Alice");
        Observer user2 = new User("Bob");
        
        newsAgency.addObserver(user1);
        newsAgency.addObserver(user2);
        newsAgency.notifyObservers("Breaking News: Java 21 Released!");
        
        newsAgency.removeObserver(user1);
        newsAgency.notifyObservers("More News: Observer Pattern is Cool.");
    }
}
