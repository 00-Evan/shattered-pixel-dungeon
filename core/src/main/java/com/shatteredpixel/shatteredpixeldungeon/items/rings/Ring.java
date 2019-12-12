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

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ItemStatusHandler;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Ring extends KindofMisc {
	
	protected Buff buff;
	
	private static final Class<?>[] rings = {
		RingOfAccuracy.class,
		RingOfEvasion.class,
		RingOfElements.class,
		RingOfForce.class,
		RingOfFuror.class,
		RingOfHaste.class,
		RingOfEnergy.class,
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
	
	//rings cannot be 'used' like other equipment, so they ID purely based on exp
	private float levelsToID = 1;
	
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

	//anonymous rings are always IDed, do not affect ID status,
	//and their sprite is replaced by a placeholder if they are not known,
	//useful for items that appear in UIs, or which are only spawned for their effects
	protected boolean anonymous = false;
	public void anonymize(){
		if (!isKnown()) image = ItemSpriteSheet.RING_HOLDER;
		anonymous = true;
	}
	
	public void reset() {
		super.reset();
		levelsToID = 1;
		if (handler != null && handler.contains(this)){
			image = handler.image(this);
			gem = handler.label(this);
		}
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
		return anonymous || (handler != null && handler.isKnown( this ));
	}
	
	public void setKnown() {
		if (!anonymous) {
			if (!isKnown()) {
				handler.know(this);
			}

			if (Dungeon.hero.isAlive()) {
				Catalog.setSeen(getClass());
			}
		}
	}
	
	@Override
	public String name() {
		return isKnown() ? super.name() : Messages.get(Ring.class, gem);
	}
	
	@Override
	public String info(){
		
		String desc = isKnown() ? super.desc() : Messages.get(this, "unknown_desc");
		
		if (cursed && isEquipped( Dungeon.hero )) {
			desc += "\n\n" + Messages.get(Ring.class, "cursed_worn");
			
		} else if (cursed && cursedKnown) {
			desc += "\n\n" + Messages.get(Ring.class, "curse_known");
			
		} else if (!isIdentified() && cursedKnown){
			desc += "\n\n" + Messages.get(Ring.class, "not_cursed");
			
		}
		
		if (isKnown()) {
			desc += "\n\n" + statsInfo();
		}
		
		return desc;
	}
	
	protected String statsInfo(){
		return "";
	}
	
	@Override
	public Item upgrade() {
		super.upgrade();
		
		if (Random.Int(3) == 0) {
			cursed = false;
		}
		
		return this;
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && isKnown();
	}
	
	@Override
	public Item identify() {
		setKnown();
		levelsToID = 0;
		return super.identify();
	}
	
	@Override
	public Item random() {
		//+0: 66.67% (2/3)
		//+1: 26.67% (4/15)
		//+2: 6.67%  (1/15)
		int n = 0;
		if (Random.Int(3) == 0) {
			n++;
			if (Random.Int(5) == 0){
				n++;
			}
		}
		level(n);
		
		//30% chance to be cursed
		if (Random.Float() < 0.3f) {
			cursed = true;
		}
		
		return this;
	}
	
	public static HashSet<Class<? extends Ring>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Ring>> getUnknown() {
		return handler.unknown();
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

	private static final String LEVELS_TO_ID    = "levels_to_ID";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVELS_TO_ID, levelsToID );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		levelsToID = bundle.getFloat( LEVELS_TO_ID );
		
		//pre-0.7.2 saves
		if (bundle.contains( "unfamiliarity" )){
			levelsToID = bundle.getInt( "unfamiliarity" ) / 200f;
		}
	}
	
	public void onHeroGainExp( float levelPercent, Hero hero ){
		if (isIdentified() || !isEquipped(hero)) return;
		//becomes IDed after 1 level
		levelsToID -= levelPercent;
		if (levelsToID <= 0){
			identify();
			GLog.p( Messages.get(Ring.class, "identify", toString()) );
			Badges.validateItemLevelAquired( this );
		}
	}

	public static int getBonus(Char target, Class<?extends RingBuff> type){
		int bonus = 0;
		for (RingBuff buff : target.buffs(type)) {
			bonus += buff.level();
		}
		return bonus;
	}

	public static int getBuffedBonus(Char target, Class<?extends RingBuff> type){
		int bonus = 0;
		for (RingBuff buff : target.buffs(type)) {
			bonus += buff.buffedLvl();
		}
		return bonus;
	}
	
	public int soloBonus(){
		if (cursed){
			return Math.min( 0, Ring.this.level()-2 );
		} else {
			return Ring.this.level()+1;
		}
	}

	public int soloBuffedBonus(){
		if (cursed){
			return Math.min( 0, Ring.this.buffedLvl()-2 );
		} else {
			return Ring.this.buffedLvl()+1;
		}
	}

	public class RingBuff extends Buff {
		
		@Override
		public boolean act() {
			
			spend( TICK );
			
			return true;
		}

		public int level(){
			return Ring.this.soloBonus();
		}

		public int buffedLvl(){
			return Ring.this.soloBuffedBonus();
		}

	}
}
