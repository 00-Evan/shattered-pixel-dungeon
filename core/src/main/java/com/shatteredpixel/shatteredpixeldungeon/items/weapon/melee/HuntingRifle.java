/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ArcaneCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HuntingRifle extends MeleeWeapon {

    public static final String AC_SHOOT		= "SHOOT";

    {

        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.HUNTING_RIFLE;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 6;
    }


    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            actions.add( AC_SHOOT );
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {

            if (!isEquipped( hero )) {
                usesTargeting = false;
                GLog.w(Messages.get(this, "not_equipped"));
            } else {
                usesTargeting = true;
                curUser = hero;
                curItem = this;
                GameScene.selectCell( shooter );
            }
        }
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier-1, lvl); //18 base strength req
    }

    @Override
    public int min(int lvl) {
        return  tier +  //base
                lvl + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }


    @Override
    public int max(int lvl) {
        return  (tier+1) +
                lvl*(tier-3)+(tier-3)*RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return knockArrow().targetingPos(user, dst);
    }

    private int targetPos;

    @Override
    public int damageRoll(Char owner) {
        int damage = augment.damageFactor(super.damageRoll(owner));

        if (owner instanceof Hero) {
            int exStr = ((Hero)owner).STR() - STRReq();
            if (exStr > 0) {
                damage += Random.IntRange( 0, exStr );
            }
        }

        return damage;
    }

    @Override
    protected float baseDelay(Char owner) {
            return super.baseDelay(owner);
    }

    public HuntingRifle.Bullet knockArrow(){
        return new HuntingRifle.Bullet();
    }
    public class Bullet extends MissileWeapon {

        {
            image = ItemSpriteSheet.BULLET;

            hitSound = Assets.Sounds.PUFF;
        }

        @Override
        public int damageRoll(Char owner) {
            return 3*HuntingRifle.this.damageRoll(owner);
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return HuntingRifle.this.hasEnchant(type, owner);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            return HuntingRifle.this.proc(attacker, defender, damage);
        }

        @Override
        public float delayFactor(Char user) {
            return HuntingRifle.this.delayFactor(user);
        }

        @Override
        public int STRReq(int lvl) {
            return HuntingRifle.this.STRReq(lvl);
        }

        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );
            if (enemy == null || enemy == curUser) {
                parent = null;
                CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
            } else {
                if (!curUser.shoot( enemy, this )) {
                    CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                }
            }
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
        }

        @Override
        public void cast(final Hero user, final int dst) {
            final int cell = throwPos( user, dst );
            HuntingRifle.this.targetPos = cell;
                if (user.hasTalent(Talent.SEER_SHOT)
                        && user.buff(Talent.SeerShotCooldown.class) == null){
                    int shotPos = throwPos(user, dst);
                    if (Actor.findChar(shotPos) == null) {
                        RevealedArea a = Buff.affect(user, RevealedArea.class, 5 * user.pointsInTalent(Talent.SEER_SHOT));
                        a.depth = Dungeon.depth;
                        a.pos = shotPos;
                        Buff.affect(user, Talent.SeerShotCooldown.class, 20f);
                    }
                }

                super.cast(user, dst);
            }
        }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                knockArrow().cast(curUser, target);
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{Flashbang.class, Firebomb.class, Crossbow.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 40;

            output = HuntingRifle.class;
            outQuantity = 1;
        }

    }

}
