<div align="center">
  <h1>☕ Java Practice & Interview Solutions</h1>
  <p><i>A comprehensive, curated repository of Java concepts, data structures, algorithms, and design patterns.</i></p>
  
  ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
  ![GitHub repo size](https://img.shields.io/github/repo-size/psgaikwad2003/java_practice?style=for-the-badge)
  ![GitHub commit activity](https://img.shields.io/github/commit-activity/m/psgaikwad2003/java_practice?style=for-the-badge)
  ![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)
</div>

<hr />

## 📖 Table of Contents
- [About The Project](#about-the-project)
- [Architecture & Flow](#architecture--flow)
- [Repository Structure](#repository-structure)
  - [Core Java & OOP](#-core-java--oop)
  - [Design Patterns](#-design-patterns)
  - [Concurrency & Advanced APIs](#-concurrency--advanced-apis)
  - [Data Structures & Algorithms](#-data-structures--algorithms)
  - [Application Workflows](#-application-workflows)
- [Algorithm Complexity Index](#-algorithm-complexity-index)
- [How to Run](#-how-to-run)
- [Recent Updates & Roadmap](#-recent-updates--roadmap)

---

## 🎯 About The Project

This repository serves as a **knowledge base** and a **practice playground** for Java engineering. It encompasses a wide array of implementations ranging from foundational Object-Oriented Programming (OOP) to advanced asynchronous concurrency, architectural design patterns, and high-performance algorithms. Built for continuous daily engineering growth and technical interview excellence.

---

## 🏗 Architecture & Flow

```mermaid
graph TD
    A[Java Practice Repository] --> B(Core OOP)
    A --> C(Design Patterns)
    A --> D(Concurrency & Async)
    A --> E(DSA & Algorithms)
    A --> F(Mini Projects)
    
    B --> B1(Encapsulation & Constructors)
    B --> B2(Inheritance & Dynamic Dispatch)
    B --> B3(Abstraction & Polymorphism)
    
    C --> C1(Creational: Singleton, Factory, Builder)
    C --> C2(Structural: Adapter, Decorator, Facade)
    C --> C3(Behavioral: Observer, Strategy, Chain)
    
    D --> D1(Virtual Threads & CompletableFuture)
    D --> D2(StampedLock & AtomicInteger)
    
    E --> E1(Sorting & Binary Search)
    E --> E2(Dynamic Programming & Graphs)
    E --> E3(Trees, Tries & Heaps)
```

---

## ⚡ Algorithm Complexity Index

| Algorithm | Category | Time Complexity (Best / Avg / Worst) | Space Complexity |
| :--- | :--- | :--- | :--- |
| **QuickSort** (3-Way / Median-of-3) | Sorting | `O(n log n)` / `O(n log n)` / `O(n log n)` | `O(log n)` |
| **Binary Search** | Searching | `O(1)` / `O(log n)` / `O(log n)` | `O(1)` |
| **Boyer-Moore String Search** | String Matching | `O(n/m)` / `O(n)` / `O(nm)` | `O(m + |Σ|)` |
| **Kosaraju SCC** | Graph Theory | `O(V + E)` / `O(V + E)` / `O(V + E)` | `O(V)` |
| **Red-Black BST** | Self-Balancing Tree | `O(log n)` / `O(log n)` / `O(log n)` | `O(n)` |
| **Shunting-Yard Parser** | Parsing | `O(n)` / `O(n)` / `O(n)` | `O(n)` |
| **Segmented Sieve** | Number Theory | `O((R - L + 1) log log R)` | `O(√R)` |

---

## 🚀 How to Run

Compile and execute any source file using modern Java (JDK 21+):

```bash
# Compile
javac Algorithms/AnagramCheck.java

# Execute
java Algorithms.AnagramCheck
```

---

## 📈 Recent Updates & Roadmap

- **Nov 2026**: Systematically refactored core OOP suites, enhanced mathematical algorithms (Stein's Binary GCD, Segmented Sieve, Aliquot Sums), and upgraded mini-projects with production patterns.
