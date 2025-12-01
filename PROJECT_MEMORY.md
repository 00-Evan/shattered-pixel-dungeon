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




## 5. Next Steps
1.  Verify `Toolbar.java` fix allows chests to open.
2.  Tune glow radius/alpha per tileset zoom and add localization for prompt text.
3.  Add "Slide" interpolation so the hero doesn't teleport when snapping to chest coordinates.


