/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChooseAbility;
import com.watabou.utils.Bundle;

import java.text.DecimalFormat;
import java.util.ArrayList;

abstract public class ClassArmor extends Armor {

	private static final String AC_ABILITY = "ABILITY";
	
	{
		levelKnown = true;
		cursedKnown = true;
		defaultAction = AC_ABILITY;

		bones = false;
	}

	private int armorTier;

	private Charger charger;
	public float charge = 0;
	
	public ClassArmor() {
		super( 5 );
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		charger = new Charger();
		charger.attachTo(ch);
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			if (charger != null){
				charger.detach();
				charger = null;
			}
			return true;

		} else {
			return false;

		}
	}

	public static ClassArmor upgrade (Hero owner, Armor armor ) {
		
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
		classArmor.tier = armor.tier;
		classArmor.augment = armor.augment;
		classArmor.inscribe( armor.glyph );
		classArmor.cursed = armor.cursed;
		classArmor.curseInfusionBonus = armor.curseInfusionBonus;
		classArmor.identify();

		classArmor.charge = 50;
		
		return classArmor;
	}

	private static final String ARMOR_TIER	= "armortier";
	private static final String CHARGE	    = "charge";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ARMOR_TIER, tier );
		bundle.put( CHARGE, charge );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		tier = bundle.getInt( ARMOR_TIER );
		charge = bundle.getFloat(CHARGE);
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero )) {
			actions.add( AC_ABILITY );
		}
		return actions;
	}

	@Override
	public String actionName(String action, Hero hero) {
		if (hero.armorAbility != null && action.equals(AC_ABILITY)){
			return hero.armorAbility.name().toUpperCase();
		} else {
			return super.actionName(action, hero);
		}
	}

	@Override
	public String status() {
		return Messages.format( "%.0f%%", Math.floor(charge) );
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_ABILITY)){

			//for pre-0.9.3 saves
			if (hero.armorAbility == null){
				GameScene.show(new WndChooseAbility(null, this, hero));
			} else if (!isEquipped( hero )) {
				usesTargeting = false;
				GLog.w( Messages.get(this, "not_equipped") );
			} else if (charge < hero.armorAbility.chargeUse(hero)) {
				usesTargeting = false;
				GLog.w( Messages.get(this, "low_charge") );
			} else  {
				usesTargeting = hero.armorAbility.useTargeting();
				hero.armorAbility.use(this, hero);
			}
			
		}
	}

	@Override
	public String desc() {
		String desc = super.desc();

		ArmorAbility ability = Dungeon.hero.armorAbility;
		if (ability != null){
			desc += "\n\n" + ability.shortDesc();
			float chargeUse = ability.chargeUse(Dungeon.hero);
			desc += " " + Messages.get(this, "charge_use", new DecimalFormat("#.##").format(chargeUse));
		} else {
			desc += "\n\n" + "_" + Messages.get(this, "no_ability") + "_";
		}

		return desc;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int value() {
		return 0;
	}

	public class Charger extends Buff {
		@Override
		public boolean act() {
			LockedFloor lock = target.buff(LockedFloor.class);
			if (lock == null || lock.regenOn()) {
				charge += 100 / 500f; //500 turns to full charge
				updateQuickslot();
				if (charge > 100) {
					charge = 100;
				}
			}
			spend(TICK);
			return true;
		}
	}
}
