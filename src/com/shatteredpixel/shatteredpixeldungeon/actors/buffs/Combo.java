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
package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Combo extends Buff implements ActionIndicator.Action {
	
	private int count = 0;
	private float comboTime = 0f;
	private int misses = 0;
	
	@Override
	public int icon() {
		return BuffIndicator.COMBO;
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	public void hit() {
		
		count++;
		comboTime = 3f;
		misses = 0;
		
		if (count >= 2) {

			ActionIndicator.setAction( this );
			Badges.validateMasteryCombo( count );

			GLog.p( Messages.get(this, "combo", count) );
			
		}

	}

	public void miss(){
		misses++;
		if (misses >= 2){
			detach();
		}
	}

	@Override
	public void detach() {
		super.detach();
		ActionIndicator.clearAction(this);
	}

	@Override
	public boolean act() {
		comboTime-=TICK;
		spend(TICK);
		if (comboTime <= 0) {
			detach();
		}
		return true;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	private static final String COUNT = "count";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNT, count);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		count = bundle.getInt( COUNT );
		if (count >= 2) ActionIndicator.setAction(this);
	}

	@Override
	public Image getIcon() {
		Image icon;
		if (((Hero)target).belongings.weapon != null){
			icon = new ItemSprite(Dungeon.hero.belongings.weapon);
		} else {
			icon = new ItemSprite(new Item(){ {image = ItemSpriteSheet.WEAPON; }});
		}

		if (count >= 10)    icon.tint(0xFFFF0000);
		else if (count >= 8)icon.tint(0xFFFFCC00);
		else if (count >= 6)icon.tint(0xFFFFFF00);
		else if (count >= 4)icon.tint(0xFFCCFF00);
		else                icon.tint(0xFF00FF00);

		return icon;
	}

	@Override
	public void doAction() {
		ActionIndicator.clearAction(this);
		//TODO
	}
}
