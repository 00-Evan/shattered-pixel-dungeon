/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class Flashbang extends Bomb {
	
	{
		//TODO visuals
		image = ItemSpriteSheet.FLASHBANG;
	}
	
	@Override
	public void explode(int cell) {
		//We're blowing up, so no need for a fuse anymore.
		this.fuse = null;
		
		Sample.INSTANCE.play( Assets.SND_BLAST );
		
		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.center( cell ).burst( BlastParticle.FACTORY, 30 );
		}
		
		//no regular explosion damage
		
		//FIXME currently has odd behaviour for the hero
		//TODO final balancing for effect here, based on distance? perhaps also cripple/weaken?
		for (Char ch : Actor.chars()){
			if (ch == Dungeon.hero){
				if (Dungeon.level.heroFOV[cell]){
					Buff.prolong(ch, Blindness.class, 5f);
					GameScene.flash(0xFFFFFF);
				}
			} else if (ch.fieldOfView[cell]){
				Buff.prolong(ch, Blindness.class, 5f);
				if (ch == Dungeon.hero){
					GameScene.flash(0xFFFFFF);
				}
			}
		}
		
		
	}
}
