// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DM300SpiderSprite extends MobSprite
{

    public DM300SpiderSprite()
    {
        texture("mobs/dm300spidermode.png");
        TextureFilm texturefilm = new TextureFilm(texture, 22, 20);
        idle = new com.watabou.noosa.MovieClip.Animation(10, true);
        idle.frames(texturefilm, new Object[] {
            Integer.valueOf(0), Integer.valueOf(1)
        });
        run = new com.watabou.noosa.MovieClip.Animation(10, true);
        run.frames(texturefilm, new Object[] {
            Integer.valueOf(2), Integer.valueOf(3)
        });
        attack = new com.watabou.noosa.MovieClip.Animation(15, false);
        attack.frames(texturefilm, new Object[] {
            Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6)
        });
        die = new com.watabou.noosa.MovieClip.Animation(20, false);
        die.frames(texturefilm, new Object[] {
            Integer.valueOf(0), Integer.valueOf(7), Integer.valueOf(0), Integer.valueOf(7), Integer.valueOf(0), Integer.valueOf(7), Integer.valueOf(0), Integer.valueOf(7), Integer.valueOf(0), Integer.valueOf(7), 
            Integer.valueOf(0), Integer.valueOf(7), Integer.valueOf(8)
        });
        play(idle);
    }

    public int blood()
    {
        return -120;
    }

    public void onComplete(Animation animation,Animation anim)
    {
        super.onComplete(animation);
        if (anim == zap) {
            play( run );
        }
        if(animation == die)
            emitter().burst(Speck.factory(7), 15);
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (parent != null) {
            parent.sendToBack(this);
            if (aura != null){
                parent.sendToBack(aura);
            }
        }
        renderShadow = false;
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.MAGIC_MISSILE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Spinner)ch).shootWeb();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.MISS );
    }
}
