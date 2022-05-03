//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.MovieClip.Animation;

public class OGPDLLSTT extends MobSprite {
    public OGPDLLSTT() {
        this.texture("sprites/rat.png");
        TextureFilm var1 = new TextureFilm(this.texture, 16, 15);
        this.idle = new Animation(2, true);
        Animation var2 = this.idle;
        Integer var3 = 64;
        var2.frames(var1, new Object[]{var3, var3, var3, 65});
        this.run = new Animation(10, true);
        this.run.frames(var1, new Object[]{70, 71, 72, 73, 74});
        this.attack = new Animation(15, false);
        this.attack.frames(var1, new Object[]{66, 67, 68, 69, var3});
        this.die = new Animation(10, false);
        this.die.frames(var1, new Object[]{75, 76, 77, 78});
        this.play(this.idle);
    }
}
