public class student_grade_analayzer {
    public static void main(String[] args) {
        int []marks={78,80,92,60,35};

        int toal=0;
        int highest=marks[0];
        int lowest=marks[0];

        int pass =0;
        int fail =0;

        System.out.println("student marks");

        for(int i=0;i<marks.length;i++){
            System.out.println(marks[i]);
            toal+=marks[i];

            if(marks[i]>highest){
                highest=marks[i];
            }
            if(marks[i]<lowest){
                lowest=marks[i];
            }
            if(marks[i]>=40){
                pass++;
            }else{
                fail++;
            }
        }

        //calculating average

        double average=(double )total/marks.length;

        //result display

        System.out.println("Total marks: "+total);
        System.out.println("Average marks: "+average);
        System.out.println("Highest marks: "+highest   );
        System.out.println("Lowest marks: "+lowest);
        System.out.println("Number of students passed: "+pass);
        System.out.println("Number of students failed: "+fail);

        //grade calculation

        if (average > =90){
            System.out.println("Grade: A");

        }else if(average >=80){
            System.out.println("Grade: B");
        }else if(average >=70){
            System.out.println("Grade: C");
        }else if(average >=60){
            System.out.println("Grade: D");
        }else{
            System.out.println("Grade: F");
    }
    }
    
}
