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

package net.casiello.pixeldungeonrescue.actors.blobs;

import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.Actor;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Vertigo;
import net.casiello.pixeldungeonrescue.effects.BlobEmitter;
import net.casiello.pixeldungeonrescue.effects.Speck;
import net.casiello.pixeldungeonrescue.messages.Messages;

public class ConfusionGas extends Blob {

	@Override
	protected void evolve() {
		super.evolve();

		Char ch;
		int cell;

		for (int i = area.left; i < area.right; i++){
			for (int j = area.top; j < area.bottom; j++){
				cell = i + j*Dungeon.level.width();
				if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
					if (!ch.isImmune(this.getClass())) {
						Buff.prolong(ch, Vertigo.class, 2);
					}
				}
			}
		}
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory( Speck.CONFUSION, true ), 0.4f );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}