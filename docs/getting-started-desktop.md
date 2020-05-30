## Quick Setup

If you merely wish to build the game from source, or make small personal changes to the code, then the project can be built with minimal setup:
- Ensure a [JDK (Java Development Kit)](https://www.oracle.com/java/technologies/javase-downloads.html) is installed on your computer.
- Copy the code by pressing the 'clone or download' button and then 'Download ZIP'. Unzip the downloaded zip to any directory on your computer you like.
    - or optionally use version control (see full setup for details).
- Open a command prompt from the project's root folder and type `./gradlew desktop:debug` to run the game in debug mode.
- Type `./gradlew desktop:release` to build a release JAR file, which will be placed in `/desktop/build/libs`.

Any code changes you make will be applied when you newly run or compile the game.

## Full Setup

Performing a full setup is strongly recommended if you wish to make changes to the code beyond very minor personal modifications.

### Prerequisites

To compile Shattered Pixel Dungeon for desktop using this guide you will need:
- A computer which meets the system requirements for [Android Studio](https://developer.android.com/studio#Requirements) or [Intellij](https://www.jetbrains.com/help/idea/installation-guide.html)
- (optional) a GitHub account to fork this repository, if you wish to use version control

### Installing programs

Download and install the latest version of [Android Studio](https://developer.android.com/studio) (or [Intellij](https://www.jetbrains.com/idea/download)). Either software includes all the tools you will need to compile the game.

Android Studio is recommended over Intellij as it is required to compile the [Android version](getting-started-android.md) of the game. If you prefer Intellij and are certain you do not need Android functionality, you may use that instead. This guide will assume Android Studio is used, but the two programs are very similar.

It is optional, but strongly recommended, to use version control to manage your copy of the Shattered Pixel Dungeon codebase. Version control is software which helps you manage changes to code. To use version control you will need to download and install [Git](https://git-scm.com/downloads). You are welcome to use a separate graphical git client or git CLI if you prefer, but this guide will use Android Studio's built-in git tools.

### Setting up your copy of the code

If you are using version control, fork this repository using the 'fork' button at the top-right of this web page, so that you have your own copy of the code on GitHub.

If you do not wish to use version control, press the 'clone or download' button and then 'Download ZIP'. Unzip the downloaded zip to any directory on your computer you like.

### Opening the code in Android Studio

Open Android Studio, you will be greeted with a splash page with a few options.

If you are using version control, you must first tell Android Studio where your installation of Git is located:
- Select 'Configure' then 'Settings'.
- From the settings window, select 'Version Control' then 'Git'.
- Point 'Path to Git executable:' to 'bin/git.exe', which will be located where you installed git.
- Hit the 'test' button to make sure git works, then press 'Okay' to return to the splash page.

After that, you will want to select 'check out project from version control' then 'git'. Log in to GitHub through the button (use username instead of tokens), and select your forked repository from the list of URLs. Import to whatever directory on your computer you like. Accept the default options android studio suggests when opening the project. If you would like more information about working with Git and commiting changes you make back to version control, [consult this guide](https://code.tutsplus.com/tutorials/working-with-git-in-android-studio--cms-30514) (skip to chapter 4).

If you are not using version control, select 'Import project (Gradle, Eclipse ADT, etc.)' and select the folder you unzipped the code into. Accept the default options android studio suggests when opening the project.

### Running the code

Once the code is open, you can run it from Android Studio by specifying the gradle command from the quick guide as a run configuration:
- Select 'Run' on the top toolbar, and then 'Edit Configurations...'.
- Click the + icon to add a new configuration, and select 'gradle'.
- Set 'Gradle project' to 'Shattered-pixel-dungeon', and 'tasks' to 'desktop:debug'
- Name the configuration whatever you like, and select 'Apply' and 'OK' on the bottom right.

That configuration is now saved and can be selected from the configurations dropdown menu. It can be ran using the green arrow icon, and debugged with the green bug icon.

### Generating a distributable JAR file

A JAR (Java ARchive) is a file used to distribute Java applications. Just as with running the code, a run configuration must be set up to create the jar. Follow the above steps, but with 'desktop:release' instead of 'desktop:debug'. Running this configuration will generate a distributable JAR file in the `/desktop/build/libs` folder.

Note that by distributing your modification of Shattered Pixel Dungeon, you are bound by the terms of the GPLv3 license, which requires that you make any modifications you have made open-source. If you followed this guide and are using version control, that is already set up for you as your forked repository is publicly hosted on GitHub. Just make sure to push any changes you make back to that repository.

Note that JAR files will require a [Java runtime enviroment](https://java.com/en/download/win10.jsp) to be installed on a user's computer in order to work. In future this codebase will likely be expanded to allow bundling a java runtime with the application, so that a simple runnable EXE file can be generated.