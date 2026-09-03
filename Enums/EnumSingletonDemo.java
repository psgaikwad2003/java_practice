package Enums;

public class EnumSingletonDemo {
    public static void main(String[] args) {
        System.out.println("--- Enum Singleton Pattern Demo ---");
        
        // Accessing the singleton instance
        ConfigurationManager config = ConfigurationManager.INSTANCE;
        config.setProperty("db.url", "jdbc:mysql://localhost:3306/mydb");
        config.setProperty("db.user", "admin");
        
        System.out.println("DB URL: " + config.getProperty("db.url"));
        
        // Verifying it's the same instance
        ConfigurationManager anotherConfig = ConfigurationManager.INSTANCE;
        System.out.println("DB User from another reference: " + anotherConfig.getProperty("db.user"));
        System.out.println("Are both references the same? " + (config == anotherConfig));
    }
}

// Enum is the most robust way to create a Singleton in Java
// It handles serialization and prevents reflection attacks automatically
enum ConfigurationManager {
    INSTANCE; // The single instance

    private java.util.Properties properties = new java.util.Properties();

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
