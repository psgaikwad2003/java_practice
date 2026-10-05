package Enums;

public class EnumSingletonDemo {
    public static void main(String[] args) {
        System.out.println("--- Enum Singleton Pattern Demo ---");

        ConfigurationManager config = ConfigurationManager.INSTANCE;
        config.setProperty("db.url", "jdbc:mysql://localhost:3306/mydb");
        config.setProperty("db.user", "admin");

        System.out.println("DB URL: " + config.getProperty("db.url"));

        ConfigurationManager anotherConfig = ConfigurationManager.INSTANCE;
        System.out.println("DB User from another reference: " + anotherConfig.getProperty("db.user"));
        System.out.println("Are both references the same? " + (config == anotherConfig));
    }
}

enum ConfigurationManager {
    INSTANCE;

    private java.util.Properties properties = new java.util.Properties();

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
