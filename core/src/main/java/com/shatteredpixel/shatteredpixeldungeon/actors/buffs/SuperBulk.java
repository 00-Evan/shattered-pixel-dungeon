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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class SuperBulk extends Buff  {


	{
		type = buffType.POSITIVE;
		announced = true;
	}


	int left;

	public static int boost(int HT){
		return Math.round(4 + HT/20f);
	}

	public int boost(){
		return Math.round(2f*boost(15 + 5*((Hero)target).lvl)/5f);
	}
	// 영약의 2/5과 같음

	public float reducer(Char owner, float speed ){
		if 	(Dungeon.level.map[owner.pos] == Terrain.DOOR
			|| Dungeon.level.map[owner.pos] == Terrain.OPEN_DOOR ) {
			speed /= 1f ;
		}
		return speed;
	} // 거슬리는데 일단 턴소모없이 냅둘예정

	@Override
	public boolean act() {
		left --;
		if 	(Dungeon.level.map[target.pos] == Terrain.DOOR
				|| Dungeon.level.map[target.pos] == Terrain.OPEN_DOOR ) {
			left-= 3f ;
		}
		if (left < 0){
			detach();
			return true;
		}
		spend(TICK);
		return true;
	}
	@Override
	public void detach() {
		super.detach();
		((Hero) target).updateHT( false );
	}


	public void reset(){ left = 100;}


	
	@Override
	public int icon() {
		return BuffIndicator.SUPER_BULK;
	}

	@Override
	public float iconFadePercent() {
		return (100f - left) / 100f;
	}
	 // 아이콘 페이드아웃 퍼센트

	@Override
	public String iconTextDisplay() {
		return Integer.toString(left);
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", boost(), left );
	}
	
	private static final String LEFT = "left";

	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEFT, left);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		left = bundle.getInt(LEFT);
	}
}
