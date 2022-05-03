/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.watabou.utils.Random;

public class Rat extends Mob {
	private static final String COMBO = "combo";
	private String[] attackCurse = {"你们这些肮脏的人类", "扭曲的野兽", "滚开！", "你们毁了我的家园！", "你这个狡猾的人类！", "人类，肮脏的人类！", "去死吧！！！", "300年前，这里曾经是一片祥和"};
	private int combo = 0;
	private String[] deathCurse = {"快停下...", "猎杀结束了", "啊...你这个怪物", "你这怪物", "神啊...帮帮我吧...", "四周好冷", "诅咒你这怪物", "相信我的同伴今晚会为我复仇"};
	public Rat() {
		this.spriteClass = RatSprite.class;
		this.HT = 3;
		this.HP = 3;
		this.defenseSkill = 4;
		this.EXP = 1;
		this.maxLvl = 15;
		this.state = this.SLEEPING;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 2 );
	}

	public int attackProc(Char enemy, int damage) {
		if (Random.Int(0, 10) > 7) {
			this.sprite.showStatus(16711680, this.attackCurse[Random.Int(this.attackCurse.length)], new Object[0]);
		}
		int damage2 = Rat.super.attackProc(enemy, this.combo + damage);
		this.combo++;
		return damage2;
	}

	public void die(Object cause) {
		Rat.super.die(cause);
		if (cause != Chasm.class) {
			this.sprite.showStatus(16711680, this.deathCurse[Random.Int(this.deathCurse.length)], new Object[0]);

		}
	}

	@Override
	public int attackSkill( Char target ) {
		return 8;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 1);
	}
}

