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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Corruption extends AllyBuff implements Buff.DOTbuff {

	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	private float partialDamage = 0f;

	//corrupted enemies are usually fully healed and cleansed of most debuffs
	public static void corruptionHeal(Char target){
		target.HP = target.HT;
		target.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(target.HT), FloatingText.HEALING);
		for (Buff buff : target.buffs()) {
			if (buff.type == Buff.buffType.NEGATIVE
					&& !(buff instanceof SoulMark)) {
				buff.detach();
			}
		}
	}

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)){
			target.needsIncomingDOTUpdate = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean act() {
		partialDamage += target.HT/100f;

		int damage = (int)partialDamage;
		partialDamage -= damage;

		if (damage > 0) {
			target.damage(damage, this);
		}

		spend(TICK);

		return true;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add( CharSprite.State.DARKENED );
		else if (target.invisible == 0) target.sprite.remove( CharSprite.State.DARKENED );
	}

	@Override
	public int icon() {
		return BuffIndicator.CORRUPT;
	}

	@Override
	public int totalIncomingDMG() {
		return target.HT;
	}

	public static final String PARTIAL_DAMAGE = "partial_damage";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PARTIAL_DAMAGE, partialDamage);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		partialDamage = bundle.getInt(PARTIAL_DAMAGE);
	}
}
