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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import java.util.ArrayList;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.utils.Random;

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
            if (this instanceof Boomerang)
                super.onThrow( cell );
            else
                miss( cell );
		} else {
			if (!curUser.shoot( enemy, this )) {
				miss( cell );
			} else if (!(this instanceof Boomerang)){
                int bonus = 0;
                for (Buff buff : curUser.buffs(RingOfSharpshooting.Aim.class)) {
                    bonus += ((RingOfSharpshooting.Aim)buff).level;
                }
                if (Random.Float() > Math.pow(0.7, bonus))
                    Dungeon.level.drop( this, cell ).sprite.drop();
            }
		}
	}
	
	protected void miss( int cell ) {
        int bonus = 0;
        for (Buff buff : curUser.buffs(RingOfSharpshooting.Aim.class)) {
            bonus += ((RingOfSharpshooting.Aim)buff).level;
        }

        if (Random.Float() < Math.pow(0.6, -bonus))
            super.onThrow( cell );
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		
		super.proc( attacker, defender, damage );
		
		Hero hero = (Hero)attacker;
        if (hero.rangedWeapon == null && stackable) {
            if (quantity == 1) {
                doUnequip( hero, false, false );
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
