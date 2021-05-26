package com.elementalpixel.elementalpixeldungeon.items.weapon;

import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.actors.Actor;
import com.elementalpixel.elementalpixeldungeon.actors.Char;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.Blob;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.Fire;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.ToxicGas;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Paralysis;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Roots;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.effects.CellEmitter;
import com.elementalpixel.elementalpixeldungeon.effects.Splash;
import com.elementalpixel.elementalpixeldungeon.effects.particles.FlameParticle;
import com.elementalpixel.elementalpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.scenes.CellSelector;
import com.elementalpixel.elementalpixeldungeon.scenes.GameScene;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.elementalpixel.elementalpixeldungeon.sprites.MissileSprite;
import com.elementalpixel.elementalpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AlchemistFlask extends Weapon {

    public static final String AC_USE =      "USE";

    {
        image = ItemSpriteSheet.ELIXIR_TOXIC;

        defaultAction = AC_USE;
        usesTargeting = true;

        unique = true;
        bones = false;
    }
    public int debuff;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_EQUIP);
        actions.add(AC_USE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_USE)) {

            curUser = hero;
            curItem = this;
            GameScene.selectCell( shooter );

        }
    }

    @Override
    public String info() {
        String info = desc();

        info += "\n\n" + Messages.get( AlchemistFlask.class, "stats",
                Math.round(augment.damageFactor(min())),
                Math.round(augment.damageFactor(max())),
                STRReq());

        if (STRReq() > Dungeon.hero.STR()) {
            info += " " + Messages.get(Weapon.class, "too_heavy");
        } else if (Dungeon.hero.STR() > STRReq()){
            info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
        }

        switch (augment) {
            case SPEED:
                info += "\n\n" + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += "\n\n" + Messages.get(Weapon.class, "stronger");
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
    public int STRReq(int lvl) {
        return STRReq(1, lvl); //tier 1
    }

    @Override
    public int min(int lvl) {
        return 1 + Dungeon.hero.lvl/5
                + (curseInfusionBonus ? 1 : 0);
    }

    @Override
    public int max(int lvl) {
        return 5 + (int)(Dungeon.hero.lvl/3f)
                + (curseInfusionBonus ? 2 : 0);
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return knockProjectile().targetingPos(user, dst);
    }

    private int targetPos;
    public int damage = 0;

    @Override
    public int damageRoll(Char owner) {
        damage += augment.damageFactor(super.damageRoll(owner));
        System.out.println(damage);
        if (owner instanceof Hero) {
            int exStr = ((Hero)owner).STR() - STRReq();
            if (exStr > 0) {
                damage += Random.IntRange( 0, exStr );
            }
        }
        System.out.println(STRReq());

        return damage;
    }

    @Override
    public int level() {
        return (Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5) + (curseInfusionBonus ? 1 : 0);
    }

    @Override
    public int buffedLvl() {
        return level();
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public AlchemistFlask.AchemistProjectile knockProjectile(){
        return new AchemistProjectile();
    }

    public class AchemistProjectile extends MissileWeapon {

        {
            debuff = Random.Int(1, 10);

            switch (debuff) {
                case 1:
                    image = ItemSpriteSheet.POTION_CHARCOAL;
                    break;
                case 2:
                    image = ItemSpriteSheet.POTION_AZURE;
                    break;
                case 3:
                    image = ItemSpriteSheet.POTION_CRIMSON;
                    break;
                case 4:
                    image = ItemSpriteSheet.POTION_BISTRE;
                    break;
                case 5:
                    image = ItemSpriteSheet.POTION_TURQUOISE;
                    break;
                default:
                    image = ItemSpriteSheet.POTION_AMBER;
            }

                //image = ItemSpriteSheet.ALCHEMIST_PROJECTILE;

                hitSound = Assets.Sounds.SHATTER;

        }

        @Override
        public int damageRoll(Char owner) {
            return AlchemistFlask.this.damageRoll(owner);
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return AlchemistFlask.this.hasEnchant(type, owner);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            return AlchemistFlask.this.proc(attacker, defender, damage);
        }

        @Override
        public float speedFactor(Char user) {
            return AlchemistFlask.this.speedFactor(user);
        }

        @Override
        public float accuracyFactor(Char owner) {
            if (AlchemistFlask.this.augment == Augment.DAMAGE){
                return Float.POSITIVE_INFINITY * 100;
            } else {
                return super.accuracyFactor(owner) * 100;
            }
        }

        @Override
        public int STRReq(int lvl) {
            return AlchemistFlask.this.STRReq(lvl);
        }


        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );
            if (enemy == null || enemy == curUser) {
                parent = null;
                Splash.at( cell, 0xCC99FFFF, 1 );
            } else {

                switch (debuff) {
                    case 1:
                        damageRoll(curUser);
                        damage *= 2;
                        damage = 0;
                        break;
                    case 2:
                        Buff.affect( enemy, Paralysis.class, Paralysis.DURATION - 9f);
                        damage = 0;
                        break;
                    case 3:
                        Buff.affect( enemy, Roots.class, Roots.DURATION - 4);
                        damage = 0;
                        break;
                    case 4:
                        if (!Dungeon.level.solid[targetPos]) {
                            GameScene.add( Blob.seed( targetPos, 1, Fire.class ) );
                            CellEmitter.get( targetPos ).burst( FlameParticle.FACTORY, 1 );
                        }

                        damage = 0;
                        break;
                    case 5:
                        GameScene.add( Blob.seed( targetPos, 50 + 20 * Dungeon.depth, ToxicGas.class ) );
                        Sample.INSTANCE.play(Assets.Sounds.GAS);
                        damage = 0;
                        break;
                    default:
                        damageRoll( curUser );
                        damage = 0;
                        break;
                }

                if (!curUser.shoot(enemy, this)) {
                    Splash.at(cell, 0xCC99FFFF, 1);
                }
            }
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play( Assets.Sounds.ATK_SPIRITBOW, 1, Random.Float(0.87f, 1.15f) );
        }

        int flurryCount = -1;

        @Override
        public void cast(final Hero user, final int dst) {
            final int cell = throwPos( user, dst );
            AlchemistFlask.this.targetPos = cell;
            if (AlchemistFlask.this.augment == Augment.SPEED){
                if (flurryCount == -1) flurryCount = 3;

                final Char enemy = Actor.findChar( cell );

                if (enemy == null){
                    user.spendAndNext(castDelay(user, dst));
                    flurryCount = -1;
                    return;
                }
                QuickSlotButton.target(enemy);

                final boolean last = flurryCount == 1;

                user.busy();

                throwSound();

                ((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
                        reset(user.sprite,
                                cell,
                                this,
                                new Callback() {
                                    @Override
                                    public void call() {
                                        if (enemy.isAlive()) {
                                            curUser = user;
                                            onThrow(cell);
                                        }

                                        if (last) {
                                            user.spendAndNext(castDelay(user, dst));
                                            flurryCount = -1;
                                        }
                                    }
                                });

                user.sprite.zap(cell, new Callback() {
                    @Override
                    public void call() {
                        flurryCount--;
                        if (flurryCount > 0){
                            cast(user, dst);
                        }
                    }
                });

            } else {

                super.cast(user, dst);
            }
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                knockProjectile().cast(curUser, target);
            }
        }
        @Override
        public String prompt() {
            return Messages.get(AlchemistFlask.class, "prompt");
        }
    };
}
