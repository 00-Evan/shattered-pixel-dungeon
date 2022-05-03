//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb.Fuse;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class BoomSword extends MeleeWeapon {
    public BoomSword() {
        this.image = ItemSpriteSheet.DG1;
        this.tier = 3;
    }

    public int damageRoll(Char var1) {
        if (var1 instanceof Hero) {
            Hero var2 = (Hero)var1;
            Char var3 = var2.enemy();
            if (var3 instanceof Mob && ((Mob)var3).surprisedBy(var2)) {
                int var4 = this.max();
                int var5 = this.min();
                var4 = this.augment.damageFactor(Random.NormalIntRange(this.min() + Math.round((float)(var4 - var5) * 0.75F), this.max()));
                int var6 = var2.STR() - this.STRReq();
                var5 = var4;
                if (var6 > 0) {
                    var5 = var4 + Random.IntRange(0, var6);
                }

                return var5;
            }
        }

        return super.damageRoll(var1);
    }

    public int max() {
        return this.max(this.level()) + 2;
    }

    public int max(int var1) {
        return (this.tier + 1) * 4 + (this.tier + 1) * var1;
    }

    @Override
    public int proc(Char var1, Char var2, int var3) {
        if (Random.Int(3) == 0) {
            Bomb var4 = new Bomb();
            Fuse var5 = new Fuse();
            var5.bomb = var4;
            var4.fuse = var5;
            Actor.add(var5, Actor.now + 0.0F);
            int var6 = var2.pos;
            Dungeon.level.drop(var4, var6).sprite.drop();
        }

        return super.proc(var1, var2, var3);
    }
}
