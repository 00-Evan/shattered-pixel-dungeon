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

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.GoldenLiquid;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
//A magical version of rockarmor, essentially
public class Goldlize extends Buff {

	{
		type = buffType.POSITIVE;
		immunities.add ( Golden.class );
		immunities.add ( GoldenLiquid.class );
	}


	private int armor;

	public void addArmor(int goldtoAdd){

		armor += goldtoAdd;
		if (armor > 50){
			armor = 50;
		}
	}

	public int absorb( int damage ) {
		int block = damage - damage/2;
		if (armor <= block) {
			detach();
			return damage - armor;
		} else {
			armor -= block;
			return damage - block;
		}
	}


	private static final String ARMOR	= "armor";


	{
		type = buffType.NEGATIVE;
		announced = true;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ARMOR, armor );

	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		armor = bundle.getInt( ARMOR );

	}

	@Override
	public int icon() {
		return BuffIndicator.ARMOR;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0.8f, 0.2f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, 1);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(armor);
	}


	@Override
	public String desc() {
		return Messages.get(this, "desc", armor);
	}

}
