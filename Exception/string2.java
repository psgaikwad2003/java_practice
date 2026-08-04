import java.util.Scanner;
public class string2 {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String input = sc.nextLine();

        String reverse = "";
        for(int i = input.length()-1;i>=0;i--){
            reverse += input.charAt(i);
        }
        System.out.println("Reversed string: " + reverse);

        //palindrome check

        if(input.equals(reverse)){
            System.out.println("The string is a palindrome.");
        }else{
            System.out.println("The string is not a palindrome.");
        }

        //Vowels

        int vowel = 0, consonant = 0, digit = 0, specialChar = 0;
        for(int i = 0; i < input.length(); i++){
            char ch = input.charAt(i);
            if(Character.isLetter(ch)){
                ch = Character.toLowerCase(ch);
                if(ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u'){
                    vowel++;
                }else{
                    consonant++;
                }
            }else if(Character.isDigit(ch)){
                digit++;
            }else{
                specialChar++;
            }

        }
        System.out.println("Vowels: " + vowel);
        System.out.println("Consonants: " + consonant);
        System.out.println("Digits: " + digit);
        System.out.println("Special Characters: " + specialChar);
    }
}
