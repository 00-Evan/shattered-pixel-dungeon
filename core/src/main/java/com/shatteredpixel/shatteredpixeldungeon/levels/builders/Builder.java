package com.shatteredpixel.shatteredpixeldungeon.levels.builders;

import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;

import java.util.ArrayList;

public abstract class Builder {
	
	//If builders require additional parameters, they should request them in their constructor
	
	//builders take a list of rooms and returns them as a connected map
	//returns null on failure
	public abstract ArrayList<Room> build(ArrayList<Room> rooms);
	
}
