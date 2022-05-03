package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class BlueNecromancerSprite extends MobSprite {

    private Animation charging;
    private Emitter summoningBones;

    public BlueNecromancerSprite(){
        super();

        texture( Assets.Sprites.NECROBLUE );
        TextureFilm film = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 1, true );
        idle.frames( film, 0, 0, 0, 1, 0, 0, 0, 0, 1 );

        run = new Animation( 8, true );
        run.frames( film, 0, 0, 0, 2, 3, 4 );

        zap = new Animation( 10, false );
        zap.frames( film, 5, 6, 7, 8 );

        charging = new Animation( 5, true );
        charging.frames( film, 7, 8 );

        die = new Animation( 10, false );
        die.frames( film, 9, 10, 11, 12 );

        attack = zap.clone();

        idle();
    }
}

