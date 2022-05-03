package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class WhiteGirlSprites extends MobSprite {
    public WhiteGirlSprites() {
    texture( Assets.Sprites.WHITE );
    TextureFilm var1 = new TextureFilm(this.texture, 16, 16);
    this.idle = new MovieClip.Animation(1, true);
    this.idle.frames(var1, new Object[]{0, 1});
    this.run = new MovieClip.Animation(10, true);
    this.run.frames(var1, new Object[]{0});
    this.die = new MovieClip.Animation(10, false);
    this.die.frames(var1, new Object[]{0});
    this.play(this.idle);
    }
}

