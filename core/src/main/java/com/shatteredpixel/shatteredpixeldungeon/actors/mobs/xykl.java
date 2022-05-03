/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RedBloodMoon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT;
import com.watabou.utils.Random;

public class xykl extends Mob {
    private static final String COMBO = "combo";
    public RedBloodMoon weapon;
    private String[] attackCurse = {"烧，快烧起来！", "扭曲的世界",
            "滚开！",
            "你不可能再继续前进了！", "你这个狡猾的恶魔！", "去死，全部都去死吧！",
            "去死，你这肮脏的怪物！", "啊！",
            "尝尝我的怒火！",
            "誓死守护我的主人！"};
    private int combo = 0;
    private String[] deathCurse = {"再一次，我辜负了你！", "对不起，主人！我尽力了！",
            "我……对不起……", "我再一次没能守护好你", "神啊...帮帮我吧...",
            "一切已经结束了吗？", "我……太弱了……", "300年前，我辜负了她；300年，后我辜负了你！"};

    public xykl() {
        this.spriteClass = SRPDHBLRTT.class;
        this.HT = 3;
        this.HP = 3;
        this.defenseSkill = 0;
        this.EXP = 0;
        this.state = this.SLEEPING;
        this.loot = new Crossbow();
        this.lootChance = 0.05f;
    }

    public int attackSkill(Char target) {
        return 16;
    }

    public int damageRoll() {
        return Random.NormalIntRange(1, 5);
    }

    public int attackProc(Char enemy, int damage) {
        if (Random.Int(0, 10) > 7) {
            this.sprite.showStatus(16711680, this.attackCurse[Random.Int(this.attackCurse.length)], new Object[0]);
        }
        int damage2 = xykl.super.attackProc(enemy, this.combo + damage);
        this.combo++;
        if (Dungeon.level.flamable[enemy.pos]) {
            GameScene.add(Blob.seed(enemy.pos, 9, Fire.class));
        }
        if (enemy.buff(Burning.class) == null) {
            Buff.affect(enemy, Burning.class).reignite(enemy);
        }
        if (this.combo > 5) {
            this.combo = 1;
        }
        return damage2;
    }


    public void die(Object cause) {
        xykl.super.die(cause);
        if (cause != Chasm.class) {
            this.sprite.showStatus(16711680, this.deathCurse[Random.Int(this.deathCurse.length)], new Object[0]);
            RedBloodMoon.deadking--;
        }
    }

}
