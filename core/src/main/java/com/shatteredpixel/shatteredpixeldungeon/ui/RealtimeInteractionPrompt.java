package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.input.RealtimeInput;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class RealtimeInteractionPrompt extends Component {

    private static final float RANGE = 1.6f;

    private final BitmapText nameLabel;
    private final ItemSprite highlightSprite;
    private int targetCell = -1;
    private int lastImageId = -1;

    public RealtimeInteractionPrompt() {
        super();
        // 1. Setup the Sprite (Game Layer)
        highlightSprite = new ItemSprite();
        highlightSprite.hardlight(0xFFFFFF); // White Silhouette
        highlightSprite.camera = Camera.main; // CRITICAL: bind to world camera
        highlightSprite.visible = false;
        add(highlightSprite);

        // 2. Setup the Text (UI Layer)
        nameLabel = new BitmapText(PixelScene.pixelFont);
        nameLabel.hardlight(0xFFEEAA);
        nameLabel.visible = false;
        // Slightly reduced label size for less visual clutter
        nameLabel.scale.set(0.7f);
        add(nameLabel);
    }

    @Override
    public void update() {
        super.update();
        // 1) HARD SAFETY CHECKS: bail out during transitions or when input/UI should be disabled
        if (!RealtimeInput.isEnabled() || 
                Dungeon.hero == null || 
                !Dungeon.hero.ready || // hero not ready during level transitions
                Dungeon.level == null) {
            hideAll();
            return;
        }
        try {
            Heap best = findClosestInteractableHeap();
            if (best == null) {
                hideAll();
                return;
            }

            // --- SPRITE LOGIC (World Space) ---
            Item item = best.peek();
            targetCell = best.pos;
            boolean isContainer = best.type != Heap.Type.HEAP && best.type != Heap.Type.FOR_SALE;
            if (isContainer || item != null) {
                // Update visual if changed (track heap types with negative keys to avoid collisions)
                int desiredKey = isContainer ? (-100 - best.type.ordinal()) : item.image();
                if (desiredKey != lastImageId) {
                    if (isContainer) {
                        highlightSprite.view(best);
                    } else {
                        highlightSprite.view(item);
                    }
                    // Force pure-white silhouette, remove any item-specific glow
                    highlightSprite.glow(null);
                    highlightSprite.color(0xFFFFFF);
                    lastImageId = desiredKey;
                }
                // Constant alpha (no flashing)
                highlightSprite.alpha(0.17f);
                highlightSprite.visible = true;

                // --- PRECISE WORLD POSITIONING USING ItemSprite.worldToCamera ---
                // Slightly upscale the silhouette, then compute exact world TL for this cell
                float overlayScale = 1.01f;
                highlightSprite.scale.set(overlayScale);
                highlightSprite.origin.set(0, 0);
                PointF worldTL = highlightSprite.worldToCamera(targetCell);
                highlightSprite.x = worldTL.x;
                highlightSprite.y = worldTL.y;
            } else {
                // No item and not a container we render
                highlightSprite.visible = false;
            }

            // --- TEXT LOGIC (Screen/UI Space) ---
            nameLabel.text(getHeapName(best));
            nameLabel.visible = true;

            nameLabel.measure();

            float labelX;
            float labelY;

            if (highlightSprite.visible) {
                // Center directly under the rendered silhouette bottom-center
                // 1) Use the same world TL we used to place the sprite
                PointF spriteTL = highlightSprite.worldToCamera(targetCell);
                float spriteBottomCenterX = spriteTL.x + highlightSprite.width() / 2f;
                float spriteBottomY = spriteTL.y + highlightSprite.height();

                // 2) Convert bottom-center to UI coordinates
                Point bcScreen = Camera.main.cameraToScreen(spriteBottomCenterX, spriteBottomY);
                PointF bcUI = PixelScene.uiCamera.screenToCamera(bcScreen.x, bcScreen.y);

                // 3) Center horizontally under the sprite; add small padding below
                labelX = bcUI.x - (nameLabel.width() / 2f);
                labelY = bcUI.y + 2f;
            } else {
                // Fallback: center under tile
                PointF worldTL = DungeonTilemap.tileToWorld(targetCell);
                Point screen = Camera.main.cameraToScreen(worldTL.x, worldTL.y);
                PointF ui = PixelScene.uiCamera.screenToCamera(screen.x, screen.y);
                float zoom = Camera.main.zoom;
                float tileSizeUI = DungeonTilemap.SIZE * zoom;
                labelX = ui.x + (tileSizeUI / 2f) - (nameLabel.width() / 2f);
                labelY = ui.y + tileSizeUI + 2f;
            }

            nameLabel.x = labelX;
            nameLabel.y = labelY;
        } catch (Exception e) {
            // During level transitions or any unexpected state, fail silently for this frame
            hideAll();
        }
    }

    private void hideAll() {
        nameLabel.visible = false;
        highlightSprite.visible = false;
        targetCell = -1;
        lastImageId = -1;
    }

    private Heap findClosestInteractableHeap() {
        float bestDist = Float.MAX_VALUE;
        Heap best = null;

        int w = Dungeon.level.width();
        float hx = Dungeon.hero.exactX; // Use exact sub-pixel position
        float hy = Dungeon.hero.exactY;

        // Efficient iteration over heaps
        for (Heap h : Dungeon.level.heaps.valueList()) {
            if (h == null) continue;
            if (h.type == Heap.Type.FOR_SALE) continue; // Don't steal from shopkeepers yet

            // Only show if it's a container or has items
            boolean showable = h.type != Heap.Type.HEAP || !h.isEmpty();
            if (!showable) continue;

            // Euclidean Distance Check
            int cx = h.pos % w;
            int cy = h.pos / w;
            float dx = cx - hx;
            float dy = cy - hy;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            
            if (dist <= RANGE && dist < bestDist) {
                bestDist = dist;
                best = h;
            }
        }
        return best;
    }

    private String getHeapName(Heap h) {
        switch (h.type) {
            case LOCKED_CHEST: return "Locked Chest";
            case CRYSTAL_CHEST: return "Crystal Chest";
            case CHEST: return "Chest";
            case SKELETON: return "Skeletal Remains";
            case REMAINS: return "Remains";
            case TOMB: return "Tomb";
            default:
                if (!h.isEmpty() && h.peek() != null) {
                    return h.peek().name();
                }
                return "Loot";
        }
    }
}