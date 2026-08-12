public class immutable {
    public static void main(String[] args) {
     Student s1 = new Student(20,"Yash");
     college c1 = new college("MIT");
     s1.college = c1.collegeName;
     System.out.println(s1.getAge());
     System.out.println(s1.getName());
    
    }
}

final class Student{

    private final int age;
    private final String name;
    private final String college;

    Student(int age,String name){   
        this.age = age;
        this.name = name;

    }

    public int getAge(){
        return this.age;
    }
    public String getName(){
        return this.name;
    }
}
class college{
    String collegeName;
    college(String collegeName){
        this.collegeName=collegeName;
    }
}

