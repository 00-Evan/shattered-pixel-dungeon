# CLAUDE.md - AI Assistant Guide for Shattered Pixel Dungeon

**Last Updated:** December 7, 2025
**Version:** 3.2.5 (vCode: 877)
**Project Type:** Multi-module Gradle + LibGDX Game Engine
**Primary Language:** Java 11+

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Repository Structure](#repository-structure)
3. [Core Architecture](#core-architecture)
4. [Development Workflows](#development-workflows)
5. [Key Conventions](#key-conventions)
6. [Common Tasks](#common-tasks)
7. [Testing Strategy](#testing-strategy)
8. [Build System](#build-system)
9. [Important Notes for AI Assistants](#important-notes-for-ai-assistants)

---

## Project Overview

**Shattered Pixel Dungeon** is an open-source traditional roguelike dungeon crawler based on the original Pixel Dungeon by Watabou. This fork has been extensively modified to include a **real-time interaction system** layered on top of the original turn-based architecture.

### Key Characteristics

- **Genre:** Roguelike dungeon crawler with procedural generation
- **Engine:** Custom Noosa engine (Watabou) built on LibGDX 1.12.1
- **Platforms:** Desktop (LWJGL3), Android, iOS
- **License:** GPLv3 (see file headers for copyright)
- **Original Author:** Evan Debenham (00-Evan)
- **This Fork:** Real-time mod with WASD movement and spacebar interactions

### Project Status

This repository is actively being modified for real-time gameplay:
- ✅ **Completed:** WASD movement, spacebar interactions (stairs, doors, chests, items)
- ✅ **Completed:** Save/load system debugging and version compatibility fixes
- ✅ **Completed:** Refactoring to Controller pattern for interaction logic
- 🚧 **In Progress:** Real-time combat system
- 📋 **Planned:** Animation synchronization, cooldown systems

**Important:** The original Shattered Pixel Dungeon repository does NOT accept pull requests. This is a fork for experimental real-time modifications.

---

## Repository Structure

```
shattered-pixel-dungeon/
├── SPD-classes/                    # Watabou engine (shared utilities)
│   └── src/main/java/com/watabou/
│       ├── noosa/                  # Graphics engine (Scene, Visual, Camera)
│       ├── gltextures/             # Texture loading and caching
│       ├── glwrap/                 # OpenGL wrapper
│       ├── input/                  # Input handling (KeyBindings, ControllerHandler)
│       └── utils/                  # Core utilities (Bundle, FileUtils, Random, PathFinder)
│
├── core/                           # Main game logic (1,160+ Java files)
│   ├── src/main/java/com/shatteredpixel/shatteredpixeldungeon/
│   │   ├── actors/                 # Actor system (Hero, Mobs, Buffs, Blobs)
│   │   ├── items/                  # Item system (356 classes)
│   │   ├── levels/                 # Level generation and terrain
│   │   ├── scenes/                 # Game scenes (17 scene types)
│   │   ├── ui/                     # UI components (55+ classes)
│   │   ├── mechanics/              # Game mechanics (NEW: RealtimeController)
│   │   ├── input/                  # Input systems (NEW: RealtimeInput)
│   │   ├── sprites/                # Sprite rendering
│   │   ├── effects/                # Visual effects
│   │   ├── tiles/                  # Tilemap rendering
│   │   ├── messages/               # Localization (11+ languages)
│   │   ├── journal/                # Encyclopedia and quest tracking
│   │   ├── plants/                 # Plant/seed system
│   │   ├── windows/                # Modal dialogs
│   │   └── utils/                  # Game-specific utilities
│   │
│   ├── src/main/assets/            # Game assets (37 MB)
│   │   ├── sprites/                # Sprite atlases (.png)
│   │   ├── environment/            # Tileset textures
│   │   ├── sounds/                 # SFX (.ogg)
│   │   ├── music/                  # Background music (.ogg)
│   │   ├── fonts/                  # Pixel fonts
│   │   ├── splashes/               # Title/loading screens
│   │   └── messages/               # Localization .properties files
│   │
│   └── src/test/java/              # Unit tests (JUnit 4 + Mockito)
│       └── com/shatteredpixel/shatteredpixeldungeon/
│           ├── input/              # RealtimeInputTest
│           └── mechanics/          # RealtimeInteractionTest, Integration tests
│
├── desktop/                        # Desktop launcher (LWJGL3)
│   ├── src/main/java/              # DesktopLauncher.java
│   └── src/main/assets/icons/      # Desktop icons
│
├── services/                       # Update/news services
│   ├── updates/
│   │   ├── githubUpdates/          # GitHub release checker
│   │   └── debugUpdates/           # Debug (no-op) updates
│   └── news/
│       ├── shatteredNews/          # Official news feed
│       └── debugNews/              # Debug (no-op) news
│
├── docs/                           # Documentation
│   ├── getting-started-android.md
│   ├── getting-started-desktop.md
│   ├── getting-started-ios.md
│   └── recommended-changes.md      # How to fork this project
│
├── build.gradle                    # Root build config (version, dependencies)
├── settings.gradle                 # Module definitions
├── gradle.properties               # JVM settings, R8 flags
├── README.md                       # Project overview
└── PROJECT_MEMORY.md               # Real-time mod development log
```

### Key Files by Purpose

| Purpose | File Path | Description |
|---------|-----------|-------------|
| **Entry Point** | `core/.../ShatteredPixelDungeon.java` | Game initialization, platform setup |
| **Game Loop** | `core/.../scenes/GameScene.java` | Main render/update loop, input handling |
| **State Manager** | `core/.../Dungeon.java` | Global game state (1,089 lines) |
| **Player Logic** | `core/.../actors/hero/Hero.java` | Player character (health, inventory, movement) |
| **Real-time Input** | `core/.../input/RealtimeInput.java` | WASD + spacebar state (volatile flags) |
| **Interaction Logic** | `core/.../mechanics/RealtimeController.java` | Interaction mechanics (stairs, doors, chests) |
| **Settings** | `core/.../SPDSettings.java` | Persistent game settings |
| **Localization** | `core/.../messages/Messages.java` | Multi-language string system |
| **Save System** | `core/.../utils/GamesInProgress.java` | Save slot management |

---

## Core Architecture

### 1. Game Engine Architecture

**Engine:** Custom Noosa engine (Watabou) built on top of LibGDX

```
LibGDX (OpenGL, Input, Audio, Files)
    ↓
Noosa Engine (SPD-classes/)
    ├── Scene management
    ├── Visual rendering (Sprite, Visual, Group)
    ├── Camera system
    ├── Input handling
    └── Audio playback
    ↓
Game Logic (core/)
    ├── Actor system (turn-based + real-time hybrid)
    ├── Item/inventory system
    ├── Level generation
    ├── UI system
    └── Save/load system
```

### 2. Actor System (Turn-Based Foundation)

**Core Concept:** Priority queue scheduling with floating-point time

```java
// Priority order (lower = later in turn)
VFX_PRIO = 100      // Visual effects
HERO_PRIO = 0       // Player character
MOB_PRIO = -20      // Enemies
BUFF_PRIO = -30     // Status effects
```

**Actor Hierarchy:**
```
Actor (abstract base)
├── Char (characters)
│   ├── Hero (player)
│   └── Mob (enemies, NPCs)
├── Blob (area effects: fire, gas, water)
└── Buff (status effects on characters)
```

**Key Methods:**
- `act()` - Returns `true` when ready to act
- `spend(time)` - Advances actor's turn timer (can be modified)
- `spendConstant(time)` - Fixed time spend (cannot be modified)

**Actor Lifecycle:**
```
Add actor → Actor.add(actor)
    ↓
Scheduled in priority queue
    ↓
act() called when time reaches 0
    ↓
Returns time to spend until next turn
    ↓
Repeat or remove via Actor.remove(actor)
```

### 3. Real-Time Hybrid System (NEW - December 2025)

**Design Pattern:** Layer real-time input on top of turn-based engine without breaking existing systems.

**Input Flow:**
```
GameScene.inputListener()
    ↓
RealtimeInput.moveLeft/Right/Up/Down = true/false (volatile)
RealtimeInput.onActionPressed() called on SPACE
    ↓
Hero.performRealtimeInteraction()
    ↓
RealtimeController.performInteraction(hero)
    ├── tryLevelTransition()        # Priority 1: Stairs/portals
    ├── tryUnlockAdjacentDoors()    # Priority 2: Locked doors
    └── scanForTarget() + interact  # Priority 3: Containers → Items
```

**Key Classes:**
- **`RealtimeInput.java`** - Input state holder (WASD + spacebar)
  - Volatile boolean flags for thread-safe input
  - `isEnabled()` gate for real-time mode
  - `onActionPressed()` callback

- **`RealtimeController.java`** - Stateless interaction logic
  - Contains NO state (all methods are static)
  - Zero-allocation hot paths (performance-critical)
  - Distance checks use squared distances (avoid Math.sqrt)

- **`Hero.performRealtimeInteraction()`** - Execution bridge
  - Called from input system
  - Delegates to RealtimeController
  - Integrates with existing Hero state

- **`RealtimeInteractionPrompt.java`** - UI feedback
  - Shows white silhouette of target item/container
  - Displays item name below sprite
  - World-aligned positioning

### 4. Item System

**Item Hierarchy:**
```
Item (base: Bundlable for save/load)
├── EquipableItem
│   ├── Weapon (33 melee classes)
│   │   └── Enchantments
│   └── Armor (13 classes)
│       └── Glyphs
├── Wands (40+ variants)
├── Potions (exotic, elixirs, brews)
├── Scrolls (exotic variants)
├── Food
├── Stones (runestones)
├── Artifacts (unique items)
├── Keys (IronKey, GoldenKey, SkeletonKey, CrystalKey)
└── Special (Amulet, Gold, Ankh)
```

**Item Storage:**
- `Heap` - Items on ground (indexed by cell position)
- `Belongings` - Hero's inventory/equipment
- `Bag` - Inventory containers (SeedPouch, ScrollHolder, etc.)

### 5. Level System

**Level Types:**
```
Level (abstract)
├── RegularLevel (procedural)
│   ├── SewerLevel (depth 1-4)
│   ├── PrisonLevel (depth 5-8)
│   ├── CavesLevel (depth 9-12)
│   ├── CityLevel (depth 13-16)
│   └── HallsLevel (depth 17-20)
├── BossLevel (5 boss arenas)
└── SpecialLevel (LastLevel, shops, etc.)
```

**Level Structure:**
- **32x32 tilemap** (1,024 cells)
- **Terrain types:** Floor, wall, water, pit, grass, locked doors, etc. (`Terrain.java`)
- **Features:** LevelTransition (stairs/portals), traps, plants
- **Generation:** Builder pattern → Painter pattern → Actor/item population

**Key Methods:**
- `create()` - Generate level layout
- `press(cell, char)` - Handle cell activation (traps, plants)
- `activateTransition(hero, transition)` - Change levels

### 6. Save/Load System

**Save Format:** Custom `Bundle` serialization (key-value pairs)

**Save Files:**
- Windows: `%APPDATA%\.shatteredpixel\<package>\game<slot>\game.dat`
- Linux: `~/.shatteredpixel/<package>/game<slot>/game.dat`

**Save Chain:**
```
User saves game
    ↓
Dungeon.saveAll()
    ├── saveGame() - Writes game state (hero, inventory, statistics)
    └── saveLevel() - Writes current level (terrain, actors, items)
    ↓
FileUtils.bundleToFile(bundle, file)
    ↓
GamesInProgress.set(slot, info) - Updates in-memory cache
```

**Load Chain:**
```
User loads game
    ↓
GamesInProgress.check(slot)
    ├── Reads game.dat via Dungeon.preview()
    ├── Validates version >= 782 (v2.4.2)
    └── Creates Info object (class, depth, challenges)
    ↓
Dungeon.load(slot)
    ├── Loads game state from game.dat
    └── Loads level from depth<N>.dat
```

**Version Compatibility:**
- Saves require `versionCode >= 782` (v2.4.2 minimum)
- Development builds default to `versionCode = 859` (v3.2.0) in `DesktopLauncher.java:127`
- **CRITICAL:** Never set `versionCode = 1` or saves will be rejected

### 7. Scene Management

**Scene Hierarchy:**
```
Game (LibGDX base)
    ↓
Scene (Noosa base)
    ├── TitleScene (main menu)
    ├── StartScene (new game / continue)
    ├── GameScene (main gameplay)
    ├── InterlevelScene (level transitions)
    ├── AlchemyScene (crafting)
    ├── JournalScene (encyclopedia)
    └── ... (14 more scenes)
```

**Scene Lifecycle:**
```
Game.switchScene(newScene)
    ↓
destroy() on old scene
    ↓
create() on new scene
    ↓
update(elapsed) called every frame
    ↓
draw() renders visuals
```

---

## Development Workflows

### 1. Building and Running

**Desktop Build:**
```bash
# Build desktop JAR
./gradlew desktop:dist

# Run desktop launcher (development)
./gradlew desktop:run

# Run with specific JVM args
./gradlew desktop:run -Dorg.gradle.jvmargs="-Xmx2048m"
```

**Testing:**
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests RealtimeControllerIntegrationTest

# Run tests with verbose output
./gradlew test --info
```

**Clean Build:**
```bash
# Clean all modules
./gradlew clean

# Rebuild everything
./gradlew clean build
```

### 2. Version Management

**Location:** `build.gradle` (root)

```gradle
appVersionCode = 877      # Internal version (increment each release)
appVersionName = '3.2.5'  # User-visible version
```

**Important:** Never decrement `appVersionCode` below the current value (877) as compatibility code depends on it.

### 3. Git Workflow

**Commit Message Format:**
```
<type>: <short description>

<detailed explanation if needed>

Examples:
- "Add comprehensive tests for real-time interaction system"
- "Fix save persistence: set development versionCode to 859 instead of 1"
- "Refactor interaction logic to RealtimeController for better separation"
```

**Branch Naming:**
- Feature branches: `claude/<feature-name>-<session-id>`
- Current branch: `claude/claude-md-miv3pch3eioggfi0-012eFvQjP9TknaCtQwWpgWhc`

### 4. License Compliance (GPLv3)

**All new Java files must include:**
```java
/*
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
```

**For files derived from Pixel Dungeon (Watabou):**
```java
/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * [Rest of GPL header...]
 */
```

---

## Key Conventions

### 1. Code Style

**Package Naming:**
- Base package: `com.shatteredpixel.shatteredpixeldungeon`
- Subsystems: `actors`, `items`, `levels`, `scenes`, `ui`, `mechanics`, etc.

**Class Naming:**
- PascalCase for classes: `Hero`, `RealtimeController`, `GameScene`
- Descriptive names: `RealtimeInteractionPrompt` not `RIPrompt`

**Method Naming:**
- camelCase for methods: `performInteraction()`, `scanForTarget()`
- Boolean getters: `isEnabled()`, `hasKey()`, `canUnlock()`

**Field Naming:**
- camelCase for fields: `currentDepth`, `heroPos`, `targetHeap`
- Constants: `UPPER_SNAKE_CASE` - `VFX_PRIO`, `HERO_PRIO`

**Indentation:**
- Tabs for indentation (NOT spaces)
- Consistent with existing codebase

### 2. Localization

**All user-facing strings must use Messages.get():**
```java
// WRONG:
GLog.p("You picked up a sword!");

// CORRECT:
GLog.p(Messages.get(Item.class, "pickup"));
```

**Message Files:**
- Location: `core/src/main/assets/messages/`
- Format: `<category>.properties` (e.g., `items.properties`, `actors.properties`)
- Keys: Hierarchical dot notation

Example `items.properties`:
```properties
items.gold.pickup=You picked up %d gold.
items.key.iron_key=Iron Key
items.key.golden_key=Golden Key
```

Usage:
```java
Messages.get(Gold.class, "pickup", quantity);
Messages.get(IronKey.class, "iron_key");
```

### 3. Performance Rules

**CRITICAL for real-time code:**

1. **No Math.sqrt() in update loops**
   ```java
   // WRONG:
   float dist = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
   if (dist < radius) { ... }

   // CORRECT:
   float distSq = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
   if (distSq < radius*radius) { ... }
   ```

2. **Zero-allocation hot paths**
   - Avoid creating objects in `update()` or `act()` methods
   - Reuse existing objects when possible
   - Use primitive types over boxed types

3. **Cache calculations**
   ```java
   // WRONG:
   for (int i = 0; i < list.size(); i++) {
       process(list.get(i));
   }

   // CORRECT:
   int n = list.size();
   for (int i = 0; i < n; i++) {
       process(list.get(i));
   }
   ```

### 4. Architecture Rules (IMMUTABLE)

**From PROJECT_MEMORY.md:**

1. **Input vs Logic Separation**
   - `RealtimeInput.java` is for input detection ONLY
   - NO game logic in input classes
   - Logic belongs in `RealtimeController` or `Hero`

2. **The Controller Pattern**
   - `Hero.java` is for *State* (health, position, inventory)
   - `RealtimeController.java` is for *Logic* (interacting, opening, searching)
   - Controller classes are STATELESS (static methods only)

3. **Turn Logic Bypass**
   - Real-time interactions bypass standard `act()`/`operate()` methods
   - Use "Direct Logic" (manual key checks, direct `.open()` calls)
   - Avoids turn-based thread gating

4. **Zero-Allocation Rule**
   - Hot paths (update loops, distance checks, scanning) must avoid object allocation
   - Use raw float math, reuse calculations, prefer primitive comparisons

### 5. Interaction Priority (Real-Time System)

**Always follow this priority order:**
1. Level transitions (stairs/portals)
2. Locked doors (key checks)
3. Containers (chests, remains, tombs)
4. Ground items (pickup)

**Implementation in `RealtimeController.performInteraction()`:**
```java
// 1. Try stairs/portals first
if (tryLevelTransition(hero)) return;

// 2. Try unlocking doors
if (tryUnlockAdjacentDoors(hero)) return;

// 3. Try containers/items
if (scanForTarget(hero) && tryInteractWithHeap(hero, target)) return;

// 4. Fallback: pickup items at hero position
hero.pickup(null);
```

### 6. Common Pitfalls

**Avoid these mistakes:**

1. **Hardcoding strings instead of localization**
   - Always use `Messages.get()`

2. **Modifying shared state without synchronization**
   - Use `volatile` for cross-thread flags (see `RealtimeInput`)

3. **Forgetting to save before level transitions**
   - Always call `Dungeon.saveAll()` before `activateTransition()`

4. **Using wrong distance function**
   - Use `distSq` for comparisons, not `dist` (sqrt is expensive)

5. **Creating objects in hot paths**
   - Check if method is called every frame before allocating

---

## Common Tasks

### 1. Adding a New Item

**Steps:**
1. Create new class in `core/.../items/<category>/`
2. Extend appropriate base class (`Item`, `EquipableItem`, `Weapon`, etc.)
3. Implement required methods:
   - `storeInBundle(Bundle)` - Save state
   - `restoreFromBundle(Bundle)` - Load state
   - `defaultAction()` - What happens when used
   - `desc()` - Description string
4. Add to `Generator.java` if procedurally generated
5. Add sprite to `items.png` texture atlas
6. Add localization strings to `items.properties`

**Example:**
```java
public class NewSword extends MeleeWeapon {
    {
        image = ItemSpriteSheet.NEW_SWORD;
        tier = 3;
        // ... other properties
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }
}
```

### 2. Adding a New Enemy

**Steps:**
1. Create class in `core/.../actors/mobs/`
2. Extend `Mob` class
3. Implement AI logic in `chooseEnemy()` and `act()`
4. Define stats (HP, damage, armor, etc.)
5. Add sprite to `mobs.png` texture atlas
6. Create corresponding `CharSprite` subclass
7. Add to `Bestiary.java` for journal
8. Add localization to `actors.properties`

### 3. Adding a New Level Type

**Steps:**
1. Create class in `core/.../levels/`
2. Extend `RegularLevel` or `Level`
3. Implement `build()` for level generation
4. Choose/create appropriate painter
5. Define mob spawning rules
6. Define item generation rules
7. Add tileset textures if custom
8. Integrate into `Dungeon.createLevel()`

### 4. Modifying Real-Time Interactions

**File to modify:** `core/.../mechanics/RealtimeController.java`

**To add new interaction type:**
1. Create new helper method following pattern:
   ```java
   private static boolean tryNewInteraction(Hero hero) {
       // Check if interaction is possible
       if (!canInteract()) return false;

       // Perform interaction
       doInteraction();

       return true;  // Consumed action
   }
   ```

2. Add to priority chain in `performInteraction()`:
   ```java
   public static void performInteraction(Hero hero) {
       if (tryLevelTransition(hero)) return;
       if (tryUnlockAdjacentDoors(hero)) return;
       if (tryNewInteraction(hero)) return;  // <-- Add here
       if (scanForTarget(hero) && tryInteractWithHeap(hero, target)) return;
       hero.pickup(null);
   }
   ```

3. Follow zero-allocation rule (use primitives, avoid `new` keyword)

### 5. Adding UI Elements

**For HUD elements:**
1. Create class in `core/.../ui/`
2. Extend appropriate base (`Component`, `Button`, `Window`, etc.)
3. Override `layout()` to position elements
4. Override `update()` for dynamic updates
5. Add to `GameScene.create()` or appropriate scene

**For modal dialogs:**
1. Create class in `core/.../windows/`
2. Extend `Window` or `WndTabbed`
3. Implement content in constructor
4. Show via `GameScene.show(new YourWindow())`

### 6. Debugging Save/Load Issues

**Diagnostic steps:**
1. Check version compatibility:
   ```java
   // In DesktopLauncher.java
   Game.versionCode = 859;  // Must be >= 782
   ```

2. Add logging to save chain:
   ```java
   GLog.i("Saving game to: " + file.path());
   ```

3. Verify Bundle serialization:
   - All saved fields must be in `storeInBundle()`
   - All restored fields must be in `restoreFromBundle()`
   - Types must match exactly (int → int, float → float)

4. Check file permissions and paths:
   ```java
   GLog.i("Save path: " + FileUtils.defaultPath);
   ```

---

## Testing Strategy

### 1. Test Structure

**Location:** `core/src/test/java/`

**Framework:** JUnit 4.13.2 + Mockito 5.8.0

**Current Test Files:**
- `input/RealtimeInputTest.java` - Input state tests
- `mechanics/RealtimeInteractionTest.java` - Interaction logic tests
- `mechanics/RealtimeControllerIntegrationTest.java` - Integration test documentation

### 2. Running Tests

```bash
# All tests
./gradlew test

# Specific test class
./gradlew test --tests RealtimeInputTest

# With verbose output
./gradlew test --info

# With stack traces
./gradlew test --stacktrace
```

### 3. Writing Tests

**Unit Test Example:**
```java
@Test
public void testDistanceSquared() {
    int x1 = 0, y1 = 0;
    int x2 = 3, y2 = 4;

    float distSq = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);

    assertEquals(25.0f, distSq, 0.001f);  // 3² + 4² = 25
}
```

**Integration Test Pattern (documented but not implemented):**
```java
@Test
@Ignore("Requires mock Dungeon and Level setup")
public void testPerformInteraction_ActivatesStairsWhenHeroOnTransition() {
    // SETUP:
    // - Mock Dungeon.level with transition at hero.pos
    // - Mock successful Dungeon.saveAll()

    // EXECUTE:
    // - RealtimeController.performInteraction(hero)

    // VERIFY:
    // - activateTransition() was called
    // - saveAll() was called
}
```

### 4. Test Coverage Priorities

**HIGH Priority (critical paths):**
- Real-time interaction priority logic
- Save/load system version validation
- Distance calculations (squared vs regular)
- Key/lock mechanics

**MEDIUM Priority:**
- UI interaction flows
- Item generation
- Combat calculations

**LOW Priority:**
- Visual effects
- Cosmetic features
- Debug utilities

---

## Build System

### 1. Gradle Multi-Module Structure

**Dependency Graph:**
```
desktop
  ↓
core
  ├── SPD-classes (api)
  └── services (implementation)
```

**Module Purposes:**
- `SPD-classes` - Shared engine utilities (Noosa, libGDX wrappers)
- `core` - Main game logic
- `desktop` - Desktop platform launcher
- `services` - Update/news services

### 2. Key Build Properties

**Root `build.gradle`:**
```gradle
appVersionCode = 877
appVersionName = '3.2.5'
gdxVersion = '1.12.1'              # Stable (not SNAPSHOT)
gdxControllersVersion = '2.2.4'
appJavaCompatibility = JavaVersion.VERSION_11
```

**`gradle.properties`:**
```properties
org.gradle.jvmargs=-Xmx2048m       # 2GB heap
org.gradle.parallel=true           # Parallel builds
android.enableR8.fullMode=false    # R8 optimizer
```

### 3. Important Build Notes

**LibGDX Version:**
- Pinned to 1.12.1 (stable)
- Previously used 1.13.6-SNAPSHOT which caused missing artifacts
- Do NOT change without testing all modules

**SPD-classes Dependency:**
- Core uses file-based JAR: `SPD-classes/build/libs/SPD-classes-${appVersionName}.jar`
- Not using `project(':SPD-classes')` due to build issues
- Must rebuild SPD-classes if modified

**Excluded Files:**
- `core/src/main/java/com/watabou/utils/DeviceCompat.java` is excluded
- Reason: SPD-classes version has required methods (getRealPixelScaleX/Y)
- See `core/build.gradle` sourceSets exclusion

---

## Important Notes for AI Assistants

### 1. Navigation Strategies

**To understand game loop:**
1. `ShatteredPixelDungeon.java` (entry point)
2. `GameScene.java` (main loop)
3. `Dungeon.java` (state manager)
4. `Actor.java` & `Hero.java` (turn system)

**To understand real-time system:**
1. `RealtimeInput.java` (input state)
2. `Hero.performRealtimeInteraction()` (execution)
3. `RealtimeController.java` (logic)
4. `RealtimeInteractionPrompt.java` (UI)

**To understand items:**
1. `Item.java` (base)
2. `Heap.java` (ground containers)
3. `/items/weapon/`, `/items/armor/`, etc.
4. `Generator.java` (procedural generation)

### 2. Common Code Patterns

**Singleton Access:**
```java
Dungeon.hero           // Current player
Dungeon.level          // Current level
Dungeon.depth          // Current depth (1-26)
GameScene.scene        // Current GameScene instance
```

**Localization:**
```java
Messages.get(ClassName.class, "key")
Messages.get(ClassName.class, "key", arg1, arg2)
```

**Logging:**
```java
GLog.p("Positive message");     // Blue
GLog.n("Neutral message");      // White
GLog.w("Warning message");      // Yellow
GLog.h("Highlight message");    // Green
// Note: GLog.e() does NOT exist, use GLog.w()
```

**Cell Calculations:**
```java
int cell = pos;                           // Cell index (0-1023)
int x = cell % Level.WIDTH;               // X coordinate (0-31)
int y = cell / Level.WIDTH;               // Y coordinate (0-31)
int newCell = x + y * Level.WIDTH;        // Reconstruct cell
```

**Distance:**
```java
// For comparisons (preferred):
float distSq = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
if (distSq < radius*radius) { ... }

// For actual distance (avoid in hot paths):
float dist = (float)Math.sqrt(distSq);
```

### 3. Critical Architecture Rules

**DO:**
- ✅ Use `Messages.get()` for ALL user-facing strings
- ✅ Follow zero-allocation rule in `update()` loops
- ✅ Use `distSq` for distance comparisons
- ✅ Call `Dungeon.saveAll()` before level transitions
- ✅ Include GPL headers in new files
- ✅ Use volatile flags for cross-thread communication
- ✅ Follow Controller pattern (stateless logic classes)

**DON'T:**
- ❌ Hardcode strings instead of localization
- ❌ Use `Math.sqrt()` in hot paths
- ❌ Allocate objects in `update()`/`act()` methods
- ❌ Put game logic in input classes
- ❌ Modify `versionCode` to values < 782
- ❌ Skip `Dungeon.saveAll()` before transitions
- ❌ Use `GLog.e()` (doesn't exist, use `GLog.w()`)

### 4. When to Consult PROJECT_MEMORY.md

**PROJECT_MEMORY.md contains:**
- Real-time mod development history
- Build issue resolutions
- Architecture decisions and rationale
- Performance tuning notes
- Known issues and workarounds

**Read it when:**
- Working on real-time systems
- Debugging build errors
- Understanding why certain patterns exist
- Planning new real-time features

### 5. Debugging Tips

**Build Failures:**
```bash
# Clean rebuild
./gradlew clean build --stacktrace

# Check dependency tree
./gradlew core:dependencies

# Verify module structure
./gradlew projects
```

**Runtime Issues:**
```java
// Add logging
GLog.i("Debug: variable = " + variable);

// Check save version
GLog.i("Version: " + Game.versionCode);

// Verify file paths
GLog.i("Save path: " + FileUtils.defaultPath);
```

**Real-Time Issues:**
- Check `RealtimeInput.isEnabled()` is true
- Verify input flags are volatile
- Confirm Controller methods are static
- Test priority order in `performInteraction()`

### 6. Performance Profiling

**Hot Paths (optimize these):**
- `GameScene.update()` - Called every frame
- `Actor.act()` - Called every turn for every actor
- `RealtimeController.performInteraction()` - Called on spacebar
- Distance calculations - Used for targeting/scanning
- Rendering code - Called every frame

**Cold Paths (don't over-optimize):**
- Level generation - Once per level
- Save/load - Rare events
- Menu rendering - Not performance-critical
- Initialization code - Once at startup

### 7. Recommended Reading Order for New AI Assistants

**Essential (read first):**
1. This file (CLAUDE.md)
2. PROJECT_MEMORY.md (real-time mod history)
3. README.md (project overview)
4. `docs/recommended-changes.md` (fork guidelines)

**Architecture (read before modifying):**
1. `core/.../Dungeon.java` (state manager)
2. `core/.../actors/Actor.java` (turn system)
3. `core/.../actors/hero/Hero.java` (player)
4. `core/.../mechanics/RealtimeController.java` (real-time logic)

**Subsystems (read as needed):**
- Item system: `core/.../items/Item.java`
- Level system: `core/.../levels/Level.java`
- UI system: `core/.../ui/Component.java`
- Save system: `core/.../utils/GamesInProgress.java`

---

## Quick Reference

### File Statistics
- **Total Java files:** 1,261
- **Core Java files:** 1,160
- **Item classes:** 356
- **Actor classes:** 250
- **Languages supported:** 11+
- **Asset size:** 37 MB

### Key Version Numbers
- **Current version:** 3.2.5 (code 877)
- **Minimum save version:** 2.4.2 (code 782)
- **Development version:** 3.2.0 (code 859)
- **Java:** 11+
- **LibGDX:** 1.12.1

### Important Paths
```
Save files:      ~/.shatteredpixel/<package>/game<slot>/
Assets:          core/src/main/assets/
Tests:           core/src/test/java/
Main entry:      core/.../ShatteredPixelDungeon.java
Game loop:       core/.../scenes/GameScene.java
State manager:   core/.../Dungeon.java
```

### Contact & Resources
- **Original Author:** Evan Debenham (@00-Evan)
- **Project Site:** https://shatteredpixel.com/
- **License:** GPLv3
- **This Fork:** Real-time modification (experimental)

---

**End of CLAUDE.md**

*This document should be updated whenever major architectural changes occur or new systems are added. Last updated: December 7, 2025.*
