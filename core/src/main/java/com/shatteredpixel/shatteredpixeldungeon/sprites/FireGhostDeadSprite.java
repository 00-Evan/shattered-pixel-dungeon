//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FireGhostDead;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class FireGhostDeadSprite extends MobSprite {
    public FireGhostDeadSprite() {
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

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.FIRE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((FireGhostDead)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

}
