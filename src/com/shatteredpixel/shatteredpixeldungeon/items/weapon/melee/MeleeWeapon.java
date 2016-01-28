/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class MeleeWeapon extends Weapon {
	
	private int tier;
	
	public MeleeWeapon( int tier, float acu, float dly ) {
		super();
		
		this.tier = tier;
		
		ACU = acu;
		DLY = dly;
		
		STR = typicalSTR();

	}
	
	protected int minBase() {
		return tier;
	}

	protected int maxBase() {
		return (int)((tier * tier - tier + 10) / ACU * DLY);
	}

	@Override
	public int min() {
		return minBase() + level();
	}

	@Override
	public int max() {
		return maxBase() + level() * tier;
	}

	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean enchant ) {
		STR--;
		
		return super.upgrade( enchant );
	}
	
	public Item safeUpgrade() {
		return upgrade( enchantment != null );
	}
	
	@Override
	public Item degrade() {
		STR++;
		return super.degrade();
	}
	
	public int typicalSTR() {
		return 8 + tier * 2;
	}
	
	@Override
	public String info() {
		String name = name();
		
		String info = desc();

		info += "\n\n" + Messages.get(MeleeWeapon.class, "tier", tier);

		if (levelKnown) {
			int min = min();
			int max = max();
			float dmgfactor = (imbue == Imbue.LIGHT ? 0.7f : imbue == Imbue.HEAVY ? 1.5f : 1);
			info += " " + Messages.get(Weapon.class, "avg_dmg", Math.round((min + (max - min) / 2)*dmgfactor));
		} else {
			int min = minBase();
			int max = maxBase();
			info += " " + Messages.get(MeleeWeapon.class, "unknown", (min + (max - min) / 2), typicalSTR());
			if (typicalSTR() > Dungeon.hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}
		}

		switch (imbue) {
			case LIGHT:
				info += " " + Messages.get(Weapon.class, "lighter");
				break;
			case HEAVY:
				info += " " + Messages.get(Weapon.class, "heavier");
				break;
			case NONE:
		}

		String stats_desc = Messages.get(this, "stats_desc");
		if (!stats_desc.equals("")) info+= "\n\n" + stats_desc;

		if (levelKnown && STR > Dungeon.hero.STR()) {
			info += "\n\n" + Messages.get(Weapon.class, "too_heavy");
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "cursed");
		}
		
		return info;
	}
	
	@Override
	public int price() {
		int price = 20 * (1 << (tier - 1));
		if (enchantment != null) {
			price *= 1.5;
		}
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
	
	@Override
	public Item random() {
		super.random();
		
		if (Random.Int( 10 + level() ) == 0) {
			enchant();
		}
		
		return this;
	}
}
