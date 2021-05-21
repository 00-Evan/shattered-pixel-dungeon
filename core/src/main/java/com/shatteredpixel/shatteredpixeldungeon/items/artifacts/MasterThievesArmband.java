/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class MasterThievesArmband extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_ARMBAND;

		levelCap = 10;

		charge = 0;
	}

	private int exp = 0;

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Thievery();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		charge += Math.round(10*amount);
		updateQuickslot();
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if ( isEquipped (Dungeon.hero) ){
			if (cursed){
				desc += "\n\n" + Messages.get(this, "desc_cursed");
			} else {
				desc += "\n\n" + Messages.get(this, "desc_worn");
			}
		}
		

		return desc;
	}

	public class Thievery extends ArtifactBuff{
		public void collect(int gold){
			if (!cursed) {
				charge += gold/2 * RingOfEnergy.artifactChargeMultiplier(target);
			}
		}

		@Override
		public void detach() {
			charge *= 0.95;
			super.detach();
		}
		
		@Override
		public boolean act() {
			if (cursed) {
				
				if (Dungeon.gold > 0 && Random.Int(6) == 0){
					Dungeon.gold--;
				}
				
				spend(TICK);
				return true;
			} else {
				return super.act();
			}
		}
		
		public boolean steal(int value){
			if (value <= charge){
				charge -= value;
				exp += value;
			} else {
				float chance = stealChance(value);
				if (Random.Float() > chance)
					return false;
				else {
					if (chance <= 1)
						charge = 0;
					else
						//removes the charge it took you to reach 100%
						charge -= charge/chance;
					exp += value;
				}
			}
			Talent.onArtifactUsed(Dungeon.hero);
			while(exp >= (250 + 50*level()) && level() < levelCap) {
				exp -= (250 + 50*level());
				upgrade();
			}
			return true;
		}

		public float stealChance(int value){
				//get lvl*50 gold or lvl*3.33% item value of free charge, whichever is less.
				int chargeBonus = Math.min(level()*50, (value*level())/30);
				return (((float)charge + chargeBonus)/value);
		}
	}
}
