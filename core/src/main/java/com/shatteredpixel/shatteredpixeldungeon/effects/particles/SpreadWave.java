package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class SpreadWave extends Image {

    private float totalTime = 0.5f;
    private float r = 1f;
    private int c = 0xAAAAAA;
    private Callback callback;

    private float time;

    public SpreadWave(){
        super(Effects.get(Effects.Type.RIPPLE));
        origin.set(width / 2, height / 2);
    }

    public SpreadWave set(float radius, float time, int color, Callback cb){
        r = radius;
        totalTime = time;
        c = color;
        callback = cb;
        this.time = time;
        scale.set(0);
        return this;
    }

    public SpreadWave setPos(PointF center){
        x = center.x - width/2f;
        y = center.y - height/2f;
        return this;
    }


    public SpreadWave reset(PointF center, float radius, float time, int color, Callback cb) {
        revive();

        setPos(center);

        set(radius, time, color, cb);

        return this;
    }

    @Override
    public void update() {
        super.update();

        if ((time -= Game.elapsed) <= 0) {
            if(callback != null) callback.call();
            kill();
        } else {
            float p = time / totalTime;
            //alpha(0.9f);
            hardlight(c);
            scale.y = scale.x = (1-p)*r;
        }
    }

    public static void blast(int pos, float radius, float time, int color, Callback callback){
        PointF p = new PointF((pos % Dungeon.level.width()) * DungeonTilemap.SIZE + DungeonTilemap.SIZE / 2f,
                (pos / Dungeon.level.width()) * DungeonTilemap.SIZE + DungeonTilemap.SIZE / 2f);
        blast(p, radius, time, color, callback);
    }

    public static void blast(PointF p, float radius, float time, int color, Callback callback) {
        Group parent = Dungeon.hero.sprite.parent;
        SpreadWave b = (SpreadWave)parent.recycle(SpreadWave.class);
        parent.bringToFront(b);
        b.reset(p, radius, time, color, callback);
    }
}