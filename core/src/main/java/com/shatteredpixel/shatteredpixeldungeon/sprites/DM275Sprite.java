package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DM275Sprite extends MobSprite {

    public DM275Sprite() {
        super();

        texture( Assets.Sprites.DM275 );

        TextureFilm frames = new TextureFilm( texture, 22, 20 );

        idle = new Animation( 10, true );
        idle.frames( frames, 0,1, 2 );

        run = new Animation( 12, true );
        run.frames( frames, 3, 4, 5, 6 );

        attack = new Animation( 12, false );
        attack.frames( frames, 6,7 );

        zap = new Animation( 8, false );
        zap.frames( frames, 6,7 );

        die = new Animation( 12, false );
        die.frames( frames, 8,9 );

        play( idle );
    }
}
