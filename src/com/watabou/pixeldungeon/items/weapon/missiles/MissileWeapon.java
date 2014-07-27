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
package com.watabou.pixeldungeon.items.weapon.missiles;

import java.util.ArrayList;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.windows.WndOptions;

public class MissileWeapon extends Weapon {

	private static final String TXT_MISSILES	= "Missile weapon";
	private static final String TXT_YES			= "Yes, I know what I'm doing";
	private static final String TXT_NO			= "No, I changed my mind";
	private static final String TXT_R_U_SURE	= 
		"Do you really want to equip it as a melee weapon?";
	
	{
		stackable = true;
		levelKnown = true;
		defaultAction = AC_THROW;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.heroClass != HeroClass.HUNTRESS && hero.heroClass != HeroClass.ROGUE) {
			actions.remove( AC_EQUIP );
			actions.remove( AC_UNEQUIP );
		}
		return actions;
	}

	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == curUser) {
			super.onThrow( cell );
		} else {
			if (!curUser.shoot( enemy, this )) {
				miss( cell );
			}
		}
	}
	
	protected void miss( int cell ) {
		super.onThrow( cell );
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		
		super.proc( attacker, defender, damage );
		
		Hero hero = (Hero)attacker;
		if (!hero.usingRanged && stackable) {
			if (quantity == 1) {
				doUnequip( hero, false );
			} else {
				detach( null );
			}
		}
	}
	
	@Override
	public boolean doEquip( final Hero hero ) {
		GameScene.show( 
			new WndOptions( TXT_MISSILES, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
				@Override
				protected void onSelect(int index) {
					if (index == 0) {
						MissileWeapon.super.doEquip( hero );
					}
				};
			}
		);
		
		return false;
	}
	
	@Override
	public Item random() {
		return this;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
		
		StringBuilder info = new StringBuilder( desc() );
		
		info.append( "\n\nAverage damage of this weapon equals to " + (MIN + (MAX - MIN) / 2) + " points per hit. " );
		
		if (Dungeon.hero.belongings.backpack.items.contains( this )) {
			if (STR > Dungeon.hero.STR()) {
				info.append( 
					"Because of your inadequate strength the accuracy and speed " +
					"of your attack with this " + name + " is decreased." );
			}
			if (STR < Dungeon.hero.STR()) {
				info.append( 
					"Because of your excess strength the damage " +
					"of your attack with this " + name + " is increased." );
			}
		}
		
		if (isEquipped( Dungeon.hero )) {
			info.append( "\n\nYou hold the " + name + " at the ready." ); 
		}
		
		return info.toString();
	}
}
