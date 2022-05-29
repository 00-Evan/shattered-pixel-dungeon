package com.shatteredpixel.shatteredpixeldungeon.items.stones.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StoneOfTrapFinding extends Runestone {
    private static final int DIST = 8;

    {
        image = ItemSpriteSheet.STONE_TRAPS;

        //so traps do not activate before the effect
        pressesCell = false;
    }

    @Override
    protected void activate(final int cell) {
        boolean[] FOV = new boolean[Dungeon.level.length()];
        Point c = Dungeon.level.cellToPoint(cell);
        ShadowCaster.castShadow(c.x, c.y, FOV, Dungeon.level.losBlocking, DIST);

        int sX = Math.max(0, c.x - DIST);
        int eX = Math.min(Dungeon.level.width()-1, c.x + DIST);

        int sY = Math.max(0, c.y - DIST);
        int eY = Math.min(Dungeon.level.height()-1, c.y + DIST);

        ArrayList<Trap> disarmCandidates = new ArrayList<>();

        for (int y = sY; y <= eY; y++){
            int curr = y*Dungeon.level.width() + sX;
            for ( int x = sX; x <= eX; x++){

                if (FOV[curr]){

                    Trap t = Dungeon.level.traps.get(curr);
                    if (t != null && t.active){
                        disarmCandidates.add(t);
                    }

                }
                curr++;
            }
        }

        Collections.sort(disarmCandidates, new Comparator<Trap>() {
            @Override
            public int compare(Trap o1, Trap o2) {
                float diff = Dungeon.level.trueDistance(cell, o1.pos) - Dungeon.level.trueDistance(cell, o2.pos);
                if (diff < 0){
                    return -1;
                } else if (diff == 0){
                    return Random.Int(2) == 0 ? -1 : 1;
                } else {
                    return 1;
                }
            }
        });

        //disarms at most nine traps
        while (disarmCandidates.size() > 25){
            disarmCandidates.remove(25);
        }

        for ( Trap t : disarmCandidates){
            t.reveal();
            CellEmitter.get(t.pos).burst(Speck.factory(Speck.STEAM), 6);
        }

        Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
    }
}
