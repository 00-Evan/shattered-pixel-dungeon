### Prerequisites

To compile Shattered Pixel Dungeon for Android using this guide you will need:
- A computer which meets the [system requirements for Android Studio](https://developer.android.com/studio#Requirements)
- (optional) a GitHub account to fork this repository, if you wish to use version control
- (optional) an android phone to test your build of Shattered Pixel Dungeon

### Installing programs

Download and install the latest version of [Android Studio](https://developer.android.com/studio). This is the development environment which android apps use, it includes all the tools needed to get started with building android apps.

It is optional, but strongly recommended, to use version control to manage your copy of the Shattered Pixel Dungeon codebase. Version control is software which helps you manage changes to code. To use version control you will need to download and install [Git](https://git-scm.com/downloads). You are welcome to use a separate graphical git client or git CLI if you prefer, but this guide will use Android Studio's built-in git tools.

### Setting up your copy of the code

If you are using version control, fork this repository using the 'fork' button at the top-right of this web page, so that you have your own copy of the code on GitHub.

If you do not wish to use version control, press the 'clone or download' button and then 'Download ZIP'. Unzip the downloaded zip to any directory on your computer you like.

### Opening the code in Android Studio

Open Android Studio, you will be greeted with a splash page with a few options.

If you are using version control, you must first tell Android Studio where your installation of Git is located:
- Select 'Configure' then 'Settings'
- From the settings window, select 'Version Control' then 'Git'
- Point 'Path to Git executable:' to 'bin/git.exe', which will be located where you installed git.
- Hit the 'test' button to make sure git works, then press 'Okay' to return to the splash page.

After that, you will want to select 'check out project from version control' then 'git'. Log in to GitHub through the button (use username instead of tokens), and select your forked repository from the list of URLs. Import to whatever directory on your computer you like. Accept the default options android studio suggests when opening the project. If you would like more information about working with Git and committing changes you make back to version control, [consult this guide](https://code.tutsplus.com/tutorials/working-with-git-in-android-studio--cms-30514) (skip to chapter 4).

If you are not using version control, select 'Import project (Gradle, Eclipse ADT, etc.)' and select the folder you unzipped the code into. Accept the default options android studio suggests when opening the project.

### Running the code

Once the code is open in Android Studio, running the Android build will require either a physical android device or an android emulator. Using a physical device is recommended as the Android Emulator is less convenient to work with and has additional system requirements. Note that when you first open and run the code Android Studio may take some time, as it needs to set up the project and download various android build tools.

The Android Studio website has [a guide which covers the specifics of running a project you have already set up.](https://developer.android.com/studio/run)

This guide includes a [section on physical android devices...](https://developer.android.com/studio/run/device.html)

... and [a section on emulated android devices.](https://developer.android.com/studio/run/emulator)

If you frequently wish to run your code for debugging purposes, and the changes you make are to platform-independent modules (such as core and SPD-classes), you may find that running the [desktop build](getting-started-desktop.md) is more convenient.

### Generating an installable APK

An APK (Android PacKage) is a file used to distribute Android applications. The Android studio website has [a guide which covers building your app into an APK.](https://developer.android.com/studio/run#reference) Note that the option you will likely want to use is 'Generate Signed Bundle / APK'.

Note that APKs must be signed with a signing key. If you are making a small personal modification to Shattered Pixel Dungeon then your signing key is not important, but **if you intend to distribute your modification to other people and want them to be able to receive updates, then your signing key is critical.** The Android studio website has [a guide on signing keys.](https://developer.android.com/studio/publish/app-signing.html#opt-out)

Additionally, note that by default Shattered Pixel Dungeon uses R8 on release builds. R8 is a code optimizer which decreases the size of the APK and improves performance, but also makes error reports more difficult to read. You can disable R8 by setting minifyEnabled to false in [android/build.grade](./android/build.gradle). If you wish to keep R8 enabled, you can learn more about it [here.](https://developer.android.com/studio/build/shrink-code)

### Distributing Your APK

The Android Studio website includes [a guide for ways to distribute your app.](https://developer.android.com/studio/publish)

Note that by distributing your modification of Shattered Pixel Dungeon, you are bound by the terms of the GPLv3 license, which requires that you make any modifications you have made open-source. If you followed this guide and are using version control, that is already set up for you as your forked repository is publicly hosted on GitHub.  Just make sure to push any changes you make back to that repository.

**If you intend to make your version of the game available on Google Play PLEASE CONTACT ME AT THE FOLLOWING EMAIL ADDRESS:** Evan@ShatteredPixel.com . There are various aspects of Google's Developer Policies that go beyond the scope of a simple 'how to compile' guide. If you do not take necessary precautions before attempting to publish on Google Play, your version of the game will almost certainly be taken down for impersonating Shattered Pixel Dungeon or Pixel Dungeon.