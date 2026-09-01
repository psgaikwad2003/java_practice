public class demo {
    public static void main(String[] args) {
       
        student s1 = new student();
        s1.name = "Prajwal";
        s1.age = 23;
        System.out.println(s1);

        student s2 = new student();
        s2.name = "Prajwal";
        s2.age = 23;

        
       

        Integer i1 = 100;
        

        System.out.println(s1.hashCode() == s2.hashCode());
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

        if(obj == null){
            return false;
        }

        if(obj.getClass() != this.getClass()){
            return false;
        }

        student s = (student)obj;
        return this.name == s.name && this.age == s.age;
    }
    @Override
    public int hashCode() {
        int result = 17;
        result = result * 19 + age;
        result = result * 19 + name.hashCode();
        return result;
    }
}