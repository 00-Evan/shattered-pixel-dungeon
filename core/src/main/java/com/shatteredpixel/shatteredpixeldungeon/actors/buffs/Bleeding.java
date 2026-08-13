/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Sacrificial;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sickle;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Bleeding extends Buff implements Buff.DOTbuff {

	{
		type = buffType.NEGATIVE;
		announced = true;
	}
	
	protected float level;

	//used in specific cases where the source of the bleed is important for death logic
	private Class source;

	public float level(){
		return level;
	}
	
	private static final String LEVEL	= "level";
	private static final String SOURCE	= "source";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
		bundle.put( SOURCE, source );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
		source = bundle.getClass( SOURCE );
	}
	
	public void set( float level ) {
		set( level, null );
	}

	public void set( float level, Class source ){
		//previously bleed would reduce them dmg, now it dmgs then reduces.
		//this is essentially pre-calculating the first loss of bleed dmg.
		//this helps the total incoming DOT calculation be more consistent
		//avg. total damage is 3x the initial level, and becomes 4x the level with this pre-calc
		level = Random.NormalFloat(level / 2f, level);
		if (this.level < level) {
			this.level = Math.max(this.level, level);
			this.source = source;
		}
		if (target != null) target.needsIncomingDOTUpdate = true;
	}

	public void extend( float amount ) {
		level += amount;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.BLEEDING;
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(Math.round(level));
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			int dmg = Math.round(level);
			if (dmg > 0) {
				
				target.damage( dmg, this );
				if (target.sprite.visible) {
					Splash.at( target.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
							target.sprite.blood(), Math.min( 10 * dmg / target.HT, 10 ) );
				}
				
				if (target == Dungeon.hero && !target.isAlive()) {
					if (source == Chasm.class){
						Badges.validateDeathFromFalling();
					} else if (source == Sacrificial.class){
						Badges.validateDeathFromFriendlyMagic();
					}
					Dungeon.fail( this );
					GLog.n( Messages.get(this, "ondeath") );
				}

				if (source == Sickle.HarvestBleedTracker.class && !target.isAlive()){
					MeleeWeapon.onAbilityKill(Dungeon.hero, target);
				}
				
				spend( TICK );
			}

			level = Random.NormalFloat(level / 2f, level);
			if (Math.round(level) <= 0){
				detach();
			}
			target.needsIncomingDOTUpdate = true;
			
		} else {
			
			detach();
			
		}
		
		return true;
	}

	@Override
	public void detach() {
		target.needsIncomingDOTUpdate = true;
		super.detach();
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", Math.round(level));
	}

	@Override
	public int totalIncomingDMG() {
		//we reduce level after applying damage, otherwise this would be level*3
		//note that we also reduce level when applying bleed initially, to simulate old behaviour
		return Math.round(level*4f); //average damage
	}
}
