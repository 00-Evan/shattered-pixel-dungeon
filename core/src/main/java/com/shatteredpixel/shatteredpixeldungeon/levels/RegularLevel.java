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
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.levels.Room.Type;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.ShopPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FireTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornTrap;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class RegularLevel extends Level {

	protected ArrayList<Room> rooms;
	
	protected Room roomEntrance;
	protected Room roomExit;
	
	protected ArrayList<Room.Type> specials;
	
	public int secretDoors;
	
	@Override
	protected boolean build() {
		
		if (!initRooms()) {
			return false;
		}
	
		int distance;
		int retry = 0;
		int minDistance = (int)Math.sqrt( rooms.size() );
		do {
			do {
				roomEntrance = Random.element( rooms );
			} while (roomEntrance.width() < 4 || roomEntrance.height() < 4);
			
			do {
				roomExit = Random.element( rooms );
			} while (roomExit == roomEntrance || roomExit.width() < 4 || roomExit.height() < 4);
	
			Graph.buildDistanceMap( rooms, roomExit );
			distance = roomEntrance.distance();
			
			if (retry++ > 10) {
				return false;
			}
			
		} while (distance < minDistance);
		
		roomEntrance.type = Type.ENTRANCE;
		roomExit.type = Type.EXIT;
		
		ArrayList<Room> connected = new ArrayList<>();
		connected.add( roomEntrance );

		Graph.buildDistanceMap( rooms, roomExit );
		List<Room> path = Graph.buildPath( rooms, roomEntrance, roomExit );

		Room room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
			connected.add( room );
		}

		Graph.setPrice( path, roomEntrance.distance );

		Graph.buildDistanceMap( rooms, roomExit );
		path = Graph.buildPath( rooms, roomEntrance, roomExit );

		room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
			connected.add( room );
		}

		int nConnected = (int)(rooms.size() * Random.Float( 0.5f, 0.7f ));
		while (connected.size() < nConnected) {

			Room cr = Random.element( connected );
			Room or = Random.element( cr.neigbours );
			if (!connected.contains( or )) {

				cr.connect( or );
				connected.add( or );
			}
		}
		
		if (Dungeon.shopOnLevel()) {
			Room shop = null;
			for (Room r : roomEntrance.connected.keySet()) {
				if (r.connected.size() == 1 && ((r.width()-1)*(r.height()-1) >= ShopPainter.spaceNeeded())) {
					shop = r;
					break;
				}
			}
			
			if (shop == null) {
				return false;
			} else {
				shop.type = Room.Type.SHOP;
			}
		}
		
		specials = new ArrayList<Room.Type>( Room.SPECIALS );
		if (Dungeon.bossLevel( Dungeon.depth + 1 )) {
			specials.remove( Room.Type.WEAK_FLOOR );
		}
		if (Dungeon.isChallenged( Challenges.NO_ARMOR )){
			//no sense in giving an armor reward room on a run with no armor.
			specials.remove( Room.Type.CRYPT );
		}
		if (Dungeon.isChallenged( Challenges.NO_HERBALISM )){
			//sorry warden, no lucky sungrass or blandfruit seeds for you!
			specials.remove( Room.Type.GARDEN );
		}
		if (!assignRoomType())
			return false;
		
		paint();
		paintWater();
		paintGrass();
		
		placeTraps();
		
		return true;
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
	
	protected boolean initRooms() {

		rooms = new ArrayList<>();
		split( new Rect( 0, 0, width() - 1, height() - 1 ) );
		
		if (rooms.size() < 8) {
			return false;
		}

		Room[] ra = rooms.toArray( new Room[0] );
		for (int i=0; i < ra.length-1; i++) {
			for (int j=i+1; j < ra.length; j++) {
				ra[i].addNeigbour( ra[j] );
			}
		}
		
		return true;
	}
	
	protected boolean assignRoomType() {
		
		int specialRooms = 0;
		boolean pitMade = false;

		for (Room r : rooms) {
			if (r.type == Type.NULL &&
				r.connected.size() == 1) {

				if (specials.size() > 0 &&
					r.width() > 3 && r.height() > 3 &&
					Random.Int( specialRooms * specialRooms + 2 ) == 0) {

					if (pitRoomNeeded && !pitMade) {

						r.type = Type.PIT;
						pitMade = true;

						specials.remove( Type.ARMORY );
						specials.remove( Type.CRYPT );
						specials.remove( Type.LABORATORY );
						specials.remove( Type.LIBRARY );
						specials.remove( Type.STATUE );
						specials.remove( Type.TREASURY );
						specials.remove( Type.VAULT );
						specials.remove( Type.WEAK_FLOOR );
						
					} else if (Dungeon.depth % 5 == 2 && specials.contains( Type.LABORATORY )) {
						
						r.type = Type.LABORATORY;
						
					} else if (Dungeon.depth >= Dungeon.transmutation && specials.contains( Type.MAGIC_WELL )) {
						
						r.type = Type.MAGIC_WELL;
						
					} else {
						
						int n = specials.size();
						r.type = specials.get( Math.min( Random.Int( n ), Random.Int( n ) ) );
						if (r.type == Type.WEAK_FLOOR) {
							weakFloorCreated = true;
						}

					}
					
					Room.useType( r.type );
					specials.remove( r.type );
					specialRooms++;
					
				} else if (Random.Int( 2 ) == 0){

					ArrayList<Room> neigbours = new ArrayList<>();
					for (Room n : r.neigbours) {
						if (!r.connected.containsKey( n ) &&
							!Room.SPECIALS.contains( n.type ) &&
							n.type != Type.PIT) {
							
							neigbours.add( n );
						}
					}
					if (neigbours.size() > 1) {
						r.connect( Random.element( neigbours ) );
					}
				}
			}
		}

		if (pitRoomNeeded && !pitMade) return false;
		
		int count = 0;
		for (Room r : rooms) {
			if (r.type == Type.NULL) {
				int connections = r.connected.size();
				if (connections == 0) {
					
				} else if (Random.Int( connections * connections ) == 0) {
					r.type = Type.STANDARD;
					count++;
				} else {
					r.type = Type.TUNNEL;
				}
			}
		}

		while (count < 6) {
			Room r = randomRoom( Type.TUNNEL, 20 );
			if (r != null) {
				r.type = Type.STANDARD;
				count++;
			} else {
				return false;
			}
		}

		return true;
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
				if (room.type != Type.NULL && room.type != Type.PASSAGE && room.type != Type.TUNNEL) {
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
					if (r != null && r.type != Type.TUNNEL){
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
	
	protected void split( Rect rect ) {
		
		int w = rect.width();
		int h = rect.height();
		
		if (w > maxRoomSize && h < minRoomSize) {

			int vw = Random.Int( rect.left + 3, rect.right - 3 );
			split( new Rect( rect.left, rect.top, vw, rect.bottom ) );
			split( new Rect( vw, rect.top, rect.right, rect.bottom ) );
			
		} else
		if (h > maxRoomSize && w < minRoomSize) {

			int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
			split( new Rect( rect.left, rect.top, rect.right, vh ) );
			split( new Rect( rect.left, vh, rect.right, rect.bottom ) );
			
		} else
		if ((Random.Float() <= (minRoomSize * minRoomSize / rect.square()) && w <= maxRoomSize && h <= maxRoomSize) || w < minRoomSize || h < minRoomSize) {

			rooms.add( (Room)new Room().set( rect ) );
			
		} else {
			
			if (Random.Float() < (float)(w - 2) / (w + h - 4)) {
				int vw = Random.Int( rect.left + 3, rect.right - 3 );
				split( new Rect( rect.left, rect.top, vw, rect.bottom ) );
				split( new Rect( vw, rect.top, rect.right, rect.bottom ) );
			} else {
				int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
				split( new Rect( rect.left, rect.top, rect.right, vh ) );
				split( new Rect( rect.left, vh, rect.right, rect.bottom ) );
			}
			
		}
	}
	
	protected void paint() {
		
		for (Room r : rooms) {
			if (r.type != Type.NULL) {
				placeDoors( r );
				r.type.paint( this, r );
			} else {
				if (feeling == Feeling.CHASM && Random.Int( 2 ) == 0) {
					Painter.fill( this, r, Terrain.WALL );
				}
			}
		}
		
		for (Room r : rooms) {
			paintDoors( r );
		}
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
		
		if (r.type != Room.Type.STANDARD || n.type != Room.Type.STANDARD) {
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
			if (room.type == Type.STANDARD) stdRooms.add(room);
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
			
			Room room = randomRoom( Room.Type.STANDARD, 10 );
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
	
	protected Room randomRoom( Room.Type type, int tries ) {
		for (int i=0; i < tries; i++) {
			Room room = Random.element( rooms );
			if (room.type == type) {
				return room;
			}
		}
		return null;
	}
	
	public Room room( int pos ) {
		for (Room room : rooms) {
			if (room.type != Type.NULL && room.inside( cellToPoint(pos) )) {
				return room;
			}
		}
		
		return null;
	}
	
	protected int randomDropCell() {
		while (true) {
			Room room = randomRoom( Room.Type.STANDARD, 1 );
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
			if (room.type == Type.PIT) {
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

		rooms = new ArrayList<>( (Collection<Room>) ((Collection<?>) bundle.getCollection( "rooms" )) );
		for (Room r : rooms) {
			if (r.type == Type.WEAK_FLOOR) {
				weakFloorCreated = true;
				break;
			}
			if (r.type == Type.ENTRANCE){
				roomEntrance = r;
			} else if (r.type == Type.EXIT || r.type == Type.BOSS_EXIT){
				roomExit = r;
			}
		}
	}
	
}
