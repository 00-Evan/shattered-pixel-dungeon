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
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT;
import com.watabou.utils.Random;

public class SRPDHBLR extends Mob {
    private static final String COMBO = "combo";
    private String[] attackCurse = {"烧，快烧起来！", "扭曲的世界",
            "滚开！",
            "你不可能再继续前进了！", "你这个狡猾的人类！", "冒险家，肮脏的冒险家！",
            "去死，你这肮脏的冒险者！", "啊！",
            "尝尝我的怒火！",
            "你不能！你不能！不……"};
    private int combo = 0;
    private String[] deathCurse = {"快停下...", "猎杀结束了",
            "啊...你这个怪物", "你这怪物", "神啊...帮帮我吧...",
            "四周好冷", "诅咒你这怪物", "相信我的同伴今晚会为我复仇"};

    public SRPDHBLR() {
        this.spriteClass = SRPDHBLRTT.class;
        this.HT = 25;
        this.HP = 25;
        this.defenseSkill = 7;
        this.EXP = 15;
        this.maxLvl = 15;
        this.state = this.SLEEPING;
        this.loot = new Crossbow();
        this.lootChance = 0.05f;
    }

    public int attackSkill(Char target) {
        return 16;
    }

    public int damageRoll() {
        return Random.NormalIntRange(11, 12);
    }

    public int attackProc(Char enemy, int damage) {
        if (Random.Int(0, 10) > 7) {
            this.sprite.showStatus(16711680, this.attackCurse[Random.Int(this.attackCurse.length)], new Object[0]);
        }
        int damage2 = SRPDHBLR.super.attackProc(enemy, this.combo + damage);
        this.combo++;
        if (Dungeon.level.flamable[enemy.pos]) {
            GameScene.add(Blob.seed(enemy.pos, 4, Fire.class));
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
        SRPDHBLR.super.die(cause);
        if (cause != Chasm.class) {
            this.sprite.showStatus(16711680, this.deathCurse[Random.Int(this.deathCurse.length)], new Object[0]);

        }
    }}
