public class linearSearch {
    public static void main(String[] args) {
        int [] numbers = { 10, 20, 30, 40, 50 };

        int searchvalue = 20;

        boolean found = false;

        for(int number : numbers){
            if(number == searchvalue){
                found = true;
                break;  
            }
        }

        if(found){
            System.out.println(searchvalue + " is found in the array.");
        } else {
            System.out.println(searchvalue + " is not found in the array.");
        }
    }
}
