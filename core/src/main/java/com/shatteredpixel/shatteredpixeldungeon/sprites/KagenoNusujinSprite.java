//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.MovieClip.Animation;

public class KagenoNusujinSprite extends MobSprite {
    public KagenoNusujinSprite() {
        this.texture("sprites/zei.png");
        TextureFilm var1 = new TextureFilm(this.texture, 12, 13);
        Integer var2 = 1;
        this.idle = new Animation(1, true);
        Animation var3 = this.idle;
        Integer var4 = 0;
        Integer var5 = 3;
        var3.frames(var1, new Object[]{var4, var4, var4, var2, var4, var4, var4, var4, var2});
        this.run = new Animation(15, true);
        this.run.frames(var1, new Object[]{var4, var4, 2, var5, var5, 4});
        this.die = new Animation(10, false);
        this.die.frames(var1, new Object[]{5, 6, 7, 8, 9});
        this.attack = new Animation(12, false);
        this.attack.frames(var1, new Object[]{10, 11, 12, var4});
        this.idle();
    }
}
