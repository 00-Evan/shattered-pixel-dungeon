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

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class PotionOfAdrenalineSurge extends ExoticPotion {
	
	@Override
	public void apply(Hero hero) {
		setKnown();
		Buff.affect(hero, strBoost.class).reset();
	}
	
	public static class strBoost extends Buff {
		
		int boost;
		private static final float INTERVAL = TICK * 500f;
		
		public void reset(){
			boost = 2;
			spend(INTERVAL - cooldown());
		}
		
		public int boost(){
			return boost;
		}
		
		@Override
		public boolean act() {
			boost --;
			if (boost > 0){
				spend( INTERVAL );
			} else {
				detach();
			}
			return true;
		}
		
		//TODO visuals
		
		@Override
		public int icon() {
			return BuffIndicator.MOMENTUM;
		}
		
		@Override
		public String toString() {
			return "surge";
		}
		
		@Override
		public String desc() {
			return "cur boost: +" + boost + "\n\nleft: " + dispTurns(cooldown());
		}
	}
}
