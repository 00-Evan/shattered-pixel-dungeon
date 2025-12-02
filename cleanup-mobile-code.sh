#!/bin/bash
# Deep clean mobile-specific code from core module

PROJECT_ROOT="/home/user/shattered-pixel-dungeon"
cd "$PROJECT_ROOT"

echo "========================================"
echo "Mobile Code Deep Clean"
echo "========================================"
echo ""

# Step 1: Create DeviceCompat stub
echo "Step 1: Creating DeviceCompat stub..."
mkdir -p core/src/main/java/com/watabou/utils/

cat > core/src/main/java/com/watabou/utils/DeviceCompat.java << 'EOF'
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
}
EOF

echo "  ✓ Created DeviceCompat stub (PC-only)"

# Step 2: Create list of files to track changes
echo ""
echo "Step 2: Documenting files with mobile code..."
FILES_WITH_MOBILE=$(grep -r "DeviceCompat" core/ --include="*.java" -l | sort -u)
echo "$FILES_WITH_MOBILE" | wc -l | xargs echo "  Found mobile code in files:"
echo "$FILES_WITH_MOBILE" | sed 's/^/    - /'

echo ""
echo "========================================"
echo "Cleanup Complete!"
echo "========================================"
echo ""
echo "Created:"
echo "  - com.watabou.utils.DeviceCompat (stub for PC-only)"
echo ""
echo "Note: Mobile-specific code will now execute desktop paths"
echo "The code will compile, but mobile conditionals are now dead code."
echo ""
echo "Next steps:"
echo "  1. Test build: ./gradlew desktop:debug"
echo "  2. Review the DeviceCompat stub"
echo "  3. Optionally remove dead mobile code paths manually"
echo ""
