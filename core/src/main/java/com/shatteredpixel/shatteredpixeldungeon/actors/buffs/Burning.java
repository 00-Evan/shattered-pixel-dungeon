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

package com.elementalpixel.elementalpixeldungeon.actors.buffs;


import com.elementalpixel.elementalpixeldungeon.Badges;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.actors.Char;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.Blob;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.Fire;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.actors.hero.HeroSubClass;
import com.elementalpixel.elementalpixeldungeon.actors.mobs.Thief;
import com.elementalpixel.elementalpixeldungeon.effects.particles.ElmoParticle;
import com.elementalpixel.elementalpixeldungeon.items.Heap;
import com.elementalpixel.elementalpixeldungeon.items.Item;
import com.elementalpixel.elementalpixeldungeon.items.food.ChargrilledMeat;
import com.elementalpixel.elementalpixeldungeon.items.food.FrozenCarpaccio;
import com.elementalpixel.elementalpixeldungeon.items.food.MysteryMeat;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.Scroll;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.scenes.GameScene;
import com.elementalpixel.elementalpixeldungeon.sprites.CharSprite;
import com.elementalpixel.elementalpixeldungeon.ui.BuffIndicator;
import com.elementalpixel.elementalpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.elementalpixel.elementalpixeldungeon.items.Item.curUser;

public class Burning extends Buff implements Hero.Doom {
	
	public static final float DURATION = 8f;
	
	private float left;
	
	//for tracking burning of hero items
	private int burnIncrement = 0;
	
	private static final String LEFT	= "left";
	private static final String BURN	= "burnIncrement";

	{
		type = buffType.NEGATIVE;
		announced = true;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
		bundle.put( BURN, burnIncrement );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat( LEFT );
		burnIncrement = bundle.getInt( BURN );
	}

	@Override
	public boolean attachTo(Char target) {
		Buff.detach( target, Chill.class);

		return super.attachTo(target);
	}

	@Override
	public boolean act() {
		if (curUser.subClass == HeroSubClass.ELEMENTALIST) {
			if (target.isAlive() && !target.isImmune(getClass())) {

				int damage = Random.NormalIntRange( 1, 3 + Dungeon.depth/4 );
				Buff.detach( target, Chill.class);

				if (target instanceof Hero) {


					Hero hero = (Hero)target;
					hero.HP += damage / 2;

					if (hero.HP > hero.HT) hero.HP = hero.HT;
					burnIncrement++;

				} else {
					target.damage( damage, this );
				}

				if (target instanceof Thief && ((Thief) target).item != null) {

					Item item = ((Thief) target).item;

					if (!item.unique && item instanceof Scroll) {
						target.sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
						((Thief)target).item = null;
					} else if (item instanceof MysteryMeat) {
						target.sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
						((Thief)target).item = new ChargrilledMeat();
					}

				}

			} else {

				detach();
			}

			if (Dungeon.level.flamable[target.pos] && Blob.volumeAt(target.pos, Fire.class) == 0) {
				GameScene.add( Blob.seed( target.pos, 4, Fire.class ) );
			}

			spend( TICK );
			left -= TICK;

			if (left <= 0 ||
					(Dungeon.level.water[target.pos] && !target.flying)) {

				detach();
			}

			return true;

		} else {
			if (target.isAlive() && !target.isImmune(getClass())) {

				int damage = Random.NormalIntRange( 1, 3 + Dungeon.depth/4 );
				Buff.detach( target, Chill.class);

				if (target instanceof Hero) {


					Hero hero = (Hero)target;
					hero.damage( damage, this );
					burnIncrement++;

					//at 4+ turns, there is a (turns-3)/3 chance an item burns
					if (Random.Int(3) < (burnIncrement - 3)){
						burnIncrement = 0;

						ArrayList<Item> burnable = new ArrayList<>();
						//does not reach inside of containers
						for (Item i : hero.belongings.backpack.items){
							if (!i.unique && (i instanceof Scroll || i instanceof MysteryMeat || i instanceof FrozenCarpaccio)){
								burnable.add(i);
							}
						}

						if (!burnable.isEmpty()){
							Item toBurn = Random.element(burnable).detach(hero.belongings.backpack);
							GLog.w( Messages.get(this, "burnsup", Messages.capitalize(toBurn.toString())) );
							if (toBurn instanceof MysteryMeat || toBurn instanceof FrozenCarpaccio){
								ChargrilledMeat steak = new ChargrilledMeat();
								if (!steak.collect( hero.belongings.backpack )) {
									Dungeon.level.drop( steak, hero.pos ).sprite.drop();
								}
							}
							Heap.burnFX( hero.pos );
						}
					}

				} else {
					target.damage( damage, this );
				}

				if (target instanceof Thief && ((Thief) target).item != null) {

					Item item = ((Thief) target).item;

					if (!item.unique && item instanceof Scroll) {
						target.sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
						((Thief)target).item = null;
					} else if (item instanceof MysteryMeat) {
						target.sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
						((Thief)target).item = new ChargrilledMeat();
					}

				}

			} else {

				detach();
			}

			if (Dungeon.level.flamable[target.pos] && Blob.volumeAt(target.pos, Fire.class) == 0) {
				GameScene.add( Blob.seed( target.pos, 4, Fire.class ) );
			}

			spend( TICK );
			left -= TICK;

			if (left <= 0 ||
					(Dungeon.level.water[target.pos] && !target.flying)) {

				detach();
			}

			return true;
		}

	}

	public void reignite( Char ch ) {
		reignite( ch, DURATION );
	}
	
	public void reignite( Char ch, float duration ) {
		left = duration;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FIRE;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - left) / DURATION);
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.BURNING);
		else target.sprite.remove(CharSprite.State.BURNING);
	}

	@Override
	public String heroMessage() {
		return Messages.get(this, "heromsg");
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left));
	}

	@Override
	public void onDeath() {
		
		Badges.validateDeathFromFire();
		
		Dungeon.fail( getClass() );
		GLog.n( Messages.get(this, "ondeath") );
	}
}
