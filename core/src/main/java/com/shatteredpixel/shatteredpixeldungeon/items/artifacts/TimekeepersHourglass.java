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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TimekeepersHourglass extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_HOURGLASS;

		levelCap = 5;

		charge = 5+level();
		partialCharge = 0;
		chargeCap = 5+level();

		defaultAction = AC_ACTIVATE;
	}

	public static final String AC_ACTIVATE = "ACTIVATE";

	//keeps track of generated sandbags.
	public int sandBags = 0;

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero )
				&& !cursed
				&& hero.buff(MagicImmune.class) == null
				&& (charge > 0 || activeBuff != null)) {
			actions.add(AC_ACTIVATE);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals(AC_ACTIVATE)){

			if (!isEquipped( hero ))        GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (activeBuff != null) {
				if (activeBuff instanceof timeStasis) { //do nothing
				} else {
					activeBuff.detach();
					GLog.i( Messages.get(this, "deactivate") );
				}
			} else if (charge <= 0)         GLog.i( Messages.get(this, "no_charge") );
			else if (cursed)                GLog.i( Messages.get(this, "cursed") );
			else GameScene.show(
						new WndOptions(new ItemSprite(this),
								Messages.titleCase(name()),
								Messages.get(this, "prompt"),
								Messages.get(this, "stasis"),
								Messages.get(this, "freeze")) {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									GLog.i( Messages.get(TimekeepersHourglass.class, "onstasis") );
									GameScene.flash(0x80FFFFFF);
									Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

									activeBuff = new timeStasis();
									Talent.onArtifactUsed(Dungeon.hero);
									activeBuff.attachTo(Dungeon.hero);
								} else if (index == 1) {
									GLog.i( Messages.get(TimekeepersHourglass.class, "onfreeze") );
									GameScene.flash(0x80FFFFFF);
									Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

									Invisibility.dispel(Dungeon.hero);
									activeBuff = new timeFreeze();
									Talent.onArtifactUsed(Dungeon.hero);
									activeBuff.attachTo(Dungeon.hero);
									charge--;
									((timeFreeze)activeBuff).processTime(0f);
								}
							}
						}
				);
		}
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		if (activeBuff != null)
			activeBuff.attachTo(ch);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			if (activeBuff != null){
				activeBuff.detach();
				activeBuff = null;
			}
			return true;
		} else
			return false;
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new hourglassRecharge();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null){
			partialCharge += 0.25f*amount;
			if (partialCharge >= 1){
				partialCharge--;
				charge++;
				updateQuickslot();
			}
		}
	}

	@Override
	public Item upgrade() {
		chargeCap+= 1;

		//for artifact transmutation.
		while (level()+1 > sandBags)
			sandBags ++;

		return super.upgrade();
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if (isEquipped( Dungeon.hero )){
			if (!cursed) {
				if (level() < levelCap )
					desc += "\n\n" + Messages.get(this, "desc_hint");

			} else
				desc += "\n\n" + Messages.get(this, "desc_cursed");
		}
		return desc;
	}


	private static final String SANDBAGS =  "sandbags";
	private static final String BUFF =      "buff";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( SANDBAGS, sandBags );

		if (activeBuff != null)
			bundle.put( BUFF , activeBuff );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		sandBags = bundle.getInt( SANDBAGS );

		//these buffs belong to hourglass, need to handle unbundling within the hourglass class.
		if (bundle.contains( BUFF )){
			Bundle buffBundle = bundle.getBundle( BUFF );

			if (buffBundle.contains( timeFreeze.PRESSES ))
				activeBuff = new timeFreeze();
			else
				activeBuff = new timeStasis();

			activeBuff.restoreFromBundle(buffBundle);
		}
	}

	public class hourglassRecharge extends ArtifactBuff {
		@Override
		public boolean act() {

			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap
					&& !cursed
					&& target.buff(MagicImmune.class) == null
					&& (lock == null || lock.regenOn())) {
				//90 turns to charge at full, 60 turns to charge at 0/10
				float chargeGain = 1 / (90f - (chargeCap - charge)*3f);
				chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
				partialCharge += chargeGain;

				if (partialCharge >= 1) {
					partialCharge --;
					charge ++;

					if (charge == chargeCap){
						partialCharge = 0;
					}
				}
			} else if (cursed && Random.Int(10) == 0)
				((Hero) target).spend( TICK );

			updateQuickslot();

			spend( TICK );

			return true;
		}
	}

	public class timeStasis extends ArtifactBuff {
		
		{
			type = buffType.POSITIVE;
			actPriority = BUFF_PRIO-3; //acts after all other buffs, so they are prevented
		}

		@Override
		public boolean attachTo(Char target) {

			if (super.attachTo(target)) {

				Invisibility.dispel();

				int usedCharge = Math.min(charge, 2);
				//buffs always act last, so the stasis buff should end a turn early.
				spend(5*usedCharge);

				//shouldn't punish the player for going into stasis frequently
				Hunger hunger = Buff.affect(target, Hunger.class);
				if (hunger != null && !hunger.isStarving()) {
					hunger.satisfy(5 * usedCharge);
				}

				charge -= usedCharge;

				target.invisible++;
				target.paralysed++;
				target.next();

				updateQuickslot();

				if (Dungeon.hero != null) {
					Dungeon.observe();
				}

				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean act() {
			detach();
			return true;
		}

		@Override
		public void detach() {
			if (target.invisible > 0) target.invisible--;
			if (target.paralysed > 0) target.paralysed--;
			super.detach();
			activeBuff = null;
			Dungeon.observe();
		}

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add( CharSprite.State.INVISIBLE );
			else if (target.invisible == 0) target.sprite.remove( CharSprite.State.INVISIBLE );
		}
	}

	public class timeFreeze extends ArtifactBuff {
		
		{
			type = buffType.POSITIVE;
		}

		float turnsToCost = 2f;

		ArrayList<Integer> presses = new ArrayList<>();

		public void processTime(float time){
			turnsToCost -= time;

			//use 1/1,000 to account for rounding errors
			while (turnsToCost < -0.001f){
				turnsToCost += 2f;
				charge --;
			}

			updateQuickslot();

			if (charge < 0 || charge == 0 && turnsToCost <= 0){
				charge = 0;
				detach();
			}

		}

		public void setDelayedPress(int cell){
			if (!presses.contains(cell))
				presses.add(cell);
		}

		public void triggerPresses(){
			for (int cell : presses){
				Trap t = Dungeon.level.traps.get(cell);
				if (t != null){
					t.trigger();
				}
				Plant p = Dungeon.level.plants.get(cell);
				if (p != null){
					p.trigger();
				}
			}

			presses = new ArrayList<>();
		}

		public void disarmPresses(){
			for (int cell : presses){
				Trap t = Dungeon.level.traps.get(cell);
				if (t != null && t.disarmedByActivation) {
					t.disarm();
				}

				Dungeon.level.uproot(cell);
			}

			presses = new ArrayList<>();
		}

		@Override
		public void detach(){
			updateQuickslot();
			super.detach();
			activeBuff = null;
			triggerPresses();
			target.next();
		}

		@Override
		public void fx(boolean on) {
			if (!(target instanceof Hero)) return;
			Emitter.freezeEmitters = on;
			if (on){
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (mob.sprite != null) mob.sprite.add(CharSprite.State.PARALYSED);
				}
			} else {
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (mob.paralysed <= 0) mob.sprite.remove(CharSprite.State.PARALYSED);
				}
			}
		}

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1f, 0.5f, 0);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (2f - turnsToCost) / 2f);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString((int)turnsToCost);
		}

		private static final String PRESSES = "presses";
		private static final String TURNSTOCOST = "turnsToCost";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			int[] values = new int[presses.size()];
			for (int i = 0; i < values.length; i ++)
				values[i] = presses.get(i);
			bundle.put( PRESSES , values );

			bundle.put( TURNSTOCOST , turnsToCost);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			int[] values = bundle.getIntArray( PRESSES );
			for (int value : values)
				presses.add(value);

			turnsToCost = bundle.getFloat( TURNSTOCOST );
		}
	}

	public static class sandBag extends Item {

		{
			image = ItemSpriteSheet.SANDBAG;
		}

		@Override
		public boolean doPickUp(Hero hero, int pos) {
			TimekeepersHourglass hourglass = hero.belongings.getItem( TimekeepersHourglass.class );
			if (hourglass != null && !hourglass.cursed) {
				hourglass.upgrade();
				Sample.INSTANCE.play( Assets.Sounds.DEWDROP );
				if (hourglass.level() == hourglass.levelCap)
					GLog.p( Messages.get(this, "maxlevel") );
				else
					GLog.i( Messages.get(this, "levelup") );
				GameScene.pickUp(this, pos);
				hero.spendAndNext(TIME_TO_PICK_UP);
				return true;
			} else {
				GLog.w( Messages.get(this, "no_hourglass") );
				return false;
			}
		}

		@Override
		public int value() {
			return 30;
		}

		@Override
		public boolean isUpgradable() {
			return false;
		}

		@Override
		public boolean isIdentified() {
			return true;
		}
	}


}
