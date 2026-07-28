public class bubbleSort {
    public static void main(String[]args){

        int [] numbers = {50,40,30,20,10};  

        for(int i=0;i<numbers.length;i++){
            for(int j=0;j<numbers.length-1;j++){
                if(numbers[j]>numbers[j+1]){
                    int temp = numbers[j];
                    numbers[j] = numbers[j+1];
                    numbers[j+1] = temp;
                }
            }
        }
        System.out.println("The numbers in ascending order are: ");
        for(int i=0;i<numbers.length;i++){
            System.out.println(numbers[i]);
        }
    }
}
