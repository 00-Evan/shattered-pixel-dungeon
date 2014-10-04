package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;

/**
 * Created by Evan on 30/09/2014.
 */
public class GooWarnParticle extends FlameParticle {

    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((GooWarnParticle)emitter.recycle( GooWarnParticle.class )).reset( x, y );
        }
    };

    @Override
    public void update() {
        super.update();

        float p = left / lifespan;
        color( 0x000000 );
        am = p > 0.5f ? (1.2f - p*0.8f) : (p*3 - 0.7f);
    }
}
