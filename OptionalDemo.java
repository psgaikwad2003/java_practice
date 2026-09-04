import java.util.Optional;

public class OptionalDemo {
    public static void main(String[] args) {
        String value = "Hello, Optional!";
        Optional<String> optionalValue = Optional.ofNullable(value);
        
        optionalValue.ifPresent(v -> System.out.println("Value is present: " + v));
        
        String nullValue = null;
        Optional<String> emptyOptional = Optional.ofNullable(nullValue);
        System.out.println("Empty optional fallback: " + emptyOptional.orElse("Default Value"));
    }
}
