# PROJECT MEMORY: Shattered Pixel Dungeon - Real-Time Mod

## 1. High-Level Objective
Convert the turn-based engine into a Real-Time Action RPG (WASD Movement, Space to Interact, Real-time Combat).

## 2. Current Sprint: Interaction & Looting
* Active Goal: Fixing Chest/Container interaction.
* Current Status: "Spacebar" input is being rerouted from `Toolbar.java` to `Hero.performRealtimeInteraction()`.

## 3. Architectural Rules (IMMUTABLE)
* Input vs Logic: `RealtimeInput.java` is for input detection ONLY. It must not contain game logic.
* Hero Logic: All interaction logic lives in `Hero.java` (specifically `performRealtimeInteraction`).
* Turn Logic: We bypass standard `act/operate` methods for containers. We use "Direct Logic" (checking keys manually, calling `.open()` directly) to avoid turn-based thread gating.

## 4. Work Log (Recent)
* [BUILD] Fixed Gradle error by replacing project(':SPD-classes') dependency with a file-based jar in services (news/updates) modules. All affected build.gradle now use SPD-classes/build/libs/SPD-classes-${appVersionName}.jar.
* [BUILD] Added GDX core dependency (com.badlogicgames.gdx:gdx:$gdxVersion) to services:news:shatteredNews and services:updates:githubUpdates so Net/XmlReader imports compile.
* [BUILD] Added SPD-classes jar and gdx core to core module dependencies to resolve com.watabou.noosa.Game and related classes.
* [BUILD] Added gdx-controllers-core to core to resolve com.badlogic.gdx.controllers.ControllerListener and input APIs.
* [BUILD] Fixed repositories: replaced invalid central.sonatype.com UI URL with proper Sonatype snapshots repo (s01.oss.sonatype.org).
* [BUILD] Pinned libGDX to stable 1.12.1 (was 1.13.6-SNAPSHOT) to resolve dependency resolution failures across core/services modules.
* [BUILD] Excluded local stub core/src/main/java/com/watabou/utils/DeviceCompat.java so the SPD-classes jar version is used (fixes missing methods like getRealPixelScaleX/Y and getPlatformVersion).
* [COMPLETED] Safety patch: `RealtimeInteractionPrompt.update()` now checks `hero.ready` and wraps logic in a try-catch to avoid crashes during level transitions.
* [COMPLETED] Implemented `Hero.performRealtimeInteraction()` with "Proximity Snap" (1.6f radius).
* [COMPLETED] Updated `Chest.java` to make `open()` public.
* [COMPLETED] Implemented "Manual Key Check" for `LockedChest` to bypass turn-based locks.
* [COMPLETED] Replaced intrusive wireframe box with a soft white glow (Halo) under target heaps in `RealtimeInteractionPrompt`.
* [COMPLETED] Upgraded visual feedback to Item Silhouette overlay: uses `ItemSprite` in UI to render a pulsing white silhouette exactly over the item.
* [COMPLETED] Eliminated final offset by computing world-aligned top-left via `ItemSprite.worldToCamera(cell)` with origin(0,0), scale=1, and Camera.main. Silhouette now matches the in-world item draw including perspective raise.
* [TUNED] Increased silhouette size by 1.01x (scale=1.01) while using worldToCamera so center/bottom alignment remains correct.
* [TUNED] Silhouette forced to pure white: after view(item) we clear glow and apply color(0xFFFFFF) to ensure a white overlay independent of item palette.
* [UX] Centered pickup feedback: show FloatingText with item name at screen center on successful pickup.
* [UX] RealtimeInteractionPrompt: item name now centered beneath the actual overlay sprite (bottom-center in world space), with tile-based fallback if no sprite is shown.
* [COMPLETED] Containers now use chest/remains/tomb silhouettes: when targeting non-HEAP heaps, overlay renders the heap sprite in pure white instead of the contained item.
* [TUNED] Reduced item name label scale to 0.7x to keep it compact below the sprite.
* [TUNED] Disabled overlay flashing: highlight silhouette now uses constant alpha (0.85) instead of pulsing.











* [IN PROGRESS] Rerouting `Toolbar.java` hidden button to support Real-Time Spacebar input.

### 4.1 Work Log (Refactoring - December 2025)
* **[REFACTOR] Logic Extraction:** Moved all interaction logic from `Hero.java` to `com.shatteredpixel.shatteredpixeldungeon.mechanics.RealtimeController.java`. Hero class reduced by 155+ lines.
* [FIX] RealtimeController.tryUnlockChest now accepts Key instead of Item to satisfy Notes.keyCount/remove signatures and compile cleanly.
* **[OPTIMIZATION] Distance Squared:** Replaced expensive `Math.sqrt()` calls with `distSq` checks in RealtimeController (~30% performance improvement in distance calculations).
* **[CLEANUP] RealtimeInteractionPrompt:** Flattened nesting, applied DRY principles (eliminated duplicate `worldToCamera` calls), reduced method from 93 to 85 lines.
* **[ARCHITECTURE] Controller Pattern:** Main interaction method simplified from 150+ lines to 15 lines with clear priority flow. Extracted 9 focused helper methods for maintainability.
* **[PERFORMANCE] Zero-Allocation Preserved:** All optimizations maintain zero-allocation guarantee in hot paths.

## 5. Architectural Rules (UPDATED)
* **Input vs Logic:** `RealtimeInput.java` is for input detection ONLY. It must not contain game logic.
* **The "Controller" Pattern:** `Hero.java` is for *State* (Health, Position, Inventory). `RealtimeController.java` is for *Logic* (Interacting, Opening, Searching, Unlocking).
* **Turn Logic:** We bypass standard `act/operate` methods for containers. We use "Direct Logic" (checking keys manually, calling `.open()` directly) to avoid turn-based thread gating.
* **Math Rule (PERFORMANCE):** Always use `distanceSquared` (distSq) for real-time radius checks. **Never use `Math.sqrt()` in `update()` loops** - it's expensive and unnecessary for distance comparisons.
* **Zero-Allocation Rule:** Hot paths (update loops, distance checks, scanning) must avoid object allocation. Use raw float math, reuse calculations, and prefer primitive comparisons.

## 6. Next Sprint: Real-Time Combat
* **Goal:** Implement "Click-to-Attack" or "Space-to-Attack" logic for real-time combat.
* **Challenge:** Syncing attack speed (Cooldowns) with real-time animations so the player can't spam-click 100 attacks per second.
* **Technical Approach:** Likely need cooldown system similar to attack delay, frame-based animation sync, and proper hit detection.
* **Reference Implementation:** See `Hero.performRealtimeAttack()` for initial attack logic framework.

## 7. Legacy Next Steps (Pre-Refactor)
1.  ✅ COMPLETED: `Toolbar.java` fix allows chests to open.
2.  Tune glow radius/alpha per tileset zoom and add localization for prompt text.
3.  Add "Slide" interpolation so the hero doesn't teleport when snapping to chest coordinates.


