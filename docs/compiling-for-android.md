(Last Updated October 2026)

Follow the steps in [getting started](./getting-started.md) first if you haven't already.

Note that this guide includes links to Google documentation that uses Android Studio. They should be just as applicable to Intellij with the Android plugin, though the UI may differ in some places.

## Running the code

Firstly, to compile for Android in Intellij you have to download the Android plugin. Go to 'File -> Plugins', select the 'Marketplace' tab and search for 'Android', download and install the Android plugin.

Running the Android build will require either a physical android device or an android emulator. Using a physical device is recommended as the Android Emulator is less convenient to work with and has additional system requirements. Note that when you first build the code Intellij may take some time, as it needs to set up the project and download various android build tools. You may need to sync the project (Build -> Sync project with gradle files) before compiling.

The Google developer website has guides for:
- Setting up a [physical android device](https://developer.android.com/studio/run/device.html).
- Setting up an [emulated android devices](https://developer.android.com/studio/run/emulator).
- Actually [running your code on a device](https://developer.android.com/studio/run#basic-build-run).

If you frequently wish to run your code for debugging purposes, and the changes you make are to platform-independent modules (core and SPD-classes, which is most of the code), you may find that debugging with the [desktop build](compiling-for-desktop.md) is more convenient.

## Generating an installable APK or AAB

APK (Android PacKage) and AAB (Android App Bundle) files are used to distribute Android applications. You can build these from 'Build' ->  'Generate Signed App Bundle or APK'. APK files are best used when you are not trying to upload your app to Google Play, otherwise you must provide Google with an AAB file and they will used that to generate APK files for you.

Note that APK and AAB files must be signed with a signing key. If you are making a small personal modification to Shattered Pixel Dungeon then your signing key is not important, but **if you intend to distribute your modification to other people and want them to be able to receive updates, then your signing key is critical.** Google will also sign your app for you on Google Play, but you can provide them with the same key that you use to sign your own APKs. The Google developer website has [a guide on signing keys](https://developer.android.com/studio/publish/app-signing#sign_release) that covers both personal and Google Play distribution. **I strongly advise that you generate your own signing key, and do not have Google generate one for you.**

Additionally, note that by default Shattered Pixel Dungeon uses R8 on release builds. R8 is a code optimizer which decreases the size of the APK and improves performance, but also makes error reports more difficult to read. You can disable R8 by setting minifyEnabled to false in [android/build.grade](../android/build.gradle). If you wish to keep R8 enabled, you can learn more about it [here](https://developer.android.com/topic/performance/app-optimization/enable-app-optimization).

## Distributing Your App

The Google developer website includes [a guide for ways to distribute your app.](https://developer.android.com/studio/publish)

Note that by distributing your modification of Shattered Pixel Dungeon, you are bound by the terms of the GPLv3 license, which requires that you make any modifications you have made open-source. If you followed this guide and are using version control, that is already set up for you as your forked repository is publicly hosted on GitHub.  Just make sure to push any changes you make back to that repository.

## Recommended Changes

Now that the project is set up, consider looking at the [recommended code changes section](recommended-changes.md).

**If you intend to make your version of the game available on Google Play, there are sections of the recommended changes guide that are VERY IMPORTANT!**