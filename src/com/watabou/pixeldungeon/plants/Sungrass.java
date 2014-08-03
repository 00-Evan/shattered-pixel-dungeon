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
package com.watabou.pixeldungeon.plants;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.ShaftParticle;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;

public class Sungrass extends Plant {

	private static final String TXT_DESC = "Sungrass is renowned for its sap's healing properties.";
	
	{
		image = 4;
		plantName = "Sungrass";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			Buff.affect( ch, Health.class );
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Sungrass";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_SUNGRASS;
			
			plantClass = Sungrass.class;
			alchemyClass = PotionOfHealing.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
	
	public static class Health extends Buff {
		
		private static final float STEP = 1f;
		
		private int pos;
        private int healCurr = 2;
        private int count = 5;
        private int healTot;
		
		@Override
		public boolean attachTo( Char target ) {
			pos = target.pos;
            healTot = target.HT;
			return super.attachTo( target );
		}
		
		@Override
        public boolean act() {
            if (target.pos != pos || healTot <= 0) {
                detach();
            }
            if (count == 5) {
                if (healTot <= healCurr) {
                    target.HP = Math.min(target.HT, target.HP + healTot);
                    target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    detach();
                } else {
                    target.HP = Math.min(target.HT, target.HP + healCurr);
                    healTot -= healCurr;
                    healCurr = Math.min(healCurr+healCurr-1,(int)(target.HT*0.15));
                    target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                }
                count = 1;
            } else {
                count++;
            }
            spend( STEP );
            return true;
        }

        public int absorb( int damage ) {
            healTot -= damage;
            if (healTot <= 0)
                detach();
            return damage;
        }
		
		@Override
		public int icon() {
			return BuffIndicator.HEALING;
		}
		
		@Override
		public String toString() {
            return Utils.format( "Herbal Healing (%d)", healTot);
		}
		
		private static final String POS	= "pos";
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( POS, pos );
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			pos = bundle.getInt( POS );
		}
	}
}
