package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

/**
 * Created by Evan on 09/10/2014.
 */
public class GreatCrabSprite extends MobSprite {

    public GreatCrabSprite() {
        super();

        texture( Assets.CRAB );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 5, true );
        idle.frames( frames, 16, 17, 16, 18 );

        run = new MovieClip.Animation( 15, true );
        run.frames( frames, 19, 20, 21, 22 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 23, 24, 25 );

        die = new MovieClip.Animation( 12, false );
        die.frames( frames, 26, 27, 28, 29 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFFFFEA80;
    }
}
