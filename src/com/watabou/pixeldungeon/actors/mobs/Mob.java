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
package com.watabou.pixeldungeon.actors.mobs;

import java.util.HashSet;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Amok;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Sleep;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.Wound;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Mob extends Char {
	
	private static final String	TXT_DIED	= "You hear something died in the distance";
	
	protected static final String TXT_NOTICE1	= "?!";
	protected static final String TXT_RAGE		= "#$%^";
	protected static final String TXT_EXP		= "%+dEXP";
	
	public enum State {
		SLEEPING,
		HUNTING,
		WANDERING,
		FLEEING,
		PASSIVE
	}
	public State state = State.SLEEPING;
	
	public Class<? extends CharSprite> spriteClass;
	
	protected int target = -1;
	
	protected int defenseSkill = 0;
	
	protected int EXP = 1;
	protected int maxLvl = 30;
	
	protected Char enemy;
	protected boolean enemySeen;
	protected boolean alerted = false;

	protected static final float TIME_TO_WAKE_UP = 1f;
	
	public boolean hostile = true;
	
	// Unreachable target
	public static final Mob DUMMY = new Mob() {
		{
			pos = -1;
		}
	};
	
	private static final String STATE	= "state";
	private static final String TARGET	= "target";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );
		
		bundle.put( STATE, state.toString() );
		if (state != State.SLEEPING) {
			bundle.put( TARGET, target );
		}
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		state = State.valueOf( bundle.getString( STATE ) );
		if (state != State.SLEEPING) {
			target = bundle.getInt( TARGET );
		}
	}
	
	public CharSprite sprite() {
		CharSprite sprite = null;
		try {
			sprite = spriteClass.newInstance();
		} catch (Exception e) {
		}
		return sprite;
	}
	
	@Override
	protected boolean act() {
		
		super.act();
		
		boolean alertedNow = alerted;
		alerted = false;
		
		sprite.hideAlert();
		
		if (paralysed) {
			enemySeen = false;
			spend( TICK );
			return true;
		}
		
		enemy = chooseEnemy();
		
		boolean enemyInFOV = enemy.isAlive() && Level.fieldOfView[enemy.pos] && enemy.invisible <= 0;
		
		int oldPos = pos;
		
		switch (state) {
		
		case SLEEPING:
			if (enemyInFOV && 
				Random.Int( distance( enemy ) + enemy.stealth() + (enemy.flying ? 2 : 0) ) == 0) {
				
				enemySeen = true;

				notice();
				state = State.HUNTING;
				target = enemy.pos;
				
				spend( TIME_TO_WAKE_UP );
				
			} else {
				
				enemySeen = false;
				
				spend( TICK );
				
			}
			return true;

		case WANDERING:
			if (enemyInFOV && (alertedNow || Random.Int( distance( enemy ) / 2 + enemy.stealth() ) == 0)) {
				
				enemySeen = true;
				
				notice();
				state = State.HUNTING;
				target = enemy.pos;
				
			} else {
				
				enemySeen = false;
				
				if (target != -1 && getCloser( target )) {
					spend( 1 / speed() );
					return moveSprite( oldPos, pos );
				} else {
					target = Dungeon.level.randomDestination();
					spend( TICK );
				}
				
			}
			return true;
			
		case HUNTING:
			enemySeen = enemyInFOV;
			if (enemyInFOV && canAttack( enemy )) {
				
				return doAttack( enemy );
				
			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				}

				if (target != -1 && getCloser( target )) {
					

					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );
					
				} else {
					
					spend( TICK );
					state = State.WANDERING;
					target = Dungeon.level.randomDestination();	// <--------
					return true;
				}
			}
			
		case FLEEING:
			enemySeen = enemyInFOV;
			if (enemyInFOV) {
				target = enemy.pos;
			}
			if (target != -1 && getFurther( target )) {
				
				spend( 1 / speed() );
				return moveSprite( oldPos, pos );
				
			} else {
				
				spend( TICK );
				nowhereToRun();
				
				return true;
			}
			
		case PASSIVE:
			enemySeen = false;
			spend( TICK );
			return true;
			
		}
		
		return true;

	}
	
	protected Char chooseEnemy() {
		
		if (buff( Amok.class ) != null) {
			if (enemy == Dungeon.hero || enemy == null) {
				
				HashSet<Mob> enemies = new HashSet<Mob>();
				for (Mob mob:Dungeon.level.mobs) {
					if (mob != this && Level.fieldOfView[mob.pos]) {
						enemies.add( mob );
					}
				}
				if (enemies.size() > 0) {
					return Random.element( enemies );
				}
				
			} else {
				return enemy;
			}
		}
		
		Terror terror = (Terror)buff( Terror.class );
		if (terror != null) {
			return terror.source;
		}
		
		return Dungeon.hero;
	}
	
	protected void nowhereToRun() {
	}
	
	protected boolean moveSprite( int from, int to ) {

		if (sprite.isVisible() && (Dungeon.visible[from] || Dungeon.visible[to])) {
			sprite.move( from, to );
			return false;
		} else {
			sprite.place( to );
			return true;
		}
	}
	
	@Override
	public void add( Buff buff ) {
		super.add( buff );
		if (buff instanceof Amok) {
			if (sprite != null) {
				sprite.showStatus( CharSprite.NEGATIVE, TXT_RAGE );
			}
			state = State.HUNTING;
		} else if (buff instanceof Terror) {
			state = State.FLEEING;
		} else if (buff instanceof Sleep) {
			if (sprite != null) {
				new Flare( 4, 32 ).color( 0x44ffff, true ).show( sprite, 2f ) ;
			}
			state = State.SLEEPING;
			postpone( Sleep.SWS );
		}
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove( buff );
		if (buff instanceof Terror) {
			sprite.showStatus( CharSprite.NEGATIVE, TXT_RAGE );
			state = State.HUNTING;
		}
	}
	
	protected boolean canAttack( Char enemy ) {
		return Level.adjacent( pos, enemy.pos ) && !pacified;
	}
	
	protected boolean getCloser( int target ) {
		
		if (rooted) {
			return false;
		}
		
		int step = Dungeon.findPath( this, pos, target, 
			Level.passable, 
			Level.fieldOfView );
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean getFurther( int target ) {
		int step = Dungeon.flee( this, pos, target, 
			Level.passable, 
			Level.fieldOfView );
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void move( int step ) {
		super.move( step );
		
		if (!flying) {
			Dungeon.level.mobPress( this );
		}
	}
	
	protected float attackDelay() {
		return 1f;
	}
	
	protected boolean doAttack( Char enemy ) {
		
		boolean visible = Dungeon.visible[pos];
		
		if (visible) {
			sprite.attack( enemy.pos );
		} else {
			attack( enemy );
		}
				
		spend( attackDelay() );
		
		return !visible;
	}
	
	@Override
	public void onAttackComplete() {
		attack( enemy );
		super.onAttackComplete();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return enemySeen && !paralysed ? defenseSkill : 0;
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {
		if (!enemySeen && enemy == Dungeon.hero && ((Hero)enemy).subClass == HeroSubClass.ASSASSIN) {
			damage += Random.Int( 1, damage );
			Wound.hit( this );
		}
		return damage;
	}
	
	@Override
	public void damage( int dmg, Object src ) {

		Terror.recover( this );
		
		if (state == State.SLEEPING) {
			state = State.WANDERING;
		}
		alerted = true;
		
		super.damage( dmg, src );
	}
	
	
	@Override
	public void destroy() {
		
		super.destroy();
		
		Dungeon.level.mobs.remove( this );
		
		if (Dungeon.hero.isAlive()) {
			
			if (hostile) {
				Statistics.enemiesSlain++;
				Badges.validateMonstersSlain();
				Statistics.qualifiedForNoKilling = false;
				
				if (Dungeon.nightMode) {
					Statistics.nightHunt++;
				} else {
					Statistics.nightHunt = 0;
				}
				Badges.validateNightHunter();
			}
			
			if (Dungeon.hero.lvl <= maxLvl && EXP > 0) {
				Dungeon.hero.sprite.showStatus( CharSprite.POSITIVE, TXT_EXP, EXP );
				Dungeon.hero.earnExp( EXP );
			}
		}
	}
	
	@Override
	public void die( Object cause ) {
		
		super.die( cause );
		
		if (Dungeon.hero.lvl <= maxLvl + 2) {
			dropLoot();
		}
		
		if (Dungeon.hero.isAlive() && !Dungeon.visible[pos]) {	
			GLog.i( TXT_DIED );
		}
	}
	
	protected Object loot = null;
	protected float lootChance = 0;
	
	@SuppressWarnings("unchecked")
	protected void dropLoot() {
		if (loot != null && Random.Float() < lootChance) {
			Item item = null;
			if (loot instanceof Generator.Category) {
				
				item = Generator.random( (Generator.Category)loot );
				
			} else if (loot instanceof Class<?>) {
				
				item = Generator.random( (Class<? extends Item>)loot );
				
			} else {
				
				item = (Item)loot;
				
			}
			Dungeon.level.drop( item, pos ).sprite.drop();
		}
	}
	
	public boolean reset() {
		return false;
	}
	
	public void beckon( int cell ) {
		
		notice();
		
		if (state != State.HUNTING) {
			state = State.WANDERING;
		}
		target = cell;
	}
	
	public String description() {
		return "Real description is coming soon!";
	}
	
	public void notice() {
		sprite.showAlert();
	}
	
	public void yell( String str ) {
		GLog.n( "%s: \"%s\" ", name, str );
	}
	
	public static abstract class NPC extends Mob {
		
		{
			HP = HT = 1;
			EXP = 0;
		
			hostile = false;
			state = State.PASSIVE;
		}
		
		protected void throwItem() {
			Heap heap = Dungeon.level.heaps.get( pos );
			if (heap != null) {
				int n;
				do {
					n = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
				} while (!Level.passable[n] && !Level.avoid[n]);
				Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
			}
		}
		
		@Override
		public void beckon( int cell ) {
		}
		
		abstract public void interact();
	}
}
