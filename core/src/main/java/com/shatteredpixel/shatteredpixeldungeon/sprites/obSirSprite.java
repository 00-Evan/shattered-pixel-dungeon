//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.PixelParticle;

public class obSirSprite extends MobSprite {
    private PixelParticle coin;

    public obSirSprite() {
        this.texture("Npcs/rt.png");
        TextureFilm var1 = new TextureFilm(this.texture, 16, 16);
        this.idle = new MovieClip.Animation(1, true);
        this.idle.frames(var1, new Object[]{0, 1,2,3});
        this.run = new MovieClip.Animation(10, true);
        this.run.frames(var1, new Object[]{0});
        this.die = new MovieClip.Animation(10, false);
        this.die.frames(var1, new Object[]{0});
        this.play(this.idle);
    }

    public void onComplete(Animation var1) {
        super.onComplete(var1);
        if (this.visible && var1 == this.idle) {
            if (this.coin == null) {
                this.coin = new PixelParticle();
                this.parent.add(this.coin);
            }

        }

    }
}
