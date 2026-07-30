public class multi {    
    public static void main(String[] args) {
        
        try{
            String text = null;
            System.out.println(text.length());

            int [] arr ={1,2};
            System.out.println(arr[3]);
        }
        catch(NullPointerException e){
            System.out.println("Null Pointer Exception");
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Array Index Out Of Bounds Exception");
        }
        catch(Exception e){
            System.out.println("General Exception");
        }

    }
    
}
