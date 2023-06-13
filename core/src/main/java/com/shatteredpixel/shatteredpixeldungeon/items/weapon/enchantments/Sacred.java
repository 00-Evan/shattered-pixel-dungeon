/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Sacred extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing SILVER = new ItemSprite.Glowing( 0xDFFFFC );
	
	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, weapon.buffedLvl() );
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		float procChance = (level+1f)/(level+4f) * procChanceMultiplier(attacker);
		if (Random.Float() < procChance) {

			float powerMulti = Math.max(1f, procChance);

			if (defender.properties().contains(Char.Property.DEMONIC) || defender.properties().contains(Char.Property.UNDEAD)) {
				defender.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + level);
				Sample.INSTANCE.play(Assets.Sounds.BURNING);

				defender.damage(Math.round(damage * 0.333f * powerMulti), this);
			}


			// 2/3 확률로 발광, 1/3 확률로 축복
			if (Random.Int(2)>0) {
				Buff.prolong(attacker, Light.class,10+weapon.buffedLvl());
			}
			else {	Buff.prolong(attacker, Bless.class, 2 + weapon.buffedLvl());}
		new Flare( 6, 32 ).color(0xFFFFFF, true).show( attacker.sprite, 1f );
		Sample.INSTANCE.play( Assets.Sounds.RAY );

		}

		return damage;

	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return SILVER;
	}
}
