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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfTeleportation extends Scroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_TELEPORT;
	}

	@Override
	public void doRead() {

		Sample.INSTANCE.play( Assets.Sounds.READ );
		Invisibility.dispel();
		
		teleportPreferringUnseen( curUser );
		setKnown();

		readAnimation();
	}
	
	@Override
	public void empoweredRead() {
		
		if (Dungeon.bossLevel()){
			GLog.w( Messages.get(this, "no_tele") );
			return;
		}
		
		GameScene.selectCell(new CellSelector.Listener() {
			@Override
			public void onSelect(Integer target) {
				if (target != null) {
					//time isn't spent
					((HeroSprite)curUser.sprite).read();
					teleportToLocation(curUser, target);
					
				}
			}
			
			@Override
			public String prompt() {
				return Messages.get(ScrollOfTeleportation.class, "prompt");
			}
		});
	}
	
	public static void teleportToLocation(Hero hero, int pos){
		PathFinder.buildDistanceMap(pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
		if (PathFinder.distance[hero.pos] == Integer.MAX_VALUE
				|| (!Dungeon.level.passable[pos] && !Dungeon.level.avoid[pos])
				|| Actor.findChar(pos) != null){
			GLog.w( Messages.get(ScrollOfTeleportation.class, "cant_reach") );
			return;
		}
		
		appear( hero, pos );
		Dungeon.level.occupyCell(hero );
		Dungeon.observe();
		GameScene.updateFog();
		
	}
	
	public static void teleportHero( Hero hero ) {
		teleportChar( hero );
	}
	
	public static void teleportChar( Char ch ) {

		if (Dungeon.bossLevel()){
			GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
			return;
		}
		
		int count = 10;
		int pos;
		do {
			pos = Dungeon.level.randomRespawnCell( ch );
			if (count-- <= 0) {
				break;
			}
		} while (pos == -1 || Dungeon.level.secret[pos]);
		
		if (pos == -1) {
			
			GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
			
		} else {
			
			appear( ch, pos );
			Dungeon.level.occupyCell( ch );
			
			if (ch == Dungeon.hero) {
				GLog.i( Messages.get(ScrollOfTeleportation.class, "tele") );
				
				Dungeon.observe();
				GameScene.updateFog();
			}
			
		}
	}
	
	public static void teleportPreferringUnseen( Hero hero ){
		
		if (Dungeon.bossLevel() || !(Dungeon.level instanceof RegularLevel)){
			teleportHero( hero );
			return;
		}
		
		RegularLevel level = (RegularLevel) Dungeon.level;
		ArrayList<Integer> candidates = new ArrayList<>();
		
		for (Room r : level.rooms()){
			if (r instanceof SpecialRoom){
				int terr;
				boolean locked = false;
				for (Point p : r.getPoints()){
					terr = level.map[level.pointToCell(p)];
					if (terr == Terrain.LOCKED_DOOR || terr == Terrain.BARRICADE){
						locked = true;
						break;
					}
				}
				if (locked){
					continue;
				}
			}
			
			int cell;
			for (Point p : r.charPlaceablePoints(level)){
				cell = level.pointToCell(p);
				if (level.passable[cell] && !level.visited[cell] && !level.secret[cell] && Actor.findChar(cell) == null){
					candidates.add(cell);
				}
			}
		}
		
		if (candidates.isEmpty()){
			teleportHero( hero );
		} else {
			int pos = Random.element(candidates);
			boolean secretDoor = false;
			int doorPos = -1;
			if (level.room(pos) instanceof SpecialRoom){
				SpecialRoom room = (SpecialRoom) level.room(pos);
				if (room.entrance() != null){
					doorPos = level.pointToCell(room.entrance());
					for (int i : PathFinder.NEIGHBOURS8){
						if (!room.inside(level.cellToPoint(doorPos + i))
								&& level.passable[doorPos + i]
								&& Actor.findChar(doorPos + i) == null){
							secretDoor = room instanceof SecretRoom;
							pos = doorPos + i;
							break;
						}
					}
				}
			}
			GLog.i( Messages.get(ScrollOfTeleportation.class, "tele") );
			appear( hero, pos );
			Dungeon.level.occupyCell(hero );
			if (secretDoor && level.map[doorPos] == Terrain.SECRET_DOOR){
				Sample.INSTANCE.play( Assets.Sounds.SECRET );
				int oldValue = Dungeon.level.map[doorPos];
				GameScene.discoverTile( doorPos, oldValue );
				Dungeon.level.discover( doorPos );
				ScrollOfMagicMapping.discover( doorPos );
			}
			Dungeon.observe();
			GameScene.updateFog();
		}
		
	}

	public static void appear( Char ch, int pos ) {

		ch.sprite.interruptMotion();

		if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[ch.pos]){
			Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
		}

		ch.move( pos );
		if (ch.pos == pos) ch.sprite.place( pos );

		if (ch.invisible == 0) {
			ch.sprite.alpha( 0 );
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
		}

		if (Dungeon.level.heroFOV[pos] || ch == Dungeon.hero ) {
			ch.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		}
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
