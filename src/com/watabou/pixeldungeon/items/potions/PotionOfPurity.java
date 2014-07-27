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
package com.watabou.pixeldungeon.items.potions;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ParalyticGas;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.GasesImmunity;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.utils.BArray;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

public class PotionOfPurity extends Potion {

	private static final String TXT_FRESHNESS	= "You feel uncommon freshness in the air.";
	private static final String TXT_NO_SMELL	= "You've stopped sensing any smells!";
	
	private static final int DISTANCE	= 2;
	
	{
		name = "Potion of Purification";
	}
	
	@Override
	protected void shatter( int cell ) {
		
		PathFinder.buildDistanceMap( cell, BArray.not( Level.losBlocking, null ), DISTANCE );
		
		boolean procd = false;
		
		Blob[] blobs = {
			Dungeon.level.blobs.get( ToxicGas.class ), 
			Dungeon.level.blobs.get( ParalyticGas.class )
		};
		
		for (int j=0; j < blobs.length; j++) {
			
			Blob blob = blobs[j];
			if (blob == null) {
				continue;
			}
			
			for (int i=0; i < Level.LENGTH; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					
					int value = blob.cur[i]; 
					if (value > 0) {
						
						blob.cur[i] = 0;
						blob.volume -= value;
						procd = true;
						
						CellEmitter.get( i ).burst( Speck.factory( Speck.DISCOVER ), 1 );
					}

				}
			}
		}
		
		boolean heroAffected = PathFinder.distance[Dungeon.hero.pos] < Integer.MAX_VALUE;
		
		if (procd) {
			
			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
			
			setKnown();
			
			if (heroAffected) {
				GLog.p( TXT_FRESHNESS );
			}
			
		} else {
			
			super.shatter( cell );
			
			if (heroAffected) {
				GLog.i( TXT_FRESHNESS );
				setKnown();
			}
			
		}
	}
	
	@Override
	protected void apply( Hero hero ) {
		GLog.w( TXT_NO_SMELL );
		Buff.prolong( hero, GasesImmunity.class, GasesImmunity.DURATION );
		setKnown();
	}
	
	@Override
	public String desc() {
		return 
			"This reagent will quickly neutralize all harmful gases in the area of effect. " +
			"Drinking it will give you a temporary immunity to such gases.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
