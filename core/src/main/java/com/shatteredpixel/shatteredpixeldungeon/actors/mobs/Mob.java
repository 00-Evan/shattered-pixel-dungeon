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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Surprise;
import com.shatteredpixel.shatteredpixeldungeon.effects.Wound;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public abstract class Mob extends Char {

	{
		actPriority = MOB_PRIO;
		
		alignment = Alignment.ENEMY;
	}
	
	private static final String	TXT_DIED	= "You hear something died in the distance";
	
	protected static final String TXT_NOTICE1	= "?!";
	protected static final String TXT_RAGE		= "#$%^";
	protected static final String TXT_EXP		= "%+dEXP";

	public AiState SLEEPING     = new Sleeping();
	public AiState HUNTING		= new Hunting();
	public AiState WANDERING	= new Wandering();
	public AiState FLEEING		= new Fleeing();
	public AiState PASSIVE		= new Passive();
	public AiState state = SLEEPING;
	
	public Class<? extends CharSprite> spriteClass;
	
	protected int target = -1;
	
	protected int defenseSkill = 0;

	public int MAX_ATT = 0;
	public int MIN_ATT = 0;
	public int MAX_DEF = 0;
	public int MIN_DEF = 0;

	public int EXP = 1;
	public int maxLvl = Hero.MAX_LEVEL;
	
	protected Char enemy;
	protected boolean enemySeen;
	protected boolean alerted = false;

	protected static final float TIME_TO_WAKE_UP = 1f;
	
	private static final String STATE	= "state";
	private static final String SEEN	= "seen";
	private static final String TARGET	= "target";
	private static final String MAX_LVL	= "max_lvl";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );

		if (state == SLEEPING) {
			bundle.put( STATE, Sleeping.TAG );
		} else if (state == WANDERING) {
			bundle.put( STATE, Wandering.TAG );
		} else if (state == HUNTING) {
			bundle.put( STATE, Hunting.TAG );
		} else if (state == FLEEING) {
			bundle.put( STATE, Fleeing.TAG );
		} else if (state == PASSIVE) {
			bundle.put( STATE, Passive.TAG );
		}
		bundle.put( SEEN, enemySeen );
		bundle.put( TARGET, target );
		bundle.put( MAX_LVL, maxLvl );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );

		String state = bundle.getString( STATE );
		if (state.equals( Sleeping.TAG )) {
			this.state = SLEEPING;
		} else if (state.equals( Wandering.TAG )) {
			this.state = WANDERING;
		} else if (state.equals( Hunting.TAG )) {
			this.state = HUNTING;
		} else if (state.equals( Fleeing.TAG )) {
			this.state = FLEEING;
		} else if (state.equals( Passive.TAG )) {
			this.state = PASSIVE;
		}

		enemySeen = bundle.getBoolean( SEEN );

		target = bundle.getInt( TARGET );

		if (bundle.contains(MAX_LVL)) maxLvl = bundle.getInt(MAX_LVL);
	}
	
	public CharSprite sprite() {
		return Reflection.newInstance(spriteClass);
	}
	
	@Override
	protected boolean act() {
		
		super.act();
		
		boolean justAlerted = alerted;
		alerted = false;
		
		if (justAlerted){
			sprite.showAlert();
		} else {
			sprite.hideAlert();
			sprite.hideLost();
		}
		
		if (paralysed > 0) {
			enemySeen = false;
			spend( TICK );
			return true;
		}

		if (buff(Terror.class) != null){
			state = FLEEING;
		}
		
		enemy = chooseEnemy();
		
		boolean enemyInFOV = enemy != null && enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;

		return state.act( enemyInFOV, justAlerted );
	}
	
	//FIXME this is sort of a band-aid correction for allies needing more intelligent behaviour
	protected boolean intelligentAlly = false;
	
	protected Char chooseEnemy() {

		Terror terror = buff( Terror.class );
		if (terror != null) {
			Char source = (Char)Actor.findById( terror.object );
			if (source != null) {
				return source;
			}
		}
		
		//if we are an enemy, and have no target or current target isn't affected by aggression
		//then auto-prioritize a target that is affected by aggression, even another enemy
		if (alignment == Alignment.ENEMY
				&& (enemy == null || enemy.buff(StoneOfAggression.Aggression.class) == null)) {
			for (Char ch : Actor.chars()) {
				if (ch != this && fieldOfView[ch.pos] &&
						ch.buff(StoneOfAggression.Aggression.class) != null) {
					return ch;
				}
			}
		}

		//find a new enemy if..
		boolean newEnemy = false;
		//we have no enemy, or the current one is dead/missing
		if ( enemy == null || !enemy.isAlive() || !Actor.chars().contains(enemy) || state == WANDERING) {
			newEnemy = true;
		//We are an ally, and current enemy is another ally.
		} else if (alignment == Alignment.ALLY && enemy.alignment == Alignment.ALLY) {
			newEnemy = true;
		//We are amoked and current enemy is the hero
		} else if (buff( Amok.class ) != null && enemy == Dungeon.hero) {
			newEnemy = true;
		//We are charmed and current enemy is what charmed us
		} else if (buff(Charm.class) != null && buff(Charm.class).object == enemy.id()) {
			newEnemy = true;
		//we aren't amoked, current enemy is invulnerable to us, and that enemy isn't affect by aggression
		} else if (buff( Amok.class ) == null && enemy.isInvulnerable(getClass()) && enemy.buff(StoneOfAggression.Aggression.class) == null) {
			newEnemy = true;
		}

		if ( newEnemy ) {

			HashSet<Char> enemies = new HashSet<>();

			//if the mob is amoked...
			if ( buff(Amok.class) != null) {
				//try to find an enemy mob to attack first.
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY && mob != this
							&& fieldOfView[mob.pos] && mob.invisible <= 0) {
						enemies.add(mob);
					}
				
				if (enemies.isEmpty()) {
					//try to find ally mobs to attack second.
					for (Mob mob : Dungeon.level.mobs)
						if (mob.alignment == Alignment.ALLY && mob != this
								&& fieldOfView[mob.pos] && mob.invisible <= 0) {
							enemies.add(mob);
						}
					
					if (enemies.isEmpty()) {
						//try to find the hero third
						if (fieldOfView[Dungeon.hero.pos] && Dungeon.hero.invisible <= 0) {
							enemies.add(Dungeon.hero);
						}
					}
				}
				
			//if the mob is an ally...
			} else if ( alignment == Alignment.ALLY ) {
				//look for hostile mobs to attack
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY && fieldOfView[mob.pos]
							&& mob.invisible <= 0 && !mob.isInvulnerable(getClass()))
						//intelligent allies do not target mobs which are passive, wandering, or asleep
						if (!intelligentAlly ||
								(mob.state != mob.SLEEPING && mob.state != mob.PASSIVE && mob.state != mob.WANDERING)) {
							enemies.add(mob);
						}
				
			//if the mob is an enemy...
			} else if (alignment == Alignment.ENEMY) {
				//look for ally mobs to attack
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ALLY && fieldOfView[mob.pos] && mob.invisible <= 0 && !mob.isInvulnerable(getClass()))
						enemies.add(mob);

				//and look for the hero
				if (fieldOfView[Dungeon.hero.pos] && Dungeon.hero.invisible <= 0 && !Dungeon.hero.isInvulnerable(getClass())) {
					enemies.add(Dungeon.hero);
				}
				
			}
			
			Charm charm = buff( Charm.class );
			if (charm != null){
				Char source = (Char)Actor.findById( charm.object );
				if (source != null && enemies.contains(source) && enemies.size() > 1){
					enemies.remove(source);
				}
			}

			//neutral characters in particular do not choose enemies.
			if (enemies.isEmpty()){
				return null;
			} else {
				//go after the closest potential enemy, preferring the hero if two are equidistant
				Char closest = null;
				for (Char curr : enemies){
					if (closest == null
							|| Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)
							|| Dungeon.level.distance(pos, curr.pos) == Dungeon.level.distance(pos, closest.pos) && curr == Dungeon.hero){
						closest = curr;
					}
				}
				return closest;
			}

		} else
			return enemy;
	}
	
	@Override
	public void add( Buff buff ) {
		super.add( buff );
		if (buff instanceof Amok || buff instanceof Corruption) {
			state = HUNTING;
		} else if (buff instanceof Terror) {
			state = FLEEING;
		} else if (buff instanceof Sleep) {
			state = SLEEPING;
			postpone( Sleep.SWS );
		}
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove( buff );
		if (buff instanceof Terror) {
			if (enemySeen) {
				sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "rage"));
				state = HUNTING;
			} else {
				state = WANDERING;
			}
		}
	}
	
	protected boolean canAttack( Char enemy ) {
		if (Dungeon.level.adjacent( pos, enemy.pos )){
			return true;
		}
		for (ChampionEnemy buff : buffs(ChampionEnemy.class)){
			if (buff.canAttackWithExtraReach( enemy )){
				return true;
			}
		}
		return false;
	}
	
	protected boolean getCloser( int target ) {
		
		if (rooted || target == pos) {
			return false;
		}

		int step = -1;

		if (Dungeon.level.adjacent( pos, target )) {

			path = null;

			if (Actor.findChar( target ) == null
					&& (Dungeon.level.passable[target] || (flying && Dungeon.level.avoid[target]))
					&& (!Char.hasProp(this, Char.Property.LARGE) || Dungeon.level.openSpace[target])) {
				step = target;
			}

		} else {

			boolean newPath = false;
			//scrap the current path if it's empty, no longer connects to the current location
			//or if it's extremely inefficient and checking again may result in a much better path
			if (path == null || path.isEmpty()
					|| !Dungeon.level.adjacent(pos, path.getFirst())
					|| path.size() > 2*Dungeon.level.distance(pos, target))
				newPath = true;
			else if (path.getLast() != target) {
				//if the new target is adjacent to the end of the path, adjust for that
				//rather than scrapping the whole path.
				if (Dungeon.level.adjacent(target, path.getLast())) {
					int last = path.removeLast();

					if (path.isEmpty()) {

						//shorten for a closer one
						if (Dungeon.level.adjacent(target, pos)) {
							path.add(target);
						//extend the path for a further target
						} else {
							path.add(last);
							path.add(target);
						}

					} else {
						//if the new target is simply 1 earlier in the path shorten the path
						if (path.getLast() == target) {

						//if the new target is closer/same, need to modify end of path
						} else if (Dungeon.level.adjacent(target, path.getLast())) {
							path.add(target);

						//if the new target is further away, need to extend the path
						} else {
							path.add(last);
							path.add(target);
						}
					}

				} else {
					newPath = true;
				}

			}

			//checks if the next cell along the current path can be stepped into
			if (!newPath) {
				int nextCell = path.removeFirst();
				if (!Dungeon.level.passable[nextCell]
						|| (!flying && Dungeon.level.avoid[nextCell])
						|| (Char.hasProp(this, Char.Property.LARGE) && !Dungeon.level.openSpace[nextCell])
						|| Actor.findChar(nextCell) != null) {

					newPath = true;
					//If the next cell on the path can't be moved into, see if there is another cell that could replace it
					if (!path.isEmpty()) {
						for (int i : PathFinder.NEIGHBOURS8) {
							if (Dungeon.level.adjacent(pos, nextCell + i) && Dungeon.level.adjacent(nextCell + i, path.getFirst())) {
								if (Dungeon.level.passable[nextCell+i]
										&& (flying || !Dungeon.level.avoid[nextCell+i])
										&& (!Char.hasProp(this, Char.Property.LARGE) || Dungeon.level.openSpace[nextCell+i])
										&& Actor.findChar(nextCell+i) == null){
									path.addFirst(nextCell+i);
									newPath = false;
									break;
								}
							}
						}
					}
				} else {
					path.addFirst(nextCell);
				}
			}

			//generate a new path
			if (newPath) {
				//If we aren't hunting, always take a full path
				PathFinder.Path full = Dungeon.findPath(this, target, Dungeon.level.passable, fieldOfView, true);
				if (state != HUNTING){
					path = full;
				} else {
					//otherwise, check if other characters are forcing us to take a very slow route
					// and don't try to go around them yet in response, basically assume their blockage is temporary
					PathFinder.Path ignoreChars = Dungeon.findPath(this, target, Dungeon.level.passable, fieldOfView, false);
					if (ignoreChars != null && (full == null || full.size() > 2*ignoreChars.size())){
						//check if first cell of shorter path is valid. If it is, use new shorter path. Otherwise do nothing and wait.
						path = ignoreChars;
						if (!Dungeon.level.passable[ignoreChars.getFirst()]
								|| (!flying && Dungeon.level.avoid[ignoreChars.getFirst()])
								|| (Char.hasProp(this, Char.Property.LARGE) && !Dungeon.level.openSpace[ignoreChars.getFirst()])
								|| Actor.findChar(ignoreChars.getFirst()) != null) {
							return false;
						}
					} else {
						path = full;
					}
				}
			}

			if (path != null) {
				step = path.removeFirst();
			} else {
				return false;
			}
		}
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean getFurther( int target ) {
		if (rooted || target == pos) {
			return false;
		}
		
		int step = Dungeon.flee( this, target, Dungeon.level.passable, fieldOfView, true );
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updateSpriteState() {
		super.updateSpriteState();
		if (Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) != null
				|| Dungeon.hero.buff(Swiftthistle.TimeBubble.class) != null)
			sprite.add( CharSprite.State.PARALYSED );
	}
	
	protected float attackDelay() {
		float delay = 1f;
		if ( buff(Adrenaline.class) != null) delay /= 1.5f;
		return delay;
	}
	
	protected boolean doAttack( Char enemy ) {
		
		if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
			sprite.attack( enemy.pos );
			spend( attackDelay() );
			return false;
			
		} else {
			attack( enemy );
			spend( attackDelay() );
			return true;
		}
	}
	
	@Override
	public void onAttackComplete() {
		attack( enemy );
		super.onAttackComplete();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		if ( !surprisedBy(enemy)
				&& paralysed == 0
				&& !(alignment == Alignment.ALLY && enemy == Dungeon.hero)) {
			return this.defenseSkill;
		} else {
			return 0;
		}
	}
	
	protected boolean hitWithRanged = false;
	
	@Override
	public int defenseProc( Char enemy, int damage ) {
		
		if (enemy instanceof Hero
				&& ((Hero) enemy).belongings.weapon instanceof MissileWeapon
				&& !hitWithRanged){
			hitWithRanged = true;
			Statistics.thrownAssists++;
			Badges.validateHuntressUnlock();
		}
		
		if (surprisedBy(enemy)) {
			Statistics.sneakAttacks++;
			Badges.validateRogueUnlock();
			//TODO this is somewhat messy, it would be nicer to not have to manually handle delays here
			// playing the strong hit sound might work best as another property of weapon?
			if (Dungeon.hero.belongings.weapon instanceof SpiritBow.SpiritArrow
				|| Dungeon.hero.belongings.weapon instanceof Dart){
				Sample.INSTANCE.playDelayed(Assets.Sounds.HIT_STRONG, 0.125f);
			} else {
				Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
			}
			if (enemy.buff(Preparation.class) != null) {
				Wound.hit(this);
			} else {
				Surprise.hit(this);
			}
		}

		//if attacked by something else than current target, and that thing is closer, switch targets
		if (this.enemy == null
				|| (enemy != this.enemy && (Dungeon.level.distance(pos, enemy.pos) < Dungeon.level.distance(pos, this.enemy.pos)))) {
			aggro(enemy);
			target = enemy.pos;
		}

		if (buff(SoulMark.class) != null) {
			int restoration = Math.min(damage, HP);
			
			//physical damage that doesn't come from the hero is less effective
			if (enemy != Dungeon.hero){
				restoration = Math.round(restoration * 0.15f*Dungeon.hero.pointsInTalent(Talent.SOUL_SIPHON));
			}
			if (restoration > 0) {
				Buff.affect(Dungeon.hero, Hunger.class).affectHunger(restoration*Dungeon.hero.pointsInTalent(Talent.SOUL_EATER)/3f);
				Dungeon.hero.HP = (int) Math.ceil(Math.min(Dungeon.hero.HT, Dungeon.hero.HP + (restoration * 0.4f)));
				Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}
		}

		return damage;
	}

	public boolean surprisedBy( Char enemy ){
		return enemy == Dungeon.hero
				&& (enemy.invisible > 0 || !enemySeen)
				&& ((Hero)enemy).canSurpriseAttack();
	}

	public void aggro( Char ch ) {
		enemy = ch;
		if (state != PASSIVE){
			state = HUNTING;
		}
	}
	
	public boolean isTargeting( Char ch){
		return enemy == ch;
	}

	@Override
	public void damage( int dmg, Object src ) {

		if (state == SLEEPING) {
			state = WANDERING;
		}
		if (state != HUNTING && !(src instanceof Corruption)) {
			alerted = true;
		}
		
		super.damage( dmg, src );
	}
	
	
	@Override
	public void destroy() {
		
		super.destroy();
		
		Dungeon.level.mobs.remove( this );
		
		if (Dungeon.hero.isAlive()) {
			
			if (alignment == Alignment.ENEMY) {
				Statistics.enemiesSlain++;
				Badges.validateMonstersSlain();
				Statistics.qualifiedForNoKilling = false;
				
				int exp = Dungeon.hero.lvl <= maxLvl ? EXP : 0;
				if (exp > 0) {
					Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "exp", exp));
				}
				Dungeon.hero.earnExp(exp, getClass());
			}
		}
	}
	
	@Override
	public void die( Object cause ) {

		if (cause == Chasm.class){
			//50% chance to round up, 50% to round down
			if (EXP % 2 == 1) EXP += Random.Int(2);
			EXP /= 2;
		}

		if (alignment == Alignment.ENEMY){
			rollToDropLoot();

			if (cause == Dungeon.hero
					&& Dungeon.hero.hasTalent(Talent.LETHAL_MOMENTUM)
					&& Random.Float() < 0.34f + 0.33f* Dungeon.hero.pointsInTalent(Talent.LETHAL_MOMENTUM)){
				Buff.affect(Dungeon.hero, Talent.LethalMomentumTracker.class, 1f);
			}
		}

		if (Dungeon.hero.isAlive() && !Dungeon.level.heroFOV[pos]) {
			GLog.i( Messages.get(this, "died") );
		}

		boolean soulMarked = buff(SoulMark.class) != null;

		super.die( cause );

		if (!(this instanceof Wraith)
				&& soulMarked
				&& Random.Int(10) < Dungeon.hero.pointsInTalent(Talent.NECROMANCERS_MINIONS)){
			Wraith w = Wraith.spawnAt(pos);
			if (w != null) {
				Buff.affect(w, Corruption.class);
				if (Dungeon.level.heroFOV[pos]) {
					CellEmitter.get(pos).burst(ShadowParticle.CURSE, 6);
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
				}
			}
		}
	}
	
	public void rollToDropLoot(){
		if (Dungeon.hero.lvl > maxLvl + 2) return;
		
		float lootChance = this.lootChance;
		lootChance *= RingOfWealth.dropChanceMultiplier( Dungeon.hero );
		
		if (Random.Float() < lootChance) {
			Item loot = createLoot();
			if (loot != null) {
				Dungeon.level.drop(loot, pos).sprite.drop();
			}
		}
		
		//ring of wealth logic
		if (Ring.getBuffedBonus(Dungeon.hero, RingOfWealth.Wealth.class) > 0) {
			int rolls = 1;
			if (properties.contains(Property.BOSS)) rolls = 15;
			else if (properties.contains(Property.MINIBOSS)) rolls = 5;
			ArrayList<Item> bonus = RingOfWealth.tryForBonusDrop(Dungeon.hero, rolls);
			if (bonus != null && !bonus.isEmpty()) {
				for (Item b : bonus) Dungeon.level.drop(b, pos).sprite.drop();
				RingOfWealth.showFlareForBonusDrop(sprite);
			}
		}
		
		//lucky enchant logic
		if (Dungeon.hero.lvl <= maxLvl && buff(Lucky.LuckProc.class) != null){
			Dungeon.level.drop(Lucky.genLoot(), pos).sprite.drop();
			Lucky.showFlare(sprite);
		}

		//soul eater talent
		if (buff(SoulMark.class) != null &&
				Random.Int(10) < Dungeon.hero.pointsInTalent(Talent.SOUL_EATER)){
			Talent.onFoodEaten(Dungeon.hero, 0, null);
		}

		//bounty hunter talent
		if (Dungeon.hero.buff(Talent.BountyHunterTracker.class) != null){
			Dungeon.level.drop(new Gold(10 * Dungeon.hero.pointsInTalent(Talent.BOUNTY_HUNTER)), pos).sprite.drop();
		}

	}
	
	protected Object loot = null;
	protected float lootChance = 0;
	
	@SuppressWarnings("unchecked")
	protected Item createLoot() {
		Item item;
		if (loot instanceof Generator.Category) {

			item = Generator.random( (Generator.Category)loot );

		} else if (loot instanceof Class<?>) {

			item = Generator.random( (Class<? extends Item>)loot );

		} else {

			item = (Item)loot;

		}
		return item;
	}

	//how many mobs this one should count as when determining spawning totals
	public float spawningWeight(){
		return 1;
	}
	
	public boolean reset() {
		return false;
	}
	
	public void beckon( int cell ) {
		
		notice();
		
		if (state != HUNTING && state != FLEEING) {
			state = WANDERING;
		}
		target = cell;
	}
	
	public String description() {
		return Messages.get(this, "desc");
	}

	public String info(){
		String desc = description();

		for (Buff b : buffs(ChampionEnemy.class)){
			desc += "\n\n_" + Messages.titleCase(b.toString()) + "_\n" + b.desc();
		}

		return desc;
	}
	
	public void notice() {
		sprite.showAlert();
	}
	
	public void yell( String str ) {
		GLog.newLine();
		GLog.n( "%s: \"%s\" ", Messages.titleCase(name()), str );
	}

	//returns true when a mob sees the hero, and is currently targeting them.
	public boolean focusingHero() {
		return enemySeen && (target == Dungeon.hero.pos);
	}

	public interface AiState {
		boolean act( boolean enemyInFOV, boolean justAlerted );
	}

	protected class Sleeping implements AiState {

		public static final String TAG	= "SLEEPING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

			//debuffs cause mobs to wake as well
			for (Buff b : buffs()){
				if (b.type == Buff.buffType.NEGATIVE){
					awaken(enemyInFOV);
					return true;
				}
			}

			if (enemyInFOV) {

				float enemyStealth = enemy.stealth();

				if (enemy instanceof Hero && ((Hero) enemy).hasTalent(Talent.SILENT_STEPS)){
					if (Dungeon.level.distance(pos, enemy.pos) >= 4 - ((Hero) enemy).pointsInTalent(Talent.SILENT_STEPS)) {
						enemyStealth = Float.POSITIVE_INFINITY;
					}
				}

				if (Random.Float( distance( enemy ) + enemyStealth ) < 1) {
					awaken(enemyInFOV);
					return true;
				}

			}

			enemySeen = false;
			spend( TICK );

			return true;
		}

		protected void awaken( boolean enemyInFOV ){
			if (enemyInFOV) {
				enemySeen = true;
				notice();
				state = HUNTING;
				target = enemy.pos;
			} else {
				notice();
				state = WANDERING;
				target = Dungeon.level.randomDestination( Mob.this );
			}

			if (alignment == Alignment.ENEMY && Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE)) {
				for (Mob mob : Dungeon.level.mobs) {
					if (mob.paralysed <= 0
							&& Dungeon.level.distance(pos, mob.pos) <= 8
							&& mob.state != mob.HUNTING) {
						mob.beckon(target);
					}
				}
			}
			spend(TIME_TO_WAKE_UP);
		}
	}

	protected class Wandering implements AiState {

		public static final String TAG	= "WANDERING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && (justAlerted || Random.Float( distance( enemy ) / 2f + enemy.stealth() ) < 1)) {

				return noticeEnemy();

			} else {

				return continueWandering();

			}
		}
		
		protected boolean noticeEnemy(){
			enemySeen = true;
			
			notice();
			alerted = true;
			state = HUNTING;
			target = enemy.pos;
			
			if (alignment == Alignment.ENEMY && Dungeon.isChallenged( Challenges.SWARM_INTELLIGENCE )) {
				for (Mob mob : Dungeon.level.mobs) {
					if (mob.paralysed <= 0
							&& Dungeon.level.distance(pos, mob.pos) <= 8
							&& mob.state != mob.HUNTING) {
						mob.beckon( target );
					}
				}
			}
			
			return true;
		}
		
		protected boolean continueWandering(){
			enemySeen = false;
			
			int oldPos = pos;
			if (target != -1 && getCloser( target )) {
				spend( 1 / speed() );
				return moveSprite( oldPos, pos );
			} else {
				target = Dungeon.level.randomDestination( Mob.this );
				spend( TICK );
			}
			
			return true;
		}
		
	}

	protected class Hunting implements AiState {

		public static final String TAG	= "HUNTING";

		//prevents rare infinite loop cases
		private boolean recursing = false;

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				target = enemy.pos;
				return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				} else if (enemy == null) {
					sprite.showLost();
					state = WANDERING;
					target = Dungeon.level.randomDestination( Mob.this );
					spend( TICK );
					return true;
				}
				
				int oldPos = pos;
				if (target != -1 && getCloser( target )) {
					
					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else {

					//if moving towards an enemy isn't possible, try to switch targets to another enemy that is closer
					//unless we have already done that and still can't move toward them, then move on.
					if (!recursing) {
						Char oldEnemy = enemy;
						enemy = null;
						enemy = chooseEnemy();
						if (enemy != null && enemy != oldEnemy) {
							recursing = true;
							boolean result = act(enemyInFOV, justAlerted);
							recursing = false;
							return result;
						}
					}

					spend( TICK );
					if (!enemyInFOV) {
						sprite.showLost();
						state = WANDERING;
						target = Dungeon.level.randomDestination( Mob.this );
					}
					return true;
				}
			}
		}
	}

	//FIXME this works fairly well but is coded poorly. Should refactor
	protected class Fleeing implements AiState {

		public static final String TAG	= "FLEEING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			//loses target when 0-dist rolls a 6 or greater.
			if (enemy == null || !enemyInFOV && 1 + Random.Int(Dungeon.level.distance(pos, target)) >= 6){
				target = -1;
			
			//if enemy isn't in FOV, keep running from their previous position.
			} else if (enemyInFOV) {
				target = enemy.pos;
			}

			int oldPos = pos;
			if (target != -1 && getFurther( target )) {

				spend( 1 / speed() );
				return moveSprite( oldPos, pos );

			} else {

				spend( TICK );
				nowhereToRun();

				return true;
			}
		}

		protected void nowhereToRun() {
		}
	}

	protected class Passive implements AiState {

		public static final String TAG	= "PASSIVE";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			spend( TICK );
			return true;
		}
	}
	
	
	private static ArrayList<Mob> heldAllies = new ArrayList<>();
	
	public static void holdAllies( Level level ){
		heldAllies.clear();
		for (Mob mob : level.mobs.toArray( new Mob[0] )) {
			//preserve the ghost no matter where they are
			if (mob instanceof DriedRose.GhostHero) {
				((DriedRose.GhostHero) mob).clearDefensingPos();
				level.mobs.remove( mob );
				heldAllies.add(mob);
				
			//preserve intelligent allies if they are near the hero
			} else if (mob.alignment == Alignment.ALLY
					&& mob.intelligentAlly
					&& Dungeon.level.distance(Dungeon.hero.pos, mob.pos) <= 3){
				level.mobs.remove( mob );
				heldAllies.add(mob);
			}
		}
	}
	
	public static void restoreAllies( Level level, int pos ){
		if (!heldAllies.isEmpty()){
			
			ArrayList<Integer> candidatePositions = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8) {
				if (!Dungeon.level.solid[i+pos] && level.findMob(i+pos) == null){
					candidatePositions.add(i+pos);
				}
			}
			Collections.shuffle(candidatePositions);
			
			for (Mob ally : heldAllies) {
				level.mobs.add(ally);
				ally.state = ally.WANDERING;
				
				if (!candidatePositions.isEmpty()){
					ally.pos = candidatePositions.remove(0);
				} else {
					ally.pos = pos;
				}
				
			}
		}
		heldAllies.clear();
	}
	
	public static void clearHeldAllies(){
		heldAllies.clear();
	}
}

