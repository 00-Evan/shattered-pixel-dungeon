//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.MovieClip.Animation;
import com.watabou.utils.GameMath;

public class LtSprites extends MobSprite {
    public LtSprites() {
        this.texture("Boss/SLMKING2.png");
        TextureFilm var1 = new TextureFilm(this.texture, 20, 14);
        Integer var2 = 2;
        Integer var3 = 1;
        this.idle = new Animation(2, true);
        Animation var4 = this.idle;
        Integer var5 = 0;
        var4.frames(var1, new Object[]{var5, var5, var5, var3, var5, var5, var3, var3});
        this.run = new Animation(12, true);
        this.run.frames(var1, new Object[]{var2, 3, 2, 1, 2, var2});
        this.attack = new Animation(12, false);
        this.attack.frames(var1, new Object[]{8, 9, 10});
        this.die = new Animation(12, false);
        this.die.frames(var1, new Object[]{5, 6, 7,});
        this.play(this.idle);
    }

    private static int[] tierFrames = {0, 21, 32, 43, 54, 65};

    public void setArmor( int tier ){
        int c = tierFrames[(int) GameMath.gate(0, tier, 5)];

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle.frames( frames, 0+c, 0+c, 0+c, 0+c, 0+c, 1+c, 1+c );
        run.frames( frames, 2+c, 3+c, 4+c, 5+c, 6+c, 7+c );
        attack.frames( frames, 8+c, 9+c, 10+c );
        //death animation is always armorless

        play( idle, true );

    }

    @Override
    public int blood() {
        return 0xFFcdcdb7;
    }
}


