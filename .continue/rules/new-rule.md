System Role: The Project Scribe & Memory Manager
Objective: Establish a persistent "Save State" for our development progress to prevent context loss between sessions.

**Task: Create/Update the Project Memory File**

1.  **Check for existence:** Look for a file named `PROJECT_MEMORY.md` (or create it if it doesn't exist) in the root directory.
2.  **Maintenance Rule:** Every time we complete a major step, fix a bug, or change architecture, you MUST update this file.
3.  **Read-First Rule:** At the start of any future session, you must read this file to understand the current state of the project.

**Initial Content Generation:**
Please create `PROJECT_MEMORY.md` now and populate it with the following current state based on our recent work:

# PROJECT MEMORY: Shattered Pixel Dungeon - Real-Time Mod

## 1. High-Level Objective
Convert the turn-based engine into a Real-Time Action RPG (WASD Movement, Space to Interact, Real-time Combat).

## 2. Current Sprint: Interaction & Looting
* **Active Goal:** Fixing Chest/Container interaction.
* **Current Status:** "Spacebar" input is being rerouted from `Toolbar.java` to `Hero.performRealtimeInteraction()`.

## 3. Architectural Rules (IMMUTABLE)
* **Input vs Logic:** `RealtimeInput.java` is for input detection ONLY. It must not contain game logic.
* **Hero Logic:** All interaction logic lives in `Hero.java` (specifically `performRealtimeInteraction`).
* **Turn Logic:** We bypass standard `act/operate` methods for containers. We use "Direct Logic" (checking keys manually, calling `.open()` directly) to avoid turn-based thread gating.

## 4. Work Log (Recent)
* [COMPLETED] Implemented `Hero.performRealtimeInteraction()` with "Proximity Snap" (1.6f radius).
* [COMPLETED] Updated `Chest.java` to make `open()` public.
* [COMPLETED] Implemented "Manual Key Check" for `LockedChest` to bypass turn-based locks.
* [IN PROGRESS] Rerouting `Toolbar.java` hidden button to support Real-Time Spacebar input.

## 5. Next Steps
1.  Verify `Toolbar.java` fix allows chests to open.
2.  Add Visual Feedback (Floating "OPEN" text) when near a chest.
3.  Add "Slide" interpolation so the hero doesn't teleport when snapping to chest coordinates.