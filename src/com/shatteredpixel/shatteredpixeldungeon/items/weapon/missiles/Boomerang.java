/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;

import java.util.ArrayList;

public class Boomerang extends MissileWeapon {

	{
		image = ItemSpriteSheet.BOOMERANG;

		stackable = false;

		unique = true;
		bones = false;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		if (!isEquipped(hero)) actions.add(AC_EQUIP);
		return actions;
	}

	@Override
	public int min(int lvl) {
		return  1 +
				lvl;
	}

	@Override
	public int max(int lvl) {
		return  5 +     //half the base damage of a tier-1 weapon
				2 * lvl;//scales the same as a tier 1 weapon
	}

	@Override
	public int STRReq(int lvl) {
		lvl = Math.max(0, lvl);
		//strength req decreases at +1,+3,+6,+10,etc.
		return 10 - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}

	@Override
	public boolean isUpgradable() {
		return true;
	}
	
	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	
	@Override
	public Item upgrade( boolean enchant ) {
		super.upgrade( enchant );
		
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public Item degrade() {
		return super.degrade();
	}

	@Override
	public int proc( Char attacker, Char defender, int damage ) {
		if (attacker instanceof Hero && ((Hero)attacker).rangedWeapon == this) {
			circleBack( defender.pos, (Hero)attacker );
		}
		return super.proc( attacker, defender, damage );
	}

	@Override
	protected void miss( int cell ) {
		circleBack( cell, curUser );
	}

	private void circleBack( int from, Hero owner ) {

		((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
				reset( from, curUser.pos, curItem, null );

		if (throwEquiped) {
			owner.belongings.weapon = this;
			owner.spend( -TIME_TO_EQUIP );
			Dungeon.quickslot.replaceSimilar(this);
			updateQuickslot();
		} else
		if (!collect( curUser.belongings.backpack )) {
			Dungeon.level.drop( this, owner.pos ).sprite.drop();
		}
	}

	private boolean throwEquiped;

	@Override
	public void cast( Hero user, int dst ) {
		throwEquiped = isEquipped( user );
		if (throwEquiped) Dungeon.quickslot.convertToPlaceholder(this);
		super.cast( user, dst );
	}
	
	@Override
	public String desc() {
		String info = super.desc();
		switch (imbue) {
			case LIGHT:
				info += "\n\n" + Messages.get(Weapon.class, "lighter");
				break;
			case HEAVY:
				info += "\n\n" + Messages.get(Weapon.class, "heavier");
				break;
			case NONE:
		}

		return info;
	}
}
