/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Explosive extends Weapon.Enchantment {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	private static ItemSprite.Glowing WARM = new ItemSprite.Glowing( 0x000000, 0.5f );
	private static ItemSprite.Glowing HOT = new ItemSprite.Glowing( 0x000000, 0.25f );
	private int durability = 100;

	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {

		//average value of 5, or 20 hits to an explosion
		int durToReduce = Math.round(Random.IntRange(0, 10) * procChanceMultiplier(attacker));
		int currentDurability = durability;
		durability -= durToReduce;

		if (currentDurability > 50 && durability <= 50){
			attacker.sprite.showStatus(CharSprite.WARNING, Messages.get(this, "warm"));
			GLog.w(Messages.get(this, "desc_warm"));
			attacker.sprite.emitter().burst(SmokeParticle.FACTORY, 4);
			Item.updateQuickslot();
		} else if (currentDurability > 10 && durability <= 10){
			attacker.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "hot"));
			GLog.n(Messages.get(this, "desc_hot"));
			attacker.sprite.emitter().burst(BlastParticle.FACTORY, 5);
			Item.updateQuickslot();
		} else if (durability <= 0) {
			//explosion position is the closest adjacent cell to the defender
			// this will be the attacker's position if they are adjacent
			int explosionPos = -1;
			for (int i : PathFinder.NEIGHBOURS8){
				if (!Dungeon.level.solid[defender.pos+i] &&
						(explosionPos == -1 ||
						Dungeon.level.trueDistance(attacker.pos, defender.pos+i) < Dungeon.level.trueDistance(attacker.pos, explosionPos))){
					explosionPos = defender.pos+i;
				}
			}

			new Bomb.MagicalBomb().explode(explosionPos);

			durability = 100;
			Item.updateQuickslot();
		}

		return damage;
	}

	@Override
	public boolean curse() {
		return true;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		if (durability > 50){
			return BLACK;
		} else if (durability > 10){
			return WARM;
		} else {
			return HOT;
		}
	}

	@Override
	public String desc() {
		String desc = super.desc();
		if (durability > 50){
			desc += " " + Messages.get(this, "desc_cool");
		} else if (durability > 10){
			desc += " " + Messages.get(this, "desc_warm");
		} else {
			desc += " _" + Messages.get(this, "desc_hot") + "_";
		}
		return desc;
	}

	private static final String DURABILITY = "durability";

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		durability = bundle.getInt(DURABILITY);
		//pre-1.3 saves
		if (durability <= 0){
			durability = 100;
		}
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put(DURABILITY, durability);
	}

}
