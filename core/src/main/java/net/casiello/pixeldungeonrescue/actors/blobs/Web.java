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
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Roots;
import net.casiello.pixeldungeonrescue.effects.BlobEmitter;
import net.casiello.pixeldungeonrescue.effects.particles.WebParticle;
import net.casiello.pixeldungeonrescue.levels.Level;
import net.casiello.pixeldungeonrescue.levels.Terrain;
import net.casiello.pixeldungeonrescue.messages.Messages;

public class Web extends Blob {

	{
		//acts before the hero, to ensure terrain is adjusted correctly
		actPriority = HERO_PRIO+1;
	}
	
	@Override
	protected void evolve() {

		int cell;

		Level l = Dungeon.level;
		for (int i = area.left; i < area.right; i++){
			for (int j = area.top; j < area.bottom; j++){
				cell = i + j*l.width();
				off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

				volume += off[cell];

				l.solid[cell] = off[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.SOLID) != 0;
			}
		}
	}

	@Override
	public void seed(Level level, int cell, int amount) {
		super.seed(level, cell, amount);
		level.solid[cell] = cur[cell] > 0 || (Terrain.flags[level.map[cell]] & Terrain.SOLID) != 0;
	}

	//affects characters as they step on it. See Level.OccupyCell and Level.PressCell
	public static void affectChar( Char ch ){
		Buff.prolong( ch, Roots.class, 5f );
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		
		emitter.pour( WebParticle.FACTORY, 0.25f );
	}

	@Override
	public void clear(int cell) {
		super.clear(cell);
		if (cur == null) return;
		Level l = Dungeon.level;
		l.solid[cell] = cur[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.SOLID) != 0;
	}

	@Override
	public void fullyClear() {
		super.fullyClear();
		Dungeon.level.buildFlagMaps();
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
