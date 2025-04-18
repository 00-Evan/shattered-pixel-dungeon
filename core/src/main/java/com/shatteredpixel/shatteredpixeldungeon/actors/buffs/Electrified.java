/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.lang.annotation.Target;

public class Electrified extends FlavourBuff {

	public static final float DURATION	= 5f;

	//Dot effects can take up to %40 cur HP (after damage is applied)
	public static final float MAX_DAMAGE= 0.4f;
	int damageStack = 0;

	{
		type = buffType.NEGATIVE;
		announced = true;
	}


	@Override
	public boolean attachTo( Char target ) {
		ElectricResist resist = target.buff(ElectricResist.class);
		if(resist != null) {
			if (Dungeon.level.heroFOV[target.pos] && !resist.immunityAnnounced) {
				target.sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "immune"));
			}
			resist.immunityAnnounced = !resist.immunityAnnounced;

			return false;
		}

		if (super.attachTo( target )) {
			target.paralysed++;
			return true;
		} else {
			return false;
		}
	}
	
	public void processDamage( int damage, Object src ){
		if(damage < 0 || target == null) return;

		if(src instanceof Electricity) return;
		else if(src instanceof Buff || src instanceof Blob) {
			damageStack += damage;
			if(damageStack >= target.HP * MAX_DAMAGE) breakOut();
		}
		else breakOut();
	}
	void breakOut() {
		detach();
		ElectricResist resist = Buff.affect(target, ElectricResist.class);
		resist.left = ElectricResist.DURATION;

		if (Dungeon.level.heroFOV[target.pos]) {
			target.sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "out"));
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		if (target.paralysed > 0)
			target.paralysed--;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.ELECTRIFIED;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}

	@Override
	public void fx(boolean on) {
		if (on)                         target.sprite.add(CharSprite.State.PARALYSED);
		else if (target.paralysed <= 1) target.sprite.remove(CharSprite.State.PARALYSED);
	}


	private static final String DAMAGE_STACK = "damage_stack";
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		damageStack = bundle.getInt(DAMAGE_STACK);
	}
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		bundle.put( DAMAGE_STACK, damageStack );
	}

	public static class ElectricResist extends Buff {
		
		{
			type = buffType.POSITIVE;
		}
		public boolean immunityAnnounced = true;


		public static final float DURATION = 5f;
		private float left;
		
		@Override
		public boolean act() {
			if (target.buff(Electrified.class) == null) {
				left--;
				if (left <= 0) detach();
			}
			spend(TICK);
			return true;
		}
		
		private static final String LEFT = "left";
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			left = bundle.getInt(LEFT);
		}
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			bundle.put( LEFT, left );
		}
	}
}
