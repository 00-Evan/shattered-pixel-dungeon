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
package com.shatteredpixel.shatteredicepixeldungeon.actors.mobs;

import java.util.HashSet;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredicepixeldungeon.items.weapon.enchantments.Leech;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.BatSprite;
import com.watabou.utils.Random;

public class Bat extends Mob {

	{
		name = "vampire bat";
		spriteClass = BatSprite.class;
		
		HP = HT = 30;
		defenseSkill = 15;
		baseSpeed = 2f;
		
		EXP = 7;
		maxLvl = 15;
		
		flying = true;
		
		loot = new PotionOfHealing();
		lootChance = 0.1667f; //by default, see die()
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 6, 12 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 16;
	}
	
	@Override
	public int dr() {
		return 4;
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		
		int reg = Math.min( damage, HT - HP );
		
		if (reg > 0) {
			HP += reg;
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
		}
		
		return damage;
	}

    @Override
    public void die( Object cause ){
        //sets drop chance
        lootChance = 1f/((6 + Dungeon.limitedDrops.batHP.count ));
        super.die( cause );
    }

    @Override
    protected Item createLoot(){
        Dungeon.limitedDrops.batHP.count++;
        return super.createLoot();
    }
	
	@Override
	public String description() {
		return
			"These brisk and tenacious inhabitants of cave domes may defeat much larger opponents by " +
			"replenishing their health with each successful attack.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( Leech.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
