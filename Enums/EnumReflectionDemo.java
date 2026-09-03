package Enums;

import java.lang.reflect.Constructor;

public class EnumReflectionDemo {
    public static void main(String[] args) {
        System.out.println("--- Enum Reflection Safety Demo ---");
        
        System.out.println("Enums are protected against instantiation via Reflection.");
        
        try {
            // Attempting to instantiate an enum via reflection
            Constructor<SecureSingleton> constructor = SecureSingleton.class.getDeclaredConstructor(String.class, int.class);
            constructor.setAccessible(true);
            
            System.out.println("Trying to invoke constructor...");
            SecureSingleton instance = constructor.newInstance("HACKED_INSTANCE", 1);
            
        } catch (Exception e) {
            System.out.println("Caught Exception: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            System.out.println("Explanation: Java's Constructor.newInstance() explicitly checks if the class is an enum and throws IllegalArgumentException if it is.");
        }
    }
}

enum SecureSingleton {
    INSTANCE
}
