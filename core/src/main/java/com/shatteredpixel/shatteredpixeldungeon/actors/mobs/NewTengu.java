/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.TomeOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewPrisonBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//TODO currently has attack/defence stats for testing, need to add those
public class NewTengu extends Mob {
	
	{
		spriteClass = TenguSprite.class;
		
		HP = HT = 160;
		EXP = 20;
		defenseSkill = 0;
		
		HUNTING = new Hunting();
		
		flying = true; //doesn't literally fly, but he is fleet-of-foot enough to avoid hazards
		
		properties.add(Property.BOSS);
	}
	
	@Override
	protected void onAdd() {
		//when he's removed and re-added to the fight, his time is always set to now.
		spend(-cooldown());
		super.onAdd();
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 0, 0 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 20;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 0);
	}
	
	@Override
	public void damage(int dmg, Object src) {
		NewPrisonBossLevel.State state = ((NewPrisonBossLevel)Dungeon.level).state();
		
		int hpBracket;
		if (state == NewPrisonBossLevel.State.FIGHT_START){
			hpBracket = 20;
		} else {
			hpBracket = 20;
		}
		
		int beforeHitHP = HP;
		super.damage(dmg, src);
		dmg = beforeHitHP - HP;
		
		//tengu cannot be hit through multiple brackets at a time
		if ((beforeHitHP/hpBracket - HP/hpBracket) >= 2){
			HP = hpBracket * ((beforeHitHP/hpBracket)-1);
		}
		
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) {
			int multiple = state == NewPrisonBossLevel.State.FIGHT_START ? 1 : 4;
			lock.addTime(dmg*multiple);
		}
		
		//phase 2 of the fight is over
		if (HP == 0 && state == NewPrisonBossLevel.State.FIGHT_ARENA) {
			//let full attack action complete first
			Actor.add(new Actor() {
				
				{
					actPriority = VFX_PRIO;
				}
				
				@Override
				protected boolean act() {
					Actor.remove(this);
					((NewPrisonBossLevel)Dungeon.level).progress();
					return true;
				}
			});
			return;
		}
		
		//phase 1 of the fight is over
		if (state == NewPrisonBossLevel.State.FIGHT_START && HP <= HT/2){
			HP = (HT/2)-1;
			yell(Messages.get(this, "interesting"));
			((NewPrisonBossLevel)Dungeon.level).progress();
			BossHealthBar.bleed(true);
			
			//if tengu has lost a certain amount of hp, jump
		} else if (beforeHitHP / hpBracket != HP / hpBracket) {
			jump();
		}
	}
	
	@Override
	public boolean isAlive() {
		return HP > 0 || Dungeon.level.mobs.contains(this); //Tengu has special death rules, see prisonbosslevel.progress()
	}
	
	@Override
	public void die( Object cause ) {
		
		if (Dungeon.hero.subClass == HeroSubClass.NONE) {
			Dungeon.level.drop( new TomeOfMastery(), pos ).sprite.drop();
		}
		
		GameScene.bossSlain();
		super.die( cause );
		
		Badges.validateBossSlain();
		
		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
		}
		
		yell( Messages.get(this, "defeated") );
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}
	
	//tengu's attack is always visible
	@Override
	protected boolean doAttack(Char enemy) {
		sprite.attack( enemy.pos );
		spend( attackDelay() );
		return false;
	}
	
	private void jump() {
		
		//in case tengu hasn't had a chance to act yet
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView( this, fieldOfView );
		}
		
		if (enemy == null) enemy = chooseEnemy();
		if (enemy == null) return;
		
		int newPos;
		if (Dungeon.level instanceof NewPrisonBossLevel){
			NewPrisonBossLevel level = (NewPrisonBossLevel) Dungeon.level;
			
			//if we're in phase 1, want to warp around within the room
			if (level.state() == NewPrisonBossLevel.State.FIGHT_START) {
				
				level.cleanTenguCell();
				
				do {
					newPos = ((NewPrisonBossLevel)Dungeon.level).randomTenguCellPos();
				} while ( (level.distance(newPos, enemy.pos) <= 2 || Actor.findChar(newPos) != null));
				
				if (level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
				
				sprite.move( pos, newPos );
				move( newPos );
				
				if (level.heroFOV[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
				Sample.INSTANCE.play( Assets.SND_PUFF );
				
				spend( 1 / speed() );
				
				float fill = 0.9f - 0.5f*((HP-80)/80f);
				level.placeTrapsInTenguCell(fill);
				
				return;
				
				//otherwise.. TODO!
			} else {
				do {
					newPos = Random.Int(level.length());
				} while (
						level.solid[newPos] ||
								level.distance(newPos, enemy.pos) < 6 ||
								Actor.findChar(newPos) != null);
				
				if (level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
				
				sprite.move( pos, newPos );
				move( newPos );
				
				if (level.heroFOV[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
				Sample.INSTANCE.play( Assets.SND_PUFF );
				
				spend( 1 / speed() );
			}
			
		//if we're on another type of level
		} else {
			Level level = Dungeon.level;
			
			newPos = level.randomRespawnCell();
			
			if (level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
			
			sprite.move( pos, newPos );
			move( newPos );
			
			if (level.heroFOV[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
			Sample.INSTANCE.play( Assets.SND_PUFF );
			
			spend( 1 / speed() );
		}
		
	}
	
	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			if (HP <= HT/2) BossHealthBar.bleed(true);
			if (HP == HT) {
				yell(Messages.get(this, "notice_mine", Dungeon.hero.givenName()));
				for (Char ch : Actor.chars()){
					if (ch instanceof DriedRose.GhostHero){
						GLog.n("\n");
						((DriedRose.GhostHero) ch).sayBoss();
					}
				}
			} else {
				yell(Messages.get(this, "notice_face", Dungeon.hero.givenName()));
			}
		}
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
		if (HP <= HT/2) BossHealthBar.bleed(true);
	}
	
	//tengu is always hunting
	private class Hunting extends Mob.Hunting{
		
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {
				
				return doAttack( enemy );
				
			} else {
				
				if (enemyInFOV) {
					target = enemy.pos;
				} else {
					chooseEnemy();
					if (enemy != null) {
						target = enemy.pos;
					}
				}
				
				spend( TICK );
				return true;
				
			}
		}
	}
}
