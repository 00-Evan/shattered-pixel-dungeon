package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MagicGirlDead;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class FireMagicGirlSprite extends MobSprite {

    private MovieClip.Animation charge;
    private MovieClip.Animation slam;

    private Emitter superchargeSparks;

    public FireMagicGirlSprite() {
        super();

        texture( Assets.Sprites.FRAS );

        updateChargeState(false);
    }

    public void updateChargeState( boolean enraged ){
        if (superchargeSparks != null) superchargeSparks.on = enraged;

        int c = 0;

        TextureFilm frames = new TextureFilm( texture, 12, 16 );

        idle = new MovieClip.Animation( 4, true );
        idle.frames( frames,c+5, c+4, c+3,2,1,0 );

        run = new MovieClip.Animation(  6, true );
        run.frames( frames, c+0, c+1 );

        attack = new MovieClip.Animation( 5, false );
        attack.frames( frames, c+3, c+4 );

        //unaffected by enrage state

        if (charge == null) {
            charge = new MovieClip.Animation(4, true);
            charge.frames(frames, 0, 10);

            slam = attack.clone();

            zap = new MovieClip.Animation(15, false);
            zap.frames(frames, 6, 7, 7, 6);

            die = new MovieClip.Animation(20, false);
            die.frames(frames, 3,4);
        }

        if (curAnim != charge) play(idle);
    }

    @Override
    public void play( MovieClip.Animation anim ) {
        if (anim == die) {
            emitter().burst( SnowParticle.FACTORY, 8 );
        }
        super.play( anim );
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.TOXIC_VENT,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((MagicGirlDead)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.GAS );
    }

    public void charge(){
        play( charge );
    }

    public void slam( int cell ){
        turnTo( ch.pos , cell );
        play( slam );
        Sample.INSTANCE.play( Assets.Sounds.ROCKS );
        Camera.main.shake( 3, 0.7f );
    }

    @Override
    public void onComplete( MovieClip.Animation anim ) {

        if (anim == zap || anim == slam){
            idle();
        }

        if (anim == slam){
            ((MagicGirlDead)ch).onSlamComplete();
        }

        super.onComplete( anim );

        if (anim == die) {
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            emitter().burst( BlastParticle.FACTORY, 100 );
            killAndErase();
        }
    }

    @Override
    public void place(int cell) {
        if (parent != null) parent.bringToFront(this);
        super.place(cell);
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        superchargeSparks = emitter();
        superchargeSparks.autoKill = false;
        superchargeSparks.pour(SparkParticle.STATIC, 0.05f);
        superchargeSparks.on = false;

        if (ch instanceof MagicGirlDead && ((MagicGirlDead) ch).isSupercharged()){
            updateChargeState(true);
        }
    }

    @Override
    public void update() {
        super.update();

        if (superchargeSparks != null){
            superchargeSparks.visible = visible;
        }
    }

    @Override
    public void die() {
        super.die();
        if (superchargeSparks != null){
            superchargeSparks.on = false;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (superchargeSparks != null){
            superchargeSparks.killAndErase();
        }
    }

    @Override
    public int blood() {
        return 0xFFFFFF88;
    }
}
