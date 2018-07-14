/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.HashSet;

public class ScrollOfForesight extends ExoticScroll {
	@Override
	public void doRead() {
		
		curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );
		
		readAnimation();
		setKnown();
		
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		
		HashSet<Class<? extends Potion>> potions = Potion.getUnknown();
		HashSet<Class<? extends Scroll>> scrolls = Scroll.getUnknown();
		HashSet<Class<? extends Ring>> rings = Ring.getUnknown();
		
		int total = potions.size() + scrolls.size() + rings.size();
		
		if (total == 0){
			GLog.n("Nothing left to Identify!");
			return;
		}
		
		/*
		//items which the player holds have lower priority
		HashSet<Class <? extends Potion>> heldPotions = new HashSet<>();
		HashSet<Class <? extends Scroll>> heldScrolls = new HashSet<>();
		HashSet<Class <? extends Ring>> heldRings = new HashSet<>();
		
		for (Class <? extends Potion> p : potions){
			if (curUser.belongings.getItem(p) != null){
				heldPotions.add(p);
			}
		}
		potions.removeAll(heldPotions);
		
		for (Class <? extends Scroll> s : scrolls){
			if (curUser.belongings.getItem(s) != null){
				heldScrolls.add(s);
			}
		}
		scrolls.removeAll(heldScrolls);
		
		for (Class <? extends Ring> r : rings){
			if (curUser.belongings.getItem(r) != null){
				heldRings.add(r);
			}
		}
		rings.removeAll(heldRings);*/
		
		int left = 4;
		
		while (left > 0 && total > 0) {
			try {
				switch (Random.Int(3)) {
					case 0:
					default:
						if (potions.isEmpty()) continue;
						Potion p = Random.element(potions).newInstance();
						p.setKnown();
						GLog.i(p.name() + " identified!");
						potions.remove(p.getClass());
						break;
					case 1:
						if (scrolls.isEmpty()) continue;
						Scroll s = Random.element(scrolls).newInstance();
						s.setKnown();
						GLog.i(s.name() + " identified!");
						scrolls.remove(s.getClass());
						break;
					case 2:
						if (rings.isEmpty()) continue;
						Ring r = Random.element(rings).newInstance();
						r.setKnown();
						GLog.i(r.name() + " identified!");
						rings.remove(r.getClass());
						break;
				}
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
			}
			left --;
			total --;
		}
	}
}
