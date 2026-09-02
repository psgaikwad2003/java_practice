public class person {
    public static void main(String[] args) {
        
        Employee p1 = new Employee(1);
        Employee p2 = new Employee(1);

        System.out.println(p1.equals(p2));

    }
}

class Employee extends Object{

    int id;

    Employee(int id){
        this.id = id;
    }

    @Override
    public boolean equals(Object obj){
        
        if(obj == null){
            return false;
        }

        if(obj.getClass() != this.getClass()){
            return false;
        }

        Employee e = (Employee)obj;
        return this.id == e.id;
    }
    @Override
    public int hashCode(){
        return id;
    }
}