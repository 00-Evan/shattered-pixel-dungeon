//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;

public class SlylSprite extends MobSprite {
    public SlylSprite() {
        this.texture("Rb.png");
        TextureFilm var1 = new TextureFilm(this.texture, 16, 16);
        this.idle = new Animation(1, true);
        this.idle.frames(var1, new Object[]{0, 0, 0, 1, 0, 0, 0, 0, 1});
        this.idle();
    }
}
