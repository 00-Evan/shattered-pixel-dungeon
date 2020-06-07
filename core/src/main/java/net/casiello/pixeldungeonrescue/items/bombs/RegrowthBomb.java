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

package net.casiello.pixeldungeonrescue.items.bombs;

import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.Actor;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.blobs.Blob;
import net.casiello.pixeldungeonrescue.actors.blobs.Regrowth;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Healing;
import net.casiello.pixeldungeonrescue.effects.Splash;
import net.casiello.pixeldungeonrescue.items.Generator;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfHealing;
import net.casiello.pixeldungeonrescue.items.wands.WandOfRegrowth;
import net.casiello.pixeldungeonrescue.levels.Terrain;
import net.casiello.pixeldungeonrescue.plants.Plant;
import net.casiello.pixeldungeonrescue.plants.Starflower;
import net.casiello.pixeldungeonrescue.scenes.GameScene;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RegrowthBomb extends Bomb {
	
	{
		//TODO visuals
		image = ItemSpriteSheet.REGROWTH_BOMB;
	}
	
	@Override
	public boolean explodesDestructively() {
		return false;
	}
	
	@Override
	public void explode(int cell) {
		super.explode(cell);
		
		if (Dungeon.level.heroFOV[cell]) {
			Splash.at(cell, 0x00FF00, 30);
		}
		
		ArrayList<Integer> plantCandidates = new ArrayList<>();
		
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Char ch = Actor.findChar(i);
				if (ch != null){
					if (ch.alignment == Dungeon.hero.alignment) {
						//same as a healing potion
						Buff.affect( ch, Healing.class ).setHeal((int)(0.8f*ch.HT + 14), 0.25f, 0);
						PotionOfHealing.cure(ch);
					}
				} else if ( Dungeon.level.map[i] == Terrain.EMPTY ||
							Dungeon.level.map[i] == Terrain.EMBERS ||
							Dungeon.level.map[i] == Terrain.EMPTY_DECO ||
							Dungeon.level.map[i] == Terrain.GRASS ||
							Dungeon.level.map[i] == Terrain.HIGH_GRASS ||
							Dungeon.level.map[i] == Terrain.FURROWED_GRASS){
					
					plantCandidates.add(i);
				}
				GameScene.add( Blob.seed( i, 10, Regrowth.class ) );
			}
		}

		int plants = Random.chances(new float[]{0, 6, 3, 1});

		for (int i = 0; i < plants; i++) {
			Integer plantPos = Random.element(plantCandidates);
			if (plantPos != null) {
				Dungeon.level.plant((Plant.Seed) Generator.random(Generator.Category.SEED), plantPos);
				plantCandidates.remove(plantPos);
			}
		}
		
		Integer plantPos = Random.element(plantCandidates);
		if (plantPos != null){
			Plant.Seed plant;
			switch (Random.chances(new float[]{0, 6, 3, 1})){
				case 1: default:
					plant = new WandOfRegrowth.Dewcatcher.Seed();
					break;
				case 2:
					plant = new WandOfRegrowth.Seedpod.Seed();
					break;
				case 3:
					plant = new Starflower.Seed();
					break;
			}
			Dungeon.level.plant( plant, plantPos);
		}
	}
	
	@Override
	public int price() {
		//prices of ingredients
		return quantity * (20 + 30);
	}
}
