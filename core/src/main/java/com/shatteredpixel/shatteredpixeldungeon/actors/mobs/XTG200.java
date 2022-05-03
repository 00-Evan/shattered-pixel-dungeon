//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.STenguSprite;
import com.watabou.utils.Random;

public class XTG200 extends Mob {
    public XTG200() {
        this.spriteClass = STenguSprite.class;
        this.HT = 14;
        this.HP = 14;
        this.defenseSkill = 5;
        this.EXP = 0;
        this.maxLvl = 15;
        this.flying = true;
    }

    public int attackProc(Char var1, int var2) {
        var2 = super.attackProc(var1, var2 / 2);
        if (Random.Int(2) == 0) {
            ((Bleeding)Buff.affect(var1, Bleeding.class)).set((float)(var2 * 1));
        }

        return var2;
    }
    int generation	= 0;
    private XTG200 split() {
        XTG200 clone = new XTG200();
        clone.generation = generation + 1;
        clone.EXP = 0;
        return clone;
    }



    public int attackSkill(Char var1) {
        return 18;
    }

    public int damageRoll() {
        return Random.NormalIntRange(6, 19);
    }

    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }
}
