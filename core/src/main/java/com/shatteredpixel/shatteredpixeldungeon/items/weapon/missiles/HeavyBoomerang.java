/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class HeavyBoomerang extends MissileWeapon {
	
	{
		image = ItemSpriteSheet.BOOMERANG;
		
		tier = 4;
		sticky = false;
	}
	
	@Override
	public int max(int lvl) {
		return  4 * tier +                  //16 base, down from 20
				(tier) * lvl;               //scaling unchanged
	}
	
	@Override
	protected void rangedHit(Char enemy, int cell) {
		decrementDurability();
		if (durability > 0){
			Buff.append(Dungeon.hero, CircleBack.class).setup(this, cell, Dungeon.hero.pos, Dungeon.depth);
		}
	}
	
	@Override
	protected void rangedMiss(int cell) {
		parent = null;
		Buff.append(Dungeon.hero, CircleBack.class).setup(this, cell, Dungeon.hero.pos, Dungeon.depth);
	}
	
	public static class CircleBack extends Buff {
		
		private MissileWeapon boomerang;
		private int thrownPos;
		private int returnPos;
		private int returnDepth;
		
		private int left;
		
		public void setup( MissileWeapon boomerang, int thrownPos, int returnPos, int returnDepth){
			this.boomerang = boomerang;
			this.thrownPos = thrownPos;
			this.returnPos = returnPos;
			this.returnDepth = returnDepth;
			left = 3;
		}
		
		@Override
		public boolean act() {
			if (returnDepth == Dungeon.depth){
				left--;
				if (left <= 0){
					final Char returnTarget = Actor.findChar(returnPos);
					MissileSprite visual = ((MissileSprite) Dungeon.hero.sprite.parent.recycle(MissileSprite.class));
					visual.reset( thrownPos,
									returnPos,
									boomerang,
									new Callback() {
										@Override
										public void call() {
											if (returnTarget == Dungeon.hero){
												boomerang.doPickUp(Dungeon.hero);
												//grabbing the boomerang takes no time
												Dungeon.hero.spend( -TIME_TO_PICK_UP );
												
											} else if (returnTarget != null){
												if (curUser.shoot( returnTarget, boomerang )) {
													boomerang.decrementDurability();
												}
												if (boomerang.durability > 0) {
													Dungeon.level.drop(boomerang, returnPos).sprite.drop();
												}
												
											} else {
												Dungeon.level.drop(boomerang, returnPos).sprite.drop();
											}
										}
									});
					visual.alpha(0f);
					float duration = Dungeon.level.trueDistance(thrownPos, returnPos) / 20f;
					Dungeon.hero.sprite.parent.add(new AlphaTweener(visual, 1f, duration));
					detach();
				}
			}
			spend( TICK );
			return true;
		}
		
		private static final String BOOMERANG = "boomerang";
		private static final String THROWN_POS = "thrown_pos";
		private static final String RETURN_POS = "return_pos";
		private static final String RETURN_DEPTH = "return_depth";
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(BOOMERANG, boomerang);
			bundle.put(THROWN_POS, thrownPos);
			bundle.put(RETURN_POS, returnPos);
			bundle.put(RETURN_DEPTH, returnDepth);
		}
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			boomerang = (MissileWeapon) bundle.get(BOOMERANG);
			thrownPos = bundle.getInt(THROWN_POS);
			returnPos = bundle.getInt(RETURN_POS);
			returnDepth = bundle.getInt(RETURN_DEPTH);
		}
	}
	
}
