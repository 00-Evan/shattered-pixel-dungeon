package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;

public class IceFishSword extends MeleeWeapon{


    {
        image = ItemSpriteSheet.ICEFISHSWORD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;
        tier = 6;
        ACC = 2.90f; //20% boost to accuracy
        DLY = 0.7f; //2x speed
    }

    public void bolt(Mob mob){
        if (mob.buff(Burning.class) != null){

            final Ballistica shot = new Ballistica( curUser.pos, Ballistica.IGNORE_SOFT_SOLID);
            Buff.affect( mob, Burning.class ).reignite( mob, 7f );
            fx(shot, () -> onHit(shot, mob));
        }
    }

    protected void onHit(Ballistica bolt, Mob mob) {

        //presses all tiles in the AOE first

        if (mob != null){

            if (mob.isAlive() && bolt.path.size() > bolt.dist+1) {
                Buff.prolong(mob, Chill.class, Chill.DURATION/2f);
            }
        }

    }

    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent, MagicMissile.WIND, curUser.sprite, bolt.collisionPos, callback);
    }

    public int proc(Char attacker, Char defender, int damage) {
        if(attacker instanceof Hero){
        for(Mob mob : ((Hero) attacker).visibleEnemiesList()){
            bolt(mob);
        }
    }

        return super.proc(attacker, defender, damage);
    }
}
