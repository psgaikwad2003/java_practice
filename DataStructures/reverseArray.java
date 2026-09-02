public class reverseArray {
    public static void main(String[]arg){

        int [] numbers= {10,20,30,40,50};

        System.out.println("The numbers in reverse order are: ");

        for(int i=numbers.length-1;i>=0;i--){
            System.out.println(numbers[i]);
        }
    }
}
