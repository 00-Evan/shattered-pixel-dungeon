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

package com.shatteredpixel.shatteredpixeldungeon.levels.builders;

import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class FigureEightBuilder extends RegularBuilder {
	
	//These methods allow for the adjusting of the shape of the loop
	//by default the loop is a perfect circle, but it can be adjusted
	
	//increasing the exponent will increase the the curvature, making the loop more oval shaped.
	private int curveExponent = 0;
	
	//This is a percentage (range 0-1) of the intensity of the curve function
	// 0 makes for a perfect linear curve (circle)
	// 1 means the curve is completely determined by the curve exponent
	private float curveIntensity = 1;
	
	//Adjusts the starting point along the loop.
	// a common example, setting to 0.25 will make for a short fat oval instead of a long one.
	private float curveOffset = 0;
	
	public FigureEightBuilder setLoopShape(int exponent, float intensity, float offset){
		this.curveExponent = Math.abs(exponent);
		curveIntensity = intensity % 1f;
		curveOffset = offset % 0.5f;
		return this;
	}
	
	private float targetAngle( float percentAlong ){
		percentAlong += curveOffset;
		return 360f * (float)(
				curveIntensity * curveEquation(percentAlong)
						+ (1-curveIntensity)*(percentAlong)
						- curveOffset);
	}
	
	private double curveEquation( double x ){
		return Math.pow(4, 2*curveExponent)
				*(Math.pow((x % 0.5f )-0.25, 2*curveExponent + 1))
				+ 0.25 + 0.5*Math.floor(2*x);
	}
	
	private Room landmarkRoom;
	
	public FigureEightBuilder setLandmarkRoom(Room room){
		landmarkRoom = room;
		return this;
	}
	
	ArrayList<Room> firstLoop, secondLoop;
	PointF firstLoopCenter, secondLoopCenter;
	
	@Override
	public ArrayList<Room> build(ArrayList<Room> rooms) {
		setupRooms(rooms);
		
		//TODO might want to make this able to work without an exit. Probably a random room would be landmark and the landmark room would become exit
		if (landmarkRoom == null){
			landmarkRoom = Random.element(multiConnections);
		}
		
		if (multiConnections.contains(landmarkRoom)){
			multiConnections.remove(landmarkRoom);
		}
		
		float startAngle = Random.Float(0, 180);
		
		int roomsOnLoop = (int)(multiConnections.size()*pathLength) + Random.chances(pathLenJitterChances);
		roomsOnLoop = Math.min(roomsOnLoop, multiConnections.size());
		
		int roomsOnFirstLoop = roomsOnLoop/2;
		if (roomsOnLoop % 2 == 1) roomsOnFirstLoop += Random.Int(2);
		
		firstLoop = new ArrayList<>();
		float[] pathTunnels = pathTunnelChances.clone();
		for (int i = 0; i <= roomsOnFirstLoop; i++){
			if (i == 0)
				firstLoop.add(landmarkRoom);
			else
				firstLoop.add(multiConnections.remove(0));
			
			int tunnels = Random.chances(pathTunnels);
			if (tunnels == -1){
				pathTunnels = pathTunnelChances.clone();
				tunnels = Random.chances(pathTunnels);
			}
			pathTunnels[tunnels]--;
			
			for (int j = 0; j < tunnels; j++){
				firstLoop.add(ConnectionRoom.createRoom());
			}
		}
		if (entrance != null) firstLoop.add((firstLoop.size()+1)/2, entrance);
		
		int roomsOnSecondLoop = roomsOnLoop - roomsOnFirstLoop;
		
		secondLoop = new ArrayList<>();
		for (int i = 0; i <= roomsOnSecondLoop; i++){
			if (i == 0)
				secondLoop.add(landmarkRoom);
			else
				secondLoop.add(multiConnections.remove(0));
			
			int tunnels = Random.chances(pathTunnels);
			if (tunnels == -1){
				pathTunnels = pathTunnelChances.clone();
				tunnels = Random.chances(pathTunnels);
			}
			pathTunnels[tunnels]--;
			
			for (int j = 0; j < tunnels; j++){
				secondLoop.add(ConnectionRoom.createRoom());
			}
		}
		if (exit != null) secondLoop.add((secondLoop.size()+1)/2, exit);
		
		landmarkRoom.setSize();
		landmarkRoom.setPos(0, 0);
		
		Room prev = landmarkRoom;
		float targetAngle;
		for (int i = 1; i < firstLoop.size(); i++){
			Room r = firstLoop.get(i);
			targetAngle = startAngle + targetAngle( i / (float)firstLoop.size());
			if (placeRoom(rooms, prev, r, targetAngle) != -1) {
				prev = r;
				if (!rooms.contains(prev))
					rooms.add(prev);
			} else {
				//FIXME this is lazy, there are ways to do this without relying on chance
				return null;
			}
		}
		
		//FIXME this is still fairly chance reliant
		// should just write a general function for stitching two rooms together in builder
		while (!prev.connect(landmarkRoom)){
			
			ConnectionRoom c = ConnectionRoom.createRoom();
			if (placeRoom(rooms, prev, c, angleBetweenRooms(prev, landmarkRoom)) == -1){
				return null;
			}
			firstLoop.add(c);
			rooms.add(c);
			prev = c;
		}
		
		prev = landmarkRoom;
		startAngle += 180f;
		for (int i = 1; i < secondLoop.size(); i++){
			Room r = secondLoop.get(i);
			targetAngle = startAngle + targetAngle( i / (float)secondLoop.size());
			if (placeRoom(rooms, prev, r, targetAngle) != -1) {
				prev = r;
				if (!rooms.contains(prev))
					rooms.add(prev);
			} else {
				//FIXME this is lazy, there are ways to do this without relying on chance
				return null;
			}
		}
		
		//FIXME this is still fairly chance reliant
		// should just write a general function for stitching two rooms together in builder
		while (!prev.connect(landmarkRoom)){
			
			ConnectionRoom c = ConnectionRoom.createRoom();
			if (placeRoom(rooms, prev, c, angleBetweenRooms(prev, landmarkRoom)) == -1){
				return null;
			}
			secondLoop.add(c);
			rooms.add(c);
			prev = c;
		}
		
		if (shop != null) {
			float angle;
			int tries = 10;
			do {
				angle = placeRoom(firstLoop, entrance, shop, Random.Float(360f));
				tries--;
			} while (angle == -1 && tries >= 0);
			if (angle == -1) return null;
		}
		
		firstLoopCenter = new PointF();
		for (Room r : firstLoop){
			firstLoopCenter.x += (r.left + r.right)/2f;
			firstLoopCenter.y += (r.top + r.bottom)/2f;
		}
		firstLoopCenter.x /= firstLoop.size();
		firstLoopCenter.y /= firstLoop.size();
		
		secondLoopCenter = new PointF();
		for (Room r : secondLoop){
			secondLoopCenter.x += (r.left + r.right)/2f;
			secondLoopCenter.y += (r.top + r.bottom)/2f;
		}
		secondLoopCenter.x /= secondLoop.size();
		secondLoopCenter.y /= secondLoop.size();
		
		ArrayList<Room> branchable = new ArrayList<>(firstLoop);
		branchable.addAll(secondLoop);
		branchable.remove(landmarkRoom); //remove once so it isn't present twice
		
		ArrayList<Room> roomsToBranch = new ArrayList<>();
		roomsToBranch.addAll(multiConnections);
		roomsToBranch.addAll(singleConnections);
		weightRooms(branchable);
		createBranches(rooms, branchable, roomsToBranch, branchTunnelChances);
		
		findNeighbours(rooms);
		
		for (Room r : rooms){
			for (Room n : r.neigbours){
				if (!n.connected.containsKey(r)
						&& Random.Float() < extraConnectionChance){
					r.connect(n);
				}
			}
		}
		
		return rooms;
	}
	
	@Override
	protected float randomBranchAngle( Room r ) {
		PointF center;
		if (firstLoop.contains(r)){
			center = firstLoopCenter;
		} else {
			center = secondLoopCenter;
		}
		if (center == null)
			return super.randomBranchAngle( r );
		else {
			//generate four angles randomly and return the one which points closer to the center
			float toCenter = angleBetweenPoints( new PointF((r.left + r.right)/2f, (r.top + r.bottom)/2f), center);
			if (toCenter < 0) toCenter += 360f;
			
			float currAngle = Random.Float(360f);
			for( int i = 0; i < 4; i ++){
				float newAngle = Random.Float(360f);
				if (Math.abs(toCenter - newAngle) < Math.abs(toCenter - currAngle)){
					currAngle = newAngle;
				}
			}
			return currAngle;
		}
	}
	
}
