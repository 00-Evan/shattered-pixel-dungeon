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
package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import java.util.HashSet;

public class EarthImbue extends FlavourBuff {

	public static final float DURATION	= 30f;

	public void proc(Char enemy){
		Buff.affect(enemy, Roots.class, 2);
		CellEmitter.bottom(enemy.pos).start(EarthParticle.FACTORY, 0.05f, 8);
	}

	@Override
	public int icon() {
		return BuffIndicator.ROOTS;
	}

	@Override
	public String toString() {
		return "Imbued with Earth";
	}

	@Override
	public String desc() {
		return "You are inbued with the power of earth!\n" +
				"\n" +
				"All physical attacks will command roots to lock the enemy in place while the effect lasts.\n" +
				"\n" +
				"You are imbued for " + dispTurns() + ".";
	}

	{
		immunities.add( Paralysis.class );
		immunities.add( Roots.class );
		immunities.add( Slow.class );
	}
}