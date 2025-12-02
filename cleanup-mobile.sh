#!/bin/bash
# cleanup-mobile.sh - Remove mobile platform code for PC-only build
# This script removes Android, iOS, and Web platform modules

set -e  # Exit on error

PROJECT_ROOT="/home/user/shattered-pixel-dungeon"
cd "$PROJECT_ROOT"

echo "========================================="
echo "Mobile Platform Cleanup Script"
echo "========================================="
echo ""

# Step 1: Remove platform directories
echo "Step 1: Removing mobile platform directories..."
if [ -d "android" ]; then
    rm -rf android/
    echo "  ✓ Removed android/"
fi

if [ -d "ios" ]; then
    rm -rf ios/
    echo "  ✓ Removed ios/"
fi

if [ -d "pixel-dungeon-web" ]; then
    rm -rf pixel-dungeon-web/
    echo "  ✓ Removed pixel-dungeon-web/"
fi

# Step 2: Remove build artifacts
echo ""
echo "Step 2: Removing build artifacts..."
if [ -d "SPD-classes" ]; then
    rm -rf SPD-classes/
    echo "  ✓ Removed SPD-classes/"
fi

# Step 3: Update settings.gradle
echo ""
echo "Step 3: Updating settings.gradle..."
cat > settings.gradle << 'EOF'
//core game code modules
include ':core'

//platform modules (PC only)
include ':desktop'

//service modules (may be needed for updates/news)
include ':services'
    //updates
    include ':services:updates:debugUpdates'
    include ':services:updates:githubUpdates'
    //news
    include ':services:news:debugNews'
    include ':services:news:shatteredNews'
EOF
echo "  ✓ Updated settings.gradle (removed android, ios, web, SPD-classes)"

# Step 4: Update build.gradle
echo ""
echo "Step 4: Updating build.gradle..."
cat > build.gradle << 'EOF'
buildscript {
    repositories {
        mavenCentral()
    }
}

allprojects {

    ext {
        appName = 'Shattered Pixel Dungeon'
        appPackageName = 'com.shatteredpixel.shatteredpixeldungeon'

        appVersionCode = 877
        appVersionName = '3.2.5'

        appJavaCompatibility = JavaVersion.VERSION_11

        gdxVersion = '1.13.6-SNAPSHOT'
        gdxControllersVersion = '2.2.4'
    }
    version = appVersionName

    repositories {
        mavenCentral()
        maven { url 'https://central.sonatype.com/repository/maven-snapshots/' }
    }

}
EOF
echo "  ✓ Updated build.gradle (removed Android/iOS dependencies)"

# Step 5: Remove gradle wrapper batch file (Windows-specific, but keep for cross-platform dev)
# Keeping gradlew.bat in case needed for Windows development

# Step 6: Summary
echo ""
echo "========================================="
echo "Cleanup Complete!"
echo "========================================="
echo ""
echo "Removed:"
echo "  - android/ (Android platform)"
echo "  - ios/ (iOS platform)"
echo "  - pixel-dungeon-web/ (Web/TeaVM platform)"
echo "  - SPD-classes/ (build artifacts)"
echo ""
echo "Updated:"
echo "  - settings.gradle (removed mobile module includes)"
echo "  - build.gradle (removed Android build tools & config)"
echo ""
echo "Remaining modules:"
echo "  - core/ (game logic)"
echo "  - desktop/ (PC launcher)"
echo "  - services/ (updates & news - may be useful for PC)"
echo ""
echo "Next steps:"
echo "  1. Review changes: git diff"
echo "  2. Test build: ./gradlew desktop:dist"
echo "  3. Commit changes: git add -A && git commit -m 'Remove mobile platforms for PC-only build'"
echo ""
