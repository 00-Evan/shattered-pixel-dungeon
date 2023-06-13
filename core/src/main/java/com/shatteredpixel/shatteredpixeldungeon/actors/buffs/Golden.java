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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Golden extends Buff implements Hero.Doom {


	
	private static final float DURATION = 6f;
	
	private float left;
	private int golddropcount = 0;
	
	//for tracking golden of hero items
	private int goldenIncrement = 0;

	private static final String LEFT	  = "left";
	private static final String GOLDEN    = "goldenincrement";

	private static final String DROPCOUNT = "golddropcount";

	{
		type = buffType.NEGATIVE;
		announced = true;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
		bundle.put( GOLDEN, goldenIncrement );
		bundle.put( DROPCOUNT, golddropcount );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat( LEFT );
		goldenIncrement = bundle.getInt( GOLDEN );
		golddropcount = bundle.getInt( DROPCOUNT );
	}

	@Override
	public boolean act() {
		
		if (target.isAlive() && !target.isImmune(getClass())) {
			
			int damage = Random.NormalIntRange( 1, 1 + Dungeon.scalingDepth() / 5 );

			if (target instanceof Hero && target.buff(TimekeepersHourglass.timeStasis.class) == null) {
				
				Hero hero = (Hero)target;
				if (hero.buff(Goldlize.class) == null) {
					hero.damage(damage, this);
				}

				if (golddropcount < 5 + Dungeon.scalingDepth() / 5) {
					Dungeon.level.drop(new Gold(damage), target.pos).sprite.drop();
				}

				golddropcount += damage;
				goldenIncrement++;

				if (Random.Int(3) < (goldenIncrement - 3)) {
					goldenIncrement = 0;
					ArrayList<Item> goldable = new ArrayList<>();
					//가방 안 랜덤 아이템 황금화
					if (hero.buff(LostInventory.class) == null) {
						for (Item i : hero.belongings.backpack.items) {
							if (!i.unique && i.value() > 0 && !(i instanceof MissileWeapon)) {
								goldable.add(i);
							}
						}
					}
					if (!goldable.isEmpty()){
						Item Golden = Random.element(goldable).detach(hero.belongings.backpack);
						GLog.w( Messages.capitalize(Messages.get(this, "item_to_gold", Golden.title())) );

						Dungeon.level.drop( new Gold(Golden.value()), hero.pos ).sprite.drop();

						Heap.bubbleFX( hero.pos );
					}
				}


			} else {
				target.damage( damage, this );
				if (golddropcount < 5 + Dungeon.scalingDepth() / 5) {
					Dungeon.level.drop(new Gold(damage), target.pos).sprite.drop();
				}

				golddropcount += damage;
			}


			if (target instanceof Thief && ((Thief) target).item != null) {

				Item item = ((Thief) target).item;

				if (!item.unique && item.value() > 0 &&!(item instanceof MissileWeapon)) {
					CellEmitter.get( target.pos ).burst( Speck.factory( Speck.BUBBLE ), 5 );
					Dungeon.level.drop(new Gold(item.value()),target.pos).sprite.drop();
					((Thief)target).item = null;
				}
				if (item instanceof MissileWeapon){
					((MissileWeapon)item).repair(Random.Int( 1, 1 + Dungeon.scalingDepth() / 5 ));
				}
			}

		} else {

			detach();
		}

		spend( TICK );
		left -= TICK;
		
		if (left <= 0 ||
			(Dungeon.level.water[target.pos] && !target.flying)) {
			
			detach();
		}
		
		return true;
	}

	public void set( float duration ) {
		this.left = duration;
	}

	@Override
	public int icon() {
		return BuffIndicator.GOLDEN_BLOOD;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - left) / DURATION);
	}



	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)left);
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.GOLDEN);
		else target.sprite.remove(CharSprite.State.GOLDEN);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left));
	}

	@Override
	public void onDeath() {
		
		Badges.validateDeathFromGolden();
		
		Dungeon.fail( getClass() );
		GLog.n( Messages.get(this, "ondeath") );
	}

	//황금 액체가 몸에서 떨어지도록 이펙트
	public static class goldParticle extends PixelParticle {

		public static final Emitter.Factory FACTORY = new Emitter.Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((goldParticle)emitter.recycle( goldParticle.class )).reset( x, y );}
		};

		public static final Emitter.Factory UP = new Emitter.Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((goldParticle)emitter.recycle( goldParticle.class )).resetUp( x, y );}
		};


		public goldParticle() {
			super();
			speed.set( 0, Random.Float( 5, 8 ) );
			lifespan = 1.2f;
		}

		public void reset( float x, float y ) {
			revive();

			this.x = x;
			this.y = y - speed.y * lifespan;

			left = lifespan;
		}

		public void resetUp( float x, float y ) {
			revive();

			speed.set( Random.Float( -8, +8 ), Random.Float( -12, -18 ) );
			this.x = x;
			this.y = y;

			size = 6;
			left = lifespan = 1f;
		}

		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			color( ColorMath.interpolate( 0xFFFF00, 0xFFFFDE, p ) );
			am = (p < 0.5f ? p : 1 - p) * 1.5f;
		}


	}
}
