package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class NinjaRatSprite extends MobSprite {

    public NinjaRatSprite() {
        super();
        tint( 0.098f, 0.098f, 0.50f ,0.6f);//深蓝色
        texture( Assets.Sprites.RAT );

        TextureFilm frames = new TextureFilm( texture, 16, 15 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 6, 7, 8, 9, 10 );

        attack = new Animation( 15, false );
        attack.frames( frames, 2, 3, 4, 5, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );


        zap = attack.clone();

        die = new Animation( 8, false );
        die.frames( frames, 8, 9, 10, 10, 10, 10, 10, 10 );

        play( idle );
    }


    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent( cell, ch.pos )) {

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cell, new NinjaRatShuriken(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );

            zap( ch.pos );

        } else {

            super.attack( cell );

        }
    }

    @Override
    public void resetColor(){
        super.resetColor( );
        tint(0.098f, 0.098f, 0.50f ,0.6f);
    }

public static class NinjaRatShuriken extends Item {
    {
        image = ItemSpriteSheet.SHURIKEN;
    }
    }

}