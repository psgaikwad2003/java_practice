import java.util.Scanner;
public class electricity {
    public static void main(String[] args) {

        Scanner sc =new Scanner(System.in);

        int units;
        double bill;

        sout("Enter the number of units consumed: ");
        units = sc.nextInt();

        if (units >=0 && units <=100){
            bill = units * 5;
        }else if(units >100 && units <=200){
            bill = 100 * 5 + (units - 100) * 7;
        }else if(units >200 && units <=300){
            bill = 100 * 5 + 100 * 7 + (units - 200) * 10;
        }else{ 
            bill = 100 * 5 + 100 * 7 + 100 * 10 + (units - 300) * 15;
        }
        System.out.println("Total electricity bill: " + bill);
        sc.close();
    }
}
