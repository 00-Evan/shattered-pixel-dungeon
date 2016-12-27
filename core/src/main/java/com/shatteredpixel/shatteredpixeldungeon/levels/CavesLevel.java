/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.levels.Room.Type;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FireTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.LightningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ParalyticTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SpearTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.VenomTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class CavesLevel extends RegularLevel {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
		
		viewDistance = 6;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}
	
	protected boolean[] water() {
		return Patch.generate( this, feeling == Feeling.WATER ? 0.60f : 0.45f, 6 );
	}
	
	protected boolean[] grass() {
		return Patch.generate( this, feeling == Feeling.GRASS ? 0.55f : 0.35f, 3 );
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class[]{ FireTrap.class, FrostTrap.class, PoisonTrap.class, SpearTrap.class, VenomTrap.class,
				ExplosiveTrap.class, FlashingTrap.class, GrippingTrap.class, ParalyticTrap.class, LightningTrap.class, RockfallTrap.class, OozeTrap.class,
				ConfusionTrap.class, FlockTrap.class, GuardianTrap.class, PitfallTrap.class, SummoningTrap.class, TeleportationTrap.class,
				WarpingTrap.class};
	}

	@Override
	protected float[] trapChances() {
		return new float[]{ 8, 8, 8, 8, 8,
				4, 4, 4, 4, 4, 4, 4,
				2, 2, 2, 2, 2, 2,
				1 };
	}
	
	@Override
	protected boolean assignRoomType() {
		if (!super.assignRoomType()) return false;

		if (!Blacksmith.Quest.spawn( rooms ) && Dungeon.depth == 14)
			return false;

		return true;
	}
	
	@Override
	protected void decorate() {
		
		for (Room room : rooms) {
			if (room.type != Room.Type.STANDARD) {
				continue;
			}
			
			if (room.width() <= 3 || room.height() <= 3) {
				continue;
			}
			
			int s = room.square();
			
			if (Random.Int( s ) > 8) {
				int corner = (room.left + 1) + (room.top + 1) * width();
				if (map[corner - 1] == Terrain.WALL && map[corner - width()] == Terrain.WALL) {
					map[corner] = Terrain.WALL;
					traps.remove(corner);
				}
			}
			
			if (Random.Int( s ) > 8) {
				int corner = (room.right - 1) + (room.top + 1) * width();
				if (map[corner + 1] == Terrain.WALL && map[corner - width()] == Terrain.WALL) {
					map[corner] = Terrain.WALL;
					traps.remove(corner);
				}
			}
			
			if (Random.Int( s ) > 8) {
				int corner = (room.left + 1) + (room.bottom - 1) * width();
				if (map[corner - 1] == Terrain.WALL && map[corner + width()] == Terrain.WALL) {
					map[corner] = Terrain.WALL;
					traps.remove(corner);
				}
			}
			
			if (Random.Int( s ) > 8) {
				int corner = (room.right - 1) + (room.bottom - 1) * width();
				if (map[corner + 1] == Terrain.WALL && map[corner + width()] == Terrain.WALL) {
					map[corner] = Terrain.WALL;
					traps.remove(corner);
				}
			}

			for (Room n : room.connected.keySet()) {
				if ((n.type == Room.Type.STANDARD || n.type == Room.Type.TUNNEL) && Random.Int( 3 ) == 0) {
					Painter.set( this, room.connected.get( n ), Terrain.EMPTY_DECO );
				}
			}
		}
		
		for (int i=width() + 1; i < length() - width(); i++) {
			if (map[i] == Terrain.EMPTY) {
				int n = 0;
				if (map[i+1] == Terrain.WALL) {
					n++;
				}
				if (map[i-1] == Terrain.WALL) {
					n++;
				}
				if (map[i+width()] == Terrain.WALL) {
					n++;
				}
				if (map[i-width()] == Terrain.WALL) {
					n++;
				}
				if (Random.Int( 6 ) <= n) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		for (int i=0; i < length() - width(); i++) {
			if (map[i] == Terrain.WALL &&
					DungeonTileSheet.floorTile(map[i + width()])
					&& Random.Int( 4 ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		placeSign();
		
		if (Dungeon.bossLevel( Dungeon.depth + 1 )) {
			return;
		}
		
		for (Room r : rooms) {
			if (r.type == Type.STANDARD) {
				for (Room n : r.neigbours) {
					if (n.type == Type.STANDARD && !r.connected.containsKey( n )) {
						Rect w = r.intersect( n );
						if (w.left == w.right && w.bottom - w.top >= 5) {
							
							w.top += 2;
							w.bottom -= 1;
							
							w.right++;
							
							Painter.fill( this, w.left, w.top, 1, w.height(), Terrain.CHASM );
							
						} else if (w.top == w.bottom && w.right - w.left >= 5) {
							
							w.left += 2;
							w.right -= 1;
							
							w.bottom++;
							
							Painter.fill( this, w.left, w.top, w.width(), 1, Terrain.CHASM );
						}
					}
				}
			}
		}
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.GRASS:
				return Messages.get(CavesLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_name");
			case Terrain.WATER:
				return Messages.get(CavesLevel.class, "water_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc( int tile ) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CavesLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CavesLevel.class, "exit_desc");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_desc");
			case Terrain.WALL_DECO:
				return Messages.get(CavesLevel.class, "wall_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CavesLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		addCavesVisuals( this, visuals );
		return visuals;
	}
	
	public static void addCavesVisuals( Level level, Group group ) {
		for (int i=0; i < level.length(); i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				group.add( new Vein( i ) );
			}
		}
	}
	
	private static class Vein extends Group {
		
		private int pos;
		
		private float delay;
		
		public Vein( int pos ) {
			super();
			
			this.pos = pos;
			
			delay = Random.Float( 2 );
		}
		
		@Override
		public void update() {
			
			if (visible = Dungeon.visible[pos]) {
				
				super.update();

				if ((delay -= Game.elapsed) <= 0) {

					//pickaxe can remove the ore, should remove the sparkling too.
					if (Dungeon.level.map[pos] != Terrain.WALL_DECO){
						kill();
						return;
					}
					
					delay = Random.Float();
					
					PointF p = DungeonTilemap.tileToWorld( pos );
					((Sparkle)recycle( Sparkle.class )).reset(
						p.x + Random.Float( DungeonTilemap.SIZE ),
						p.y + Random.Float( DungeonTilemap.SIZE ) );
				}
			}
		}
	}
	
	public static final class Sparkle extends PixelParticle {
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan = 0.5f;
		}
		
		@Override
		public void update() {
			super.update();
			
			float p = left / lifespan;
			size( (am = p < 0.5f ? p * 2 : (1 - p) * 2) * 2 );
		}
	}
}