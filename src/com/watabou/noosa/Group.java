/*
 * Copyright (C) 2012-2015 Oleg Dolya
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

import java.util.ArrayList;

public class Group extends Gizmo {

	protected ArrayList<Gizmo> members;
	
	// Accessing it is a little faster, 
	// than calling memebers.getSize()
	public int length;
	
	public Group() {
		members = new ArrayList<Gizmo>();
		length = 0;
	}
	
	@Override
	public void destroy() {
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null) {
				g.destroy();
			}
		}
		
		members.clear();
		members = null;
		length = 0;
	}
	
	@Override
	public void update() {
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && g.exists && g.active) {
				g.update();
			}
		}
	}
	
	@Override
	public void draw() {
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && g.exists && g.visible) {
				g.draw();
			}
		}
	}
	
	@Override
	public void kill() {
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
	
	public int indexOf( Gizmo g ) {
		return members.indexOf( g );
	}
	
	public Gizmo add( Gizmo g ) {
		
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
	
	public Gizmo addToBack( Gizmo g ) {
		
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
	
	public Gizmo recycle( Class<? extends Gizmo> c ) {

		Gizmo g = getFirstAvailable( c );
		if (g != null) {
			
			return g;
			
		} else if (c == null) {
			
			return null;
			
		} else {
			
			try {
				return add( c.newInstance() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	// Fast removal - replacing with null
	public Gizmo erase( Gizmo g ) {
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
	public Gizmo remove( Gizmo g ) {
		if (members.remove( g )) {
			length--;
			g.parent = null;
			return g;
		} else {
			return null;
		}
	}
	
	public Gizmo replace( Gizmo oldOne, Gizmo newOne ) {
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
	
	public Gizmo getFirstAvailable( Class<? extends Gizmo> c ) {
		
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && !g.exists && ((c == null) || g.getClass() == c)) {
				return g;
			}
		}
		
		return null;
	}
	
	public int countLiving() {
		
		int count = 0;
		
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && g.exists && g.alive) {
				count++;
			}
		}
		
		return count;
	}
	
	public int countDead() {
		
		int count = 0;
		
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null && !g.alive) {
				count++;
			}
		}
		
		return count;
	}
	
	public Gizmo random() {
		if (length > 0) {
			return members.get( (int)(Math.random() * length) );
		} else {
			return null;
		}
	}
	
	public void clear() {
		for (int i=0; i < length; i++) {
			Gizmo g = members.get( i );
			if (g != null) {
				g.parent = null;
			}
		}
		members.clear();
		length = 0;
	}
	
	public Gizmo bringToFront( Gizmo g ) {
		if (members.contains( g )) {
			members.remove( g );
			members.add( g );
			return g;
		} else {
			return null;
		}
	}
	
	public Gizmo sendToBack( Gizmo g ) {
		if (members.contains( g )) {
			members.remove( g );
			members.add( 0, g );
			return g;
		} else {
			return null;
		}
	}
}
