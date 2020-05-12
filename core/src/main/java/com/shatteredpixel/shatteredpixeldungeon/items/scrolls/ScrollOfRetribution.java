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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRetribution extends Scroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_RETRIB;
	}
	
	@Override
	public void doRead() {
		
		GameScene.flash( 0xFFFFFF );
		
		//scales from 0x to 1x power, maxing at ~10% HP
		float hpPercent = (curUser.HT - curUser.HP)/(float)(curUser.HT);
		float power = Math.min( 4f, 4.45f*hpPercent);
		
		Sample.INSTANCE.play( Assets.Sounds.BLAST );
		Invisibility.dispel();
		
		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			if (Dungeon.level.heroFOV[mob.pos]) {
				//deals 10%HT, plus 0-90%HP based on scaling
				mob.damage(Math.round(mob.HT/10f + (mob.HP * power * 0.225f)), this);
				if (mob.isAlive()) {
					Buff.prolong(mob, Blindness.class, Blindness.DURATION);
				}
			}
		}
		
		Buff.prolong(curUser, Weakness.class, Weakness.DURATION);
		Buff.prolong(curUser, Blindness.class, Blindness.DURATION);
		Dungeon.observe();
		
		setKnown();
		
		readAnimation();
		
	}
	
	@Override
	public void empoweredRead() {
		GameScene.flash( 0xFFFFFF );
		
		Sample.INSTANCE.play( Assets.Sounds.BLAST );
		Invisibility.dispel();
		
		//scales from 3x to 5x power, maxing at ~20% HP
		float hpPercent = (curUser.HT - curUser.HP)/(float)(curUser.HT);
		float power = Math.min( 5f, 3f + 2.5f*hpPercent);
		
		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			if (Dungeon.level.heroFOV[mob.pos]) {
				mob.damage(Math.round(mob.HP * power/5f), this);
			}
		}
		
		setKnown();
		
		readAnimation();
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
}
