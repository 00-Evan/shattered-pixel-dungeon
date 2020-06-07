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

package net.casiello.pixeldungeonrescue.actors.mobs;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.blobs.Freezing;
import net.casiello.pixeldungeonrescue.actors.buffs.Blindness;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Burning;
import net.casiello.pixeldungeonrescue.actors.buffs.Chill;
import net.casiello.pixeldungeonrescue.effects.Lightning;
import net.casiello.pixeldungeonrescue.effects.Splash;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfFrost;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfLiquidFlame;
import net.casiello.pixeldungeonrescue.items.quest.Embers;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfRecharging;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfTransmutation;
import net.casiello.pixeldungeonrescue.items.wands.CursedWand;
import net.casiello.pixeldungeonrescue.items.weapon.enchantments.Shocking;
import net.casiello.pixeldungeonrescue.mechanics.Ballistica;
import net.casiello.pixeldungeonrescue.scenes.GameScene;
import net.casiello.pixeldungeonrescue.sprites.CharSprite;
import net.casiello.pixeldungeonrescue.sprites.ElementalSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Elemental extends Mob {

	{
		HP = HT = 60;
		defenseSkill = 20;
		
		EXP = 10;
		maxLvl = 20;
		
		flying = true;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 16, 26 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 25;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}
	
	private int rangedCooldown = Random.NormalIntRange( 3, 5 );
	
	@Override
	protected boolean act() {
		if (state == HUNTING){
			rangedCooldown--;
		}
		
		return super.act();
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		if (rangedCooldown <= 0) {
			return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT ).collisionPos == enemy.pos;
		} else {
			return super.canAttack( enemy );
		}
	}
	
	protected boolean doAttack( Char enemy ) {
		
		if (Dungeon.level.adjacent( pos, enemy.pos )) {
			
			return super.doAttack( enemy );
			
		} else {
			
			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				zap();
				return true;
			}
		}
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		meleeProc( enemy, damage );
		
		return damage;
	}
	
	private void zap() {
		spend( 1f );
		
		if (hit( this, enemy, true )) {
			
			rangedProc( enemy );
			
		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}

		rangedCooldown = Random.NormalIntRange( 3, 5 );
	}
	
	public void onZapComplete() {
		zap();
		next();
	}
	
	@Override
	public void add( Buff buff ) {
		if (harmfulBuffs.contains( buff.getClass() )) {
			damage( Random.NormalIntRange( HT/2, HT * 3/5 ), buff );
		} else {
			super.add( buff );
		}
	}
	
	protected abstract void meleeProc( Char enemy, int damage );
	protected abstract void rangedProc( Char enemy );
	
	protected ArrayList<Class<? extends Buff>> harmfulBuffs = new ArrayList<>();
	
	private static final String COOLDOWN = "cooldown";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( COOLDOWN, rangedCooldown );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (bundle.contains( COOLDOWN )){
			rangedCooldown = bundle.getInt( COOLDOWN );
		}
	}
	
	public static class FireElemental extends Elemental {
		
		{
			spriteClass = ElementalSprite.Fire.class;
			
			loot = new PotionOfLiquidFlame();
			lootChance = 1/8f;
			
			properties.add( Property.FIERY );
			
			harmfulBuffs.add( net.casiello.pixeldungeonrescue.actors.buffs.Frost.class );
			harmfulBuffs.add( Chill.class );
		}
		
		@Override
		protected void meleeProc( Char enemy, int damage ) {
			if (Random.Int( 2 ) == 0 && !Dungeon.level.water[enemy.pos]) {
				Buff.affect( enemy, Burning.class ).reignite( enemy );
				Splash.at( enemy.sprite.center(), sprite.blood(), 5);
			}
		}
		
		@Override
		protected void rangedProc( Char enemy ) {
			if (!Dungeon.level.water[enemy.pos]) {
				Buff.affect( enemy, Burning.class ).reignite( enemy, 4f );
			}
			Splash.at( enemy.sprite.center(), sprite.blood(), 5);
		}
	}
	
	//used in wandmaker quest
	public static class NewbornFireElemental extends FireElemental {
		
		{
			spriteClass = ElementalSprite.NewbornFire.class;
			
			HT = 60;
			HP = HT/2; //32
			
			defenseSkill = 12;
			
			EXP = 7;
			
			loot = new Embers();
			lootChance = 1f;
			
			properties.add(Property.MINIBOSS);
		}

		@Override
		public boolean reset() {
			return true;
		}
		
	}
	
	public static class FrostElemental extends Elemental {
		
		{
			spriteClass = ElementalSprite.Frost.class;
			
			loot = new PotionOfFrost();
			lootChance = 1/8f;
			
			properties.add( Property.ICY );
			
			harmfulBuffs.add( Burning.class );
		}
		
		@Override
		protected void meleeProc( Char enemy, int damage ) {
			if (Random.Int( 3 ) == 0 || Dungeon.level.water[enemy.pos]) {
				Freezing.freeze( enemy.pos );
				Splash.at( enemy.sprite.center(), sprite.blood(), 5);
			}
		}
		
		@Override
		protected void rangedProc( Char enemy ) {
			Freezing.freeze( enemy.pos );
			Splash.at( enemy.sprite.center(), sprite.blood(), 5);
		}
	}
	
	public static class ShockElemental extends Elemental {
		
		{
			spriteClass = ElementalSprite.Shock.class;
			
			loot = new ScrollOfRecharging();
			lootChance = 1/4f;
			
			properties.add( Property.ELECTRIC );
		}
		
		@Override
		protected void meleeProc( Char enemy, int damage ) {
			ArrayList<Char> affected = new ArrayList<>();
			ArrayList<Lightning.Arc> arcs = new ArrayList<>();
			Shocking.arc( this, enemy, 2, affected, arcs );
			
			if (!Dungeon.level.water[enemy.pos]) {
				affected.remove( enemy );
			}
			
			for (Char ch : affected) {
				ch.damage( Math.round( damage * 0.4f ), this );
			}
			
			sprite.parent.addToFront( new Lightning( arcs, null ) );
			Sample.INSTANCE.play( Assets.SND_LIGHTNING );
		}
		
		@Override
		protected void rangedProc( Char enemy ) {
			Buff.affect( enemy, Blindness.class, 5f );
			if (enemy == Dungeon.hero) {
				GameScene.flash(0xFFFFFF);
			}
		}
	}
	
	public static class ChaosElemental extends Elemental {
		
		{
			spriteClass = ElementalSprite.Chaos.class;
			
			loot = new ScrollOfTransmutation();
			lootChance = 1f;
		}
		
		@Override
		protected void meleeProc( Char enemy, int damage ) {
			CursedWand.cursedZap( null, this, new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT ), new Callback() {
				@Override
				public void call() {
					next();
				}
			} );
		}
		
		@Override
		protected void rangedProc( Char enemy ) {
			CursedWand.cursedZap( null, this, new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT ), new Callback() {
				@Override
				public void call() {
					next();
				}
			} );
		}
	}
	
	public static Class<? extends Elemental> random(){
		if (Random.Int( 50 ) == 0){
			return ChaosElemental.class;
		}
		
		float roll = Random.Float();
		if (roll < 0.4f){
			return FireElemental.class;
		} else if (roll < 0.8f){
			return FrostElemental.class;
		} else {
			return ShockElemental.class;
		}
	}
}
