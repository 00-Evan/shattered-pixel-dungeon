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

package com.saqfish.spdnet.items.potions.exotic;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.blobs.Blob;
import com.saqfish.spdnet.actors.blobs.CorrosiveGas;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class PotionOfCorrosiveGas extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_CORROGAS;
	}
	
	@Override
	public void shatter( int cell ) {
		
		if (Dungeon.level.heroFOV[cell]) {
			identify();
			
			splash( cell );
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			Sample.INSTANCE.play( Assets.Sounds.GAS );
		}
		
		GameScene.add( Blob.seed( cell, 200, CorrosiveGas.class ).setStrength( 1 + Dungeon.depth/5));
	}
}
