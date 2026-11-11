class EngineSpecs {
    public String getType() { return "Generic Internal Combustion Engine"; }
    @Override public String toString() { return getType(); }
}

class ElectricMotorSpecs extends EngineSpecs {
    @Override public String getType() { return "Dual Motor Permanent Magnet Synchronous"; }
}

class Vehicle {
    protected String brand;

    public Vehicle(String brand) {
        this.brand = brand;
    }

    public void run() {
        System.out.println(brand + " vehicle is in motion.");
    }

    public EngineSpecs getSpecs() {
        return new EngineSpecs();
    }

    // Static method hiding demo
    public static void category() {
        System.out.println("Category: General Transportation Vehicle");
    }
}

class Car extends Vehicle {
    public Car(String brand) { super(brand); }

    @Override
    public void run() {
        System.out.println(brand + " car cruises smoothly on 4 radial tires.");
    }

    public static void category() {
        System.out.println("Category: Passenger Motor Vehicle");
    }
}

class ElectricCar extends Car {
    public ElectricCar(String brand) { super(brand); }

    @Override
    public void run() {
        System.out.println(brand + " electric car accelerates silently with instant torque.");
    }

    // Covariant return type demonstration
    @Override
    public ElectricMotorSpecs getSpecs() {
        return new ElectricMotorSpecs();
    }
}

public class methodOverriding {
    public static void main(String[] args) {
        System.out.println("=== Dynamic Method Dispatch & Covariant Returns ===");

        Vehicle v1 = new Vehicle("Universal");
        Vehicle v2 = new Car("Honda");
        Vehicle v3 = new ElectricCar("Tesla");

        // Runtime polymorphism (Dynamic Method Dispatch)
        v1.run();
        v2.run();
        v3.run();

        System.out.println("\n=== Covariant Return Types ===");
        System.out.println("v1 specs: " + v1.getSpecs());
        System.out.println("v3 specs: " + v3.getSpecs());

        System.out.println("\n=== Static Method Hiding (Not Polymorphic) ===");
        Vehicle.category();
        Car.category();
    }
}
