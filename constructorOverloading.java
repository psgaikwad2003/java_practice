class constructorOverloading{

    String name;
    double price;

    constructorOverloading(){
        name = "Unknown";
        price = 0.0;
    }

    constructorOverloading(String name, double price){
        this.name = name;
        this.price = price;
    }

    void display(){
        System.out.println("Name: "+name);
        System.out.println("Price: "+price);
    }

    public static void main(String[] args) {
        
        constructorOverloading book1 = new constructorOverloading();
        constructorOverloading book2 = new constructorOverloading("Java Programming", 29.99);

        book1.display();
        book2.display();
    }
     
}