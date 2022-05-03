//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.State;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.MovieClip.Animation;

public class FireGhostSprite extends MobSprite {
    public FireGhostSprite() {
        this.texture("sprites/wengi.png");
        TextureFilm var1 = new TextureFilm(this.texture, 14, 15);
        Integer var2 = 1;
        this.idle = new Animation(5, true);
        Animation var3 = this.idle;
        Integer var4 = 0;
        var3.frames(var1, new Object[]{var4, var2});
        this.run = new Animation(10, true);
        this.run.frames(var1, new Object[]{var4, var2});
        this.attack = new Animation(10, false);
        this.attack.frames(var1, new Object[]{var4, 2, 3});
        this.die = new Animation(8, false);
        this.die.frames(var1, new Object[]{var4, 4, 5, 6, 7});
        this.play(this.idle);
    }

    public int blood() {
        return -33517;
    }

    public void die() {
        super.die();
        this.remove(State.BURNING);
    }

    public void link(Char var1) {
        super.link(var1);
        this.add(State.BURNING);
    }
}
