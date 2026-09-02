class Animal{

    void eat(){
        System.out.println("eating...");
    }
}

class Cat extends Animal{

    void meow(){
        System.out.println("meowing...");
    }
}

class Dog extends Animal{

    void bark(){
        System.out.println("barking...");
    }
}

public class inheritance {
    public static void main(String[] args) {
        Animal a = new Animal();
        Cat c = new Cat();
        Dog d = new Dog();

        a.eat();
        c.eat();
        c.meow();
        d.eat();
        d.bark();
    }
}
