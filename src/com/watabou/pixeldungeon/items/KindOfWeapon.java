/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.items;

import java.util.ArrayList;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class KindOfWeapon extends EquipableItem {

	private static final String TXT_EQUIP_CURSED	= "you wince as your grip involuntarily tightens around your %s";
	private static final String TXT_UNEQUIP_CURSED	= "you can't remove cursed %s!";
	
	protected static final float TIME_TO_EQUIP = 1f;
	
	public int		MIN	= 0;
	public int		MAX = 1;
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		return actions;
	}
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.weapon == this;
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		
		detachAll( hero.belongings.backpack );
		
		if (hero.belongings.weapon == null || hero.belongings.weapon.doUnequip( hero, true )) {
			
			hero.belongings.weapon = this;
			activate( hero );
			
			QuickSlot.refresh();
			
			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( TXT_EQUIP_CURSED, name() );
			}
			
			hero.spendAndNext( TIME_TO_EQUIP );
			return true;
			
		} else {
			
			collect( hero.belongings.backpack );
			return false;
		}
	}
	
	@Override
	public boolean doUnequip( Hero hero, boolean collect ) {
		
		if (cursed) {
			GLog.w( TXT_UNEQUIP_CURSED, name() );
			return false;
		}
		
		hero.belongings.weapon = null;
		hero.spendAndNext( TIME_TO_EQUIP );
		
		if (collect && !collect( hero.belongings.backpack )) {
			Dungeon.level.drop( this, hero.pos );
		}
					
		return true;
	}
	
	public void activate( Hero hero ) {
	}
	
	public int damageRoll( Hero owner ) {
		return Random.NormalIntRange( MIN, MAX );
	}
	
	public float acuracyFactor( Hero hero ) {
		return 1f;
	}
	
	public float speedFactor( Hero hero ) {
		return 1f;
	}
	
	public void proc( Char attacker, Char defender, int damage ) {
	}
	
}
