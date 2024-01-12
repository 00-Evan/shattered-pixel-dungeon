/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.List;

//used for various enemy attacks to keep track of rocks that will fall after some number of turns
public class DelayedRockFall extends FlavourBuff {

	private int[] rockPositions;
	private ArrayList<Emitter> rockEmitters = new ArrayList<>();

	public void setRockPositions( List<Integer> rockPositions ) {
		this.rockPositions = new int[rockPositions.size()];
		for (int i = 0; i < rockPositions.size(); i++){
			this.rockPositions[i] = rockPositions.get(i);
		}

		fx(true);
	}

	@Override
	public boolean act() {
		for (int i : rockPositions){
			CellEmitter.get( i ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );

			Char ch = Actor.findChar(i);
			if (ch != null){
				affectChar(ch);
			} else {
				affectCell(i);
			}
		}

		PixelScene.shake( 3, 0.7f );
		Sample.INSTANCE.play(Assets.Sounds.ROCKS);

		detach();
		return super.act();
	}

	public void affectChar( Char ch ){
		//do nothing by default
	}

	public void affectCell( int cell ){
		//do nothing by default
	}

	@Override
	public void fx(boolean on) {
		if (on && rockPositions != null){
			for (int i : this.rockPositions){
				Emitter e = CellEmitter.get(i);
				e.y -= DungeonTilemap.SIZE*0.2f;
				e.height *= 0.4f;
				e.pour(EarthParticle.FALLING, 0.1f);
				rockEmitters.add(e);
			}
		} else {
			for (Emitter e : rockEmitters){
				e.on = false;
			}
		}
	}

	private static final String POSITIONS = "positions";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(POSITIONS, rockPositions);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		rockPositions = bundle.getIntArray(POSITIONS);
	}

}
