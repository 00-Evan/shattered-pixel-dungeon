(Last Updated October 2026)

## Prerequisites

To compile Shattered Pixel Dungeon using this guide you will need:

**All Platforms:**
- (required) A computer which meets the [system requirements for Intellij](https://www.jetbrains.com/help/idea/installation-guide.html#requirements).
- (recommended) A GitHub account to fork this repository, if you wish to use version control.

**Android:**
- (recommended) An Android device to test your build of Shattered Pixel Dungeon. Using an emulator is also possible but they are much less convenient.

**Desktop:**
- (recommended) When building OS-specific executables using jpackage (which don't require the user to install Java), your computer must match the OS that you want to build for.

**iOS:**
- (required) An Apple computer must be used to compile the game for iOS.
- (recommended) A physical iOS device to test your build of Shattered Pixel Dungeon. Using a simulator is also possible but they are much less convenient.

## Installing Initial Programs

Download and install the latest version of [Intellij](https://www.jetbrains.com/idea/download/). Note that the free trial is for 'Ultimate' features that aren't really useful to us, not the whole program. Android Studio (which is based on Intellij) is another option, but it is not the IDE this guide is written for.

After installing and first launching Intellij, There are a number of configuration changes that you should make:
- (required) when first launching Intellij, agree to the EULA and either accept or reject anonymous statistics collection
- (required) Several plugins should be disabled upon first launch, as they mess with the interface and require paid subscriptions to use. Go to File -> Plugins to access them (you can search for plugins by name if needed):
  - Disable the 'Air' plugin.
  - Disable the 'MCP server' plugin (and associated MCP toolset plugins).
  - Disable the 'Full Line Code Completion' plugin.
- (recommended) While you are in the plugins screen, you may also optionally want to:
  - Download the 'hide trial' plugin, to hide the annoying free trial button in the interface.
  - Download and install the 'Android' plugin, if you wish to build for Android.
  - Download and install the 'MobiVM' plugin, if you wish to build for iOS.
- (recommended) To disable annoying LLM-based grammar checking (basic spellcheck is unaffected): go to File -> Settings, search for "natural languages", go to 'grammar and style', and disable all checkboxes under scope -> 'Check in:' and all checkboxes under Rules in the 'Other' domain.

It is strongly recommended to use version control to manage your copy of the Shattered Pixel Dungeon codebase. Version control is software which helps you manage changes to code. To use version control you will need to download and install [Git](https://git-scm.com/install/). You are welcome to use a separate graphical git client or git CLI if you prefer, but this guide will use Intellij's built-in git tools.

## Setting Up Your Copy of the Code

If you are using version control, fork this repository using the 'fork' button at the top-right of this web page, so that you have your own copy of the code on GitHub. This requires a github account.

If you do not wish to use version control, press the green 'code' button on [this repository's main page](https://github.com/00-Evan/shattered-pixel-dungeon), and then 'Download ZIP'. Unzip the downloaded zip to any directory on your computer you like.

## Opening the Code in Intellij

If you are using version control, see [here for instructions on setting up a Git repository in Intellij](https://www.jetbrains.com/help/idea/set-up-a-git-repository.html).

If you would like more information about working with Git and committing changes you make back to version control, [Intellij has a bunch of documentation on its GIT integration](https://www.jetbrains.com/help/idea/using-git-integration.html).

If you are not using version control, see [here for instructions on importing a project from the existing sources you downloaded.](https://www.jetbrains.com/help/idea/import-project-or-module-wizard.html).

While examining the code, you should be prompted to download 'JBR', this stands for 'JetBrains (java) Runtime', and you should download/install it.

## Next Steps

Now that you've got the code imported, it's time to compile it! The process is a bit different for each of Shattered's main platform targets:

- [Compiling for Desktop](./compiling-for-desktop.md)
- [Compiling for Android](./compiling-for-android.md)
- [Compiling for iOS](./compiling-for-ios.md)