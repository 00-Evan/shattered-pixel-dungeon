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

package net.casiello.pixeldungeonrescue.plants;

import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.blobs.Blob;
import net.casiello.pixeldungeonrescue.actors.blobs.Fire;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.FireImbue;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.actors.hero.HeroSubClass;
import net.casiello.pixeldungeonrescue.effects.CellEmitter;
import net.casiello.pixeldungeonrescue.effects.particles.FlameParticle;
import net.casiello.pixeldungeonrescue.scenes.GameScene;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;

public class Firebloom extends Plant {
	
	{
		image = 1;
	}
	
	@Override
	public void activate( Char ch ) {
		
		if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN){
			Buff.affect(ch, FireImbue.class).set(15f);
		}
		
		GameScene.add( Blob.seed( pos, 2, Fire.class ) );
		
		if (Dungeon.level.heroFOV[pos]) {
			CellEmitter.get( pos ).burst( FlameParticle.FACTORY, 5 );
		}
	}
	
	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_FIREBLOOM;

			plantClass = Firebloom.class;
		}
	}
}
