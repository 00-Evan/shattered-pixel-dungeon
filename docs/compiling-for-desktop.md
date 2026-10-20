(Last Updated October 2026)

Follow the steps in [getting started](./getting-started.md) first if you haven't already.

## Running the code

Once the code is open in Intellij, you can run it by specifying the gradle commands from the quick guide as a [run configuration](https://www.jetbrains.com/help/idea/run-debug-configuration.html#launch-run-configuration):
- Select the run configurations dropdown menu on the top right toolbar, and then 'Edit Configurations...'.
- Click the + icon to add a new configuration, and select 'gradle'.
- Set 'Gradle project' to 'shattered-pixel-dungeon:desktop', and 'Tasks and arguments' to 'debug'
- Name the configuration whatever you like (e.g. 'desktop:debug'), and select 'Apply' and 'OK' on the bottom right.

That configuration is now saved and can be selected from the run configurations dropdown menu. It can be ran using the green arrow icon, and debugged with the green bug icon.

## Generating a distributable JAR file

A JAR (Java ARchive) is a file used to distribute Java applications. Just as with running the code, a run configuration must be set up to create the jar. Follow the above steps, but with 'release' instead of 'debug'. Running this configuration will generate a distributable JAR file in the `/desktop/build/libs` folder.

Note that by distributing your modification of Shattered Pixel Dungeon, you are bound by the terms of the GPLv3 license, which requires that you make any modifications you have made open-source. If you followed this guide and are using version control, that is already set up for you as your forked repository is publicly hosted on GitHub. Just make sure to push any changes you make back to that repository.

Note that JAR files will require a [Java 11+ runtime environment](https://adoptium.net/temurin/releases) (a 'JRE', not a 'JDK') to be installed on a user's computer in order to work. This is unfortunately a pretty big technical hurdle for most users, so JAR builds aren't suitable for wider distribution.

## Generating OS-specific executables

Shattered Pixel Dungeon uses [Jpackage](https://docs.oracle.com/en/java/javase/25/jpackage/packaging-overview.html) and [Badass Runtime](https://badass-runtime-plugin.beryx.org/releases/latest/) to generate platform-specific executables that don't require users to install Java to run the game. This process is unfortunately more awkward for you as a developer, and locks the game to a specific operating system. Because of this JARs are recommended as a simpler starting point, but executable are better for wider distribution.

Just like with a JAR, generating a platform-specific executable requires a new run configuration. Use 'jpackageImage' instead of 'release'. This may take a bit of time, and will generate an executable file in the `/desktop/build/jpackage` folder. All the specific configurations for badass runtime and jpackage are already set up, but you can view them in the 'runtime' block in [desktop/build.gradle](../desktop/build.gradle).

You will only be able to create an executable that matches the operation system you are currently using:
- If you have a Windows computer, you can generate a folder with a Windows .exe file inside (zip before distributing)
- If you have a MacOS computer, you can generate a .app file which Mac users can run or place in their applications folder
  - **Note that your .app will probably need to be [notarized](https://developer.apple.com/documentation/security/notarizing_macos_software_before_distribution) by Apple for users to be able to use it.** The [desktop/notarize.sh](../desktop/notarize.sh) file helps automate this process if you already have notarization set up.
- If you have a Linux computer, you can generate a folder with a Linux executable inside (zip before distributing)

Note that these tools can also be used to generate platform-specific installers as well, but this functionality is unused in Shattered Pixel Dungeon.

## Recommended Changes

Now that the project is set up, consider looking at the [recommended code changes section](recommended-changes.md).