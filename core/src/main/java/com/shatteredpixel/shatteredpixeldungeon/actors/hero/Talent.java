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

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public enum Talent {

	TEST_WARRIOR_1(0),
	TEST_WARRIOR_2(1),
	TEST_WARRIOR_3(2),
	TEST_WARRIOR_4(3),

	TEST_MAGE_1(16),
	TEST_MAGE_2(17),
	TEST_MAGE_3(18),
	TEST_MAGE_4(19),

	TEST_ROGUE_1(32),
	TEST_ROGUE_2(33),
	TEST_ROGUE_3(34),
	TEST_ROGUE_4(35),

	TEST_HUNTRESS_1(48),
	TEST_HUNTRESS_2(49),
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

	private static final int TALENT_TIERS = 1;

	public static void initClassTalents( Hero hero ){
		while (hero.talents.size() < TALENT_TIERS){
			hero.talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		//tier 1
		switch (hero.heroClass){
			case WARRIOR: default:
				Collections.addAll(tierTalents, TEST_WARRIOR_1, TEST_WARRIOR_2, TEST_WARRIOR_3, TEST_WARRIOR_4);
				break;
			case MAGE:
				Collections.addAll(tierTalents, TEST_MAGE_1, TEST_MAGE_2, TEST_MAGE_3, TEST_MAGE_4);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, TEST_ROGUE_1, TEST_ROGUE_2, TEST_ROGUE_3, TEST_ROGUE_4);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, TEST_HUNTRESS_1, TEST_HUNTRESS_2, TEST_HUNTRESS_3, TEST_HUNTRESS_4);
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
