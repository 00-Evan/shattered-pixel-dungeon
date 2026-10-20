(Last Updated October 2026)

This guide covers a few technical changes developers will likely want to make when creating their own Pixel Dungeon version based on Shattered's source code.

## Application name, version name, and package name

There are a number of variables defined in the root [build.gradle](../build.gradle) file that you may want to change:
- `appName` defines the user-visible name of your app. You must change this to whatever you wish to call your game.
- `appPackageName` defines the internal name of your app. Android and iOS use this name to distinguish your app from others and Desktop uses it and appName to determine the game's save directory. You must change this from its initial value. You should use the format `com.<dev name>.<game name>`
- `appVersionCode` defines the internal version number of your app. You want to increment this whenever releasing a new update. Read the next section for more details on this one.
- `appVersionName` defines the user-visible version name of your app. Change this to whatever you like, and increment it whenever you release a new update. The industry standard format is `<major update>.<minor update>.<patch>`

The other variables do not need to be changed when setting up your own version of Shattered, they are mainly technical configurations that do not need to be adjusted.

Note that some older guides may recommend that you change the package structure (i.e. the folder names) of the application. This used to be required, but is now optional as the `appPackageName` variable can be used instead.

## Differentiating your game from Shattered

You will likely want to change core application details to distinguish your version from Shattered Pixel Dungeon, in addition to whatever changes you plan to make to the game itself. This is as simple as modifying text and image assets, but there are a few of them.

For the name, modify `appName` (as mentioned above) to whatever you like. "X Pixel Dungeon" is a common naming convention, but you may want to choose something further separate from Shattered Pixel Dungeon.

For the title screen, you can find the game's title graphic and separate glow layer [Here](../core/src/main/assets/interfaces/banners.png). Consider modifying this to match your changed name.

For icons, you can find the icons for each platform here: [Android(debug)](../android/src/debug/res), [Android(release)](../android/src/main/res), [Desktop](../desktop/src/main/assets/icons), [iOS](../ios/assets/Assets.xcassets). These should also be modified, ideally more than a recolor.

If you just want to make a version to share on PD communities then changes here can be minimal, but **if you plan to release your version on storefronts such as Google Play, then larger changes here are NECESSARY!** Google Play in particular has a history of taking down PD versions for 'impersonating' Shattered, and so making changes to the name and core visuals to sufficiently differentiate your version is important. Google also has a mechanism to give them [advanced notice](https://support.google.com/googleplay/android-developer/answer/6320428) that you have permission to use Shattered's code/assets under the GPLv3 license. Sadly, in 2026 it seems that removing "Pixel Dungeon" from your app name is the most consistent way to avoid trouble here.

## Application version code

Shattered Pixel Dungeon has an internal version code which should be incremented with each release. It is defined with the `appVersionCode` variable in the root [build.gradle](../build.gradle) file.

You may be tempted to set this value back to 1, but Shattered has compatibility code for previous versions which may be incorrectly triggered if you decrement the version code. The version code is entirely internal and has a max value of 2^31, so there is no harm in using Shattered's current version code as a starting point.

If you wish to set it to 1 anyway, the various constant variables toward the top of [ShatteredPixelDungeon.java](../core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/ShatteredPixelDungeon.java) are a good starting point for finding all of the cases where the game refers to the version code for compatibility purposes.

## Credits & Supporter button

You will likely want to add yourself to the credits or change the current supporter link. Feel free to adjust these however you like, the relevant code is in [AboutScene.java](../core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/scenes/AboutScene.java) and [SupporterScene.java](../core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/scenes/SupporterScene.java). If you wish to disable the supporter link, simply comment out the line `add(btnSupport);` in [TitleScene.java](../core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/scenes/TitleScene.java).

The game also has a one-time nag window that appears when the player first defeats Goo. If you wish to edit this window, the code is in [WndSupportPrompt.java](../core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/windows/WndSupportPrompt.java). If you wish to disable it entirely, the triggering logic is in [WornKey.java](../core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/items/keys/WornKey.java).

Note that due to the GPLv3 license, any edits you make to the credits scene must avoid removing any existing credits, though you can reposition them however you like. I would however suggest that you remove or reposition the link to my Patreon, it makes little sense to feature it prominently in a version not made by me.

**Various storefronts (such as Google Play) also have policies against external links to payment services. You should disable links to Patreon and similar websites entirely if you intend to release on storefronts.**

## Update Notification

Shattered Pixel Dungeon includes a github-based update notification which likely will not be useful unless it is modified.

To simply disable the notification change `:services:updates:githubUpdates` to `:services:updates:debugUpdates` for the release configurations in the build.gradle files in the [desktop](../desktop/build.gradle) and [android](../android/build.gradle) modules. The debug updates module does nothing by default and so works just fine in release builds.

To modify the notification to point to your own github releases, go to [GitHubUpdates.java](../services/updates/githubUpdates/src/main/java/com/shatteredpixel/shatteredpixeldungeon/services/updates/GitHubUpdates.java) and change the line: `httpGet.setUrl("https://api.github.com/repos/00-Evan/shattered-pixel-dungeon/releases");` to match your own username and repository name. The github updater looks for a title, body of text followed by three dashes, and the phrase \` internal version number: # \` in your release.

More advanced developers can change the format for releases if they like, or make entirely new update notification services.

## News Feed

Shattered Pixel Dungeon includes a news feed which pulls blog posts from [ShatteredPixel.com](http://ShatteredPixel.com). The articles there may not be useful to you so you may wish to remove them.

To simply disable news entirely, comment out the line `add(btnNews);` in [TitleScene.java](../core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/scenes/TitleScene.java).

You can also point the news checker to a different feed by modifying the URLs in [ShatteredNews.java](../services/news/shatteredNews/src/main/java/com/shatteredpixel/shatteredpixeldungeon/services/news/ShatteredNews.java). Note that the current logic expects an atom feed and is slightly customized to ShatteredPixel.com, but the logic can be modified to work with other xml feed types.

More advanced developers can also write their own news checker services and use those.

## Translations

Shattered Pixel Dungeon supporters a number of languages which are translated via a [community translation project](https://explore.transifex.com/shattered-pixel/shattered-pixel-dungeon/).

If you plan to add new text to the game, maintaining these translations may be difficult or impossible, and so you may wish to remove them:
- In [Languages.java](../core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/messages/Languages.java) remove all of the enum constants except for ENGLISH.
- In the [messages resource folders](../core/src/main/assets/messages) remove all of the .properties files which include an underscore followed by a language code (e.g. remove actors_ru.properties, but not actors.properties)
- Finally remove the language picker by commenting out the lines `add( langs );` and `add( langsTab );` in [WndSettings.java](../core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/windows/WndSettings.java)
- Optionally, if you are multilingual or have translators and wish to retain some languages, do not comment out the language picker and only remove the enums/resources for the languages you won't be using.

If you want to have a language other than English as the base language, you can simply remove the .properties files that do not have a language code, and remove the underscore+language code from the language you want to use. The game will consider this language to be English internally however, so you may want to look into where the ENGLISH variable is used and make adjustments accordingly, and possibly rename it.