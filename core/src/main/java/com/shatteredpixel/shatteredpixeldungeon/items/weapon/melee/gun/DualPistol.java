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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfReload;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DualPistol extends GunsWeapon {

    public static final String AC_SHOOT		= "SHOOT";
    public static final String AC_RELOAD = "RELOAD";

    private int max_round;
    private int round;
    private float reload_time;
    private static final String TXT_STATUS = "%d/%d";

    {

        defaultAction = AC_SHOOT;
        usesTargeting = true;

        image = ItemSpriteSheet.DUAL_PISTOL;                                  //if you make something different guns, you should change this
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 2;                                                               //if you make something different guns, you should change this
    }

    private static final String ROUND = "round";
    private static final String MAX_ROUND = "max_round";
    private static final String RELOAD_TIME = "reload_time";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAX_ROUND, max_round);
        bundle.put(ROUND, round);
        bundle.put(RELOAD_TIME, reload_time);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        max_round = bundle.getInt(MAX_ROUND);
        round = bundle.getInt(ROUND);
        reload_time = bundle.getFloat(RELOAD_TIME);
    }




    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            actions.add(AC_SHOOT);
            actions.add(AC_RELOAD);
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
                if (round <= 0) {
                    reload();
                } else {
                    usesTargeting = true;
                    curUser = hero;
                    curItem = this;
                    GameScene.selectCell(shooter);
                }
            }
        }
        if (action.equals(AC_RELOAD)) {
            max_round = 8;                                                                  //if you make something different guns, you should change this
            if (round == max_round){
                GLog.w(Messages.get(this, "already_loaded"));
            }
            reload();
        }
    }

    public void reload() {
        max_round = 8;                                                                      //if you make something different guns, you should change this
        curUser.spend(reload_time);
        curUser.busy();
        Sample.INSTANCE.play(Assets.Sounds.UNLOCK, 2, 1.1f);
        curUser.sprite.operate(curUser.pos);
        round = Math.max(max_round, round);

        GLog.i(Messages.get(this, "reloading"));

        updateQuickslot();
    }


    public int getRound() { return this.round; }

    @Override
    public String status() {
        max_round = 8;                                                                      //if you make something different guns, you should change this
        return Messages.format(TXT_STATUS, round, max_round);
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier, lvl); //18 base strength req, Changeable
    }

    public int min(int lvl) {
        return tier +                                                                      //if you make something different guns, you should change this
               lvl;                                                                        //if you make something different guns, you should change this
    }

    public int max(int lvl) {
        return 3 * (tier + 1) +                                                            //if you make something different guns, you should change this
               lvl;                                                           //if you make something different guns, you should change this
    }

    public int Bulletmin(int lvl) {
        return tier +                                                                  //if you make something different guns, you should change this
               lvl +                                                                  //if you make something different guns, you should change this
               RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    public int Bulletmax(int lvl) {
        return 2 * (tier)   +                                                           //if you make something different guns, you should change this
               lvl * (tier) +                                                           //if you make something different guns, you should change this
               RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public String info() {

        max_round = 8;                                                                       //if you make something different guns, you should change this
        reload_time = 2f* RingOfReload.reloadMultiplier(Dungeon.hero);         //if you make something different guns, you should change this;

        String info = desc();

        if (levelKnown) {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(Weapon.class, "too_heavy");
            } else if (Dungeon.hero.STR() > STRReq()){
                info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
            }
            info += "\n\n" + Messages.get(DualPistol.class, "stats_known",
                    Bulletmin(DualPistol.this.buffedLvl()),
                    Bulletmax(DualPistol.this.buffedLvl()),
                    round, max_round, reload_time);
        } else {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
            info += "\n\n" + Messages.get(DualPistol.class, "stats_unknown",
                    Bulletmin(0),
                    Bulletmax(0),
                    round, max_round, reload_time);
        }

        String statsInfo = statsInfo();
        if (!statsInfo.equals("")) info += "\n\n" + statsInfo;

        switch (augment) {
            case SPEED:
                info += " " + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += " " + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        return info;
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return knockBullet().targetingPos(user, dst);
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
    }                           //초과 힘에 따른 추가 데미지

    @Override
    protected float baseDelay(Char owner) {
        return super.baseDelay(owner);
    }                   //공격 속도

    public DualPistol.Bullet knockBullet(){
        return new DualPistol.Bullet();
    }
    public class Bullet extends MissileWeapon {

        {
            image = ItemSpriteSheet.DUAL_BULLET;

            hitSound = Assets.Sounds.PUFF;
            tier = 2;                                                                            //if you make something different guns, you should change this
        }

        @Override
        public int damageRoll(Char owner) {
            int bulletdamage;
            if (owner.buff(Momentum.class) != null && owner.buff(Momentum.class).freerunning()) {
                bulletdamage = Math.round(Random.NormalIntRange(Bulletmin(DualPistol.this.buffedLvl()), Bulletmax(DualPistol.this.buffedLvl())) * (1f + 0.15f * ((Hero) owner).pointsInTalent(Talent.PROJECTILE_MOMENTUM)));
            } else {
                bulletdamage = Random.NormalIntRange(Bulletmin(DualPistol.this.buffedLvl()), Bulletmax(DualPistol.this.buffedLvl()));
            }
            bulletdamage *= (1 + 0.1f * Dungeon.hero.pointsInTalent(Talent.ARM_ENHANCE));
            return bulletdamage;
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return DualPistol.this.hasEnchant(type, owner);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            return DualPistol.this.proc(attacker, defender, damage);
        }

        @Override
        public float delayFactor(Char user) {
            return DualPistol.this.delayFactor(user);
        }

        @Override
        public int STRReq(int lvl) {
            return STRReq(tier, DualPistol.this.buffedLvl());
        }

        @Override
        protected void onThrow( int cell ) {
            for (int i=1; i<=2; i++) {                                                           //i<=n에서 n이 반복하는 횟수, 즉 발사 횟수
                Char enemy = Actor.findChar(cell);
                if (enemy == null || enemy == curUser) {
                    parent = null;
                    CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                } else {
                    if (!curUser.shoot(enemy, this)) {
                        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                    }
                }
                round--;
                updateQuickslot();
            }
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
        }

        @Override
        public void cast(final Hero user, final int dst) {
            super.cast(user, dst);
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target == curUser.pos) {
                    reload();
                } else {
                    knockBullet().cast(curUser, target);
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

}