package com.shatteredpixel.shatteredpixeldungeon.levels.builders;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.ShopRoom;
import com.watabou.utils.Graph;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.List;

//This builder exactly mimics pre-0.6.0 levelgen, including all of its limitations
//Currently implemented during this transition period, it will likely not survive to 0.6.0 release
public class LegacyBuilder extends Builder {
	
	public enum Type{
		REGULAR,
		SEWER_BOSS,
		LAST_SHOP
	}
	
	private Type type;
	private int width, height;
	private int minRoomSize, maxRoomSize;
	
	public LegacyBuilder(Type t, int w, int h, int min, int max){
		type = t;
		
		width = w;
		height = h;
		
		minRoomSize = min;
		maxRoomSize = max;
	}
	
	private ArrayList<Room> rooms;
	
	public Room roomEntrance;
	public Room roomExit;
	
	protected ArrayList<Room.Type> specials;
	
	@Override
	//The list of rooms passed to this method is ignored
	public ArrayList<Room> build(ArrayList<Room> ignoredRooms) {
		
		if (!initRooms()){
			return null;
		}
		
		switch(type){
			case REGULAR: default:
				return buildRegularLevel();
			case SEWER_BOSS:
				return buildSewerBossLevel();
			case LAST_SHOP:
				return buildsLastShopLevel();
		}
	}
	
	private ArrayList<Room> buildRegularLevel(){
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
				return null;
			}
			
		} while (distance < minDistance);
		
		roomEntrance.type = Room.Type.ENTRANCE;
		roomExit.type = Room.Type.EXIT;
		
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
				if (r.connected.size() == 1 && ((r.width()-1)*(r.height()-1) >= ShopRoom.spaceNeeded())) {
					shop = r;
					break;
				}
			}
			
			if (shop == null) {
				return null;
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
			return null;
		
		//Quest generation logic
		if (Dungeon.depth >= 6 && Dungeon.depth <= 9){
			if (!Wandmaker.Quest.spawnRoom( rooms ) && Dungeon.depth == 9)
				return null;
		} else if (Dungeon.depth >= 11 && Dungeon.depth <= 14){
			if (!Blacksmith.Quest.spawn( rooms ) && Dungeon.depth == 14)
				return null;
		}
		
		
		return rooms;
	}
	
	private ArrayList<Room> buildSewerBossLevel(){
		int distance;
		//if we ever need to try 20 or more times to find a room, better to give up and try again.
		int retry = 0;
		
		//start with finding an entrance room (will also contain exit)
		//the room must be at least 4x4 and be nearer the top of the map(so that it is less likely something connects to the top)
		do {
			if (retry++ > 20) {
				return null;
			}
			roomEntrance = Random.element( rooms );
		} while (roomEntrance.width() != 8 || roomEntrance.height() < 5 || roomEntrance.top == 0 || roomEntrance.top >= 8);
		
		roomEntrance.type = Room.Type.ENTRANCE;
		roomExit = roomEntrance;
		
		
		//now find the rest of the rooms for this boss mini-maze
		Room curRoom = null;
		Room lastRoom = roomEntrance;
		//we make 4 rooms, last iteration is tieing the final room to the start
		for(int i = 0; i <= 4; i++){
			retry = 0;
			//find a suitable room the first four times
			//suitable room should be empty, have a distance of 2 from the current room, and not touch the entrance.
			if (i < 4) {
				do {
					if (retry++ > 20) {
						return null;
					}
					curRoom = Random.element(rooms);
					Graph.buildDistanceMap(rooms, curRoom);
					distance = lastRoom.distance();
				} while (curRoom.type != Room.Type.NULL || distance != 3 || curRoom.neigbours.contains(roomEntrance));
				
				curRoom.type = Room.Type.STANDARD;
				
				//otherwise, we're on the last iteration.
			} else {
				//set the current room to the entrance, so we can build a connection to it.
				curRoom = roomEntrance;
			}
			
			//now build a connection between the current room and the last one.
			Graph.buildDistanceMap( rooms, curRoom );
			List<Room> path = Graph.buildPath( rooms, lastRoom, curRoom );
			
			Graph.setPrice( path, lastRoom.distance );
			
			path = Graph.buildPath( rooms, lastRoom, curRoom );
			
			Room room = lastRoom;
			for (Room next : path) {
				room.connect( next );
				room = next;
			}
			
			if (i == 4) {
				
				//we must find a room for his royal highness!
				//look at rooms adjacent to the final found room (likely to be furthest from start)
				ArrayList<Room> candidates = new ArrayList<Room>();
				for (Room r : lastRoom.neigbours) {
					if (r.type == Room.Type.NULL && r.connected.size() == 0 && !r.neigbours.contains(roomEntrance)) {
						candidates.add(r);
					}
				}
				
				//if we have candidates, pick a room and put the king there
				if (candidates.size() > 0) {
					Room kingsRoom = Random.element(candidates);
					kingsRoom.connect(lastRoom);
					kingsRoom.type = Room.Type.RAT_KING;
					
					//unacceptable! make a new level...
				} else {
					return null;
				}
			}
			lastRoom = curRoom;
		}
		
		//the connection structure ensures that (most of the time) there is a nice loop for the player to kite the
		//boss around. What's nice is that there is enough chaos such that the loop is rarely straightforward
		//and boring.
		
		//fills our connection rooms in with tunnel
		for (Room r : rooms) {
			if (r.type == Room.Type.NULL && r.connected.size() > 0) {
				r.type = Room.Type.TUNNEL;
			}
		}
		
		return rooms;
	}
	
	private ArrayList<Room> buildsLastShopLevel(){
		int distance;
		int retry = 0;
		int minDistance = (int)Math.sqrt( rooms.size() );
		do {
			int innerRetry = 0;
			do {
				if (innerRetry++ > 10) {
					return null;
				}
				roomEntrance = Random.element( rooms );
			} while (roomEntrance.width() < 4 || roomEntrance.height() < 4);
			
			innerRetry = 0;
			do {
				if (innerRetry++ > 10) {
					return null;
				}
				roomExit = Random.element( rooms );
			} while (roomExit == roomEntrance || roomExit.width() < 6 || roomExit.height() < 6 || roomExit.top == 0);
			
			Graph.buildDistanceMap( rooms, roomExit );
			distance = Graph.buildPath( rooms, roomEntrance, roomExit ).size();
			
			if (retry++ > 10) {
				return null;
			}
			
		} while (distance < minDistance);
		
		roomEntrance.type = Room.Type.ENTRANCE;
		roomExit.type = Room.Type.EXIT;
		
		Graph.buildDistanceMap( rooms, roomExit );
		List<Room> path = Graph.buildPath( rooms, roomEntrance, roomExit );
		
		Graph.setPrice( path, roomEntrance.distance );
		
		Graph.buildDistanceMap( rooms, roomExit );
		path = Graph.buildPath( rooms, roomEntrance, roomExit );
		
		Room room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
		}
		
		Room roomShop = null;
		int shopSquare = 0;
		for (Room r : rooms) {
			if (r.type == Room.Type.NULL && r.connected.size() > 0) {
				r.type = Room.Type.PASSAGE;
				if (r.square() > shopSquare) {
					roomShop = r;
					shopSquare = r.square();
				}
			}
		}
		
		if (roomShop == null || shopSquare < 54) {
			return null;
		} else {
			roomShop.type = Imp.Quest.isCompleted() ? Room.Type.SHOP : Room.Type.STANDARD;
		}
		
		return rooms;
	}
	
	private boolean initRooms(){
		rooms = new ArrayList<>();
		split( new Rect( 0, 0, width-1, height-1));
		
		if (rooms.size() < 8){
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
	
	private void split( Rect rect ) {
		
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
			
			rooms.add( new Room(rect) );
			
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
	
	protected boolean assignRoomType() {
		
		int specialRooms = 0;
		boolean pitMade = false;
		
		for (Room r : rooms) {
			if (r.type == Room.Type.NULL &&
					r.connected.size() == 1) {
				
				if (specials.size() > 0 &&
						r.width() > 3 && r.height() > 3 &&
						Random.Int( specialRooms * specialRooms + 2 ) == 0) {
					
					if (Level.pitRoomNeeded && !pitMade) {
						
						r.type = Room.Type.PIT;
						pitMade = true;
						
						specials.remove( Room.Type.ARMORY );
						specials.remove( Room.Type.CRYPT );
						specials.remove( Room.Type.LABORATORY );
						specials.remove( Room.Type.LIBRARY );
						specials.remove( Room.Type.STATUE );
						specials.remove( Room.Type.TREASURY );
						specials.remove( Room.Type.VAULT );
						specials.remove( Room.Type.WEAK_FLOOR );
						
					} else if (Dungeon.depth % 5 == 2 && specials.contains( Room.Type.LABORATORY )) {
						
						r.type = Room.Type.LABORATORY;
						
					} else if (Dungeon.depth >= Dungeon.transmutation && specials.contains( Room.Type.MAGIC_WELL )) {
						
						r.type = Room.Type.MAGIC_WELL;
						
					} else {
						
						int n = specials.size();
						r.type = specials.get( Math.min( Random.Int( n ), Random.Int( n ) ) );
						if (r.type == Room.Type.WEAK_FLOOR) {
							Level.weakFloorCreated = true;
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
								n.type != Room.Type.PIT) {
							
							neigbours.add( n );
						}
					}
					if (neigbours.size() > 1) {
						r.connect( Random.element( neigbours ) );
					}
				}
			}
		}
		
		if (Level.pitRoomNeeded && !pitMade) return false;
		
		Room.Type tunnelType = Room.Type.TUNNEL;
		if ((Dungeon.depth > 5 && Dungeon.depth <= 10) ||
				(Dungeon.depth > 15 && Dungeon.depth <= 20)){
			tunnelType = Room.Type.PASSAGE;
		}
		
		int count = 0;
		for (Room r : rooms) {
			if (r.type == Room.Type.NULL) {
				int connections = r.connected.size();
				if (connections == 0) {
					
				} else if (Random.Int( connections * connections ) == 0) {
					r.type = Room.Type.STANDARD;
					count++;
				} else {
					r.type = tunnelType;
				}
			}
		}
		
		while (count < 6) {
			Room r = randomRoom( tunnelType, 20 );
			if (r != null) {
				r.type = Room.Type.STANDARD;
				count++;
			} else {
				return false;
			}
		}
		
		return true;
	}
	
	private Room randomRoom( Room.Type type, int tries ) {
		for (int i=0; i < tries; i++) {
			Room room = Random.element( rooms );
			if (room.type == type) {
				return room;
			}
		}
		return null;
	}
}
