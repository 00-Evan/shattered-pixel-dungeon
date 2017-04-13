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
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.RatKingRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.SewerBossEntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SewerBossLevel extends SewerLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}
	
	private int stairs = 0;
	
	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		initRooms.add ( roomEntrance = roomExit = new SewerBossEntranceRoom());
		
		int standards = standardRooms();
		for (int i = 0; i < standards; i++)
			initRooms.add(StandardRoom.createRoom());
		
		initRooms.add(new RatKingRoom());
		return initRooms;
	}
	
	@Override
	protected int standardRooms() {
		return 3+Random.chances(new float[]{4, 2, 2});
	}
	
	protected Builder builder(){
		//TODO want to use a more simple circular builder here
		return super.builder();
	}
	
	@Override
	protected float waterFill(){
		return 0.50f;
	}
	
	@Override
	protected int waterSmoothing(){
		return 5;
	}
	
	@Override
	protected float grassFill() {
		return 0.20f;
	}
	
	@Override
	protected int grassSmoothing() {
		return 4;
	}
	
	protected int nTraps() {
		return 0;
	}

	@Override
	protected void createMobs() {
		Mob mob = Bestiary.mob( Dungeon.depth );
		Room room;
		do {
			room = Random.element(rooms);
		} while (!(room instanceof StandardRoom) || room == roomEntrance);
		mob.pos = pointToCell(room.random());
		mobs.add( mob );
	}
	
	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = pointToCell(roomEntrance.random());
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell() {
		return pointToCell(roomEntrance.random());
	}

	
	public void seal() {
		if (entrance != 0) {

			super.seal();
			
			set( entrance, Terrain.WATER );
			GameScene.updateMap( entrance );
			GameScene.ripple( entrance );
			
			stairs = entrance;
			entrance = 0;
		}
	}
	
	public void unseal() {
		if (stairs != 0) {

			super.unseal();
			
			entrance = stairs;
			stairs = 0;
			
			set( entrance, Terrain.ENTRANCE );
			GameScene.updateMap( entrance );

		}
	}
	
	private static final String STAIRS	= "stairs";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( STAIRS, stairs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		stairs = bundle.getInt( STAIRS );
		roomExit = roomEntrance;
	}
}
