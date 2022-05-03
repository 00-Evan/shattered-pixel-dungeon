//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.MovieClip.Animation;

public class OGPDNQHZTT extends MobSprite {
    public OGPDNQHZTT() {
        this.texture("sprites/rat.png");
        TextureFilm var1 = new TextureFilm(this.texture, 16, 15);
        this.idle = new Animation(2, true);
        Animation var2 = this.idle;
        Integer var3 = 48;
        var2.frames(var1, new Object[]{var3, var3, var3, 49});
        this.run = new Animation(10, true);
        this.run.frames(var1, new Object[]{54, 55, 56, 57, 58});
        this.attack = new Animation(15, false);
        this.attack.frames(var1, new Object[]{50, 51, 52, 53, var3});
        this.die = new Animation(10, false);
        this.die.frames(var1, new Object[]{59, 60, 61, 62});
        this.play(this.idle);
    }
}
