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

package com.watabou.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Graph {

	public static <T extends Node> void setPrice( List<T> nodes, int value ) {
		for (T node : nodes) {
			node.price( value );
		}
	}
	
	public static <T extends Node> void buildDistanceMap( Collection<T> nodes, Node focus ) {
		
		for (T node : nodes) {
			node.distance( Integer.MAX_VALUE );
		}
		
		LinkedList<Node> queue = new LinkedList<>();
		
		focus.distance( 0 );
		queue.add( focus );
		
		while (!queue.isEmpty()) {
			
			Node node = queue.poll();
			int distance = node.distance();
			int price = node.price();
			
			for (Node edge : node.edges()) {
				if (edge.distance() > distance + price) {
					queue.add( edge );
					edge.distance( distance + price );
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Node> List<T> buildPath( Collection<T> nodes, T from, T to ) {
		
		List<T> path = new ArrayList<>();
		
		T room = from;
		while (room != to) {
			
			int min = room.distance();
			T next = null;
			
			Collection<? extends Node> edges = room.edges();
			
			for (Node edge : edges) {
				
				int distance = edge.distance();
				if (distance < min) {
					min = distance;
					next = (T)edge;
				}
			}
			
			if (next == null) {
				return null;
			}
			
			path.add( next );
			room = next;
		}
		
		return path;
	}
	
	public interface Node {
		
		int distance();
		void distance( int value );
		
		int price();
		void price( int value );
		
		Collection<? extends Node> edges();
		
	}
}
