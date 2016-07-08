/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ItemStatusHandler;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class Ring extends KindofMisc {

	private static final int TICKS_TO_KNOW    = 200;
	
	protected Buff buff;
	
	private static final Class<?>[] rings = {
		RingOfAccuracy.class,
		RingOfEvasion.class,
		RingOfElements.class,
		RingOfForce.class,
		RingOfFuror.class,
		RingOfHaste.class,
		RingOfMagic.class,
		RingOfMight.class,
		RingOfSharpshooting.class,
		RingOfTenacity.class,
		RingOfWealth.class,
	};

	private static final HashMap<String, Integer> gems = new HashMap<String, Integer>() {
		{
			put("garnet",ItemSpriteSheet.RING_GARNET);
			put("ruby",ItemSpriteSheet.RING_RUBY);
			put("topaz",ItemSpriteSheet.RING_TOPAZ);
			put("emerald",ItemSpriteSheet.RING_EMERALD);
			put("onyx",ItemSpriteSheet.RING_ONYX);
			put("opal",ItemSpriteSheet.RING_OPAL);
			put("tourmaline",ItemSpriteSheet.RING_TOURMALINE);
			put("sapphire",ItemSpriteSheet.RING_SAPPHIRE);
			put("amethyst",ItemSpriteSheet.RING_AMETHYST);
			put("quartz",ItemSpriteSheet.RING_QUARTZ);
			put("agate",ItemSpriteSheet.RING_AGATE);
			put("diamond",ItemSpriteSheet.RING_DIAMOND);
		}
	};
	
	private static ItemStatusHandler<Ring> handler;
	
	private String gem;
	
	private int ticksToKnow = TICKS_TO_KNOW;
	
	@SuppressWarnings("unchecked")
	public static void initGems() {
		handler = new ItemStatusHandler<>( (Class<? extends Ring>[])rings, gems );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}

	public static void saveSelectively( Bundle bundle, ArrayList<Item> items ) {
		handler.saveSelectively( bundle, items );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<>( (Class<? extends Ring>[])rings, gems, bundle );
	}
	
	public Ring() {
		super();
		reset();
	}
	
	public void reset() {
		super.reset();
		image	= handler.image( this );
		gem		= handler.label( this );
	}
	
	public void activate( Char ch ) {
		buff = buff();
		buff.attachTo( ch );
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

			hero.remove( buff );
			buff = null;

			return true;

		} else {

			return false;

		}
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
		return isKnown() ? super.name() : Messages.get(Ring.class, gem);
	}
	
	@Override
	public String info() {

		String desc = isKnown()? desc() : Messages.get(this, "unknown_desc");

		if (cursed && isEquipped( Dungeon.hero )) {
			
			desc += "\n\n" + Messages.get(Ring.class, "cursed_worn");
			
		} else if (cursed && cursedKnown) {

			desc += "\n\n" + Messages.get(Ring.class, "curse_known");

		}

		return desc;
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
		int n = 1;
		if (Random.Int(3) == 0) {
			n++;
			if (Random.Int(5) == 0){
				n++;
			}
		}

		if (Random.Float() < 0.3f) {
			level(-n);
			cursed = true;
		} else
			level(n);

		return this;
	}
	
	public static boolean allKnown() {
		return handler.known().size() == rings.length - 2;
	}
	
	@Override
	public int price() {
		int price = 75;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level() > 0) {
				price *= (level() + 1);
			} else if (level() < 0) {
				price /= (1 - level());
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

	private static final String UNFAMILIRIARITY    = "unfamiliarity";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, ticksToKnow );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((ticksToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			ticksToKnow = TICKS_TO_KNOW;
		}
	}

	public static int getBonus(Char target, Class<?extends RingBuff> type){
		int bonus = 0;
		for (RingBuff buff : target.buffs(type)) {
			bonus += buff.level();
		}
		return bonus;
	}

	public class RingBuff extends Buff {
		
		@Override
		public boolean attachTo( Char target ) {

			if (target instanceof Hero && ((Hero)target).heroClass == HeroClass.ROGUE && !isKnown()) {
				setKnown();
				GLog.i( Messages.get(Ring.class, "known", name()) );
				Badges.validateItemLevelAquired( Ring.this );
			}
			
			return super.attachTo(target);
		}
		
		@Override
		public boolean act() {
			
			if (!isIdentified() && --ticksToKnow <= 0) {
				identify();
				GLog.w( Messages.get(Ring.class, "identify", Ring.this.toString()) );
				Badges.validateItemLevelAquired( Ring.this );
			}
			
			spend( TICK );
			
			return true;
		}

		public int level(){
			return Ring.this.level();
		}

	}
}
