(Last Updated December 2022)

## Prerequisites

To compile Shattered Pixel Dungeon for iOS using this guide you will need:
- (required) An Apple computer which meets the system requirements for [Android Studio](https://developer.android.com/studio#get-android-studio) and [Xcode](https://developer.apple.com/xcode/)
- (recommended) A GitHub account to fork this repository, if you wish to use version control

## Installing initial programs

Download and install the latest version of [Android Studio](https://developer.android.com/studio). This is the development environment which android apps use, it includes all the tools needed to get started with building android apps.

It is optional, but strongly recommended, to use version control to manage your copy of the Shattered Pixel Dungeon codebase. Version control is software which helps you manage changes to code. To use version control you will need to download and install [Git](https://git-scm.com/downloads). You are welcome to use a separate graphical git client or git CLI if you prefer, but this guide will use Android Studio's built-in git tools.

## Setting up your copy of the code

If you are using version control, fork this repository using the 'fork' button at the top-right of this web page, so that you have your own copy of the code on GitHub.

If you do not wish to use version control, press the green 'code' button on [this repository's main page](https://github.com/00-Evan/shattered-pixel-dungeon), and then 'Download ZIP'. Unzip the downloaded zip to any directory on your computer you like.

## Opening the code in Android Studio

Open Android Studio, you will be greeted with a splash page with a few options.

If you are using version control, you must first tell Android Studio where your installation of Git is located:
- Select 'Configure' then 'Settings'
- From the settings window, select 'Version Control' then 'Git'
- If it wasn't auto-detected, Point 'Path to Git executable:' to 'bin/git.exe', which will be located where you installed git.
- Hit the 'test' button to make sure git works, then press 'Okay' to return to the splash page.

After that, you will want to select 'check out project from version control' then 'git'. Log in to GitHub through the button (use username instead of tokens), and select your forked repository from the list of URLs. Import to whatever directory on your computer you like. Accept the default options android studio suggests when opening the project. If you would like more information about working with Git and committing changes you make back to version control, [consult this guide](https://code.tutsplus.com/tutorials/working-with-git-in-android-studio--cms-30514) (skip to chapter 4).

If you are not using version control, select 'Import project (Gradle, Eclipse ADT, etc.)' and select the folder you unzipped the code into. Accept the default options android studio suggests when opening the project.

# ...Good Luck!

You have now set up the initial project on your computer. From this point compiling the game for Android or Desktop is simple, but iOS is significantly more complicated. This is due to a mixture of Apple's development process and the RoboVM tool that Shattered uses to cross-compile for iOS.

I'll be frank, I do not have a set-by-step process down for how to get this working from scratch, good luck! A great starting point is: [Deploying your libGDX game to iOS in 2020](https://medium.com/@bschulte19e/deploying-your-libgdx-game-to-ios-in-2020-4ddce8fff26c). Some steps can be skipped as the actual application code is all done for you. Provisioning profiles and Xcode setup are the largest hurdles.

Please contact me if you have any specific questions and I may be able to help, or if you wish to make a version based on Shattered available on the App Store.