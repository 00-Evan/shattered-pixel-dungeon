/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.watabou.utils.Random;

public class Gnoll extends Mob {
	
	{
		spriteClass = GnollSprite.class;
		
		HP = HT = 12;/*生命值12 */
		defenseSkill = 4;/*闪避4 */
		
		EXP = 2;/*经验2 */
		maxLvl = 8;/*最大等级8 */
		
		loot = Gold.class;/*掉落物金币 */
		lootChance = 0.5f;/*掉落概率50% */
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 6 );
	}/*攻击力1-6 */
	
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}/*命中10 */
	
	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 2);
	}/*随机数0-2 */
}
