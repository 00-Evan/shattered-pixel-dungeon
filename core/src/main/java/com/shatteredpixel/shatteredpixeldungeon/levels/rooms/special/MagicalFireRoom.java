/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class MagicalFireRoom extends SpecialRoom {

	@Override
	public int minWidth() { return 7; }
	public int minHeight() { return 7; }

	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Door door = entrance();
		door.set( Door.Type.REGULAR );

		Point firePos = center();
		Room behindFire = new EmptyRoom();

		if (door.x == left || door.x == right){
			firePos.y = top+1;
			while (firePos.y != bottom){
				Blob.seed(level.pointToCell(firePos), 1, EternalFire.class, level);
				Painter.set(level, firePos, Terrain.EMPTY_SP);
				firePos.y++;
			}
			if (door.x == left){
				behindFire.set(firePos.x+1, top+1, right-1, bottom-1);
			} else {
				behindFire.set(left+1, top+1, firePos.x-1, bottom-1);
			}
		} else {
			firePos.x = left+1;
			while (firePos.x != right){
				Blob.seed(level.pointToCell(firePos), 1, EternalFire.class, level);
				Painter.set(level, firePos, Terrain.EMPTY_SP);
				firePos.x++;
			}
			if (door.y == top){
				behindFire.set(left+1, firePos.y+1, right-1, bottom-1);
			} else {
				behindFire.set(left+1, top+1, right-1, firePos.y-1);
			}
		}

		Painter.fill(level, behindFire, Terrain.EMPTY_SP);

		boolean honeyPot = Random.Int( 2 ) == 0;

		int n = Random.IntRange( 3, 4 );

		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(behindFire.random(0));
			} while (level.heaps.get(pos) != null);
			if (honeyPot){
				level.drop( new Honeypot(), pos);
				honeyPot = false;
			} else
				level.drop( prize( level ), pos );
		}

		level.addItemToSpawn(new PotionOfFrost());

	}

	private static Item prize( Level level ) {

		if (Random.Int(3) != 0){
			Item prize = level.findPrizeItem();
			if (prize != null)
				return prize;
		}

		return Generator.random( Random.oneOf(
				Generator.Category.POTION,
				Generator.Category.SCROLL,
				Generator.Category.FOOD,
				Generator.Category.GOLD
		) );
	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		Blob fire = l.blobs.get(EternalFire.class);

		//disallow placing on special tiles or next to fire if fire is present.
		//note that this is slightly brittle, assumes the fire is either all there or totally gone
		if (fire != null && fire.volume > 0){
			int cell = l.pointToCell(p);
			if (l.map[cell] == Terrain.EMPTY_SP) return false;

			if (fire.cur[cell] > 0)     return false;
			for (int i : PathFinder.NEIGHBOURS4){
				if (fire.cur[cell+i] > 0)   return false;
			}
		}

		return super.canPlaceCharacter(p, l);
	}

	public static class EternalFire extends Blob {

		@Override
		protected void evolve() {

			int cell;

			Freezing freeze = (Freezing)Dungeon.level.blobs.get( Freezing.class );
			Blizzard bliz = (Blizzard)Dungeon.level.blobs.get( Blizzard.class );

			Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );

			//if any part of the fire is cleared, cleanse the whole thing
			//Note that this is a bit brittle atm, it assumes only one group of eternal fire per floor
			boolean clearAll = false;

			Level l = Dungeon.level;
			for (int i = area.left - 1; i <= area.right; i++){
				for (int j = area.top - 1; j <= area.bottom; j++){
					cell = i + j*l.width();

					if (cur[cell] > 0){
						//evaporates in the presence of water, frost, or blizzard
						//this blob is not considered interchangeable with fire, so those blobs do not interact with it otherwise
						//potion of purity can cleanse it though
						if (l.water[cell]){
							cur[cell] = 0;
							clearAll = true;
						}
						if (freeze != null && freeze.volume > 0 && freeze.cur[cell] > 0){
							freeze.clear(cell);
							cur[cell] = 0;
							clearAll = true;
						}
						if (bliz != null && bliz.volume > 0 && bliz.cur[cell] > 0){
							bliz.clear(cell);
							cur[cell] = 0;
							clearAll = true;
						}
						l.passable[cell] = cur[cell] == 0 && (Terrain.flags[l.map[cell]] & Terrain.PASSABLE) != 0;
					}

					if (cur[cell] > 0
							|| cur[cell-1] > 0
							|| cur[cell+1] > 0
							|| cur[cell-Dungeon.level.width()] > 0
							|| cur[cell+Dungeon.level.width()] > 0) {
						//spread fire to nearby flammable cells
						if (Dungeon.level.flamable[cell] && (fire == null || fire.volume == 0 || fire.cur[cell] == 0)){
							GameScene.add(Blob.seed(cell, 4, Fire.class));
						}

						//ignite adjacent chars
						Char ch = Actor.findChar(cell);
						if (ch != null && !ch.isImmune(getClass())) {
							Buff.affect(ch, Burning.class).reignite(ch, 4f);
						}
					}

					off[cell] = cur[cell];
					volume += off[cell];
				}
			}

			if (clearAll){
				fullyClear();
				return;
			}

		}

		@Override
		public void seed(Level level, int cell, int amount) {
			super.seed(level, cell, amount);
			level.passable[cell] = cur[cell] == 0 && (Terrain.flags[level.map[cell]] & Terrain.PASSABLE) != 0;
		}

		@Override
		public void clear(int cell) {
			if (volume > 0 && cur[cell] > 0) {
				fullyClear();
			}
		}

		@Override
		public void fullyClear() {
			super.fullyClear();
			Dungeon.level.buildFlagMaps();
		}

		@Override
		public void use( BlobEmitter emitter ) {
			super.use( emitter );
			emitter.pour( ElmoParticle.FACTORY, 0.02f );
		}

		@Override
		public String tileDesc() {
			return Messages.get(this, "desc");
		}

		@Override
		public void onBuildFlagMaps( Level l ) {
			if (volume > 0){
				for (int i=0; i < l.length(); i++) {
					l.passable[i] = l.passable[i] && cur[i] == 0;
				}
			}
		}
	}

}
