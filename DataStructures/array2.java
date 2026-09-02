public class array2{
    public static void main(String[] args) {

        int [] marks = { 90, 80, 70, 60, 50 };
        int sum = 0;

        for (int mark : marks) {
            sum += mark;
        }

        double average = (double)sum/marks.length;
        System.out.println("sum= "+sum);
        System.out.println("average= "+average);
    }
}