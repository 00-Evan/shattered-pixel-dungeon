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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class ToxicImbue extends Buff {
	
	{
		type = buffType.POSITIVE;
		announced = true;
	}

	public static final float DURATION	= 50f;

	protected float left;

	private static final String LEFT	= "left";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		left = bundle.getFloat( LEFT );
	}

	public void set( float duration ) {
		this.left = duration;
	}

	public void extend( float duration ) {
		left += duration;
	}

	@Override
	public boolean act() {
		if (left > 0) {
			//spreads 54 units of gas total
			int centerVolume = 6;
			for (int i : PathFinder.NEIGHBOURS8) {
				if (!Dungeon.level.solid[target.pos + i]) {
					GameScene.add(Blob.seed(target.pos + i, 6, ToxicGas.class));
				} else {
					centerVolume += 6;
				}
			}
			GameScene.add(Blob.seed(target.pos, centerVolume, ToxicGas.class));
		}

		spend(TICK);
		left -= TICK;
		if (left <= -5){
			detach();
		}

		return true;
	}

	@Override
	public int icon() {
		return left > 0 ? BuffIndicator.IMBUE : BuffIndicator.NONE;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 1.5f, 0f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - left) / DURATION);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)left);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left));
	}

	{
		immunities.add( ToxicGas.class );
		immunities.add( Poison.class );
	}

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)){
			Buff.detach(target, Poison.class);
			return true;
		} else {
			return false;
		}
	}
}
