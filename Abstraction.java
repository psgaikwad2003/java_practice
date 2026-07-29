abstract class Shape{
    abstract void area();
}

class Circle extends Shape{

    double radius;

    @Override
    void area(){
        double area = 3.14 * radius * radius;
        System.out.println("Area of Circle: "+area);
    }
}
public class Abstraction {
    public static void main(String[] args) {
        Circle c = new Circle();
        c.radius = 5;
        c.area();
    }
}
