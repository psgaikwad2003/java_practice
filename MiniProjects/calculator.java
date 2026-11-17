import java.util.ArrayDeque;
import java.util.Deque;

public class calculator {

    public static double basicOp(double a, double b, char op) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new ArithmeticException("Division by zero");
                yield a / b;
            }
            case '^' -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }

    private static int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }

    public static double evaluateExpression(String expression) {
        if (expression == null || expression.isBlank()) return 0;
        char[] tokens = expression.toCharArray();
        Deque<Double> values = new ArrayDeque<>();
        Deque<Character> ops = new ArrayDeque<>();

        for (int i = 0; i < tokens.length; i++) {
            if (Character.isWhitespace(tokens[i])) continue;

            if (Character.isDigit(tokens[i]) || tokens[i] == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    sb.append(tokens[i++]);
                }
                i--;
                values.push(Double.parseDouble(sb.toString()));
            } else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                if (!ops.isEmpty()) ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '^') {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(tokens[i])) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(tokens[i]);
            }
        }

        while (!ops.isEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.isEmpty() ? 0 : values.pop();
    }

    private static double applyOp(char op, double b, double a) {
        return basicOp(a, b, op);
    }

    public static void main(String[] args) {
        System.out.println("=== Scientific Calculator & Shunting-Yard Parser ===");

        String[] exprs = {
            "3 + 5 * 2",
            "(3 + 5) * 2",
            "10 + 2 * 6",
            "100 * ( 2 + 12 ) / 14",
            "2 ^ 3 + 4 * 5"
        };

        for (String expr : exprs) {
            System.out.printf("%-26s = %.2f%n", expr, evaluateExpression(expr));
        }
    }
}
