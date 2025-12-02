/*
 * Stub for DeviceCompat - PC-only build
 * All mobile platform checks return false
 */
package com.watabou.utils;

public class DeviceCompat {

    // Platform checks - always false for mobile
    public static boolean isAndroid() {
        return false;
    }

    public static boolean isiOS() {
        return false;
    }

    public static boolean isDesktop() {
        return true;
    }

    public static boolean isMobile() {
        return false;
    }

    // Input capabilities - PC has keyboard
    public static boolean hasHardKeyboard() {
        return true;
    }

    // System capabilities
    public static boolean supportsVibration() {
        return false;
    }

    // Logging (PC uses standard output)
    public static void log(String tag, String message) {
        System.out.println("[" + tag + "] " + message);
    }

    // Debug mode check
    public static boolean isDebug() {
        return System.getProperty("Specification-Version", "").contains("INDEV");
    }
}
