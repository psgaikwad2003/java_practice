public class demo {
    public static void main(String[] args) {
       
        student s1 = new student();
        s1.name = "Prajwal";
        s1.age = 23;
        System.out.println(s1);

        student s2 = new student();
        s2.name = "Prajwal";
        s2.age = 23;

        System.out.println(s1.equals(s2));
       // System.out.println(s1 == s2);
    }
}

class student extends Object{

    String name;
    int age;

    @Override
    public String toString() {
        return "student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';

    }
    @Override
    public boolean equals(Object obj){
        student s = (student)obj;
        return this.name == s.name && this.age == s.age;
    }
}