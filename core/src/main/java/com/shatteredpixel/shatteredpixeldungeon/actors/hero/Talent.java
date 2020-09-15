/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public enum Talent {

	HEARTY_MEAL(0),
	ARMSMASTERS_INTUITION(1),
	TEST_SUBJECT(2),
	IRON_WILL(3),

	ENERGIZING_MEAL(16),
	SCHOLARS_INTUITION(17),
	TEST_MAGE_3(18),
	TEST_MAGE_4(19),

	RATIONED_MEAL(32),
	THIEFS_INTUITION(33),
	TEST_ROGUE_3(34),
	TEST_ROGUE_4(35),

	INVIGORATING_MEAL(48),
	SURVIVALISTS_INTUITION(49),
	TEST_HUNTRESS_3(50),
	TEST_HUNTRESS_4(51);

	int icon;

	Talent(int icon ){
		this.icon = icon;
	}

	public int icon(){
		return icon;
	}

	public int maxPoints(){
		return 2;
	}

	public String title(){
		return Messages.get(this, name() + ".title");
	}

	public String desc(){
		return Messages.get(this, name() + ".desc");
	}

	public static void onTalentUpgraded( Hero hero, Talent talent){
		if (talent == ARMSMASTERS_INTUITION && hero.pointsInTalent(ARMSMASTERS_INTUITION) == 2){
			if (hero.belongings.weapon != null) hero.belongings.weapon.identify();
			if (hero.belongings.armor != null)  hero.belongings.armor.identify();
		}
		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.identify();
			if (hero.belongings.misc instanceof Ring) hero.belongings.misc.identify();
			for (Item item : Dungeon.hero.belongings){
				if (item instanceof Ring){
					((Ring) item).setKnown();
				}
			}
		}
		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 1){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.setKnown();
			if (hero.belongings.misc instanceof Ring) ((Ring) hero.belongings.misc).setKnown();
		}
	}

	public static void onFoodEaten( Hero hero, float foodVal ){
		if (hero.hasTalent(HEARTY_MEAL) && hero.HP <= hero.HT/2){
			//4/6 HP healed
			hero.HP = Math.min( hero.HP + 2*(1+hero.pointsInTalent(HEARTY_MEAL)), hero.HT );
			hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), hero.pointsInTalent(HEARTY_MEAL) );
		}
		if (hero.hasTalent(ENERGIZING_MEAL) && hero.HP <= hero.HT/2){
			//4/6 turns of recharging
			Buff.affect( hero, Recharging.class, 2*(1+hero.pointsInTalent(ENERGIZING_MEAL)) );
			ScrollOfRecharging.charge( hero );
		}
		if (hero.hasTalent(RATIONED_MEAL) && hero.HP <= hero.HT/2){
			//20%/30% bonus food value
			float bonusSatiety = foodVal * 0.1f*(1+hero.pointsInTalent(RATIONED_MEAL));
			Buff.affect(hero, Hunger.class).affectHunger(bonusSatiety, true);
			//TODO vfx
		}
		if (hero.hasTalent(INVIGORATING_MEAL) && hero.HP <= hero.HT/2){
			//eating food takes 1 turn, instead of 3
			hero.spend(-2);
			//effectively 2/3 turns of haste
			Buff.affect( hero, Haste.class, 2+hero.pointsInTalent(INVIGORATING_MEAL));
			//TODO VFX
		}
	}

	public static float itemIDSpeedFactor( Hero hero, Item item ){
		// 1.5x/2x speed with huntress talent
		float factor = 1f + hero.pointsInTalent(SURVIVALISTS_INTUITION)/2f;

		//otherwise 2x/3x speed (also ID instantly though)
		if (item instanceof MeleeWeapon || item instanceof Armor){
			factor *= 1f + hero.pointsInTalent(ARMSMASTERS_INTUITION);
		}
		if (item instanceof Wand){
			factor *= 1f + hero.pointsInTalent(SCHOLARS_INTUITION);
		}
		if (item instanceof Ring){
			factor *= 1f + hero.pointsInTalent(THIEFS_INTUITION);
		}
		return factor;
	}

	public static void onItemEquipped( Hero hero, Item item ){
		if (hero.hasTalent(ARMSMASTERS_INTUITION) && (item instanceof Weapon || item instanceof Armor)){
			item.identify();
		}
		if (hero.hasTalent(THIEFS_INTUITION) && item instanceof Ring){
			if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
				item.identify();
			} else {
				((Ring) item).setKnown();
			}
		}
	}

	public static void onItemCollected( Hero hero, Item item ){
		if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (item instanceof Ring) ((Ring) item).setKnown();
		}
	}

	private static final int TALENT_TIERS = 1;

	public static void initClassTalents( Hero hero ){
		while (hero.talents.size() < TALENT_TIERS){
			hero.talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		//tier 1
		switch (hero.heroClass){
			case WARRIOR: default:
				Collections.addAll(tierTalents, HEARTY_MEAL, ARMSMASTERS_INTUITION, TEST_SUBJECT, IRON_WILL);
				break;
			case MAGE:
				Collections.addAll(tierTalents, ENERGIZING_MEAL, SCHOLARS_INTUITION, TEST_MAGE_3, TEST_MAGE_4);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, RATIONED_MEAL, THIEFS_INTUITION, TEST_ROGUE_3, TEST_ROGUE_4);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, INVIGORATING_MEAL, SURVIVALISTS_INTUITION, TEST_HUNTRESS_3, TEST_HUNTRESS_4);
				break;
		}
		for (Talent talent : tierTalents){
			hero.talents.get(0).put(talent, 0);
		}
		tierTalents.clear();

		//tier 2+
		//TBD
	}

	public static void initSubclassTalents( Hero hero ){
		//Nothing here yet. Hm.....
	}

	private static final String TALENTS = "talents";

	public static void storeTalentsInBundle( Bundle bundle, Hero hero ){
		Bundle talentBundle = new Bundle();

		for (Talent talent : values()){
			if (hero.hasTalent(talent)){
				talentBundle.put(talent.name(), hero.pointsInTalent(talent));
			}
		}

		bundle.put(TALENTS, talentBundle);
	}

	public static void restoreTalentsFromBundle( Bundle bundle, Hero hero ){
		if (hero.heroClass != null) initClassTalents(hero);
		if (hero.subClass != null)  initSubclassTalents(hero);

		if (!bundle.contains(TALENTS)) return;
		Bundle talentBundle = bundle.getBundle(TALENTS);

		for (Talent talent : values()){
			if (talentBundle.contains(talent.name())){
				for (LinkedHashMap<Talent, Integer> tier : hero.talents){
					if (tier.containsKey(talent)){
						tier.put(talent, talentBundle.getInt(talent.name()));
					}
				}
			}
		}
	}

}
