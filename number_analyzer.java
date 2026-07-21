import java.util.Scanner;

public class number_analyzer {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of elements: ");
        int n= sc .nextInt();

        int original =num;
        int temp=Math.abs(num);

        //Positive/Negative/Zero Check

        if(num>0){
            System.out.println(num+" is a positive number.");
        }
        else if(num<0){
            System.out.println(num+" is a negative number.");
        }
        else{
            System.out.println(num+" is zero.");
        }

        //Even/Odd Check

        if(num%==0){
            System.out.println(num+" is an even number.");
        }
        else{
            System.out.println(num+" is an odd number.");
        }

        //Reverse,Digit Count,Sum

        int reverse=0;
        int digitcount=0;
        int digitsum=0;

        while(temp>0){
            int digit=temp%10;
            reverse=reverse*10+digit;
            digitsum+=digit;
            digitcount++;
            temp/=10;
        }
        System.out.println("Reverse of "+original+" is: "+reverse);
        System.out.println("Number of digits in "+original+" is: "+digitcount);
        System.out.println("Sum of digits in "+original+" is: "+digitsum);

    }
    //Palindrome Check
    if(original==reverse){
        isPrime=False;
    }else{
        for(int i=2;i<=Math.sqrt(original);i++){
            if(original%i==0){
                isPrime=False;
                break;
            }
        }
    }
    if(isPrime){
        System.out.println(original+" is a prime number.");
    }else{
        System.out.println(original+" is not a prime number.");
    
}
