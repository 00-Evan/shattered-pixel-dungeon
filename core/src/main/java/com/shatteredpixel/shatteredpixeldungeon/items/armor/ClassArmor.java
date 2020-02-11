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

package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

abstract public class ClassArmor extends Armor {

	private static final String AC_SPECIAL = "SPECIAL";
	
	{
		levelKnown = true;
		cursedKnown = true;
		defaultAction = AC_SPECIAL;

		bones = false;
	}

	private int armorTier;

	protected float charge = 0;
	
	public ClassArmor() {
		super( 6 );
	}
	
	public static ClassArmor upgrade ( Hero owner, Armor armor ) {
		
		ClassArmor classArmor = null;
		
		switch (owner.heroClass) {
		case WARRIOR:
			classArmor = new WarriorArmor();
			BrokenSeal seal = armor.checkSeal();
			if (seal != null) {
				classArmor.affixSeal(seal);
			}
			break;
		case ROGUE:
			classArmor = new RogueArmor();
			break;
		case MAGE:
			classArmor = new MageArmor();
			break;
		case HUNTRESS:
			classArmor = new HuntressArmor();
			break;
		}
		
		classArmor.level(armor.level() - (armor.curseInfusionBonus ? 1 : 0));
		classArmor.armorTier = armor.tier;
		classArmor.augment = armor.augment;
		classArmor.inscribe( armor.glyph );
		classArmor.cursed = armor.cursed;
		classArmor.curseInfusionBonus = armor.curseInfusionBonus;
		classArmor.identify();

		classArmor.charge = 0;
		if (owner.lvl > 18){
			classArmor.charge += (owner.lvl-18)*25;
			if (classArmor.charge > 100) classArmor.charge = 100;
		}
		
		return classArmor;
	}

	private static final String ARMOR_TIER	= "armortier";
	private static final String CHARGE	    = "charge";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ARMOR_TIER, armorTier );
		bundle.put( CHARGE, charge );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		armorTier = bundle.getInt( ARMOR_TIER );
		charge = bundle.getFloat(CHARGE);
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.HP >= 3 && isEquipped( hero )) {
			actions.add( AC_SPECIAL );
		}
		return actions;
	}

	@Override
	public String status() {
		return Messages.format( "%.0f%%", charge );
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_SPECIAL)) {
			
			if (!isEquipped( hero )) {
				GLog.w( Messages.get(this, "not_equipped") );
			} else if (charge < 35) {
				GLog.w( Messages.get(this, "low_charge") );
			} else  {
				curUser = hero;
				doSpecial();
			}
			
		}
	}

	@Override
	public void onHeroGainExp(float levelPercent, Hero hero) {
		super.onHeroGainExp(levelPercent, hero);
		charge += 50 * levelPercent;
		if (charge > 100) charge = 100;
		updateQuickslot();
	}

	abstract public void doSpecial();

	@Override
	public int STRReq(int lvl) {
		lvl = Math.max(0, lvl);

		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + Math.round(armorTier * 2)) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}

	@Override
	public int DRMax(int lvl){
		int max = armorTier * (2 + lvl) + augment.defenseFactor(lvl);
		if (lvl > max){
			return ((lvl - max)+1)/2;
		} else {
			return max;
		}
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 0;
	}

}
