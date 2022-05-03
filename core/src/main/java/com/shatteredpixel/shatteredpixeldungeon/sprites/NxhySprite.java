//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.PixelParticle;

public class NxhySprite extends MobSprite {
    private PixelParticle coin;

    public NxhySprite() {
        this.texture("Npcs/Nxhy.png");
        TextureFilm var1 = new TextureFilm(this.texture, 14, 14);
        Integer var2 = 1;
        this.idle = new Animation(5, true);
        Animation var3 = this.idle;
        Integer var4 = 0;
        var3.frames(var1, new Object[]{var2, var2, var2, var2, var2, var4, var4, var4, var4});
        this.die = new Animation(20, false);
        this.die.frames(var1, new Object[]{var4});
        this.run = this.idle.clone();
        this.attack = this.idle.clone();
        this.idle();
    }

    public void onComplete(Animation var1) {
        super.onComplete(var1);
        if (this.visible && var1 == this.idle) {
            if (this.coin == null) {
                this.coin = new PixelParticle();
                this.parent.add(this.coin);
            }

            PixelParticle var4 = this.coin;
            float var2 = this.x;
            byte var3;
            if (this.flipHorizontal) {
                var3 = 0;
            } else {
                var3 = 13;
            }

            var4.reset(var2 + (float)var3, this.y + 7.0F, 0x00ffff, 1.0F, 0.5F);
            this.coin.speed.y = -40.0F;
            this.coin.acc.y = 160.0F;
        }

    }
}
