package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Shadows;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public class FoliageMob extends Blob {

    @Override
    protected void evolve() {

        int[] map = Dungeon.level.map;

        boolean visisble = false;

        int cell;
        for (int i = area.left; i < area.right; i++) {
            for (int j = area.top; j < area.bottom; j++) {
                cell = i + j*Dungeon.level.width();
                if (cur[cell] > 0) {

                    off[cell] = cur[cell];
                    volume += off[cell];

                    if (map[cell] == Terrain.EMBERS) {
                        map[cell] = Terrain.GRASS;
                        GameScene.updateMap(cell);
                    }

                    visisble = visisble || Dungeon.level.heroFOV[cell];

                } else {
                    off[cell] = 0;
                }
            }
        }

        Hero hero = Dungeon.hero;
        if (hero.isAlive() && cur[hero.pos] > 0) {
            Shadows s = Buff.affect( hero, Shadows.class );
            if (s != null){
                s.prolong();
            }
        }

        if (visisble) {
            Notes.add( Notes.Landmark.SHOP2 );
        }

    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.start( ShaftParticle.FACTORY, 0.9f, 0 );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}

