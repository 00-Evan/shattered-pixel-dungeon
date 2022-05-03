package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class EndingBlade extends MeleeWeapon{
    {
        image = ItemSpriteSheet.ENDDIED;
        tier = 4;
        DLY = 2f;
    }

    @Override
    public int STRReq(int C) {
        return 25;
    }//力量需求

    public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {

        int enemyHealth = defender.HP - damage;
        if (enemyHealth <= 0) return damage;

        float maxChance = 0.5f;
        float chance = maxChance;

        if (Random.Float() < chance) {

            defender.damage( defender.HP, this );
            defender.sprite.emitter().burst( ShadowParticle.UP, 5 );

            if (!defender.isAlive() && attacker instanceof Hero
                    //this prevents unstable from triggering grim achievement
                    && weapon.hasEnchant(Grim.class, attacker)) {
                Badges.validateGrimWeapon();
            }

        }

        return damage;
    }

    @Override
    public String statsInfo(){
        return Messages.get(EndingBlade.class,"stats_desc");
    }//添加新文本

}
