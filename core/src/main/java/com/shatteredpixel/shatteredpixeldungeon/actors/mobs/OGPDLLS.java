//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OGPDLLSTT;

public class OGPDLLS extends Rat {
    public OGPDLLS() {
        this.spriteClass = OGPDLLSTT.class;
        this.HT = 12;
        this.HP = 12;
        this.maxLvl = 15;
        this.EXP = 6;
    }

    public int attackProc(Char var1, int var2) {
        return super.attackProc(var1, var2) - 1;
    }
}
