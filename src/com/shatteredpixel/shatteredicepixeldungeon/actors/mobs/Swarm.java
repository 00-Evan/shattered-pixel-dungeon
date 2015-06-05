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

import java.util.ArrayList;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredicepixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.SwarmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Swarm extends Mob {

	{
		name = "swarm of flies";
		spriteClass = SwarmSprite.class;
		
		HP = HT = 80;
		defenseSkill = 5;
		
		maxLvl = 10;
		
		flying = true;

        loot = new PotionOfHealing();
        lootChance = 0.2f; //by default, see die()
	}
	
	private static final float SPLIT_DELAY	= 1f;
	
	int generation	= 0;
	
	private static final String GENERATION	= "generation";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( GENERATION, generation );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		generation = bundle.getInt( GENERATION );
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 4 );
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {

		if (HP >= damage + 2) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			boolean[] passable = Level.passable;
			
			int[] neighbours = {pos + 1, pos - 1, pos + Level.WIDTH, pos - Level.WIDTH};
			for (int n : neighbours) {
				if (passable[n] && Actor.findChar( n ) == null) {
					candidates.add( n );
				}
			}
	
			if (candidates.size() > 0) {
				
				Swarm clone = split();
				clone.HP = (HP - damage) / 2;
				clone.pos = Random.element( candidates );
				clone.state = clone.HUNTING;
				
				if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
					Door.enter( clone.pos );
				}
				
				GameScene.add( clone, SPLIT_DELAY );
				Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );
				
				HP -= clone.HP;
			}
		}
		
		return damage;
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	private Swarm split() {
		Swarm clone = new Swarm();
		clone.generation = generation + 1;
		if (buff( Burning.class ) != null) {
			Buff.affect( clone, Burning.class ).reignite( clone );
		}
		if (buff( Poison.class ) != null) {
			Buff.affect( clone, Poison.class ).set( 2 );
		}
		return clone;
	}
	
	@Override
	public void die( Object cause ){
        //sets drop chance
        lootChance = 1f/((5 + Dungeon.limitedDrops.swarmHP.count ) * (generation+1) );
        super.die( cause );
    }

    @Override
    protected Item createLoot(){
        Dungeon.limitedDrops.swarmHP.count++;
        return super.createLoot();
    }
	
	@Override
	public String description() {
		return
			"The deadly swarm of flies buzzes angrily. Every non-magical attack " +
			"will split it into two smaller but equally dangerous swarms.";
	}
}
