/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.items.bags;

import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.quest.GooBlob;
import com.saqfish.spdnet.items.quest.MetalShard;
import com.saqfish.spdnet.items.stones.Runestone;
import com.saqfish.spdnet.plants.Plant;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;

public class VelvetPouch extends Bag {

	{
		image = ItemSpriteSheet.POUCH;
	}

	@Override
	public boolean canHold( Item item ) {
		if (item instanceof Plant.Seed || item instanceof Runestone
				|| item instanceof GooBlob || item instanceof MetalShard){
			return super.canHold(item);
		} else {
			return false;
		}
	}

	public int capacity(){
		return 19;
	}
	
	@Override
	public int value() {
		return 30;
	}

}
