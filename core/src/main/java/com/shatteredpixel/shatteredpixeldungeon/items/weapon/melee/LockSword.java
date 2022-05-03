//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

public class LockSword extends MeleeWeapon {
    public int lvl;

    public LockSword() {
        super.image = ItemSpriteSheet.DG3;
        super.tier = 3;
        if (this.lvl >= 150) {
            super.image = ItemSpriteSheet.DG4;
        }

        if (this.lvl >= 450) {
            super.image = ItemSpriteSheet.DG5;
        }

    }

    public int proc(Char var1, Char var2, int var3) {
        ++this.lvl;
        int var4 = var3;
        if (this.lvl >= 150) {
            var4 = (new Unstable()).proc(this, var1, var2, var3) + 3;
            super.image = ItemSpriteSheet.DG4;
        }

        var3 = var4;
        if (this.lvl >= 450) {
            var3 = (new Unstable()).proc(this, var1, var2, var4);
            var3 = (new Unstable()).proc(this, var1, var2, var3) + 7;
            super.image = ItemSpriteSheet.DG5;
        }

        return super.proc(var1, var2, var3);
    }

    public void restoreFromBundle(Bundle var1) {
        super.restoreFromBundle(var1);
        super.image = ItemSpriteSheet.DG3;
        if (this.lvl >= 150) {
            super.image = ItemSpriteSheet.DG4;
        }

        if (this.lvl >= 450) {
            super.image = ItemSpriteSheet.DG5;
        }

        this.lvl = var1.getInt("lvl");
    }

    public void storeInBundle(Bundle var1) {
        super.storeInBundle(var1);
        super.image = ItemSpriteSheet.DG3;
        if (this.lvl >= 150) {
            super.image = ItemSpriteSheet.DG4;
        }

        if (this.lvl >= 450) {
            super.image = ItemSpriteSheet.DG5;
        }

        var1.put("lvl", this.lvl);
    }
}
