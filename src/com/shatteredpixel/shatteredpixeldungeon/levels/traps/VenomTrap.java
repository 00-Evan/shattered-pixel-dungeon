/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.VenomGas;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TrapSprite;

public class VenomTrap extends Trap {

	{
		name = "Venom gas trap";
		color = TrapSprite.VIOLET;
		shape = TrapSprite.GRILL;
	}

	@Override
	public void activate() {

		VenomGas venomGas = Blob.seed(pos, 80 + 5 * Dungeon.depth, VenomGas.class);

		venomGas.setStrength(1+Dungeon.depth/4);

		GameScene.add(venomGas);

	}

	@Override
	public String desc() {
		return "Triggering this trap will set a cloud of deadly venom gas loose within the immediate area.";
	}
}
