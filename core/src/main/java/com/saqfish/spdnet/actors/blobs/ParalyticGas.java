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

package com.saqfish.spdnet.actors.blobs;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Actor;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.Paralysis;
import com.saqfish.spdnet.effects.BlobEmitter;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.messages.Messages;

public class ParalyticGas extends Blob {
	
	{
		//acts after mobs, to give them a chance to resist paralysis
		actPriority = MOB_PRIO - 1;
	}
	
	@Override
	protected void evolve() {
		super.evolve();
		
		Char ch;
		int cell;

		for (int i = area.left; i < area.right; i++) {
			for (int j = area.top; j < area.bottom; j++) {
				cell = i + j * Dungeon.level.width();
				if (cur[cell] > 0 && (ch = Actor.findChar(cell)) != null) {
					if (!ch.isImmune(this.getClass()))
						Buff.prolong(ch, Paralysis.class, Paralysis.DURATION);
				}
			}
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		
		emitter.pour( Speck.factory( Speck.PARALYSIS ), 0.4f );
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
