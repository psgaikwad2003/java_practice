public class constructor {

    int id;
    String name;

    constructor(int id,String name){
        this.id=id;
        this.name=name;
    }
    void display(){
        System.out.println("Id: "+id);
        System.out.println("Name: "+name);
    }
    public static void main(String[] args) {
       
        constructor s1=new constructor(101,"John");
        constructor s2=new constructor(102,"David");
        s1.display();
        s2.display();
    }
}
