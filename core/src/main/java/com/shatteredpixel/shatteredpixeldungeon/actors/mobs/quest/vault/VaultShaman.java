/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DwarfToken;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShamanSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class VaultShaman extends Shaman {

	{
		activateSteathGameplayBehaviour();
		spriteClass = ShamanSprite.Vault.class;

		defenseSkill = 18;

		maxLvl = 30;
		EXP = 0;
		loot = DwarfToken.class;
		lootChance = 1;
	}

	@Override
	public int attackSkill( Char target ) {
		return 25;
	}

	@Override
	public float lootChance() {
		return 1;
	}

	@Override
	public Item createLoot() {
		return new DwarfToken();
	}

	int type = Random.Int(5);

	@Override
	protected void debuff( Char enemy ) {
		switch (type){
			case 0: case 1:
				Buff.prolong( enemy, Weakness.class, Weakness.DURATION );
				break;
			case 2: case 3:
				Buff.prolong( enemy, Vulnerable.class, Vulnerable.DURATION );
				break;
			case 4:
				Buff.prolong( enemy, Hex.class, Hex.DURATION );
				break;
		}
	}

	@Override
	public int damageRoll() {
		//buff to melee damage, equal to a brute (no rage), as shamans are otherwise weak in melee
		return Random.NormalIntRange( 5, 25 );
	}

	@Override
	public int drRoll() {
		//buff to DR to help offset high hero HP and bonus dmg from excess str
		return super.drRoll() + 5;
	}

	public static final String TYPE = "type";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TYPE, type);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		type = bundle.getInt(TYPE);
	}
}
