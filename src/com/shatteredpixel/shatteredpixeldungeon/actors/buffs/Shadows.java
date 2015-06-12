/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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

import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Shadows extends Invisibility {
	
	protected float left;
	
	private static final String LEFT	= "left";

	{
		type = buffType.SILENT;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
		
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		left = bundle.getFloat( LEFT );
	}
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			Sample.INSTANCE.play( Assets.SND_MELD );
			Dungeon.observe();
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			
			spend( TICK * 2 );
			
			if (--left <= 0 || Dungeon.hero.visibleEnemies() > 0) {
				detach();
			}
			
		} else {
			
			detach();
			
		}
		
		return true;
	}
	
	public void prolong() {
		left = 2;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.SHADOWS;
	}
	
	@Override
	public String toString() {
		return "Shadowmelded";
	}

	@Override
	public String desc() {
		return "You are blended into the shadows around you, granting you invisibility and slowing your metabolism.\n" +
				"\n" +
				"While you are invisible enemies are unable to attack or follow you. " +
				"Most physical attacks and magical effects (such as scrolls and wands) will immediately cancel invisibility. " +
				"Additionally, while shadowmelded, your rate of hunger is slowed.\n" +
				"\n" +
				"You will remain shadowmelded until you leave the shadows or an enemy comes into contact with you.";
	}
}
