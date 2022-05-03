package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class RenSprite extends MobSprite {
    public RenSprite() {
        texture( Assets.Sprites.REN );
        TextureFilm var1 = new TextureFilm(this.texture, 12, 14);
        this.idle = new Animation(2, true);
        this.idle.frames(var1, new Object[]{0, 1, 2, 3});
        this.run = new Animation(10, true);
        this.run.frames(var1, new Object[]{0});
        this.die = new Animation(10, false);
        this.die.frames(var1, new Object[]{0});
        this.play(this.idle);
    }
}
