// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class DM75Sprite extends MobSprite
{

    public DM75Sprite()
    {
        texture("mobs/dm75.png");
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

    public void onComplete(com.watabou.noosa.MovieClip.Animation animation)
    {
        super.onComplete(animation);
        if(animation == die)
            emitter().burst(Speck.factory(7), 15);
    }
}
