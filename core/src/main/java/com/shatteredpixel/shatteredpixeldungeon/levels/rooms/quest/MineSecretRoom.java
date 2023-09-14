/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.watabou.utils.Random;

public class MineSecretRoom extends SecretRoom {

	@Override
	public int maxWidth() { return 7; }

	@Override
	public int maxHeight() { return 7; }

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		if (Blacksmith.Quest.Type() == Blacksmith.Quest.CRYSTAL) {
			Painter.fill(level, this, 1, Terrain.MINE_CRYSTAL);
		} else {
			Painter.fill(level, this, 1, Terrain.EMPTY);
		}

		entrance().set( Door.Type.HIDDEN );

		int goldAmount = Random.NormalIntRange(4, 7);

		//TODO maybe add per-quest decorations here?

		for (int i = 0; i < goldAmount; i++){
			Painter.set(level, random(1), Terrain.WALL_DECO);
		}

	}
}
