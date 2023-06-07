package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HolyFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HolyFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.VineArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Holyfire extends Buff implements Hero.Doom{

    private static final float DURATION = 5f;

    private float left;

    //for tracking burning of hero items
    private int burnIncrement = 0;

    private static final String LEFT	= "left";
    private static final String BURN	= "burnIncrement";

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );
        bundle.put( BURN, burnIncrement );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat( LEFT );
        burnIncrement = bundle.getInt( BURN );
    }

    @Override
    public boolean attachTo(Char target) {
        Buff.detach( target, Chill.class);

        return super.attachTo(target);
    }

    @Override
    public boolean act() {

        if (target.isAlive() && !target.isImmune(getClass())) {

            int damage = Random.NormalIntRange( 3, 5 + Dungeon.scalingDepth()/3 );
            Buff.detach( target, Chill.class);

            if (target instanceof Hero && target.buff(TimekeepersHourglass.timeStasis.class) == null) {

                Hero hero = (Hero)target;

                hero.damage( damage, this );
                burnIncrement++;

            } else {
                target.damage( damage, this );
            }


        } else {

            detach();
        }

        if ( Dungeon.level.flamable[target.pos] && Blob.volumeAt(target.pos, HolyFire.class ) == 0) {
            GameScene.add( Blob.seed( target.pos, 4, HolyFire.class ) );}

        Emitter emitter =  target.sprite.emitter();
        emitter.start( HolyFlameParticle.FACTORY, 0.03f, 1 );

        spend( TICK );

        left -= TICK;



        return true;
    }

    public void reignite( Char ch ) {
        reignite( ch, DURATION );
    }

    public void reignite( Char ch, float duration ) {
        if (ch.isImmune(Holyfire.class)){
            //TODO this only works for the hero, not others who can have brimstone+arcana effect
            // e.g. prismatic image, shadow clone
            if (ch instanceof Hero
                    && ((Hero) ch).belongings.armor() != null
                    && ((Hero) ch).belongings.armor().hasGlyph(Brimstone.class, ch)){
                //has a 2*boost/50% chance to generate 1 shield per turn, to a max of 4x boost
                float shieldChance = 2*(Armor.Glyph.genericProcChanceMultiplier(ch) - 1f);
                int shieldCap = Math.round(shieldChance*4f);
                if (shieldCap > 0 && Random.Float() < shieldChance){
                    Barrier barrier = Buff.affect(ch, Barrier.class);
                    if (barrier.shielding() < shieldCap){
                        barrier.incShield(1);
                    }
                }
            }
        }
        left = duration;
    }

    @Override
    public int icon() {
        return BuffIndicator.FIRE;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - left) / DURATION);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int)left);
    }

   /* @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.BURNING);
        else target.sprite.remove(CharSprite.State.BURNING);
    }*/

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    @Override
    public void onDeath() {

        Badges.validateDeathFromFire();

        Dungeon.fail( getClass() );
        GLog.n( Messages.get(this, "ondeath") );
    }

/*

    public static final float DURATION = 5f;
    public static int level = 1;
    private int interval = 1;


    @Override
    public boolean act( ) {
        if (target.isAlive() && !target.isImmune(getClass())  ) {

            int damage = Random.NormalIntRange( 1, 3 + Dungeon.scalingDepth()/4 );
            if(Dungeon.hero.buff(Holyfire.class) != null ){

                damage = Math.min( hero.HP, Random.NormalIntRange( 1, 3 + Dungeon.scalingDepth()/4) );

                hero.HP -= damage;

                spend( 1f );
            }

        }   else {

            detach();
        }

        spend(interval);

        if (Dungeon.level.holyflamable[target.pos] && Blob.volumeAt(target.pos, HolyFire.class) == 0) {
            GameScene.add( Blob.seed( target.pos, 1, HolyFire.class ) );
        }

        Emitter emitter = hero.sprite.centerEmitter();
        emitter.start( HolyFlameParticle.FACTORY, 0.05f, 4 );


        return true;
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        //decide whether to override, preferring high value + low interval
        if ( Math.sqrt(interval)*level <= Math.sqrt(time)*value ) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public float iconFadePercent() {
        if (target instanceof Hero) {
            float max = ((Hero) target).lvl;
            return Math.max(0, (max - level) / max);
        }else return 1;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight( 0xa0d0ff );
    }

*/
}



