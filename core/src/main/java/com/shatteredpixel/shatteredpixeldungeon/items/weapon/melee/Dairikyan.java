//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Dairikyan extends MeleeWeapon {
    public Dairikyan() {
        this.image = ItemSpriteSheet.DG2;
        this.tier = 3;
        this.ACC = 1.24F;
        this.DLY = 0.5F;
    }

    public int max(int var1) {
        return (this.tier + 1) * 4 + (this.tier + 1) * var1;
    }
}
