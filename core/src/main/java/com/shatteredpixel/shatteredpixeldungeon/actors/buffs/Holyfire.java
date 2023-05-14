package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HolyFire;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Holyfire extends Buff {

    private float left;

    {
        type = buffType.POSITIVE;
    }

    public static int level = 1;
    private int interval = 1;

    @Override
    public boolean act( ) {
        if (target.isAlive() && !target.isImmune(getClass())) {

            int damage = Random.NormalIntRange( 1, 3 + Dungeon.scalingDepth()/4 );
            Buff.detach( target, Chill.class);
            target.damage( damage, this );

            spend( 1f );
               // GLog.w("每回合造成烧伤");


            spend(interval);
            if (level <= 0) {

                detach();
            }

        }   else {

            detach();
        }

        if (Dungeon.level.holyflamable[target.pos] && Blob.volumeAt(target.pos, HolyFire.class) == 0) {
            GameScene.add( Blob.seed( target.pos, 4, HolyFire.class ) );
        }

        spend( TICK );
        left -= TICK;

        if (left <= 0 ||
                (Dungeon.level.water[target.pos] && !target.flying)) {

            detach();
        }

        Emitter emitter = hero.sprite.centerEmitter();
        emitter.start( FlameParticle.FACTORY, 5f, 3 );


        return true;
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        //decide whether to override, preferring high value + low interval
        if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (level - left) / level);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
    }


    @Override
    public int icon() {
        return BuffIndicator.FIRE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight( 0xa0d0ff );
    }



}



