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

package com.saqfish.spdnet.actors.buffs;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Actor;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.actors.hero.Talent;
import com.saqfish.spdnet.items.weapon.SpiritBow;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.sprites.ItemSprite;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.ui.ActionIndicator;
import com.saqfish.spdnet.ui.BuffIndicator;
import com.saqfish.spdnet.ui.QuickSlotButton;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class SnipersMark extends FlavourBuff implements ActionIndicator.Action {

	public int object = 0;
	public int level = 0;

	private static final String OBJECT    = "object";
	private static final String LEVEL    = "level";

	public static final float DURATION = 4f;

	{
		type = buffType.POSITIVE;
	}

	public void set(int object, int level){
		this.object = object;
		this.level = level;
	}
	
	@Override
	public boolean attachTo(Char target) {
		ActionIndicator.setAction(this);
		return super.attachTo(target);
	}
	
	@Override
	public void detach() {
		super.detach();
		ActionIndicator.clearAction(this);
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( OBJECT, object );
		bundle.put( LEVEL, level );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		object = bundle.getInt( OBJECT );
		level = bundle.getInt( LEVEL );
	}

	@Override
	public int icon() {
		return BuffIndicator.MARK;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
	
	@Override
	public Image getIcon() {
		return new ItemSprite(ItemSpriteSheet.SPIRIT_BOW, null);
	}
	
	@Override
	public void doAction() {
		
		Hero hero = Dungeon.hero;
		if (hero == null) return;
		
		SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
		if (bow == null) return;
		
		SpiritBow.SpiritArrow arrow = bow.knockArrow();
		if (arrow == null) return;
		
		Char ch = (Char) Actor.findById(object);
		if (ch == null) return;
		
		int cell = QuickSlotButton.autoAim(ch, arrow);
		if (cell == -1) return;
		
		bow.sniperSpecial = true;
		bow.sniperSpecialBonusDamage = level*Dungeon.hero.pointsInTalent(Talent.SHARED_UPGRADES)/15f;
		
		arrow.cast(hero, cell);
		detach();
		
	}
}
