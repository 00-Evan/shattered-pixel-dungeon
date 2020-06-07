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

package net.casiello.pixeldungeonrescue.levels.traps;

import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.blobs.Blob;
import net.casiello.pixeldungeonrescue.actors.blobs.CorrosiveGas;
import net.casiello.pixeldungeonrescue.scenes.GameScene;

public class CorrosionTrap extends Trap {

	{
		color = GREY;
		shape = GRILL;
	}

	@Override
	public void activate() {

		CorrosiveGas corrosiveGas = Blob.seed(pos, 80 + 5 * Dungeon.depth, CorrosiveGas.class);

		corrosiveGas.setStrength(1+Dungeon.depth/4);

		GameScene.add(corrosiveGas);

	}
}
