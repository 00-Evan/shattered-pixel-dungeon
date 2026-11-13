(Last Updated October 2026)

# Quick Setup (Desktop Only!)

If you merely wish to build the game from source, or make small personal changes to the code, then the project can be built with minimal setup:
- Ensure a [JDK (Java Development Kit)](https://adoptium.net/) is installed on your computer. (Java 21 is currently recommended)
- Copy the code by pressing the green 'code' button on [this repository's main page](https://github.com/00-Evan/shattered-pixel-dungeon), and then 'Download ZIP'. Unzip the downloaded zip to any directory on your computer you like.
    - or optionally use version control (see [full setup](./getting-started.md) for details).
- Open a command prompt/terminal window from the project's root folder and type `./gradlew desktop:debug` to run the game in debug mode.
- Type `./gradlew desktop:release` to build a release JAR file, which will be placed in `/desktop/build/libs`. JARs are OS-independent but require an installation of Java.
- Type `./gradlew desktop:jpackageImage` to build a native executable bundle, which will be placed in `/desktop/build/jpackage`. This executable will be built for whatever OS your computer is running.

Any code changes you make will be applied when you newly run or compile the game.

If you want to make more substantial changes to Shattered, it's strongly recommended that you go through the [full setup](./getting-started.md).