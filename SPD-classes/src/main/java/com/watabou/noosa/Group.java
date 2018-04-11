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

package com.watabou.noosa;

import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Group extends Gizmo {

	protected ArrayList<Gizmo> members;
	
	// Accessing it is a little faster,
	// than calling members.getSize()
	public int length;

	public static boolean freezeEmitters = false;
	
	public Group() {
		members = new ArrayList<Gizmo>();
		length = 0;
	}
	
	@Override
	public synchronized void destroy() {
		super.destroy();
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null) {
				g.destroy();
			}
		}
		
		if (members != null) {
			members.clear();
			members = null;
		}
		length = 0;
	}
	
	@Override
	public synchronized void update() {
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && g.exists && g.active
					//functionality for the freezing of emitters(particle effects), effects are given a second
					//from load to get started so they aren't frozen before anything is generated.
					&& !(freezeEmitters && Game.timeTotal > 1f && g instanceof Emitter)) {
				g.update();
			}
		}
	}
	
	@Override
	public synchronized void draw() {
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && g.exists && g.isVisible()) {
				g.draw();
			}
		}
	}
	
	@Override
	public synchronized void kill() {
		// A killed group keeps all its members,
		// but they get killed too
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && g.exists) {
				g.kill();
			}
		}
		
		super.kill();
	}
	
	public synchronized int indexOf( Gizmo g ) {
		return members.indexOf( g );
	}
	
	public synchronized Gizmo add( Gizmo g ) {
		
		if (g.parent == this) {
			return g;
		}
		
		if (g.parent != null) {
			g.parent.remove( g );
		}
		
		// Trying to find an empty space for a new member
		for (int i=0; i < length; i++) {
			if (members.get( i ) == null) {
				members.set( i, g );
				g.parent = this;
				return g;
			}
		}
		
		members.add( g );
		g.parent = this;
		length++;
		return g;
	}
	
	public synchronized Gizmo addToFront( Gizmo g){

		if (g.parent == this) {
			return g;
		}

		if (g.parent != null) {
			g.parent.remove( g );
		}

		// Trying to find an empty space for a new member
		// starts from the front and never goes over a none-null element
		for (int i=length-1; i >= 0; i--) {
			if (members.get( i ) == null) {
				if (i == 0 || members.get(i - 1) != null) {
					members.set(i, g);
					g.parent = this;
					return g;
				}
			} else {
				break;
			}
		}

		members.add( g );
		g.parent = this;
		length++;
		return g;
	}
	
	public synchronized Gizmo addToBack( Gizmo g ) {
		
		if (g.parent == this) {
			sendToBack( g );
			return g;
		}
		
		if (g.parent != null) {
			g.parent.remove( g );
		}
		
		if (members.get( 0 ) == null) {
			members.set( 0, g );
			g.parent = this;
			return g;
		}
		
		members.add( 0, g );
		g.parent = this;
		length++;
		return g;
	}
	
	public synchronized Gizmo recycle( Class<? extends Gizmo> c ) {

		Gizmo g = getFirstAvailable( c );
		if (g != null) {
			
			return g;
			
		} else if (c == null) {
			
			return null;
			
		} else {
			
			try {
				return add( c.newInstance() );
			} catch (Exception e) {
				Game.reportException(e);
			}
		}
		
		return null;
	}
	
	// Fast removal - replacing with null
	public synchronized Gizmo erase( Gizmo g ) {
		int index = members.indexOf( g );

		if (index != -1) {
			members.set( index, null );
			g.parent = null;
			return g;
		} else {
			return null;
		}
	}
	
	// Real removal
	public synchronized Gizmo remove( Gizmo g ) {
		if (members.remove( g )) {
			length--;
			g.parent = null;
			return g;
		} else {
			return null;
		}
	}
	
	public synchronized Gizmo replace( Gizmo oldOne, Gizmo newOne ) {
		int index = members.indexOf( oldOne );
		if (index != -1) {
			members.set( index, newOne );
			newOne.parent = this;
			oldOne.parent = null;
			return newOne;
		} else {
			return null;
		}
	}
	
	public synchronized Gizmo getFirstAvailable( Class<? extends Gizmo> c ) {
		
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && !g.exists && ((c == null) || g.getClass() == c)) {
				return g;
			}
		}
		
		return null;
	}
	
	public synchronized int countLiving() {
		
		int count = 0;
		
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && g.exists && g.alive) {
				count++;
			}
		}
		
		return count;
	}
	
	public synchronized int countDead() {
		
		int count = 0;
		
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && !g.alive) {
				count++;
			}
		}
		
		return count;
	}
	
	public synchronized Gizmo random() {
		if (length > 0) {
			return members.get( Random.Int(length) );
		} else {
			return null;
		}
	}
	
	public synchronized void clear() {
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null) {
				g.parent = null;
			}
		}
		members.clear();
		length = 0;
	}
	
	public synchronized Gizmo bringToFront( Gizmo g ) {
		if (members.contains( g )) {
			members.remove( g );
			members.add( g );
			return g;
		} else {
			return null;
		}
	}
	
	public synchronized Gizmo sendToBack( Gizmo g ) {
		if (members.contains( g )) {
			members.remove( g );
			members.add( 0, g );
			return g;
		} else {
			return null;
		}
	}
}
