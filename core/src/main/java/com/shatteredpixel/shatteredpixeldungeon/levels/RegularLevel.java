/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.LegacyBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.PassageRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.PitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.TunnelRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.WeakFloorRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FireTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornTrap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class RegularLevel extends Level {
	
	protected ArrayList<Room> rooms;
	
	protected Builder builder;
	
	protected Room roomEntrance;
	protected Room roomExit;
	
	public int secretDoors;
	
	@Override
	protected boolean build() {
		
		builder = builder();
		
		rooms = builder.build(null);
		
		if (rooms == null){
			return false;
		}
		
		roomEntrance = ((LegacyBuilder)builder).roomEntrance;
		roomExit = ((LegacyBuilder)builder).roomExit;
		
		if (!paint()){
			return false;
		}
		
		paintWater();
		paintGrass();
		
		placeTraps();
		
		return true;
	}
	
	protected Builder builder(){
		return new LegacyBuilder(LegacyBuilder.Type.REGULAR,
				width, height, minRoomSize, maxRoomSize);
	}

	protected void placeSign(){
		while (true) {
			int pos = pointToCell(roomEntrance.random());
			if (pos != entrance && traps.get(pos) == null && findMob(pos) == null) {
				map[pos] = Terrain.SIGN;
				break;
			}
		}
	}
	
	protected void paintWater() {
		boolean[] lake = water();
		for (int i=0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && lake[i]) {
				map[i] = Terrain.WATER;
			}
		}
	}
	
	protected void paintGrass() {
		boolean[] grass = grass();
		
		if (feeling == Feeling.GRASS) {
			
			for (Room room : rooms) {
				if (!(room instanceof TunnelRoom || room instanceof PassageRoom)) {
					grass[(room.left + 1) + (room.top + 1) * width()] = true;
					grass[(room.right - 1) + (room.top + 1) * width()] = true;
					grass[(room.left + 1) + (room.bottom - 1) * width()] = true;
					grass[(room.right - 1) + (room.bottom - 1) * width()] = true;
				}
			}
		}

		for (int i=width()+1; i < length()-width()-1; i++) {
			if (map[i] == Terrain.EMPTY && grass[i]) {
				int count = 1;
				for (int n : PathFinder.NEIGHBOURS8) {
					if (grass[i + n]) {
						count++;
					}
				}
				map[i] = (Random.Float() < count / 12f) ? Terrain.HIGH_GRASS : Terrain.GRASS;
			}
		}
	}
	
	protected abstract boolean[] water();
	protected abstract boolean[] grass();
	
	protected void placeTraps() {
		
		int nTraps = nTraps();
		float[] trapChances = trapChances();
		Class<?>[] trapClasses = trapClasses();

		ArrayList<Integer> validCells = new ArrayList<>();

		for (int i = 0; i < length(); i ++) {
			if (map[i] == Terrain.EMPTY){

				if(Dungeon.depth == 1){
					//extra check to prevent annoying inactive traps in hallways on floor 1
					Room r = room(i);
					if (r instanceof StandardRoom){
						validCells.add(i);
					}
				} else
					validCells.add(i);
			}
		}

		//no more than one trap every 5 valid tiles.
		nTraps = Math.min(nTraps, validCells.size()/5);

		for (int i = 0; i < nTraps; i++) {
			
			Integer trapPos = Random.element(validCells);
			validCells.remove(trapPos); //removes the integer object, not at the index

			try {
				Trap trap = ((Trap)trapClasses[Random.chances( trapChances )].newInstance()).hide();
				setTrap( trap, trapPos );
				//some traps will not be hidden
				map[trapPos] = trap.visible ? Terrain.TRAP : Terrain.SECRET_TRAP;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	protected int nTraps() {
		return Random.NormalIntRange( 1, 4+(Dungeon.depth/2) );
	}
	
	protected Class<?>[] trapClasses(){
		return new Class<?>[]{WornTrap.class};
	}

	protected float[] trapChances() {
		return new float[]{1};
	}
	
	protected int minRoomSize = 7;
	protected int maxRoomSize = 9;
	
	protected boolean paint() {
		
		for (Room r : rooms) {
			placeDoors( r );
			r.paint( this );
		}
		
		for (Room r : rooms) {
			paintDoors( r );
		}
		
		return true;
	}
	
	private void placeDoors( Room r ) {
		for (Room n : r.connected.keySet()) {
			Room.Door door = r.connected.get( n );
			if (door == null) {
				
				Rect i = r.intersect( n );
				if (i.width() == 0) {
					door = new Room.Door(
						i.left,
						Random.Int( i.top + 1, i.bottom ) );
				} else {
					door = new Room.Door(
						Random.Int( i.left + 1, i.right ),
						i.top);
				}

				r.connected.put( n, door );
				n.connected.put( r, door );
			}
		}
	}
	
	protected void paintDoors( Room r ) {
		for (Room n : r.connected.keySet()) {

			if (joinRooms( r, n )) {
				continue;
			}
			
			Room.Door d = r.connected.get( n );
			int door = d.x + d.y * width();
			
			switch (d.type) {
			case EMPTY:
				map[door] = Terrain.EMPTY;
				break;
			case TUNNEL:
				map[door] =  tunnelTile();
				break;
			case REGULAR:
				if (Dungeon.depth <= 1) {
					map[door] = Terrain.DOOR;
				} else {
					boolean secret = (Dungeon.depth < 6 ? Random.Int( 12 - Dungeon.depth ) : Random.Int( 6 )) == 0;
					map[door] = secret ? Terrain.SECRET_DOOR : Terrain.DOOR;
					if (secret) {
						secretDoors++;
					}
				}
				break;
			case UNLOCKED:
				map[door] = Terrain.DOOR;
				break;
			case HIDDEN:
				map[door] = Terrain.SECRET_DOOR;
				secretDoors++;
				break;
			case BARRICADE:
				map[door] = Terrain.BARRICADE;
				break;
			case LOCKED:
				map[door] = Terrain.LOCKED_DOOR;
				break;
			}
		}
	}
	
	protected boolean joinRooms( Room r, Room n ) {
		
		if (!(r instanceof StandardRoom && n instanceof StandardRoom)) {
			return false;
		}
		
		Rect w = r.intersect( n );
		if (w.left == w.right) {
			
			if (w.bottom - w.top < 3) {
				return false;
			}
			
			if (w.height() == Math.max( r.height(), n.height() )) {
				return false;
			}
			
			if (r.width() + n.width() > maxRoomSize) {
				return false;
			}
			
			w.top += 1;
			w.bottom -= 0;
			
			w.right++;
			
			Painter.fill( this, w.left, w.top, 1, w.height(), Terrain.EMPTY );
			
		} else {
			
			if (w.right - w.left < 3) {
				return false;
			}
			
			if (w.width() == Math.max( r.width(), n.width() )) {
				return false;
			}
			
			if (r.height() + n.height() > maxRoomSize) {
				return false;
			}
			
			w.left += 1;
			w.right -= 0;
			
			w.bottom++;
			
			Painter.fill( this, w.left, w.top, w.width(), 1, Terrain.EMPTY );
		}
		
		return true;
	}
	
	@Override
	public int nMobs() {
		switch(Dungeon.depth) {
			case 1:
				//mobs are not randomly spawned on floor 1.
				return 0;
			default:
				return 2 + Dungeon.depth % 5 + Random.Int(5);
		}
	}
	
	@Override
	protected void createMobs() {
		//on floor 1, 10 rats are created so the player can get level 2.
		int mobsToSpawn = Dungeon.depth == 1 ? 10 : nMobs();

		ArrayList<Room> stdRooms = new ArrayList<>();
		for (Room room : rooms) {
			if (room instanceof StandardRoom) stdRooms.add(room);
		}
		Iterator<Room> stdRoomIter = stdRooms.iterator();

		while (mobsToSpawn > 0) {
			if (!stdRoomIter.hasNext())
				stdRoomIter = stdRooms.iterator();
			Room roomToSpawn = stdRoomIter.next();

			Mob mob = Bestiary.mob( Dungeon.depth );
			mob.pos = pointToCell(roomToSpawn.random());

			if (findMob(mob.pos) == null && Level.passable[mob.pos]) {
				mobsToSpawn--;
				mobs.add(mob);

				//TODO: perhaps externalize this logic into a method. Do I want to make mobs more likely to clump deeper down?
				if (mobsToSpawn > 0 && Random.Int(4) == 0){
					mob = Bestiary.mob( Dungeon.depth );
					mob.pos = pointToCell(roomToSpawn.random());

					if (findMob(mob.pos)  == null && Level.passable[mob.pos]) {
						mobsToSpawn--;
						mobs.add(mob);
					}
				}
			}
		}

		for (Mob m : mobs){
			if (map[m.pos] == Terrain.HIGH_GRASS) {
				map[m.pos] = Terrain.GRASS;
				losBlocking[m.pos] = false;
			}

		}

	}
	
	@Override
	public int randomRespawnCell() {
		int count = 0;
		int cell = -1;
		
		while (true) {
			
			if (++count > 30) {
				return -1;
			}
			
			Room room = randomRoom( StandardRoom.class, 10 );
			if (room == null) {
				continue;
			}
			
			cell = pointToCell(room.random());
			if (!Dungeon.visible[cell] && Actor.findChar( cell ) == null && Level.passable[cell]) {
				return cell;
			}
			
		}
	}
	
	@Override
	public int randomDestination() {
		
		int cell = -1;
		
		while (true) {
			
			Room room = Random.element( rooms );
			if (room == null) {
				continue;
			}
			
			cell = pointToCell(room.random());
			if (Level.passable[cell]) {
				return cell;
			}
			
		}
	}
	
	@Override
	protected void createItems() {
		
		int nItems = 3;
		int bonus = RingOfWealth.getBonus(Dungeon.hero, RingOfWealth.Wealth.class);

		//just incase someone gets a ridiculous ring, cap this at 80%
		bonus = Math.min(bonus, 10);
		while (Random.Float() < (0.3f + bonus*0.05f)) {
			nItems++;
		}
		
		for (int i=0; i < nItems; i++) {
			Heap.Type type = null;
			switch (Random.Int( 20 )) {
			case 0:
				type = Heap.Type.SKELETON;
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				type = Heap.Type.CHEST;
				break;
			case 5:
				type = Dungeon.depth > 1 ? Heap.Type.MIMIC : Heap.Type.CHEST;
				break;
			default:
				type = Heap.Type.HEAP;
			}
			int cell = randomDropCell();
			if (map[cell] == Terrain.HIGH_GRASS) map[cell] = Terrain.GRASS;
			drop( Generator.random(), cell ).type = type;
		}

		for (Item item : itemsToSpawn) {
			int cell;
			do {
				cell = randomDropCell();
				if (item instanceof Scroll) {
					while (traps.get(cell) instanceof FireTrap) {
						cell = randomDropCell();
					}

				} else if (item instanceof Potion) {
					while (traps.get(cell) instanceof ChillingTrap) {
						cell = randomDropCell();
					}
				}
			} while (traps.get(cell) instanceof ExplosiveTrap);
			drop( item, cell ).type = Heap.Type.HEAP;
			if (map[cell] == Terrain.HIGH_GRASS) map[cell] = Terrain.GRASS;
		}
		
		Item item = Bones.get();
		if (item != null) {
			int cell = randomDropCell();
			if (map[cell] == Terrain.HIGH_GRASS) map[cell] = Terrain.GRASS;
			drop( item, cell ).type = Heap.Type.REMAINS;
		}
	}
	
	protected Room randomRoom( Class<?extends Room> type, int tries ) {
		for (int i=0; i < tries; i++) {
			Room room = Random.element( rooms );
			if (room.getClass().equals(type)) {
				return room;
			}
		}
		return null;
	}
	
	public Room room( int pos ) {
		for (Room room : rooms) {
			if (room.inside( cellToPoint(pos) )) {
				return room;
			}
		}
		
		return null;
	}
	
	protected int randomDropCell() {
		while (true) {
			Room room = randomRoom( StandardRoom.class, 1 );
			if (room != null) {
				int pos = pointToCell(room.random());
				if (passable[pos]) {
					return pos;
				}
			}
		}
	}
	
	@Override
	public int pitCell() {
		for (Room room : rooms) {
			if (room instanceof PitRoom) {
				return pointToCell(room.random());
			}
		}
		
		return super.pitCell();
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "rooms", rooms );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );

		//TODO implement legacytype support here
		rooms = new ArrayList<>( (Collection<Room>) ((Collection<?>) bundle.getCollection( "rooms" )) );
		for (Room r : rooms) {
			if (r instanceof WeakFloorRoom || r.legacyType.equals("WEAK_FLOOR")) {
				weakFloorCreated = true;
				break;
			}
			if (r instanceof EntranceRoom || r.legacyType.equals("ENTRANCE")){
				roomEntrance = r;
			} else if (r instanceof ExitRoom  || r.legacyType.equals("EXIT")){
				roomExit = r;
			}
		}
	}
	
}
