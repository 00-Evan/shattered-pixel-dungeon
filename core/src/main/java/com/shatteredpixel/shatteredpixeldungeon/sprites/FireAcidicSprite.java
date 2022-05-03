package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;

public class FireAcidicSprite extends IceFireScorpioSprite {

    public FireAcidicSprite() {
        super();

        texture( Assets.Sprites.IFE_SCORPIO );

        TextureFilm frames = new TextureFilm( texture, 18, 17 );

        idle = new Animation( 12, true );
        idle.frames( frames, 14, 14, 14, 14, 14, 14, 14, 14, 15, 16, 15, 16, 15, 16 );

        run = new Animation( 4, true );
        run.frames( frames, 19, 20 );

        attack = new Animation( 15, false );
        attack.frames( frames, 14, 17, 18 );

        zap = attack.clone();

        die = new Animation( 12, false );
        die.frames( frames, 14, 21, 22, 23, 24 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFF66FF22;
    }

    public class IceScorpioShot extends Item {
        {
            image = ItemSpriteSheet.FIRE_BOMB;
        }
    }
}
