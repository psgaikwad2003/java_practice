import java.util.ArrayList;
import java.util.List;

abstract class Shape {
    private final String name;

    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double area();
    public abstract double perimeter();

    public void display() {
        System.out.printf("%-12s | Area: %8.2f | Perimeter: %8.2f%n", name, area(), perimeter());
    }
}

class Circle extends Shape {
    private final double radius;

    public Circle(double radius) {
        super("Circle");
        if (radius < 0) throw new IllegalArgumentException("Radius cannot be negative");
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
}

class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        super("Rectangle");
        if (width < 0 || height < 0) throw new IllegalArgumentException("Dimensions must be positive");
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}

class Triangle extends Shape {
    private final double a;
    private final double b;
    private final double c;

    public Triangle(double a, double b, double c) {
        super("Triangle");
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException("Violates triangle inequality theorem");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double perimeter() {
        return a + b + c;
    }

    @Override
    public double area() {
        double s = perimeter() / 2.0;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
}

public class Abstraction {

    public static double sumTotalArea(List<Shape> shapes) {
        double total = 0;
        for (Shape s : shapes) total += s.area();
        return total;
    }

    public static void main(String[] args) {
        System.out.println("=== Geometric Abstraction & Polymorphism ===");

        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(5.0));
        shapes.add(new Rectangle(4.0, 6.0));
        shapes.add(new Triangle(3.0, 4.0, 5.0));

        for (Shape s : shapes) {
            s.display();
        }

        System.out.println("----------------------------------------------");
        System.out.printf("Combined Surface Area: %.2f%n", sumTotalArea(shapes));
    }
}
