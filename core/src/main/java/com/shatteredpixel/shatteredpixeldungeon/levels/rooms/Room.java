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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

//Note that this class should be treated as if it were abstract
// it is currently not abstract to maintain compatibility with pre-0.6.0 saves
// TODO make this class abstract after dropping support for pre-0.6.0 saves
public class Room extends Rect implements Graph.Node, Bundlable {
	
	public ArrayList<Room> neigbours = new ArrayList<Room>();
	public LinkedHashMap<Room, Door> connected = new LinkedHashMap<Room, Door>();
	
	public int distance;
	public int price = 1;
	
	public Room(){
		super();
	}
	
	public Room( Rect other ){
		super(other);
	}
	
	public Room set( Room other ) {
		super.set( other );
		for (Room r : other.neigbours){
			neigbours.add(r);
			r.neigbours.remove(other);
			r.neigbours.add(this);
		}
		for (Room r : other.connected.keySet()){
			Door d = other.connected.get(r);
			r.connected.remove(other);
			r.connected.put(this, d);
			connected.put(r, d);
		}
		return this;
	}
	
	//Note: when overriding these it is STRONGLY ADVISED to store any randomly decided values.
	//With the same room and the same parameters these should always return
	//the same value over multiple calls, even if there's some randomness initially.
	public int minWidth(){
		return -1;
	}
	public int maxWidth() { return -1; }
	
	public int minHeight() { return -1; }
	public int maxHeight() { return -1; }
	
	public boolean setSize(){
		return setSize(minWidth(), maxWidth(), minHeight(), maxHeight());
	}
	
	public boolean setSize( int w, int h){
		return setSize( w, w, h, h );
	}
	
	public boolean setSize(int minW, int maxW, int minH, int maxH) {
		if (minW < minWidth()
				|| maxW > maxWidth()
				|| minH < minHeight()
				|| maxH > maxHeight()){
			return false;
		} else {
			//subtract one because rooms are inclusive to their right and bottom sides
			resize(left + Random.NormalIntRange(minW, maxW) - 1,
					top + Random.NormalIntRange(minH, maxH) - 1);
			return true;
		}
	}
	
	//Width and height are increased by 1 because rooms are inclusive to their right and bottom sides
	@Override
	public int width() {
		return super.width()+1;
	}
	
	@Override
	public int height() {
		return super.height()+1;
	}
	
	public void paint(Level level){ }
	
	public Point random() {
		return random( 0 );
	}
	
	public Point random( int m ) {
		return new Point( Random.Int( left + 1 + m, right - m ),
				Random.Int( top + 1 + m, bottom - m ));
	}
	
	public boolean addNeigbour( Room other ) {
		if (neigbours.contains(other))
			return true;
		
		Rect i = intersect( other );
		if ((i.width() == 0 && i.height() >= 2) ||
			(i.height() == 0 && i.width() >= 2)) {
			neigbours.add( other );
			other.neigbours.add( this );
			return true;
		}
		return false;
	}
	
	public boolean connect( Room room ) {
		if (!neigbours.contains(room) && !addNeigbour(room))
			return false;
		if (!connected.containsKey( room )) {
			connected.put( room, null );
			room.connected.put( this, null );
		}
		return true;
	}
	
	public boolean inside( Point p ) {
		return p.x > left && p.y > top && p.x < right && p.y < bottom;
	}
	
	public Point center() {
		return new Point(
			(left + right) / 2 + (((right - left) % 2) == 1 ? Random.Int( 2 ) : 0),
			(top + bottom) / 2 + (((bottom - top) % 2) == 1 ? Random.Int( 2 ) : 0) );
	}
	
	// **** Graph.Node interface ****

	@Override
	public int distance() {
		return distance;
	}

	@Override
	public void distance( int value ) {
		distance = value;
	}
	
	@Override
	public int price() {
		return price;
	}

	@Override
	public void price( int value ) {
		price = value;
	}

	@Override
	public Collection<Room> edges() {
		return neigbours;
	}
	
	public String legacyType = "NULL";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( "left", left );
		bundle.put( "top", top );
		bundle.put( "right", right );
		bundle.put( "bottom", bottom );
		if (!legacyType.equals("NULL"))
			bundle.put( "type", legacyType );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		left = bundle.getInt( "left" );
		top = bundle.getInt( "top" );
		right = bundle.getInt( "right" );
		bottom = bundle.getInt( "bottom" );
		if (bundle.contains( "type" ))
			legacyType = bundle.getString( "type" );
	}
	
	public static class Door extends Point {
		
		public enum Type {
			EMPTY, TUNNEL, REGULAR, UNLOCKED, HIDDEN, BARRICADE, LOCKED
		}
		public Type type = Type.EMPTY;
		
		public Door( int x, int y ) {
			super( x, y );
		}
		
		public void set( Type type ) {
			if (type.compareTo( this.type ) > 0) {
				this.type = type;
			}
		}
	}
}