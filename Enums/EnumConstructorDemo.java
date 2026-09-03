package Enums;

public class EnumConstructorDemo {
    public static void main(String[] args) {
        System.out.println("--- Enum with Constructors Demo ---");
        
        for (Planet planet : Planet.values()) {
            System.out.printf("Planet: %s, Mass: %e, Radius: %e, Surface Gravity: %f%n",
                    planet.name(), planet.mass(), planet.radius(), planet.surfaceGravity());
        }
    }
}

enum Planet {
    MERCURY (3.303e+23, 2.4397e6),
    VENUS   (4.869e+24, 6.0518e6),
    EARTH   (5.976e+24, 6.37814e6),
    MARS    (6.421e+23, 3.3972e6);

    private final double mass;   // in kilograms
    private final double radius; // in meters

    // Constructor is implicitly private
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public double mass() { return mass; }
    public double radius() { return radius; }

    // universal gravitational constant  (m3 kg-1 s-2)
    public static final double G = 6.67300E-11;

    double surfaceGravity() {
        return G * mass / (radius * radius);
    }
}
