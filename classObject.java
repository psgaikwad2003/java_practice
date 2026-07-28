public class classObject {

    int rollno;
    String name;

    void display(){
        System.out.println("Roll No: " + rollno );
        System.out.println("Name: " + name );
    }
    public static void main(String[] args){

        classobject s1 = new classObject();
        s1.rollno = 101;
        s1.name = "John";
        s1.display();

    }
}
