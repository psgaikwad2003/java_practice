import java.util.ArrayDeque;
import java.util.Deque;

public class ValidParentheses {

    public static boolean isValid(String s) {
        if (s == null || (s.length() & 1) != 0) return false;

        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else {
                if (stack.isEmpty() || stack.pop() != c) return false;
            }
        }
        return stack.isEmpty();
    }

    public static int longestValidParentheses(String s) {
        if (s == null || s.length() < 2) return 0;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1);
        int maxLen = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i);
                } else {
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        return maxLen;
    }

    public static String minRemoveToMakeValid(String s) {
        if (s == null || s.isEmpty()) return s;

        StringBuilder sb = new StringBuilder(s);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == '(') {
                stack.push(i);
            } else if (c == ')') {
                if (!stack.isEmpty()) {
                    stack.pop();
                } else {
                    sb.setCharAt(i, '*');
                }
            }
        }

        while (!stack.isEmpty()) {
            sb.setCharAt(stack.pop(), '*');
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) != '*') {
                result.append(sb.charAt(i));
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String[] testStrings = {"()[]{}", "([)]", "{[]}", "((())"};
        System.out.println("--- Valid Parentheses Validation ---");
        for (String str : testStrings) {
            System.out.println("\"" + str + "\" -> valid? " + isValid(str));
        }

        String[] longestTests = {")()())", "(()", "()(()))))", ""};
        System.out.println("\n--- Longest Valid Parentheses Length ---");
        for (String str : longestTests) {
            System.out.println("\"" + str + "\" -> longest valid: " + longestValidParentheses(str));
        }

        String[] minRemoveTests = {"lee(t(c)o)de)", "a)b(c)d", "))(("};
        System.out.println("\n--- Min Remove To Make Valid ---");
        for (String str : minRemoveTests) {
            System.out.println("\"" + str + "\" -> \"" + minRemoveToMakeValid(str) + "\"");
        }
    }
}
