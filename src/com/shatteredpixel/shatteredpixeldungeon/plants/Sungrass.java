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
package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;

public class Sungrass extends Plant {

	private static final String TXT_DESC = "Sungrass is renowned for its sap's slow but effective healing properties.";
	
	{
		image = 4;
		plantName = "Sungrass";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			Buff.affect( ch, Health.class ).level = ch.HT;
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

            bones = true;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
	
	public static class Health extends Buff {
		
		private static final float STEP = 1f;
		
		private int pos;
        private int healCurr = 1;
        private int count = 0;
        private int level;
		
		@Override
		public boolean attachTo( Char target ) {
			pos = target.pos;
			return super.attachTo( target );
		}
		
		@Override
        public boolean act() {
            if (target.pos != pos) {
                detach();
            }
            if (count == 5) {
                if (level <= healCurr*.025*target.HT) {
                    target.HP = Math.min(target.HT, target.HP + level);
                    target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    detach();
                } else {
                    target.HP = Math.min(target.HT, target.HP+(int)(healCurr*.025*target.HT));
                    level -= (healCurr*.025*target.HT);
                    if (healCurr < 6)
                        healCurr ++;
                    target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                }
                count = 1;
            } else {
                count++;
            }
            if (level <= 0)
                detach();
            spend( STEP );
            return true;
        }

        public int absorb( int damage ) {
            level -= damage;
            if (level <= 0)
                detach();
            return damage;
        }
		
		@Override
		public int icon() {
			return BuffIndicator.HEALING;
		}
		
		@Override
		public String toString() {
            return Utils.format( "Herbal Healing (%d)", level);
		}
		
		private static final String POS	= "pos";
        private static final String HEALCURR = "healCurr";
        private static final String COUNT = "count";
        private static final String LEVEL = "level";

        @Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( POS, pos );
            bundle.put( HEALCURR, healCurr);
            bundle.put( COUNT, count);
            bundle.put( LEVEL, level);
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			pos = bundle.getInt( POS );
            healCurr = bundle.getInt( HEALCURR );
            count = bundle.getInt( COUNT );
            level = bundle.getInt( LEVEL );

		}
	}
}
