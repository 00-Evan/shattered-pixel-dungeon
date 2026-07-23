/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DwarfToken;

//does not spawn or follow a partner due to overriding AI states and partnerID
// but will still buddy up with nearby ghouls for the purposes of survival
public class VaultGhoul extends Ghoul {

	{
		activateSteathGameplayBehaviour();
		partnerID = -2; //does not spawn a partner

		//uses base ghoul ACC and EVA

		maxLvl = 30;
		EXP = 0;
		loot = DwarfToken.class;
		lootChance = 1;
	}

	@Override
	protected boolean act() {
		if (state == WANDERING || state == SLEEPING){
			//finds nearby ghouls and is drawn to whatever they're targeting
			// to simulate normal ghoul behaviour of partners sharing aggro
			for (Mob m : Dungeon.level.mobs){
				if (m instanceof VaultGhoul
						&& Dungeon.level.distance(pos, m.pos) < 4
						&& (m.state == m.INVESTIGATING || m.state == m.HUNTING)){
					beckon(m.pos);
					break;
				}
			}
		}
		return super.act();
	}
}
