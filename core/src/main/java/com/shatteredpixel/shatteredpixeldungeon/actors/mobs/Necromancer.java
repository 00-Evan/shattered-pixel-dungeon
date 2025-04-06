/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import static java.lang.Math.max;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Necromancer extends Mob {
	
	{
		spriteClass = NecromancerSprite.class;
		
		HP = HT = 40;
		defenseSkill = 14;
		
		EXP = 7;
		maxLvl = 14;
		
		loot = PotionOfHealing.class;
		lootChance = 0.2f; //see lootChance()
		
		properties.add(Property.UNDEAD);
		
		HUNTING = new Hunting();

		//make sure Necromancer moves after its summon to not get cheesed
		actPriority = MOB_PRIO - 1;
	}
	
	public boolean summoning = false;
	public int summoningPos = -1;

	private class Polished {
		private static final String SUMMON_COOLDOWN = "summon_cooldown";
		private static final String ZAP_COOLDOWN = "zap_cooldown";
		private static final String TP_COOLDOWN = "zap_cooldown";

		public int summonCooldown = -1;
		public int zapCooldown = -1;
		public int tpCooldown = -1;


		public boolean alt_pos = false;
	}
	Polished polished = new Polished();

	protected boolean firstSummon = true;
	
	private NecroSkeleton mySkeleton;
	private int storedSkeletonID = -1;

	@Override
	protected boolean act() {
		if (summoning && state != HUNTING){
			summoning = false;
			if (sprite instanceof NecromancerSprite) ((NecromancerSprite) sprite).cancelSummoning();
		}

		if( state == WANDERING && paralysed <= 0 &&
			mySkeleton != null && mySkeleton.enemySeen && mySkeleton.target != -1 )
		{
			state = HUNTING;
			enemy = mySkeleton.enemy;
			target = mySkeleton.target;
			alerted=true;
		}

		boolean next = super.act();
		if(polished.summonCooldown > 0 && mySkeleton == null) polished.summonCooldown--;
		if(polished.zapCooldown > 0) polished.zapCooldown--;
		if(polished.tpCooldown > 0) polished.tpCooldown--;
		return next;
	}

	@Override
	public void aggro(Char ch) {
		super.aggro(ch);
		if (mySkeleton != null && mySkeleton.isAlive()
				&& Dungeon.level.mobs.contains(mySkeleton)
				&& mySkeleton.alignment == alignment){
			mySkeleton.aggro(ch);
		}
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 5);
	}
	
	@Override
	public float lootChance() {
		return super.lootChance() * ((6f - Dungeon.LimitedDrops.NECRO_HP.count) / 6f);
	}
	
	@Override
	public Item createLoot(){
		Dungeon.LimitedDrops.NECRO_HP.count++;
		return super.createLoot();
	}
	
	@Override
	public void die(Object cause) {
		if (storedSkeletonID != -1){
			Actor ch = Actor.findById(storedSkeletonID);
			storedSkeletonID = -1;
			if (ch instanceof NecroSkeleton){
				mySkeleton = (NecroSkeleton) ch;
			}
		}
		
		if (mySkeleton != null && mySkeleton.isAlive() && mySkeleton.alignment == alignment){
			mySkeleton.die(null);
		}
		
		super.die(cause);
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return false;
	}

	private static final String SUMMONING = "summoning";
	private static final String FIRST_SUMMON = "first_summon";
	private static final String SUMMONING_POS = "summoning_pos";
	private static final String MY_SKELETON = "my_skeleton";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( SUMMONING, summoning );
		bundle.put( FIRST_SUMMON, firstSummon );
		if (summoning){
			bundle.put( SUMMONING_POS, summoningPos);
		}
		if (mySkeleton != null){
			bundle.put( MY_SKELETON, mySkeleton.id() );
		} else if (storedSkeletonID != -1){
			bundle.put( MY_SKELETON, storedSkeletonID );
		}

		if (polished.summonCooldown != -1) {
			bundle.put(Polished.SUMMON_COOLDOWN, polished.summonCooldown);
		}
		if (polished.zapCooldown != -1) {
			bundle.put(Polished.ZAP_COOLDOWN, polished.zapCooldown);
		}
		if (polished.tpCooldown != -1) {
			bundle.put(Polished.TP_COOLDOWN, polished.tpCooldown);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		summoning = bundle.getBoolean( SUMMONING );
		if (bundle.contains(FIRST_SUMMON)) firstSummon = bundle.getBoolean(FIRST_SUMMON);
		if (summoning){
			summoningPos = bundle.getInt( SUMMONING_POS );
		}
		if (bundle.contains( MY_SKELETON )){
			storedSkeletonID = bundle.getInt( MY_SKELETON );
		}

		if (bundle.contains( Polished.SUMMON_COOLDOWN )){
			polished.summonCooldown = bundle.getInt( Polished.SUMMON_COOLDOWN );
		}
		if (bundle.contains( Polished.ZAP_COOLDOWN )){
			polished.zapCooldown = bundle.getInt( Polished.ZAP_COOLDOWN );
		}
		if (bundle.contains( Polished.TP_COOLDOWN )){
			polished.tpCooldown = bundle.getInt( Polished.TP_COOLDOWN );
		}
	}
	
	public void onZapComplete(){
		if (mySkeleton == null || mySkeleton.sprite == null || !mySkeleton.isAlive()){
			return;
		}


		if (sprite.visible || mySkeleton.sprite.visible) {
			sprite.parent.add(new Beam.HealthRay(sprite.center(), mySkeleton.sprite.center()));
		}

		mySkeleton.HP = Math.min(mySkeleton.HP + mySkeleton.HT/5, mySkeleton.HT);
		if (mySkeleton.sprite.visible) {
			mySkeleton.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString( mySkeleton.HT/5 ), FloatingText.HEALING );
		}

		if (mySkeleton.buff(Adrenaline.class) != null) {
			Buff.prolong(mySkeleton, Adrenaline.class, 5f);
		}
		else if (mySkeleton.buff(Speed.class) != null) {
			Buff.detach(mySkeleton, Speed.class);
			Buff.affect(mySkeleton, Adrenaline.class, 3f);
		}
		else {
			Buff.affect(mySkeleton, Speed.class, 3f);
		}
		next();
	}

	public void summonMinion(){
		if (Actor.findChar(summoningPos) != null) {

			//cancel if character cannot be moved, except if there's no other summon positions
			if (Char.hasProp(Actor.findChar(summoningPos), Property.IMMOVABLE) && polished.alt_pos){
				summoning = false;
				((NecromancerSprite)sprite).finishSummoning();
				spend(-TICK);
				return;
			}

			int pushPos = pos;
			for (int c : PathFinder.NEIGHBOURS8) {
				if (Actor.findChar(summoningPos + c) == null
						&& Dungeon.level.passable[summoningPos + c]
						&& (Dungeon.level.openSpace[summoningPos + c] || !hasProp(Actor.findChar(summoningPos), Property.LARGE))
						&& Dungeon.level.trueDistance(pos, summoningPos + c) > Dungeon.level.trueDistance(pos, pushPos)) {
					pushPos = summoningPos + c;
				}
			}

			//no push if char is immovable
			if (Char.hasProp(Actor.findChar(summoningPos), Property.IMMOVABLE)){
				pushPos = pos;
			}

			//push enemy, or wait a turn if there is no valid pushing position
			if (pushPos != pos) {
				Char ch = Actor.findChar(summoningPos);
				Actor.add( new Pushing( ch, ch.pos, pushPos ) );

				ch.pos = pushPos;
				Dungeon.level.occupyCell(ch );

			} else {

				Char blocker = Actor.findChar(summoningPos);
				if (blocker.alignment != alignment){
					blocker.damage( Random.NormalIntRange(2, 10), new SummoningBlockDamage() );
					if (blocker == Dungeon.hero && !blocker.isAlive()){
						Badges.validateDeathFromEnemyMagic();
						Dungeon.fail(this);
						GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
					}
				}
				return;
			}
		}

		summoning = firstSummon = false;
		polished.summonCooldown = 2;

		mySkeleton = new NecroSkeleton();
		mySkeleton.pos = summoningPos;
		GameScene.add( mySkeleton );
		Dungeon.level.occupyCell( mySkeleton );
		((NecromancerSprite)sprite).finishSummoning();

		if(enemy != null) {
			mySkeleton.aggro(enemy);
			mySkeleton.target = target;
		}

		for (Buff b : buffs()){
			if (b.revivePersists) {
				Buff.affect(mySkeleton, b.getClass());
			}
		}
	}

	public static class SummoningBlockDamage{}
	
	private class Hunting extends Mob.Hunting{
		
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;

			if (enemySeen){
				target = enemy.pos;
			}
			
			if (storedSkeletonID != -1){
				Actor ch = Actor.findById(storedSkeletonID);
				storedSkeletonID = -1;
				if (ch instanceof NecroSkeleton){
					mySkeleton = (NecroSkeleton) ch;
				}
			}
			
			if (summoning){
				summonMinion();
				spend(TICK);
				return true;
			}
			
			if (mySkeleton != null &&
					(!mySkeleton.isAlive()
					|| !Dungeon.level.mobs.contains(mySkeleton)
					|| mySkeleton.alignment != alignment)){
				mySkeleton = null;
			}
			
			//if enemy is seen, and enemy is within range, and we have no skeleton, summon a skeleton!
			if (enemySeen && Dungeon.level.distance(pos, enemy.pos) <= 4 && mySkeleton == null && polished.summonCooldown <= 0){
				
				summoningPos = -1;
				polished.alt_pos = false;

				//we can summon around blocking terrain, but not through it, except unlocked doors
				boolean[] passable = BArray.not(Dungeon.level.solid, null);
				BArray.or(Dungeon.level.passable, passable, passable);
				PathFinder.buildDistanceMap(pos, passable, Dungeon.level.distance(pos, enemy.pos)+3);

				for (int c : PathFinder.NEIGHBOURS8){
					if (
							Actor.findChar(enemy.pos+c) == null
							&& PathFinder.distance[enemy.pos+c] != Integer.MAX_VALUE
							&& Dungeon.level.passable[enemy.pos+c]
							&& (!hasProp(Necromancer.this, Property.LARGE) || Dungeon.level.openSpace[enemy.pos+c])
							&& fieldOfView[enemy.pos+c]
							&& Dungeon.level.trueDistance(pos, enemy.pos+c) < Dungeon.level.trueDistance(pos, summoningPos))
					{
						polished.alt_pos = summoningPos != -1;
						summoningPos = enemy.pos+c;
					}
				}
				
				if (summoningPos != -1){
					
					summoning = true;
					sprite.zap( summoningPos );

					if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[summoningPos]){
						Dungeon.hero.interrupt();
					}

					//leave this in case we change it later
					spend( firstSummon ? TICK : TICK );
				} else {
					//wait for a turn
					spend(TICK);
				}
				
				return true;
			//otherwise, if enemy is seen, and we have a skeleton...
			} else if (enemySeen && mySkeleton != null){
				
				spend(TICK);
				
				if (!fieldOfView[mySkeleton.pos] && alignment == mySkeleton.alignment) {
					
					//if the skeleton is not next to the enemy
					//teleport them to the closest spot next to the enemy that can be seen
					if (!Dungeon.level.adjacent(mySkeleton.pos, enemy.pos)){
						int telePos = -1;
						for (int c : PathFinder.NEIGHBOURS8){
							if (Actor.findChar(enemy.pos+c) == null
									&& Dungeon.level.passable[enemy.pos+c]
									&& fieldOfView[enemy.pos+c]
									&& (Dungeon.level.openSpace[enemy.pos+c] || !Char.hasProp(mySkeleton, Property.LARGE))
									&& Dungeon.level.trueDistance(pos, enemy.pos+c) < Dungeon.level.trueDistance(pos, telePos)){
								telePos = enemy.pos+c;
							}
						}
						
						if (telePos != -1 && polished.tpCooldown <= 0) {
							ScrollOfTeleportation.appear(mySkeleton, telePos);
							mySkeleton.teleportSpend(enemy);
							//its actually 2 turns
							polished.tpCooldown = 3;
						}
					}
					
					return true;
					
				} else if(polished.zapCooldown <= 0 && fieldOfView[mySkeleton.pos]) {
					
					//zap skeleton
					//its actually 1 turn
					polished.zapCooldown = 2;
					if (sprite != null && sprite.visible){
						sprite.zap(mySkeleton.pos);
						return false;
					} else {
						onZapComplete();
					}

					return true;

				} else {
					//do nothing
					return true;
				}

			} else {
				if(!enemySeen || Dungeon.level.distance(pos, enemy.pos) > 4) return super.act(enemyInFOV, justAlerted);
				else {
					spend(TICK);
					return true;
				}
			}
		}
	}
	
	public static class NecroSkeleton extends Skeleton {
		
		{
			state = WANDERING;
			
			spriteClass = NecroSkeletonSprite.class;
			
			//no loot or exp
			maxLvl = -5;
			
			//20/25 health to start
			//HP = 20;
		}

		@Override
		public float spawningWeight() {
			return 0;
		}

		private void teleportSpend(Char enemy){
			spend(GameMath.gate(0f,enemy.cooldown() - this.cooldown(), 1f));
		}
		
		public static class NecroSkeletonSprite extends SkeletonSprite{
			
			public NecroSkeletonSprite(){
				super();
				brightness(0.75f);
			}
			
			@Override
			public void resetColor() {
				super.resetColor();
				brightness(0.75f);
			}
		}
		
	}
}
