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

package net.casiello.pixeldungeonrescue.items.potions;

import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Bleeding;
import net.casiello.pixeldungeonrescue.actors.buffs.Blindness;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Cripple;
import net.casiello.pixeldungeonrescue.actors.buffs.Drowsy;
import net.casiello.pixeldungeonrescue.actors.buffs.Healing;
import net.casiello.pixeldungeonrescue.actors.buffs.Poison;
import net.casiello.pixeldungeonrescue.actors.buffs.Slow;
import net.casiello.pixeldungeonrescue.actors.buffs.Vertigo;
import net.casiello.pixeldungeonrescue.actors.buffs.Vulnerable;
import net.casiello.pixeldungeonrescue.actors.buffs.Weakness;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.utils.GLog;

public class PotionOfHealing extends Potion {

	{
		initials = 3;

		bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		//starts out healing 30 hp, equalizes with hero health total at level 11
		Buff.affect( hero, Healing.class ).setHeal((int)(0.8f*hero.HT + 14), 0.25f, 0);
		cure( hero );
		GLog.p( Messages.get(this, "heal") );
	}
	
	public static void cure( Char ch ) {
		Buff.detach( ch, Poison.class );
		Buff.detach( ch, Cripple.class );
		Buff.detach( ch, Weakness.class );
		Buff.detach( ch, Vulnerable.class );
		Buff.detach( ch, Bleeding.class );
		Buff.detach( ch, Blindness.class );
		Buff.detach( ch, Drowsy.class );
		Buff.detach( ch, Slow.class );
		Buff.detach( ch, Vertigo.class);
	}

	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
