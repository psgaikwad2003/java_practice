public class number {
    public static void main(String[] args) {
         String number = "123a"; 

         try{
            int value = Integer.parseInt(number); 
            System.out.println("Parsed value: " + value);
         }
         catch(NumberFormatException e){
            System.out.println("Error: Invalid number format.");

         }
    }
        
}     
