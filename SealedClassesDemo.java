public class SealedClassesDemo {
    public sealed class Shape permits Circle, Square {}
    
    public final class Circle extends Shape {
        public double radius = 5.0;
    }
    
    public final class Square extends Shape {
        public double side = 4.0;
    }

    public static void main(String[] args) {
        SealedClassesDemo demo = new SealedClassesDemo();
        Shape shape = demo.new Circle();
        System.out.println("Created a shape instance: " + shape.getClass().getSimpleName());
    }
}
