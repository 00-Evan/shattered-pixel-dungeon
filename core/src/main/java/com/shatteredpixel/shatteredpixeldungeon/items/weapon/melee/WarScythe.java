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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class WarScythe extends MeleeWeapon {

	{
		image = ItemSpriteSheet.WAR_SCYTHE;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 0.9f;

		tier = 5;
		ACC = 0.8f; //20% penalty to accuracy
	}

	@Override
	public int max(int lvl) {
		return  Math.round(6.67f*(tier+1)) +    //40 base, up from 30
				lvl*(tier+1);                   //scaling unchanged
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		//replaces damage with 22.5+3.5*lvl bleed, exactly 100% avg damage.
		int bleedAmt = augment.damageFactor(Math.round(22.5f + 3.5f*buffedLvl()));
		Sickle.harvestAbility(hero, target, 0f, bleedAmt, this);
	}

	@Override
	public String abilityInfo() {
		int bleedAmt = levelKnown ? Math.round(22.5f + 3.5f*buffedLvl()) : 23;
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(bleedAmt));
		} else {
			return Messages.get(this, "typical_ability_desc", bleedAmt);
		}
	}

}
