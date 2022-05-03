//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KagenoNusujinSprite;
import com.watabou.utils.Random;

public class KagenoNusujin extends Mob {
    public KagenoNusujin() {
        this.spriteClass = KagenoNusujinSprite.class;
        this.HT = this.HP =10;
        this.defenseSkill = 16;
        this.maxLvl = 34;
        this.properties.add(Property.UNDEAD);
    }

    public int attackProc(Char var1, int var2) {
        int var3 = var2;
        if (Random.Int(3) == 0) {
            var3 = var2 +2;
            TeleportationTrap var4 = new TeleportationTrap();
            var4.pos = super.pos;
            var4.activate();
        } else {
            var3 = var2 +4;
            //BlazingTrap var4 = new BlazingTrap();
            //var4.pos = super.pos;
            //var4.activate();
        }

        return var3;
    }

    public int attackSkill(Char var1) {
        return 6;
    }

    public int damageRoll() {
        return Random.NormalIntRange(3, 9);
    }

    public int drRoll() {
        return Random.NormalIntRange(0, 3);
    }
}
