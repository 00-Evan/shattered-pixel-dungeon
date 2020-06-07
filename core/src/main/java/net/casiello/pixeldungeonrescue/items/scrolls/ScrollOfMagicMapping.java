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
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.buffs.Awareness;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Invisibility;
import net.casiello.pixeldungeonrescue.actors.buffs.MindVision;
import net.casiello.pixeldungeonrescue.effects.CellEmitter;
import net.casiello.pixeldungeonrescue.effects.Speck;
import net.casiello.pixeldungeonrescue.effects.SpellSprite;
import net.casiello.pixeldungeonrescue.levels.Terrain;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.scenes.GameScene;
import net.casiello.pixeldungeonrescue.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfMagicMapping extends Scroll {

	{
		initials = 2;
	}

	@Override
	public void doRead() {
		
		int length = Dungeon.level.length();
		int[] map = Dungeon.level.map;
		boolean[] mapped = Dungeon.level.mapped;
		boolean[] discoverable = Dungeon.level.discoverable;
		
		boolean noticed = false;
		
		for (int i=0; i < length; i++) {
			
			int terr = map[i];
			
			if (discoverable[i]) {
				
				mapped[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					
					Dungeon.level.discover( i );
					
					if (Dungeon.level.heroFOV[i]) {
						GameScene.discoverTile( i, terr );
						discover( i );
						
						noticed = true;
					}
				}
			}
		}
		GameScene.updateFog();
		
		GLog.i( Messages.get(this, "layout") );
		if (noticed) {
			Sample.INSTANCE.play( Assets.SND_SECRET );
		}
		
		SpellSprite.show( curUser, SpellSprite.MAP );
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		
		setKnown();

		readAnimation();
	}
	
	@Override
	public void empoweredRead() {
		doRead();
		Buff.affect( curUser, MindVision.class, MindVision.DURATION );
		Buff.affect( curUser, Awareness.class, Awareness.DURATION );
		Dungeon.observe();
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
	
	public static void discover( int cell ) {
		CellEmitter.get( cell ).start( Speck.factory( Speck.DISCOVER ), 0.1f, 4 );
	}
}
