//
// Decompiled by Jadx - 787ms
//
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gauntlet;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ShieldHuntsman extends Mob {
    private static final String COMBO = "combo";
    private String[] attackCurse = {"离我远点", "肮脏的诅咒！", "快滚开！", "你毁了我的家，我要复仇！", "你这恶魔！", "不可能，这 不 可 能 ！ ", "水晶之城!!!!!!!!!"};
    private int combo = 0;
    private String[] deathCurse = {"快停下...", "为了水晶之城...", "啊.......你这个怪物", "你这怪物...", "诅咒你！", "守住水晶之城...", "诅咒你这个怪物！", "啊啊...", "诅咒你！", "Why?????????"};

    public ShieldHuntsman() {
        this.spriteClass = ShieldHuntsmanSprite.class;
        this.HT = HP = Random.Int(80,90);
        this.defenseSkill = 15;
        this.EXP = 19;
        this.state = this.SLEEPING;
        this.loot = new PotionOfHealing();
        this.lootChance = 0.15f;
    }

    public int attackSkill(Char target) {
        return 16;
    }

    public int damageRoll() {
        return Random.NormalIntRange(11, 15);
    }

    public int attackProc(Char enemy, int damage) {
        if (Random.Int(0, 10) > 7) {
            this.sprite.showStatus(16711680, this.attackCurse[Random.Int(this.attackCurse.length)], new Object[0]);
        }
        int damage2 = ShieldHuntsman.super.attackProc(enemy, this.combo + damage);
        this.combo++;
        if (enemy == Dungeon.hero) {
            int hitsToDisarm = Random.Int(0, 15);
            Hero hero = Dungeon.hero;
            KindOfWeapon weapon = hero.belongings.weapon;
            if (weapon != null && !(weapon instanceof Gauntlet) && !weapon.cursed && hitsToDisarm > 10) {
                //如果结果大于5 那么触发下面的行动
                hero.belongings.weapon = null;
                Dungeon.quickslot.convertToPlaceholder(weapon);
                KindOfWeapon.updateQuickslot();
                Dungeon.level.drop(weapon, hero.pos).sprite.drop();
                GLog.w("你的武器被猎人击飞了！", new Object[0]);
            }
        }
        if (Random.Int(0, 10) > 7) {
            //如果结果大于7 那么触发下面的行动
            Buff.prolong(enemy, Paralysis.class, Random.Float(1.0f, 2.0f));
            enemy.sprite.emitter().burst(Speck.factory(2), 12);
            Buff.affect(hero, Burning.class ).reignite( hero, 15f );
            GLog.n("你被猎人的盾牌重重地撞了一下，你全身僵直难以呼吸，同时感觉到一团烈焰正在从你身体内熊熊燃烧！", new Object[0]);
        }
        if (this.combo > 5) {
            this.combo = 1;
        }
        return damage2;
    }

    public void die(Object cause) {
        ShieldHuntsman.super.die(cause);
        if (cause != Chasm.class) {
            this.sprite.showStatus(16711680, this.deathCurse[Random.Int(this.deathCurse.length)], new Object[0]);
        }
    }

    public void storeInBundle(Bundle bundle) {
        ShieldHuntsman.super.storeInBundle(bundle);
        bundle.put("combo", this.combo);
    }

    public void restoreFromBundle(Bundle bundle) {
        ShieldHuntsman.super.restoreFromBundle(bundle);
        this.combo = bundle.getInt("combo");
    }
}
