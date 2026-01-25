/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class Flail extends MeleeWeapon {

    //private static int spinBoost = 0;

    {
        image = ItemSpriteSheet.FLAIL;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 4;
        ACC = 0.8f; //0.8x accuracy
        //also cannot surprise attack, see Hero.canSurpriseAttack
    }

    @Override
    public int max(int lvl) {
        return Math.round(7 * (tier + 1)) +        //35 base, up from 25
                lvl * Math.round(1.6f * (tier + 1));  //+8 per level, up from +5
    }

//    @Override
//    public int damageRoll(Char owner) {
//        int dmg = super.damageRoll(owner) + spinBoost;
//       if (spinBoost > 0) Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
//       spinBoost = 0;
//        return dmg;
//    }

//    @Override
//    public float accuracyFactor(Char owner, Char target) {
//		SpinAbilityTracker spin = owner.buff(SpinAbilityTracker.class);
//		if (spin != null) {
//			Actor.add(new Actor() {
//				{ actPriority = VFX_PRIO; }
//				@Override
//				protected boolean act() {
//					if (owner instanceof Hero && !target.isAlive()){
//						onAbilityKill((Hero)owner, target);
//					}
//					Actor.remove(this);
//					return true;
//				}
//			});
//			//we detach and calculate bonus here in case the attack misses (e.g. vs. monks)
//			spin.detach();
//			//+(8+2*lvl) damage per spin, roughly +40% base damage, +45% scaling
//			// so +120% base dmg, +135% scaling at 3 spins
//			spinBoost = spin.spins * augment.damageFactor(8 + 2*buffedLvl());
//			return Float.POSITIVE_INFINITY;
//        } else {
//        spinBoost = 0;
//        return super.accuracyFactor(owner, target);
//        }
//    }


//    public String upgradeAbilityStat(int level) {
//        return "+" + augment.damageFactor(8 + 2 * level);
//    }

//	public static class SpinAbilityTracker extends FlavourBuff {
//
//		{
//			type = buffType.POSITIVE;
//		}
//
//		public int spins = 0;
//
//		@Override
//		public int icon() {
//			return BuffIndicator.DUEL_SPIN;
//		}
//
//		@Override
//		public void tintIcon(Image icon) {
//			switch (spins){
//				case 1: default:
//					icon.hardlight(0, 1, 0);
//					break;
//				case 2:
//					icon.hardlight(1, 1, 0);
//					break;
//				case 3:
//					icon.hardlight(1, 0, 0);
//					break;
//			}
//		}
//
//		@Override
//		public float iconFadePercent() {
//			return Math.max(0, (3 - visualcooldown()) / 3);
//		}
//
//		@Override
//		public String desc() {
//			return Messages.get(this, "desc", (int)Math.round((spins/3f)*100f), dispTurns());
//		}
//
//		public static String SPINS = "spins";
//
//		@Override
//		public void storeInBundle(Bundle bundle) {
//			super.storeInBundle(bundle);
//			bundle.put(SPINS, spins);
//		}
//
//		@Override
//		public void restoreFromBundle(Bundle bundle) {
//			super.restoreFromBundle(bundle);
//			spins = bundle.getInt(SPINS);
//		}
//	}
}
