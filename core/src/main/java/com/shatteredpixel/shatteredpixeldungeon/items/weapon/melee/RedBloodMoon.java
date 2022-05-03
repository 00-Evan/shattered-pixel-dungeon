//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.xykl;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RedBloodMoon extends MeleeWeapon {

    public static int deadking=0;

    public RedBloodMoon() {
        this.image = ItemSpriteSheet.RedBloodMoon;
        this.tier = 3;
    }

    public int damageRoll(Char var1) {
        if (var1 instanceof Hero) {
            Hero var2 = (Hero)var1;
            Char var3 = var2.enemy();
            if (var3 instanceof Mob && ((Mob)var3).surprisedBy(var2)) {
                int var4 = this.max();
                int var5 = this.min();
                var4 = this.augment.damageFactor(Random.NormalIntRange(this.min() + Math.round((float)(var4 - var5) * 0.9F), this.max()));
                int var6 = var2.STR() - this.STRReq();
                var5 = var4;
                if (var6 > 0) {
                    var5 = var4 + Random.IntRange(0, var6);
                }

                if (Random.Int(2) == 0) {
                    ((Bleeding)Buff.affect(var3, Bleeding.class)).set((float)(var5 * 4));
                }

                return var5;
            }
        }

        return super.damageRoll(var1);
    }

    public int max(int var1) {
        int var2 = Math.round((float)(this.tier + 1) * 1.125F);
        int var3 = Math.round((float)(this.tier + 4) * 1.125F);
        this.DLY = 1.25F;
        return var2 * var1 + var3;
    }

    public int min(int var1) {
        return Math.round((float)(this.tier + 0) * 1.08F) * var1 + Math.round((float)(this.tier + 1) * 0.6675F);
    }

  @Override
    public int proc(Char attacker, Char defender, int damage) {
        for (int i : PathFinder.NEIGHBOURS9){

            if (!Dungeon.level.solid[attacker.pos + i]
                    && !Dungeon.level.pit[attacker.pos + i]
                    && Actor.findChar(attacker.pos + i) == null
                    && attacker == Dungeon.hero&& deadking<6) {

                GuardianKnight guardianKnight1 = new GuardianKnight();
                guardianKnight1.weapon = this;
                guardianKnight1.pos = attacker.pos + i;
                guardianKnight1.aggro(defender);
                GameScene.add(guardianKnight1);
                Dungeon.level.occupyCell(guardianKnight1);
                deadking++;

                CellEmitter.get(guardianKnight1.pos).burst(Speck.factory(Speck.EVOKE), 4);
                break;
            } else if(!Dungeon.level.solid[attacker.pos + i]) {
                return super.proc( attacker, defender, damage );
            }
        }
        return super.proc(attacker, defender, damage);
    }

    public static class GuardianKnight extends xykl {
        {
            state = WANDERING;
            spriteClass = SRPDHBLRTT.class;
            alignment = Alignment.ALLY;
        }

        public GuardianKnight() {
            HP = HT = 5 + Dungeon.escalatingDepth() * 2;
            defenseSkill = 4 + Dungeon.escalatingDepth();
        }

        @Override
        public void die(Object cause) {
            weapon = null;
            super.die(cause);
        }

        @Override
        public int drRoll() {
            return Random.Int(Dungeon.escalatingDepth(), Dungeon.escalatingDepth());
        }
    }

    public static class SRPDHBLRTT extends com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT {

        public SRPDHBLRTT(){
            super();
            tint(0, 1, 1, 0.4f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(0, 1, 1, 0.4f);
        }
    }


}
