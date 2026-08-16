import java.util.ArrayList;
import java.util.List;

/**
 * Technical Interview Question: Deep Copy vs Shallow Copy in Java
 * 
 * Demonstrates:
 * 1. Default shallow copy pitfalls with mutable reference fields.
 * 2. Deep copy using copy constructors.
 * 3. Overriding clone() method for defensive copying.
 */
public class CloningDemo {

    static class Address implements Cloneable {
        String city;
        String country;

        public Address(String city, String country) {
            this.city = city;
            this.country = country;
        }

        @Override
        public Address clone() {
            try {
                return (Address) super.clone();
            } catch (CloneNotSupportedException e) {
                return new Address(this.city, this.country);
            }
        }

        @Override
        public String toString() {
            return city + ", " + country;
        }
    }

    static class Person implements Cloneable {
        String name;
        Address address;
        List<String> skills;

        // Constructor
        public Person(String name, Address address, List<String> skills) {
            this.name = name;
            this.address = address;
            this.skills = skills;
        }

        // Copy Constructor for Deep Copy
        public Person(Person other) {
            this.name = other.name;
            this.address = other.address != null ? other.address.clone() : null;
            this.skills = other.skills != null ? new ArrayList<>(other.skills) : new ArrayList<>();
        }

        // Shallow Copy clone
        public Person shallowCopy() {
            return new Person(this.name, this.address, this.skills);
        }

        @Override
        public String toString() {
            return "Person[name=" + name + ", address=" + address + ", skills=" + skills + "]";
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Shallow Copy vs Deep Copy Demonstration");
        System.out.println("==================================================");

        List<String> skills = new ArrayList<>();
        skills.add("Java");
        skills.add("SQL");

        Person original = new Person("John", new Address("New York", "USA"), skills);
        Person shallow = original.shallowCopy();
        Person deep = new Person(original);

        System.out.println("Original before modification : " + original);

        // Mutate address and skills via shallow copy
        shallow.address.city = "San Francisco";
        shallow.skills.add("Docker");

        System.out.println("\n--- After Mutating Shallow Copy ---");
        System.out.println("Original (affected by shallow!): " + original);
        System.out.println("Shallow Copy                   : " + shallow);
        System.out.println("Deep Copy (isolated & safe!)   : " + deep);
    }
}
