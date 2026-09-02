
class Vehicle {
    String brand;
    int speed;

    Vehicle(String brand, int speed) {
        this.brand = brand;
        this.speed = speed;
    }

    void start() {
        System.out.println(brand + " vehicle started.");
    }

    void showSpeed() {
        System.out.println("Speed: " + speed + " km/h");
    }
}

class Car extends Vehicle {
    int doors;

    Car(String brand, int speed, int doors) {
        super(brand, speed);
        this.doors = doors;
    }

    void carInfo() {
        System.out.println("Car Brand: " + brand + " | Doors: " + doors);
    }
}

class ElectricCar extends Car {
    int batteryCapacity;

    ElectricCar(String brand, int speed, int doors, int batteryCapacity) {
        super(brand, speed, doors);
        this.batteryCapacity = batteryCapacity;
    }

    void electricInfo() {
        System.out.println("Battery: " + batteryCapacity + " kWh | Range: ~" + (batteryCapacity * 6) + " km");
    }
}

public class multiLevelInheritance {
    public static void main(String[] args) {

        System.out.println("========== Multi-Level Inheritance ==========");

        ElectricCar tesla = new ElectricCar("Tesla Model 3", 250, 4, 75);

        tesla.start();       
        tesla.showSpeed();   
        tesla.carInfo();     
        tesla.electricInfo(); 

        System.out.println("=============================================");
    }
}
