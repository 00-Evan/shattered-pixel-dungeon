package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;

/**
 * Created by Evan on 20/04/2015.
 */
public class Surprise extends Image {

    private static final float TIME_TO_FADE = 0.8f;

    private float time;

    public Surprise() {
        super(Effects.get(Effects.Type.EXCLAMATION));
        origin.set(width / 2, height / 2);
    }

    public void reset(int p) {
        revive();

        x = (p % Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
        y = (p / Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

        time = TIME_TO_FADE;
    }

    @Override
    public void update() {
        super.update();

        if ((time -= Game.elapsed) <= 0) {
            kill();
        } else {
            float p = time / TIME_TO_FADE;
            alpha(p);
            scale.y = 1 + p/2;
        }
    }

    public static void hit(Char ch) {
        hit(ch, 0);
    }

    public static void hit(Char ch, float angle) {
        if (ch.sprite.parent != null) {
            Surprise s = (Surprise) ch.sprite.parent.recycle(Surprise.class);
            ch.sprite.parent.bringToFront(s);
            s.reset(ch.pos);
            s.angle = angle;
        }
    }

    public static void hit(int pos) {
        hit(pos, 0);
    }

    public static void hit(int pos, float angle) {
        Group parent = Dungeon.hero.sprite.parent;
        Wound w = (Wound) parent.recycle(Wound.class);
        parent.bringToFront(w);
        w.reset(pos);
        w.angle = angle;
    }
}
