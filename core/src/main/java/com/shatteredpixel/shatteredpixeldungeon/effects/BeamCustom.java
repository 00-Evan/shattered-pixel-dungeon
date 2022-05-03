package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class BeamCustom extends Image {
    private static final double A = 180 / Math.PI;

    private float duration;

    private float timeLeft;

    private Callback callback;

    private float chargeTime = 0f;
    private float keepTime = 0f;
    private float fadeTime = 0.5f;

    public BeamCustom(PointF s, PointF e, Effects.Type asset, Callback callback){
        super( Effects.get( asset ) );

        origin.set( 0, height / 2 );

        x = s.x - origin.x;
        y = s.y - origin.y;

        float dx = e.x - s.x;
        float dy = e.y - s.y;
        angle = (float)(Math.atan2( dy, dx ) * A);
        scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / width;

        //Sample.INSTANCE.play( Assets.Sounds.RAY );

        timeLeft = this.duration = 0.5f;

        this.callback = callback;
    }

    public BeamCustom(PointF s, PointF e, Effects.Type asset) {
        this(s, e, asset, null);
    }

    public BeamCustom setDiameter(float diameterModifier){
        scale.y *= diameterModifier;
        return this;
    }

    public BeamCustom setColor(int color){
        hardlight(color);
        return this;
    }

    public BeamCustom setTime(float rise, float flat, float down){
        chargeTime = rise;
        keepTime = flat;
        fadeTime = down;
        timeLeft = rise + flat + down;
        updateImage();
        return this;
    }
    //avoid using both setLifespan and setTime.
    //Old interface, should abandon
    public BeamCustom setLifespan(float life){
        timeLeft = this.duration = life;
        fadeTime = life;
        chargeTime = keepTime = 0f;
        return this;
    }
/*
    public static class DeathRay extends Beam{
        public DeathRay(PointF s, PointF e){
            super(s, e, Effects.Type.DEATH_RAY, 0.5f);
        }
    }

    public static class LightRay extends Beam{
        public LightRay(PointF s, PointF e){
            super(s, e, Effects.Type.LIGHT_RAY, 1f);
        }
    }

    public static class HealthRay extends Beam {
        public HealthRay(PointF s, PointF e){
            super(s, e, Effects.Type.HEALTH_RAY, 0.75f);
        }
    }

 */

    @Override
    public void update() {
        super.update();

        updateImage();

        if ((timeLeft -= Game.elapsed) <= 0) {
            if(callback != null){
                callback.call();
            }
            killAndErase();
        }
    }

    protected void updateImage(){
        if(timeLeft > keepTime + fadeTime){
            float p = ( keepTime + fadeTime + chargeTime - timeLeft) / Math.max(chargeTime, Game.elapsed);
            alpha( p );
            scale.set( scale.x, p );
        }else if(timeLeft > fadeTime){
            alpha(1f);
            scale.set( scale.x, 1f );
        }else{
            float p = ( fadeTime - timeLeft) / Math.max(fadeTime, Game.elapsed);
            alpha( 1f-p );
            scale.set( scale.x, 1f-p );
        }
    }

    @Override
    public void draw() {
        Blending.setLightMode();
        super.draw();
        Blending.setNormalMode();
    }

}

