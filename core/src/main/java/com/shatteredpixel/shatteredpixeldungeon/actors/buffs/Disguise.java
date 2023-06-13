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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Disguise extends FlavourBuff {

	public static final float DURATION	= 40f;

	public int count = 0;
	{
		type = buffType.POSITIVE;
		announced = true;
	}
	
	@Override
	public boolean attachTo( Char target ) {

		if (super.attachTo( target )) {
			target.ignored++;
			if (target instanceof Mob){

			}
			return true;
		} else {
			return false;
		}
	}


	public void move () {
		if (Random.Float(0.9f) < iconFadePercent() && count < 1 && target instanceof Hero){

			((Hero)target).interrupt();
			Plant.Seed seed;
			seed = (Plant.Seed) Generator.random(Generator.Category.SEED);

			count++;

			target.sprite.highjump(target.pos, target.pos, 8 ,0.17f , new Callback() {
				@Override
				public void call() {
					((Hero)target).busy();
					((HeroSprite)target.sprite).dig();
					Sample.INSTANCE.play(Assets.Sounds.PLANT);

					if (Buff.affect(target, Talent.FindSeedCounter.class) != null
							&& Buff.affect(target, Talent.FindSeedCounter.class).count() < 2){
						Dungeon.level.drop(seed, target.pos).sprite.drop();
						GLog.p(Messages.get(Disguise.class, "find_seeds"), seed.name());
						//첫 사용에서 필득
					} else if (Random.Int(7) < 2) {
						Dungeon.level.drop(seed, target.pos).sprite.drop();
						GLog.p(Messages.get(Disguise.class, "find_seeds"), seed.name());

					} else {
						GLog.p(Messages.get(Disguise.class, "find_non"));
					}
				}
			});
		}
	}

	
	@Override
	public void detach() {
		if (target.ignored > 0)
		    {target.ignored--;}

		super.detach();

		CellEmitter.get( target.pos ).burst( Speck.factory( Speck.WOOL ), 10 );
		Sample.INSTANCE.play( Assets.Sounds.PUFF );
		if (target instanceof Hero) {
			((HeroSprite) target.sprite).updateArmor();
		}
		if (target instanceof Mob){

		}
	}

	public static float stealthFactor(Char owner , float stealth){
		//눈에 띄는 정도를 줄임.. 남은 지속시간 * 2.5
		float stealthPow = owner.buff(Disguise.class).DURATION;
		if (owner instanceof Hero && stealthPow > 0) {
			stealth += stealthPow * 2.5f;
		}
		return stealth;
	}



	@Override
	public int icon() {
		return BuffIndicator.IMBUE;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0.6f, 0f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}

	private static final String COUNT	= "count";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( COUNT, count);

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		count = bundle.getInt( COUNT );
	}

	public boolean ignoreNextHit = false;

	public void recover() {
		if (ignoreNextHit){
			ignoreNextHit = false;
			return;
		}
		spend(-5f);
		if (cooldown() <= 0){
			detach();
		}
	}
}
