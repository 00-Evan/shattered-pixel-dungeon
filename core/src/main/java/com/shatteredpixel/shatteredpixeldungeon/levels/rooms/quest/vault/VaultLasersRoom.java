package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultLaser;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Random;

public class VaultLasersRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 2, Terrain.EMPTY);

		for (Room.Door door : connected.values()) {
			Painter.drawInside(level, this, door, 2, Terrain.EMPTY);
			door.set(Room.Door.Type.REGULAR);
		}

		for (int x = left+2; x <= right-2; x++){
			if (level.map[x + (top+1)*level.width()] == Terrain.WALL
					&& level.map[x + (bottom-1)*level.width()] == Terrain.WALL){
				VaultLaser laser = new VaultLaser();
				if (Random.Int(2) == 0){
					int cell = x + level.width()*(top+1);
					Painter.set(level, cell, Terrain.PEDESTAL);
					laser.laserDirs = new int[]{cell+level.width()};
					laser.pos = cell;
				} else {
					int cell = x + level.width()*(bottom-1);
					Painter.set(level, cell, Terrain.PEDESTAL);
					laser.laserDirs = new int[]{cell-level.width()};
					laser.pos = cell;
				}
				laser.initialLaserCooldown = Random.IntRange(3, 7);
				laser.cooldown = Random.IntRange(1, laser.initialLaserCooldown);
				level.mobs.add(laser);
			}
		}

		for (int y = top+2; y <= bottom-2; y++){
			if (level.map[left+1 + (y)*level.width()] == Terrain.WALL
					&& level.map[right-1 + (y)*level.width()] == Terrain.WALL){
				VaultLaser laser = new VaultLaser();
				if (Random.Int(2) == 0){
					int cell = left+1 + level.width()*y;
					Painter.set(level, cell, Terrain.PEDESTAL);
					laser.laserDirs = new int[]{cell+1};
					laser.pos = cell;
				} else {
					int cell = right-1 + level.width()*y;
					Painter.set(level, cell, Terrain.PEDESTAL);
					laser.laserDirs = new int[]{cell-1};
					laser.pos = cell;
				}
				laser.initialLaserCooldown = Random.IntRange(3, 7);
				laser.cooldown = Random.IntRange(1, laser.initialLaserCooldown);
				level.mobs.add(laser);
			}
		}

	}

}
