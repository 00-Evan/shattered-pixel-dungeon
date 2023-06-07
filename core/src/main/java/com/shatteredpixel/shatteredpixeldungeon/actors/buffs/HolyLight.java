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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.HighOrderKnightArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PapalKnightArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PastorClothe;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class HolyLight extends Buff {
	
	{
		type = buffType.POSITIVE;
	}

	public static final int DISTANCE	= 4;
	public static int level = 1;
	private int interval = 1;


	public int level() {
		return level;
	}

	public void set( int value, int time ) {
		//decide whether to override, preferring high value + low interval
		if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
			level = value;
			interval = time;
			spend(time - cooldown() - 1);
		}
	}

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			if (Dungeon.level != null) {
				target.viewDistance = Math.max( Dungeon.level.viewDistance, DISTANCE );
				Dungeon.observe();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int icon() {
		return BuffIndicator.ARMOR;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight( 0xff0000);
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.ILLUMINATED);
		else target.sprite.remove(CharSprite.State.ILLUMINATED);
	}

	@Override
	public boolean act() {
		super.act();
		spend(interval);
		if (level <= 0 || ( !( hero.belongings.armor instanceof PastorClothe)
				&& !( hero.belongings.armor instanceof PapalKnightArmor)
				&& !( hero.belongings.armor instanceof HighOrderKnightArmor)
				&& !( hero.belongings.armor instanceof ClassArmor)      ) ) {

			detach();
		}

		return true;
	}


}
