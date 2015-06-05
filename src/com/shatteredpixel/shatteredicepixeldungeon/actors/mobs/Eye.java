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
import com.shatteredpixel.shatteredicepixeldungeon.ResultDescriptions;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredicepixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredicepixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredicepixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredicepixeldungeon.items.weapon.enchantments.Death;
import com.shatteredpixel.shatteredicepixeldungeon.items.weapon.enchantments.Leech;
import com.shatteredpixel.shatteredicepixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.EyeSprite;
import com.shatteredpixel.shatteredicepixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredicepixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class Eye extends Mob {
	
	private static final String TXT_DEATHGAZE_KILLED = "%s's deathgaze killed you...";
	
	{
		name = "evil eye";
		spriteClass = EyeSprite.class;
		
		HP = HT = 100;
		defenseSkill = 20;
		viewDistance = Light.DISTANCE;
		
		EXP = 13;
		maxLvl = 25;
		
		flying = true;
		
		loot = new Dewdrop();
		lootChance = 0.5f;
	}
	
	@Override
	public int dr() {
		return 10;
	}
	
	private int hitCell;
	
	@Override
	protected boolean canAttack( Char enemy ) {
		
		hitCell = Ballistica.cast( pos, enemy.pos, true, false );
		
		for (int i=1; i < Ballistica.distance; i++) {
			if (Ballistica.trace[i] == enemy.pos) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 30;
	}
	
	@Override
	protected float attackDelay() {
		return 1.6f;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {

		spend( attackDelay() );
		
		boolean rayVisible = false;
		
		for (int i=0; i < Ballistica.distance; i++) {
			if (Dungeon.visible[Ballistica.trace[i]]) {
				rayVisible = true;
			}
		}
		
		if (rayVisible) {
			sprite.attack( hitCell );
			return false;
		} else {
			attack( enemy );
			return true;
		}
	}
	
	@Override
	public boolean attack( Char enemy ) {
		
		for (int i=1; i < Ballistica.distance; i++) {
			
			int pos = Ballistica.trace[i];
			
			Char ch = Actor.findChar( pos );
			if (ch == null) {
				continue;
			}
			
			if (hit( this, ch, true )) {
				ch.damage( Random.NormalIntRange( 14, 20 ), this );
				
				if (Dungeon.visible[pos]) {
					ch.sprite.flash();
					CellEmitter.center( pos ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
				}
				
				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail( Utils.format( ResultDescriptions.MOB, Utils.indefinite( name ) ) );
					GLog.n( TXT_DEATHGAZE_KILLED, name );
				}
			} else {
				ch.sprite.showStatus( CharSprite.NEUTRAL,  ch.defenseVerb() );
			}
		}
		
		return true;
	}
	
	@Override
	public String description() {
		return
			"One of this demon's other names is \"orb of hatred\", because when it sees an enemy, " +
			"it uses its deathgaze recklessly, often ignoring its allies and wounding them.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( WandOfDisintegration.class );
		RESISTANCES.add( Death.class );
		RESISTANCES.add( Leech.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
