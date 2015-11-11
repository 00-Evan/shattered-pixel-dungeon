/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class CapeOfThorns extends Artifact {

	{
		name = "Cape of Thorns";
		image = ItemSpriteSheet.ARTIFACT_CAPE;

		levelCap = 10;

		charge = 0;
		chargeCap = 100;
		cooldown = 0;

		defaultAction = "NONE";
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Thorns();
	}

	@Override
	public String desc() {
		String desc = "These collapsed sheets of metal from the DM-300 have formed together into a rigid wearable " +
				"cape. The metal is old and coated in thick flakes of rust. It seems to store a deep energy, " +
				"perhaps it has some of the DM-300's power?";
		if (isEquipped( Dungeon.hero )) {
			desc += "\n\n";
			if (cooldown == 0)
				desc += "The cape feels reassuringly heavy on your shoulders. You're not sure if it will directly " +
						"help you in a fight, but it seems to be gaining energy from the damage you take.";
			else
				desc += "The cape seems to be releasing some stored energy, " +
						"it is radiating a protective power at all angles. ";
		}

		return desc;
	}

	public class Thorns extends ArtifactBuff{

		@Override
		public boolean act(){
			if (cooldown > 0) {
				cooldown--;
				if (cooldown == 0) {
					BuffIndicator.refreshHero();
					GLog.w("Your Cape becomes inert again.");
				}
				updateQuickslot();
			}
			spend(TICK);
			return true;
		}

		public int proc(int damage, Char attacker, Char defender){
			if (cooldown == 0){
				charge += damage*(0.5+level()*0.05);
				if (charge >= chargeCap){
					charge = 0;
					cooldown = 10+level();
					GLog.p("Your Cape begins radiating energy, you feel protected!");
					BuffIndicator.refreshHero();
				}
			}

			if (cooldown != 0){
				int deflected = Random.NormalIntRange(0, damage);
				damage -= deflected;

				if (attacker != null && Level.adjacent(attacker.pos, defender.pos)) {
					attacker.damage(deflected, this);
				}

				exp+= deflected;

				if (exp >= (level()+1)*5 && level() < levelCap){
					exp -= (level()+1)*5;
					upgrade();
					GLog.p("Your Cape grows stronger!");
				}

			}
			updateQuickslot();
			return damage;
		}

		@Override
		public String toString() {
				return "Thorns";
		}

		@Override
		public String desc() {
			return "Your cape is radiating energy, surrounding you in a field of deflective force!\n" +
					"\n" +
					"All damage you receive is reduced while the thorns effect is active. Additionally, " +
					"if the attacker is next to you, the reduced amount is deflected back at the attacker.\n" +
					"\n" +
					"Your cape will continue radiating energy for " + dispTurns(cooldown) + ".";
		}

		@Override
		public int icon() {
			if (cooldown == 0)
				return BuffIndicator.NONE;
			else
				return BuffIndicator.THORNS;
		}

		@Override
		public void detach(){
			cooldown = 0;
			charge = 0;
			super.detach();
		}

	}


}
