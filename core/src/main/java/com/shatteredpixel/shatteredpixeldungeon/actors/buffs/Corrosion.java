/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorrosion;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Corrosion extends Buff implements Hero.Doom {

	private float damage = 1;
	protected float left;

	//used in specific cases where the source of the corrosion is important for death logic
	private Class source;

	private static final String DAMAGE	= "damage";
	private static final String LEFT	= "left";
	private static final String SOURCE	= "source";

	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DAMAGE, damage );
		bundle.put( LEFT, left );
		bundle.put( SOURCE, source);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		damage = bundle.getFloat( DAMAGE );
		left = bundle.getFloat( LEFT );
		source = bundle.getClass( SOURCE );
	}

	public void set(float duration, int damage){
		set(duration, damage, null);
	}

	public void set(float duration, int damage, Class source) {
		this.left = Math.max(duration, left);
		if (this.damage < damage) this.damage = damage;
		this.source = source;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.POISON;
	}
	
	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0.5f, 0f);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)damage);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left), (int)damage);
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {
			target.damage((int)damage, this);
			if (damage < (Dungeon.scalingDepth()/2)+2) {
				damage++;
			} else {
				damage += 0.5f;
			}
			
			spend( TICK );
			if ((left -= TICK) <= 0) {
				detach();
			}
		} else {
			detach();
		}

		return true;
	}
	
	@Override
	public void onDeath() {
		if (source == WandOfCorrosion.class){
			Badges.validateDeathFromFriendlyMagic();
		}

		Dungeon.fail( getClass() );
		GLog.n(Messages.get(this, "ondeath"));
	}

}
