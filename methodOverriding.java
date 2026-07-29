class vehicle{
    void run(){
        System.out.println("Vehicle is running");
    }
}

class car extends vehicle{
    
    @Override
    void run(){
        System.out.println("Car is running");
    }
}

class bike extends vehicle{
    
    @Override
    void run(){
        System.out.println("Bike is running");
    }
}
public class methodOverriding {
    public static void main(String[] args) {
        
        vehicle v = new vehicle();
        car c = new car();
        bike b = new bike();

        v.run();
        c.run();
        b.run();
    }
}
