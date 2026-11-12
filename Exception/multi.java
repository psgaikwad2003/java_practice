public class multi {

    public static void executeWithMultiCatch(String str, int[] arr, int index) {
        try {
            int length = str.length();
            int element = arr[index];
            int division = length / element;
            System.out.printf("Processed [Length: %d, Element: %d, Div: %d]%n", length, element, division);
        } catch (NullPointerException | ArrayIndexOutOfBoundsException | ArithmeticException e) {
            System.out.println("[Multi-Catch] Handled expected runtime issue: " + e.getClass().getSimpleName() + " -> " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[General Catch] Handled unexpected error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Modern Multi-Catch Syntax & Exception Hierarchy ===");

        int[] sampleArr = {10, 0, 5};

        System.out.println("Case 1 (Valid execution):");
        executeWithMultiCatch("Hello", sampleArr, 0);

        System.out.println("\nCase 2 (Null Pointer):");
        executeWithMultiCatch(null, sampleArr, 0);

        System.out.println("\nCase 3 (Index Out Of Bounds):");
        executeWithMultiCatch("Test", sampleArr, 5);

        System.out.println("\nCase 4 (Arithmetic Division by Zero):");
        executeWithMultiCatch("World", sampleArr, 1);
    }
}
