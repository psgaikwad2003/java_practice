# Java Practice & Interview Solutions Repository

A curated collection of Java concepts, data structures, algorithm solutions, design patterns, and technical interview questions.

---

## 🚀 Repository Overview & Structure

### 🧠 Core Java & Object-Oriented Programming (OOP)
- **`Encapsulation.java`**: Custom `BankAccount` encapsulation, input validation, and `InsufficientFundsException` handling.
- **`immutable.java`**: Immutability principles, final fields, defensive copying, and Builder pattern.
- **`enumDemo.java`**: Java Enums with constructors, custom methods, terminal state logic, and `Optional` status code lookups.
- **`CloningDemo.java`**: Deep Copy vs Shallow Copy comparison using Copy Constructors and `Cloneable`.

### 🏗️ Design Patterns
- **`SingletonDemo.java`**: Double-Checked Locking (DCL), Bill Pugh Holder pattern, Enum Singleton, and multi-threaded `CountDownLatch` concurrency tests.
- **`DesignPatternsDemo.java`**: Factory Pattern (Notification channels) and Strategy Pattern (Payment processing).
- **`ProducerConsumerDemo.java`**: Multi-threaded Producer-Consumer pattern using `ArrayBlockingQueue`.

### ⚡ Functional Programming & Asynchronous Execution
- **`StreamsDemo.java`**: Java 8 Streams API (filtering, mapping, department grouping, partitioning by predicate, summary statistics, and string joining).
- **`AsyncDemo.java`**: Non-blocking asynchronous processing pipelines using `CompletableFuture` and custom `ExecutorService` thread pools.

### 📐 Data Structures & Algorithms (DSA)
- **`LRUCacheDemo.java`**: Custom generic `LRUCache<K, V>` using HashMap + Doubly Linked List with $O(1)$ lookup and eviction.
- **`BSTDemo.java`**: Binary Search Tree implementation with node insertion, searching, In-Order, Level-Order (BFS), and DFS traversals.
- **`CustomDataStructuresDemo.java`**: Custom generic `CustomStack<T>` and `CustomQueue<T>` implemented with singly linked nodes.
- **`binarySearch.java`**: Iterative and recursive Binary Search with integer overflow prevention (`mid = low + (high - low) / 2`).
- **`SortingBenchmarkDemo.java`**: Empirical performance comparison comparing BubbleSort, SelectionSort, InsertionSort, and QuickSort execution times.

### 💼 Application Workflows
- **`supermarket.java`**: Supermarket Billing System with OOP `CartItem` encapsulation, 5% GST tax calculation, and tiered discount logic.
- **`librarySystem.java`**: Library Management System featuring stream-based book search by title/author and status toggles.
- **`Exception/CustomExceptionDemo.java`**: Custom domain exception hierarchy (`AppException`, `ResourceNotFoundException`) and try-with-resources.

---

## 🛠️ How to Compile & Run

Compiling any Java file:
```bash
javac <FileName>.java
java <FileName>
```

Example:
```bash
javac StreamsDemo.java
java StreamsDemo
```
