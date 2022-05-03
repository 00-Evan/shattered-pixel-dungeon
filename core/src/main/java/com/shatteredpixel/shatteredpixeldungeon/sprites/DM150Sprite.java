// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class DM150Sprite extends MobSprite
{

    public DM150Sprite()
    {
        texture("mobs/dm150.png");
        TextureFilm var1 = new TextureFilm(this.texture, 22, 20);
        Integer var2 = 2;
        Integer var3 = 1;
        this.idle = new Animation(10, true);
        Animation var4 = this.idle;
        Integer var5 = 0;
        var4.frames(var1, new Object[]{var5, var5, var5, var3, var5, var5, var3, var3});
        this.run = new Animation(12, true);
        this.run.frames(var1, new Object[]{var2, 3, 2, 1, 2, var2});
        this.attack = new Animation(12, false);
        this.attack.frames(var1, new Object[]{7, 8, 9});
        this.die = new Animation(12, false);
        this.die.frames(var1, new Object[]{5, 6, 7,});
        this.play(this.idle);
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
