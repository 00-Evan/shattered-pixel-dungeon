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

import java.util.ArrayList;

import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class ScrollOfMirrorImage extends Scroll {

	private static final int NIMAGES	= 3;
	
	{
		name = "Scroll of Mirror Image";
		initials = "MI";
	}
	
	@Override
	protected void doRead() {
		
		ArrayList<Integer> respawnPoints = new ArrayList<Integer>();
		
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			int p = curUser.pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
				respawnPoints.add( p );
			}
		}
		
		int nImages = NIMAGES;
		while (nImages > 0 && respawnPoints.size() > 0) {
			int index = Random.index( respawnPoints );
			
			MirrorImage mob = new MirrorImage();
			mob.duplicate( curUser );
			GameScene.add( mob );
			ScrollOfTeleportation.appear( mob, respawnPoints.get( index ) );
			
			respawnPoints.remove( index );
			nImages--;
		}
		
		if (nImages < NIMAGES) {
			setKnown();
		}
		
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		
		curUser.spendAndNext( TIME_TO_READ );
	}
	
	@Override
	public String desc() {
		return
			"The incantation on this scroll will create illusionary twins of the reader, which will chase his enemies.";
	}
}
