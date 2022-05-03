package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class HaloBlazing extends Weapon.Enchantment {

    private static ItemSprite.Glowing SKYBLUE = new ItemSprite.Glowing( 0x00FFFF );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {
        int level = Math.max( 0, weapon.buffedLvl() );

        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%
        float procChance = (level+1f)/(level+3f) * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {

            if (defender.buff(HalomethaneBurning.class) != null){
                Buff.affect(defender, HalomethaneBurning.class).reignite(defender, 8f);
                Buff.affect(defender, Poison.class).set(4f);
                int burnDamage = Random.NormalIntRange( 1, 3 + Dungeon.depth/4 );
                defender.damage( Math.round(burnDamage * 0.67f), this );
            } else {
                Buff.affect(defender, HalomethaneBurning.class).reignite(defender, 8f);
            }

            defender.sprite.emitter().burst( FlameParticle.FACTORY, level + 1 );

        }

        return damage;

    }

    @Override
    public ItemSprite.Glowing glowing() {
        return SKYBLUE;
    }
}

