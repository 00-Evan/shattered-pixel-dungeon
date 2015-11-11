/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class ScrollOfRecharging extends Scroll {

	public static final float BUFF_DURATION = 30f;

	{
		name = "Scroll of Recharging";
		initials = "Re";
	}
	
	@Override
	protected void doRead() {

		Buff.affect(curUser, Recharging.class, BUFF_DURATION);
		charge(curUser);
		
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();

		GLog.i( "a surge of energy courses through your body, invigorating your wands.");
		SpellSprite.show( curUser, SpellSprite.CHARGE );
		setKnown();

		readAnimation();
	}
	
	@Override
	public String desc() {
		return
			"The raw magical power bound up in this parchment will, when released, " +
			"charge up all the users wands over time.";
	}
	
	public static void charge( Hero hero ) {
		hero.sprite.centerEmitter().burst( EnergyParticle.FACTORY, 15 );
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}


	public static class Recharging extends FlavourBuff {

		@Override
		public int icon() {
			return BuffIndicator.RECHARGING;
		}

		@Override
		public String toString() {
			return "Recharging";
		}

		//want to process partial turns for this buff, and not count it when it's expiring.
		//firstly, if this buff has half a turn left, should give out half the benefit.
		//secondly, recall that buffs execute in random order, so this can cause a problem where we can't simply check
		//if this buff is still attached, must instead directly check its remaining time, and act accordingly.
		//otherwise this causes inconsistent behaviour where this may detach before, or after, a wand charger acts.
		public float remainder() {
			return Math.min(1f, this.cooldown());
		}

		@Override
		public String desc() {
			return "Energy is coursing through you, improving the rate that your wands and staffs charge.\n" +
					"\n" +
					"Each turn this buff will increase current charge by one quarter, in addition to regular recharge. \n" +
					"\n" +
					"The recharging will last for " + dispTurns() + ".";
		}
	}
}
