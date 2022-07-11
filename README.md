# Shattered Pixel Dungeon

[Shattered Pixel Dungeon](https://shatteredpixel.com/shatteredpd/) is an open-source traditional roguelike dungeon crawler with randomized levels and enemies, and hundreds of items to collect and use. Its based on the [source code of Pixel Dungeon](https://github.com/00-Evan/pixel-dungeon-gradle), by [Watabou](https://www.watabou.ru).

A Roguelike RPG, with randomly generated levels, items, enemies, and traps! Based on the [source code of Pixel Dungeon](https://github.com/00-Evan/pixel-dungeon-gradle), by [Watabou](https://www.watabou.ru).

Shattered Pixel Dungeon currently compiles for Android, iOS, and Desktop platforms. You can find official releases of the game on:
![Get it on Google Play](https://shatteredpixel.com/assets/images/gplay-badge.png)
![Download on the App Store](https://shatteredpixel.com/assets/images/appstore-badge.png)
![Steam](https://shatteredpixel.com/assets/images/steam-badge.png)
![Github Releases](https://shatteredpixel.com/assets/images/github-badge.png)

If you like this game, please consider [supporting me on Patreon](https://www.patreon.com/ShatteredPixel)!

There is an official blog for this project at [ShatteredPixel.com](https://www.shatteredpixel.com/blog/).

The game also has a translation project hosted on [Transifex](https://www.transifex.com/shattered-pixel/shattered-pixel-dungeon/).

Note that **this repository does not accept pull requests!** The code here is provided in hopes that others may find it useful for their own projects, not to allow community contribution. Issue reports of all kinds (bug reports, feature requests, etc.) are welcome.

If you'd like to work with the code, you can find the following guides in `/docs`:

- [Compiling for Android.](docs/getting-started-android.md)
  - **[If you plan to distribute on Google Play please read the end of this guide.](docs/getting-started-android.md#distributing-your-apk)**
- [Compiling for desktop platforms.](docs/getting-started-desktop.md)
- [Compiling for iOS.](docs/getting-started-ios.md)
- [Recommended changes for making your own mod.](docs/recommended-changes.md)

## Mod description

### Done

- Potion of Experience now level up hero up to 30 levels
  - 2test: find and drink
- Potion of Healing now adds 5000 to Hero HP
  - 2test: really add hp
- Potion of Strength now multiply str by 10
![strength_10](images/strength_10.png)
- Shopkeepers not more so greedy
![shopkeeper](images/shopkeeper.png)
- Scroll of Upgrade now adds +10 to item and no longer dispels the enchantment
  - Not working on armor, WIP
  - Not working on weapon, WIP
  - 2test: Remove curse
  - 2test: Don't remove enchantment
