//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.MovieClip.Animation;

public class OGPDZSLSTT extends MobSprite {
    public OGPDZSLSTT() {
        this.texture("sprites/rat.png");
        TextureFilm var1 = new TextureFilm(this.texture, 16, 15);
        this.idle = new Animation(2, true);
        Animation var2 = this.idle;
        Integer var3 = 80;
        var2.frames(var1, new Object[]{var3, var3, var3, 81});
        this.run = new Animation(10, true);
        this.run.frames(var1, new Object[]{86, 87, 88, 89, 90});
        this.attack = new Animation(15, false);
        this.attack.frames(var1, new Object[]{82, 83, 84, 85, var3});
        this.die = new Animation(10, false);
        this.die.frames(var1, new Object[]{91, 92, 93, 94});
        this.play(this.idle);
    }
}
