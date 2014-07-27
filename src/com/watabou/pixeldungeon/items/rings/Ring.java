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
package com.watabou.pixeldungeon.items.rings;

import java.util.ArrayList;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.items.EquipableItem;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.ItemStatusHandler;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ring extends EquipableItem {

	private static final float TIME_TO_EQUIP = 1f;
	
	private static final String TXT_IDENTIFY = 
		"you are now familiar enough with your %s to identify it. It is %s.";
	
	protected Buff buff;
	
	private static final Class<?>[] rings = { 
		RingOfMending.class, 
		RingOfDetection.class, 
		RingOfShadows.class,
		RingOfPower.class,
		RingOfHerbalism.class,
		RingOfAccuracy.class,
		RingOfEvasion.class,
		RingOfSatiety.class,
		RingOfHaste.class,
		RingOfHaggler.class,
		RingOfElements.class,
		RingOfThorns.class
	};
	private static final String[] gems = 
		{"diamond", "opal", "garnet", "ruby", "amethyst", "topaz", "onyx", "tourmaline", "emerald", "sapphire", "quartz", "agate"};
	private static final Integer[] images = {
		ItemSpriteSheet.RING_DIAMOND, 
		ItemSpriteSheet.RING_OPAL, 
		ItemSpriteSheet.RING_GARNET, 
		ItemSpriteSheet.RING_RUBY, 
		ItemSpriteSheet.RING_AMETHYST, 
		ItemSpriteSheet.RING_TOPAZ, 
		ItemSpriteSheet.RING_ONYX, 
		ItemSpriteSheet.RING_TOURMALINE, 
		ItemSpriteSheet.RING_EMERALD, 
		ItemSpriteSheet.RING_SAPPHIRE, 
		ItemSpriteSheet.RING_QUARTZ, 
		ItemSpriteSheet.RING_AGATE};
	
	private static ItemStatusHandler<Ring> handler;
	
	private String gem;
	
	private int ticksToKnow = 200;
	
	@SuppressWarnings("unchecked")
	public static void initGems() {
		handler = new ItemStatusHandler<Ring>( (Class<? extends Ring>[])rings, gems, images );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Ring>( (Class<? extends Ring>[])rings, gems, images, bundle );
	}
	
	public Ring() {
		super();
		syncGem();
	}
	
	public void syncGem() {
		image	= handler.image( this );
		gem		= handler.label( this );
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		return actions;
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		
		if (hero.belongings.ring1 != null && hero.belongings.ring2 != null) {
			
			GLog.w( "you can only wear 2 rings at a time" );
			return false;
			
		} else {
			
			if (hero.belongings.ring1 == null) {
				hero.belongings.ring1 = this;
			} else {
				hero.belongings.ring2 = this;
			}
			
			detach( hero.belongings.backpack );
			
			activate( hero );
			
			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( "your " + this + " tightens around your finger painfully" );
			}
			
			hero.spendAndNext( TIME_TO_EQUIP );
			return true;
			
		}

	}
	
	public void activate( Char ch ) {
		buff = buff();
		buff.attachTo( ch );
	}
	
	@Override
	public boolean doUnequip( Hero hero, boolean collect ) {
		
		if (cursed) {
			GLog.w( "You can't remove cursed " + name() + "!" );
			return false;
		}
		
		if (hero.belongings.ring1 == this) {
			hero.belongings.ring1 = null;
		} else {
			hero.belongings.ring2 = null;
		}
		
		hero.remove( buff );
		buff = null;
		
		hero.spendAndNext( TIME_TO_EQUIP );
		
		if (collect && !collect( hero.belongings.backpack )) {
			Dungeon.level.drop( this, hero.pos );
		}
				
		return true;
	}
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.ring1 == this || hero.belongings.ring2 == this;
	}
	
	@Override
	public Item upgrade() {
		
		super.upgrade();
		
		if (buff != null) {
			
			Char owner = buff.target;
			buff.detach();
			if ((buff = buff()) != null) {
				buff.attachTo( owner );
			}
		}
		
		return this;
	}
	
	public boolean isKnown() {
		return handler.isKnown( this );
	}
	
	protected void setKnown() {
		if (!isKnown()) {
			handler.know( this );
		}
		
		Badges.validateAllRingsIdentified();
	}
	
	@Override
	public String name() {
		return isKnown() ? name : gem + " ring";
	}
	
	@Override
	public String desc() {
		return 
			"This metal band is adorned with a large " + gem + " gem " +
			"that glitters in the darkness. Who knows what effect it has when worn?";
	}
	
	@Override
	public String info() {
		if (isEquipped( Dungeon.hero )) {
			
			return desc() + "\n\n" + "The " + name() + " is on your finger" + 
				(cursed ? ", and because it is cursed, you are powerless to remove it." : "." );
			
		} else if (cursed && cursedKnown) {
			
			return desc() + "\n\nYou can feel a malevolent magic lurking within the " + name() + ".";
			
		} else {
			
			return desc();
			
		}
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && isKnown();
	}
	
	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}
	
	@Override
	public Item random() {
		level = Random.Int( 1, 3 );
		if (Random.Float() < 0.3f) {
			level = -level;
			cursed = true;
		}
		return this;
	}
	
	public static boolean allKnown() {
		return handler.known().size() == rings.length - 2;
	}
	
	@Override
	public int price() {
		int price = 80;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level > 0) {
				price *= (level + 1);
			} else if (level < 0) {
				price /= (1 - level);
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}
	
	protected RingBuff buff() {
		return null;
	}
	
	public class RingBuff extends Buff {
		
		private static final String TXT_KNOWN = "This is a %s"; 
		
		public int level;
		public RingBuff() {
			level = Ring.this.level;
		}
		
		@Override
		public boolean attachTo( Char target ) {

			if (target instanceof Hero && ((Hero)target).heroClass == HeroClass.ROGUE && !isKnown()) {
				setKnown();
				GLog.i( TXT_KNOWN, name() );
				Badges.validateItemLevelAquired( Ring.this );
			}
			
			return super.attachTo(target);
		}
		
		@Override
		public boolean act() {
			
			if (!isIdentified() && --ticksToKnow <= 0) {
				String gemName = name();
				identify();
				GLog.w( TXT_IDENTIFY, gemName, Ring.this.toString() );
				Badges.validateItemLevelAquired( Ring.this );
			}
			
			spend( TICK );
			
			return true;
		}
	}
}
