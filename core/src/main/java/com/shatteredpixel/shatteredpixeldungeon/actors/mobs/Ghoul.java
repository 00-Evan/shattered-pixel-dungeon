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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

//TODO some aspects of existing enemy AI make these really wonky. Need to address that.
public class Ghoul extends Mob {
	
	{
		spriteClass = GhoulSprite.class;
		
		HP = HT = 50;
		defenseSkill = 18;
		
		EXP = 5;
		maxLvl = 20;
		
		SLEEPING = new Sleeping();
		WANDERING = new Wandering();
		state = SLEEPING;
		
		properties.add(Property.UNDEAD);
	}
	
	private int partnerID = -1;
	private static final String PARTNER_ID = "partner_id";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( PARTNER_ID, partnerID );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		partnerID = bundle.getInt( PARTNER_ID );
	}
	
	@Override
	protected boolean act() {
		//create a child
		if (partnerID == -1){
			
			ArrayList<Integer> candidates = new ArrayList<>();
			
			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
			for (int n : neighbours) {
				if (Dungeon.level.passable[n] && Actor.findChar( n ) == null) {
					candidates.add( n );
				}
			}
			
			if (!candidates.isEmpty()){
				Ghoul child = new Ghoul();
				child.partnerID = this.id();
				this.partnerID = child.id();
				if (state != SLEEPING) {
					child.state = child.WANDERING;
				}
				
				child.pos = Random.element( candidates );
				
				Dungeon.level.occupyCell(child);
				
				GameScene.add( child );
				if (sprite.visible) {
					Actor.addDelayed( new Pushing( child, pos, child.pos ), -1 );
				}
			}
			
		}
		return super.act();
	}
	
	private class Sleeping extends Mob.Sleeping {
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			Ghoul partner = (Ghoul) Actor.findById( partnerID );
			if (partner != null && partner.state != partner.SLEEPING){
				state = WANDERING;
				target = partner.pos;
				return true;
			} else {
				return super.act( enemyInFOV, justAlerted );
			}
		}
	}
	
	private class Wandering extends Mob.Wandering {
		
		@Override
		protected boolean continueWandering() {
			enemySeen = false;
			
			Ghoul partner = (Ghoul) Actor.findById( partnerID );
			if (partner != null && (partner.state != partner.WANDERING || Dungeon.level.distance( pos,  partner.target) > 1)){
				target = partner.pos;
				int oldPos = pos;
				if (getCloser( target )){
					spend( 1 / speed() );
					return moveSprite( oldPos, pos );
				} else {
					spend( TICK );
					return true;
				}
			} else {
				return super.continueWandering();
			}
		}
	}
}
