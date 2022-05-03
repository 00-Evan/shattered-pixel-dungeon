package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Visual;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class MissileSpriteCustom extends ItemSprite implements Tweener.Listener {

    private static final float SPEED	= 240f;

    private Callback callback;

    public void reset(int from, int to, Item item, float speedMod, float aSpeedMod, Callback listener) {
        reset(Dungeon.level.solid[from] ? DungeonTilemap.raisedTileCenterToWorld(from) : DungeonTilemap.raisedTileCenterToWorld(from),
                Dungeon.level.solid[to] ? DungeonTilemap.raisedTileCenterToWorld(to) : DungeonTilemap.raisedTileCenterToWorld(to),
                item, speedMod, aSpeedMod, listener);
    }

    public void reset(Visual from, int to, Item item, float speedMod, float aSpeedMod, Callback listener ) {
        reset(from.center(),
                Dungeon.level.solid[to] ? DungeonTilemap.raisedTileCenterToWorld(to) : DungeonTilemap.raisedTileCenterToWorld(to),
                item, speedMod, aSpeedMod, listener);
    }

    public void reset( int from, Visual to, Item item, float speedMod, float aSpeedMod, Callback listener ) {
        reset(Dungeon.level.solid[from] ? DungeonTilemap.raisedTileCenterToWorld(from) : DungeonTilemap.raisedTileCenterToWorld(from),
                to.center(),
                item, speedMod, aSpeedMod, listener);
    }

    public void reset( Visual from, Visual to, Item item, float speedMod, float aSpeedMod, Callback listener ) {
        reset(from.center(), to.center(), item, speedMod, aSpeedMod, listener);
    }

    public void reset(PointF from, PointF to, Item item, float speedMod, float aSpeedMod, Callback listener) {
        revive();

        if (item == null)   view(0, null);
        else                view( item );

        setup( from,
                to,
                item,
                speedMod,
                aSpeedMod,
                listener);
    }

    private static final int DEFAULT_ANGULAR_SPEED = 720;

    //TODO it might be nice to have a source and destination angle, to improve thrown weapon visuals
    private void setup( PointF from, PointF to, Item item, float speedMod, float aSpeedMod, Callback listener){

        originToCenter();

        //adjust points so they work with the center of the missile sprite, not the corner
        from.x -= width()/2;
        to.x -= width()/2;
        from.y -= height()/2;
        to.y -= height()/2;

        this.callback = listener;

        point( from );

        PointF d = PointF.diff( to, from );
        speed.set(d).normalize().scale(SPEED*speedMod);

        angularSpeed = DEFAULT_ANGULAR_SPEED * aSpeedMod;

        angle = 135 - (float)(Math.atan2( d.x, d.y ) / 3.1415926 * 180);

        if (d.x >= 0){
            flipHorizontal = false;
            updateFrame();

        } else {
            angularSpeed = -angularSpeed;
            angle += 90;
            flipHorizontal = true;
            updateFrame();
        }

        float speed = SPEED*speedMod;

        PosTweener tweener = new PosTweener( this, to, d.length() / speed );
        tweener.listener = this;
        parent.add( tweener );
    }

    @Override
    public void onComplete( Tweener tweener ) {
        kill();
        if (callback != null) {
            callback.call();
        }
    }
}

