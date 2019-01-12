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

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;

public class Brimstone extends Armor.Glyph {

	private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF4400 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, see Burning.act
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return ORANGE;
	}

	//FIXME doesn't work with sad ghost
	public static class BrimstoneShield extends ShieldBuff {
		
		{
			type = buffType.POSITIVE;
		}

		@Override
		public boolean act() {
			Hero hero = (Hero)target;

			if (hero.belongings.armor == null || !hero.belongings.armor.hasGlyph(Brimstone.class, hero)) {
				detach();
				return true;
			}

			int level = hero.belongings.armor.level();

			if (hero.buff(Burning.class) != null){
				//max shielding equal to the armors level (this does mean no shield at lvl 0)
				if (shielding() < level) {
					incShield();

					//generates 0.2 + 0.1*lvl shield per turn
					spend( 10f / (2f + level));
				} else {

					//if shield is maxed, don't wait longer than 1 turn to try again
					spend( Math.min( TICK, 10f / (2f + level)));
				}

			} else if (hero.buff(Burning.class) == null){
				if (shielding() > 0){
					decShield();

					//shield decays at a rate of 1 per turn.
					spend(TICK);
				} else {
					detach();
				}
			}

			return true;
		}

		public void startDecay(){
			//sets the buff to start acting next turn. Invoked by Burning when it expires.
			spend(-cooldown()+2);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			//pre-0.7.0
			if (bundle.contains("added")){
				setShield(bundle.getInt("added"));
			}
		}
	}

}
