/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.items.potions;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.Invisibility;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class PotionOfInvisibility extends Potion {

	{
		icon = ItemSpriteSheet.Icons.POTION_INVIS;
	}

	@Override
	public void apply( Hero hero ) {
		identify();
		Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
		GLog.i( Messages.get(this, "invisible") );
		Sample.INSTANCE.play( Assets.Sounds.MELD );
	}
	
	@Override
	public int value() {
		return isKnown() ? 40 * quantity : super.value();
	}

}
