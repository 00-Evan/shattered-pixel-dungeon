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

package net.casiello.pixeldungeonrescue.items.potions.brews;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.blobs.Blizzard;
import net.casiello.pixeldungeonrescue.actors.blobs.Blob;
import net.casiello.pixeldungeonrescue.items.potions.AlchemicalCatalyst;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfFrost;
import net.casiello.pixeldungeonrescue.scenes.GameScene;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class BlizzardBrew extends Brew {
	
	{
		image = ItemSpriteSheet.BREW_BLIZZARD;
	}
	
	@Override
	public void shatter(int cell) {
		if (Dungeon.level.heroFOV[cell]) {
			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
		}
		
		GameScene.add( Blob.seed( cell, 1000, Blizzard.class ) );
	}
	
	@Override
	public int price() {
		//prices of ingredients
		return quantity * (30 + 40);
	}
	
	public static class Recipe extends net.casiello.pixeldungeonrescue.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfFrost.class, AlchemicalCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = BlizzardBrew.class;
			outQuantity = 1;
		}
		
	}
}
