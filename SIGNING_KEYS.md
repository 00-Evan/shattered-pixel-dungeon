# Android Signing Keys Setup for Sofia Station

## Creating Your Release Keystore

To release Sofia Station on Android (Google Play or direct APK distribution), you need your own signing keys.

### Step 1: Generate a Keystore

Open a terminal/command prompt and run:

```bash
keytool -genkey -v -keystore sofia-station-release.keystore -alias sofia_station -keyalg RSA -keysize 2048 -validity 10000
```

You'll be prompted for:
- **Keystore password**: Choose a strong password (SAVE THIS!)
- **Key alias password**: Can be the same as keystore password
- **Your name/organization details**: Fill these out appropriately

### Step 2: Configure Gradle

Create a file named `keystore.properties` in the Android module folder with:

```properties
storeFile=sofia-station-release.keystore
storePassword=YOUR_KEYSTORE_PASSWORD
keyAlias=sofia_station
keyPassword=YOUR_KEY_PASSWORD
```

### Step 3: Update Android Build.gradle

Add to `android/build.gradle` before the `android` block:

```gradle
def keystorePropertiesFile = rootProject.file("keystore.properties")
def keystoreProperties = new Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(new FileInputStream(keystorePropertiesFile))
}
```

And in the `android` block, add:

```gradle
signingConfigs {
    release {
        if (keystorePropertiesFile.exists()) {
            keyAlias keystoreProperties['keyAlias']
            keyPassword keystoreProperties['keyPassword']
            storeFile file(keystoreProperties['storeFile'])
            storePassword keystoreProperties['storePassword']
        }
    }
}

buildTypes {
    release {
        if (keystorePropertiesFile.exists()) {
            signingConfig signingConfigs.release
        }
        // ... existing release config
    }
}
```

## IMPORTANT Security Notes

⚠️ **NEVER commit these files to Git:**
- `sofia-station-release.keystore`
- `keystore.properties`

Add them to `.gitignore`:
```
*.keystore
keystore.properties
```

⚠️ **BACKUP your keystore file!** 
- You cannot update your app on Google Play without the same keystore
- Store backups in multiple secure locations
- Keep your passwords in a password manager

## Building a Release APK

Once configured, build a signed release APK:

```bash
./gradlew android:assembleRelease
```

The signed APK will be in `android/build/outputs/apk/release/`

## For Google Play

Google Play now requires App Bundles instead of APKs:

```bash
./gradlew android:bundleRelease
```

The AAB file will be in `android/build/outputs/bundle/release/`