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

package net.casiello.pixeldungeonrescue.items.scrolls;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Invisibility;
import net.casiello.pixeldungeonrescue.actors.buffs.Recharging;
import net.casiello.pixeldungeonrescue.effects.SpellSprite;
import net.casiello.pixeldungeonrescue.effects.particles.EnergyParticle;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRecharging extends Scroll {

	public static final float BUFF_DURATION = 30f;

	{
		initials = 6;
	}

	@Override
	public void doRead() {

		Buff.affect(curUser, Recharging.class, BUFF_DURATION);
		charge(curUser);
		
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();

		GLog.i( Messages.get(this, "surge") );
		SpellSprite.show( curUser, SpellSprite.CHARGE );
		setKnown();

		readAnimation();
	}
	
	@Override
	public void empoweredRead() {
		doRead();
		Buff.append(curUser, Recharging.class, BUFF_DURATION/3f);
	}
	
	public static void charge( Char user ) {
		user.sprite.centerEmitter().burst( EnergyParticle.FACTORY, 15 );
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
