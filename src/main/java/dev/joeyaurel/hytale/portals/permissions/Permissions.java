package dev.joeyaurel.hytale.portals.permissions;

public class Permissions {
    // Prefix for all permissions
    private static final String PREFIX = "portals.";

    // Admin permission
    public static final String ADMIN = PREFIX + "admin";

    // Specific prefixes
    public static final String NETWORK_PREFIX = PREFIX + "network.";
    public static final String PORTAL_PREFIX = PREFIX + "portal.";

    // Network permissions
    public static final String NETWORK_LIST = NETWORK_PREFIX + "list";
    public static final String NETWORK_CREATE = NETWORK_PREFIX + "create";
    public static final String NETWORK_DELETE = NETWORK_PREFIX + "delete";

    // Portal permissions
    public static final String PORTAL_LIST = PORTAL_PREFIX + "list";
    public static final String PORTAL_CREATE = PORTAL_PREFIX + "create";
    public static final String PORTAL_USE = PORTAL_PREFIX + "use";
    public static final String PORTAL_DELETE = PORTAL_PREFIX + "delete";
}
