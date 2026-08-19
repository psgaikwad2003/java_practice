import java.util.*;

/**
 * Enum Real-World Usage — Role-Based Access Control (RBAC)
 * 
 * This demonstrates how enums are used in REAL production applications:
 * - Role hierarchy with permission inheritance
 * - Type-safe configuration using enums
 * - Enum as a singleton service registry
 * - Building a mini authorization framework
 */
public class EnumRealWorldDemo {

    // ========== Permission Enum ==========
    enum Permission {
        VIEW_DASHBOARD, VIEW_REPORTS,
        CREATE_USER, EDIT_USER, DELETE_USER,
        CREATE_POST, EDIT_POST, DELETE_POST,
        MANAGE_SETTINGS, MANAGE_BILLING, FULL_ACCESS
    }

    // ========== Role Enum with Permission Inheritance ==========
    enum Role {
        VIEWER(EnumSet.of(Permission.VIEW_DASHBOARD)),

        EDITOR(EnumSet.of(
            Permission.VIEW_DASHBOARD, Permission.VIEW_REPORTS,
            Permission.CREATE_POST, Permission.EDIT_POST
        )),

        MODERATOR(EnumSet.of(
            Permission.VIEW_DASHBOARD, Permission.VIEW_REPORTS,
            Permission.CREATE_POST, Permission.EDIT_POST, Permission.DELETE_POST,
            Permission.EDIT_USER
        )),

        ADMIN(EnumSet.of(
            Permission.VIEW_DASHBOARD, Permission.VIEW_REPORTS,
            Permission.CREATE_USER, Permission.EDIT_USER, Permission.DELETE_USER,
            Permission.CREATE_POST, Permission.EDIT_POST, Permission.DELETE_POST,
            Permission.MANAGE_SETTINGS, Permission.MANAGE_BILLING
        )),

        SUPER_ADMIN(EnumSet.allOf(Permission.class));

        private final EnumSet<Permission> permissions;

        Role(EnumSet<Permission> permissions) {
            this.permissions = permissions;
        }

        public boolean hasPermission(Permission p) {
            return permissions.contains(p);
        }

        public EnumSet<Permission> getPermissions() {
            return EnumSet.copyOf(permissions);
        }

        public boolean isHigherThan(Role other) {
            return this.ordinal() > other.ordinal();
        }
    }

    // ========== Environment Config using Enum ==========
    enum Environment {
        DEV("http://localhost:8080", "dev_db", true),
        STAGING("https://staging.app.com", "staging_db", true),
        PRODUCTION("https://app.com", "prod_db", false);

        private final String baseUrl;
        private final String dbName;
        private final boolean debugMode;

        Environment(String baseUrl, String dbName, boolean debugMode) {
            this.baseUrl = baseUrl;
            this.dbName = dbName;
            this.debugMode = debugMode;
        }

        public String getBaseUrl() { return baseUrl; }
        public String getDbName() { return dbName; }
        public boolean isDebugMode() { return debugMode; }
    }

    // ========== Simple User Class ==========
    static class User {
        private final String name;
        private final Role role;

        User(String name, Role role) {
            this.name = name;
            this.role = role;
        }

        public String getName() { return name; }
        public Role getRole() { return role; }

        public boolean canPerform(Permission action) {
            return role.hasPermission(action);
        }
    }

    // ========== Authorization Check ==========
    static void performAction(User user, Permission action, String desc) {
        if (user.canPerform(action)) {
            System.out.printf("  ✓ %s (%s) -> %s: ALLOWED%n",
                user.getName(), user.getRole(), desc);
        } else {
            System.out.printf("  ✗ %s (%s) -> %s: DENIED%n",
                user.getName(), user.getRole(), desc);
        }
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" 1. Role-Permission Matrix");
        System.out.println("==================================================");
        for (Role role : Role.values()) {
            System.out.printf("  %-12s : %d permissions -> %s%n",
                role, role.getPermissions().size(), role.getPermissions());
        }

        System.out.println("\n==================================================");
        System.out.println(" 2. Authorization in Action");
        System.out.println("==================================================");
        User alice = new User("Alice", Role.VIEWER);
        User bob = new User("Bob", Role.EDITOR);
        User carol = new User("Carol", Role.ADMIN);
        User dave = new User("Dave", Role.SUPER_ADMIN);

        performAction(alice, Permission.VIEW_DASHBOARD, "View Dashboard");
        performAction(alice, Permission.DELETE_USER, "Delete User");
        performAction(bob, Permission.CREATE_POST, "Create Post");
        performAction(bob, Permission.MANAGE_SETTINGS, "Manage Settings");
        performAction(carol, Permission.DELETE_USER, "Delete User");
        performAction(carol, Permission.FULL_ACCESS, "Full Access");
        performAction(dave, Permission.FULL_ACCESS, "Full Access");

        System.out.println("\n==================================================");
        System.out.println(" 3. Role Hierarchy Comparison");
        System.out.println("==================================================");
        System.out.println("  ADMIN > EDITOR? " + Role.ADMIN.isHigherThan(Role.EDITOR));
        System.out.println("  VIEWER > ADMIN? " + Role.VIEWER.isHigherThan(Role.ADMIN));

        System.out.println("\n==================================================");
        System.out.println(" 4. Environment Configuration (Enum as Config)");
        System.out.println("==================================================");
        for (Environment env : Environment.values()) {
            System.out.printf("  %-12s | URL: %-30s | DB: %-12s | Debug: %b%n",
                env, env.getBaseUrl(), env.getDbName(), env.isDebugMode());
        }

        // Simulate selecting environment
        Environment current = Environment.STAGING;
        System.out.println("\n  Active Environment: " + current);
        System.out.println("  API Base URL: " + current.getBaseUrl());
        System.out.println("  Debug Mode: " + current.isDebugMode());

        System.out.println("\n==================================================");
        System.out.println(" 5. Permission Diff Between Roles");
        System.out.println("==================================================");
        EnumSet<Permission> adminPerms = Role.ADMIN.getPermissions();
        EnumSet<Permission> editorPerms = Role.EDITOR.getPermissions();

        EnumSet<Permission> adminOnly = EnumSet.copyOf(adminPerms);
        adminOnly.removeAll(editorPerms);
        System.out.println("  ADMIN has but EDITOR doesn't: " + adminOnly);

        System.out.println("\n==================================================");
        System.out.println(" REAL-WORLD USAGE SUMMARY:");
        System.out.println("==================================================");
        System.out.println("1. RBAC: Roles as enums with permission sets.");
        System.out.println("2. Config: Environment configs as enum constants.");
        System.out.println("3. Type Safety: No invalid roles/permissions possible.");
        System.out.println("4. EnumSet gives O(1) permission checks via bit ops.");
        System.out.println("5. Used in Spring Security, Jakarta EE, Android, etc.");
    }
}
