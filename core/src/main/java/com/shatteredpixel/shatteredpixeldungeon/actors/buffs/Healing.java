/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.GameMath;

public class Healing extends Buff {

	private int healingLeft;
	
	private float percentHealPerTick;
	private int flatHealPerTick;
	
	@Override
	public boolean act(){
		
		int healingThisTick = Math.round(healingLeft * percentHealPerTick) + flatHealPerTick;
		
		healingThisTick = (int)GameMath.gate(1, healingThisTick, healingLeft);
		
		target.HP = Math.min(target.HT, target.HP + healingThisTick);
		
		target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1 );
		target.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "value", healingThisTick));
		
		healingLeft -= healingThisTick;
		
		if (healingLeft == 0){
			detach();
		}
		
		spend( TICK );
		
		return true;
	}
	
	public void setHeal(int amount, float percentPerTick, int flatPerTick){
		healingLeft = amount;
		percentHealPerTick = percentPerTick;
		flatHealPerTick = flatPerTick;
	}
	
	public void increaseHeal( int amount ){
		healingLeft += amount;
	}
	
	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add( CharSprite.State.HEALING );
		else if (target.invisible == 0) target.sprite.remove( CharSprite.State.HEALING );
	}

}
