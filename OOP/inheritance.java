import java.util.List;

class Animal {
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println(name + " is eating nourishing food.");
    }

    public void sleep() {
        System.out.println(name + " is sleeping peacefully.");
    }

    public void makeSound() {
        System.out.println(name + " makes a generic animal sound.");
    }
}

class Mammal extends Animal {
    protected boolean warmBlooded = true;

    public Mammal(String name, int age) {
        super(name, age);
    }

    public void nurse() {
        System.out.println(name + " is nursing its offspring.");
    }
}

class Dog extends Mammal {
    private final String breed;

    public Dog(String name, int age, String breed) {
        super(name, age);
        this.breed = breed;
    }

    @Override
    public void makeSound() {
        System.out.println(name + " (" + breed + ") barks: Woof! Woof!");
    }

    public void fetch() {
        System.out.println(name + " is happily fetching the tennis ball.");
    }
}

class Cat extends Mammal {
    private final boolean indoor;

    public Cat(String name, int age, boolean indoor) {
        super(name, age);
        this.indoor = indoor;
    }

    @Override
    public void makeSound() {
        System.out.println(name + " purrs: Meow~ Meow~");
    }

    public void scratchPost() {
        System.out.println(name + " is sharpening its claws on the scratching post.");
    }
}

class Bird extends Animal {
    private final double wingSpanMeters;

    public Bird(String name, int age, double wingSpanMeters) {
        super(name, age);
        this.wingSpanMeters = wingSpanMeters;
    }

    @Override
    public void makeSound() {
        System.out.println(name + " chirps melodiously: Tweet! Chirp!");
    }

    public void fly() {
        System.out.println(name + " soars into the sky with " + wingSpanMeters + "m wingspan.");
    }
}

public class inheritance {
    public static void main(String[] args) {
        System.out.println("=== Animal Kingdom Polymorphic Inheritance ===");

        Dog dog = new Dog("Buddy", 3, "Golden Retriever");
        Cat cat = new Cat("Luna", 2, true);
        Bird bird = new Bird("Sky", 1, 0.45);

        List<Animal> animals = List.of(dog, cat, bird);

        for (Animal a : animals) {
            a.eat();
            a.makeSound();
            a.sleep();
            System.out.println();
        }

        System.out.println("=== Specific Behaviors ===");
        dog.fetch();
        cat.scratchPost();
        bird.fly();
    }
}
