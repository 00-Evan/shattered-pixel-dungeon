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

package com.saqfish.spdnet.actors.blobs;

import com.saqfish.spdnet.Challenges;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.effects.BlobEmitter;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.items.Generator;
import com.saqfish.spdnet.items.Generator.Category;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.artifacts.Artifact;
import com.saqfish.spdnet.items.potions.Potion;
import com.saqfish.spdnet.items.potions.PotionOfStrength;
import com.saqfish.spdnet.items.rings.Ring;
import com.saqfish.spdnet.items.scrolls.Scroll;
import com.saqfish.spdnet.items.scrolls.ScrollOfTransmutation;
import com.saqfish.spdnet.items.scrolls.ScrollOfUpgrade;
import com.saqfish.spdnet.items.wands.Wand;
import com.saqfish.spdnet.items.weapon.Weapon;
import com.saqfish.spdnet.items.weapon.melee.MagesStaff;
import com.saqfish.spdnet.items.weapon.melee.MeleeWeapon;
import com.saqfish.spdnet.journal.Catalog;
import com.saqfish.spdnet.journal.Notes.Landmark;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.plants.Plant;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class WaterOfTransmutation extends WellWater {
	
	@Override
	protected Item affectItem( Item item, int pos ) {
		
		item = ScrollOfTransmutation.changeItem(item);
		
		//incase a never-seen item pops out
		if (item != null&& item.isIdentified()){
			Catalog.setSeen(item.getClass());
		}

		return item;

	}
	
	@Override
	protected boolean affectHero(Hero hero) {
		return false;
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( Speck.factory( Speck.CHANGE ), 0.2f, 0 );
	}
	
	@Override
	protected Landmark record() {
		return Landmark.WELL_OF_TRANSMUTATION;
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
