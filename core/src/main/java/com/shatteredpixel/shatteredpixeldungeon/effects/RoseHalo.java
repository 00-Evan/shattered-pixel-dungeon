package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Halo;
import com.watabou.utils.PointF;

public class RoseHalo extends Halo {

    private CharSprite target;

    private float phase;

    public RoseHalo(CharSprite sprite ) {

        //rectangular sprite to circular radius. Pythagorean theorem
        super( (float)Math.sqrt(Math.pow(sprite.width()/2f, 2) + Math.pow(sprite.height()/2f, 2)),0xff00ff, 1.5f );

        am = -0.22f;
        aa = +0.22f;

        target = sprite;

        phase = 1;
    }

    @Override
    public void update() {
        super.update();

        if (phase < 1) {
            if ((phase -= Game.elapsed) <= 0) {
                killAndErase();
            } else {
                scale.set( (2 - phase) * radius / RADIUS );
                am = phase * (-1);
                aa = phase * (+1);
            }
        }

        if (visible = target.visible) {
            PointF p = target.center();
            point( p.x, p.y );
        }
    }

    @Override
    public void draw() {
        Blending.setLightMode();
        super.draw();
        Blending.setNormalMode();
    }

    public void putOut() {
        phase = 0.999f;
    }

}