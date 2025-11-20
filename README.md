# ⚔️ Mastery-System Pixel Dungeon

[Shattered Pixel Dungeon](https://shatteredpixel.com/shatteredpd/) is an open-source traditional roguelike dungeon crawler with randomized levels and enemies, and hundreds of items to collect and use. It's based on the [source code of Pixel Dungeon](https://github.com/00-Evan/pixel-dungeon-gradle), by [Watabou](https://watabou.itch.io/).

**Mastery-System Pixel Dungeon** is a highly refined fork of Shattered Pixel Dungeon, focusing on **deepening combat strategy** through a unique, tiered weapon progression system. It retains Evan's clean UI and robust core while introducing meaningful player choice.

---

## 🌟 Core Feature: Stratified Weapon Mastery System

This mod introduces the **Weapon Mastery System** to solve the over-reliance on Tier-5 weapons and give every weapon type a distinct late-game identity.

### I. Tiered Progression & Combat Stability

* **Weapon Mastery Levels:** Killing enemies with a specific weapon grants Mastery XP. Each level subtly increases the weapon's **Min and Max Damage** (Min damage increases faster), drastically improving damage stability in the late game.
* **Anti-Grinding Mechanic:** Mastery progression is capped by the current Dungeon Depth, forcing players to explore new levels to unlock higher potential.

### II. Unique Tier Mechanics (The Mastery Specialization)

We assign distinct roles to each weapon tier, turning each tier into a specialized tool:

#### T3 - The Tactical Burst (Controlled Fury)

* **Mechanic:** Landing 10 consecutive hits charges the **Controlled Fury** skill.
* **Player Choice:** The skill is activated **manually** by tapping the weapon's quickslot button, granting the Hero **Armor Penetration (Pierce)** for a short duration before entering a long cooldown.
* **Goal:** Converts mechanical skill (landing hits) into a controlled, tactical power spike, perfect for breaking heavy defenses.

#### T1 & T2 - The Late Bloomers

* **Mechanic:** Low base Crit Chance that only unlocks at Mastery Level +6. This chance gradually increases with every subsequent Mastery level.
* **Goal:** Rewards early-game resource investment and creates high-Mastery **T1/T2 "Swiftness" builds** that are reliable crit-machines, competing with T5's raw damage through speed and frequency.

#### T4 & T5 (Future Expansion): The Heavy Hitters

* (This section is for future proofing your mod and showing future potential)
* **T4:** Focuses on a guaranteed **High-Damage Single-Hit** followed by a lengthy cooldown, ideal for quick threats.
* **T5:** Focuses on **High-Frequency Damage** with a significant **Time Cost** penalty, ensuring the ultimate power comes at the cost of combat pace.

---

Shattered Pixel Dungeon currently compiles for Android, iOS, and Desktop platforms. You can find official releases of the game on:

[![Get it on Google Play](https://shatteredpixel.com/assets/images/badges/gplay.png)](https://play.google.com/store/apps/details?id=com.shatteredpixel.shatteredpixeldungeon)
[![Download on the App Store](https://shatteredpixel.com/assets/images/badges/appstore.png)](https://apps.apple.com/app/shattered-pixel-dungeon/id1563121109)
[![Steam](https://shatteredpixel.com/assets/images/badges/steam.png)](https://store.steampowered.com/app/1769170/Shattered_Pixel_Dungeon/)<br>
[![GOG.com](https://shatteredpixel.com/assets/images/badges/gog.png)](https://www.gog.com/game/shattered_pixel_dungeon)
[![Itch.io](https://shatteredpixel.com/assets/images/badges/itch.png)](https://shattered-pixel.itch.io/shattered-pixel-dungeon)
[![Github Releases](https://shatteredpixel.com/assets/images/badges/github.png)](https://github.com/00-Evan/shattered-pixel-dungeon/releases)

If you like this game, please consider [supporting me on Patreon](https://www.patreon.com/ShatteredPixel)!

There is an official blog for this project at [ShatteredPixel.com](https://www.shatteredpixel.com/blog/).

The game also has a translation project hosted on [Transifex](https://explore.transifex.com/shattered-pixel/shattered-pixel-dungeon/).

Note that **this repository does not accept pull requests!** The code here is provided in hopes that others may find it useful for their own projects, not to allow community contribution. Issue reports of all kinds (bug reports, feature requests, etc.) are welcome.

If you'd like to work with the code, you can find the following guides in `/docs`:
- [Compiling for Android.](docs/getting-started-android.md)
    - **[If you plan to distribute on Google Play please read the end of this guide.](docs/getting-started-android.md#distributing-your-apk)**
- [Compiling for desktop platforms.](docs/getting-started-desktop.md)
- [Compiling for iOS.](docs/getting-started-ios.md)
- [Recommended changes for making your own version.](docs/recommended-changes.md)