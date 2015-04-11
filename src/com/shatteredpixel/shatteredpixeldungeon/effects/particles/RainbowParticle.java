package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

/**
 * Created by Evan on 11/04/2015.
 */
public class RainbowParticle extends PixelParticle {

    public static final Emitter.Factory BURST = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((RainbowParticle)emitter.recycle( RainbowParticle.class )).resetBurst( x, y );
        }
        @Override
        public boolean lightMode() {
            return true;
        }
    };


    public RainbowParticle() {
        super();
        color( Random.Int( 0x1000000 ) );
        lifespan = 0.5f;
    }


    public void reset( float x, float y ) {
        revive();

        this.x = x;
        this.y = y;

        speed.set( Random.Float(-5, +5), Random.Float( -5, +5 ) );

        left = lifespan;
    }

    public void resetBurst( float x, float y ) {
        revive();

        this.x = x;
        this.y = y;

        speed.polar( Random.Float( PointF.PI2 ), Random.Float( 16, 32 ) );

        left = lifespan;
    }

    @Override
    public void update() {
        super.update();
        // alpha: 1 -> 0; size: 1 -> 5
        size( 5 - (am = left / lifespan) * 4 );
    }
}
