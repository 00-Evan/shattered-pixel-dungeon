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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.effects.Surprise;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MasterThievesArmband extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_ARMBAND;

		levelCap = 10;

		charge = 0;
		partialCharge = 0;
		chargeCap = 5+level()/2;

		defaultAction = AC_STEAL;
	}

	public static final String AC_STEAL = "STEAL";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero)
				&& charge > 0
				&& hero.buff(MagicImmune.class) == null
				&& !cursed) {
			actions.add(AC_STEAL);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals(AC_STEAL)){

			curUser = hero;

			if (!isEquipped( hero )) {
				GLog.i( Messages.get(Artifact.class, "need_to_equip") );
				usesTargeting = false;

			} else if (charge < 1) {
				GLog.i( Messages.get(this, "no_charge") );
				usesTargeting = false;

			} else if (cursed) {
				GLog.w( Messages.get(this, "cursed") );
				usesTargeting = false;

			} else {
				usesTargeting = true;
				GameScene.selectCell(targeter);
			}

		}
	}

	private CellSelector.Listener targeter = new CellSelector.Listener(){

		@Override
		public void onSelect(Integer target) {

			if (target == null) {
				return;
			} else if (!Dungeon.level.adjacent(curUser.pos, target) || Actor.findChar(target) == null){
				GLog.w( Messages.get(MasterThievesArmband.class, "no_target") );
			} else {
				Char ch = Actor.findChar(target);
				if (ch instanceof Shopkeeper){
					GLog.w( Messages.get(MasterThievesArmband.class, "steal_shopkeeper") );
				} else if (ch.alignment != Char.Alignment.ENEMY){
					GLog.w( Messages.get(MasterThievesArmband.class, "no_target") );
				} else if (ch instanceof Mob) {
					curUser.busy();
					curUser.sprite.attack(target, new Callback() {
						@Override
						public void call() {
							Sample.INSTANCE.play(Assets.Sounds.HIT);

							boolean surprised = ((Mob) ch).surprisedBy(curUser, false);
							float lootMultiplier = 1f + 0.1f*level();
							int debuffDuration = 3 + level()/2;

							Invisibility.dispel(curUser);

							if (surprised){
								lootMultiplier += 0.5f;
								Surprise.hit(ch);
								Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
								debuffDuration += 2;
								exp += 2;
							}

							float lootChance = ((Mob) ch).lootChance() * lootMultiplier;

							if (Dungeon.hero.lvl > ((Mob) ch).maxLvl + 2) {
								lootChance = 0;
							} else if (ch.buff(StolenTracker.class) != null){
								lootChance = 0;
							}

							if (lootChance == 0){
								GLog.w(Messages.get(MasterThievesArmband.class, "no_steal"));
							} else if (Random.Float() <= lootChance){
								Item loot = ((Mob) ch).createLoot();
								if (Challenges.isItemBlocked(loot)){
									GLog.i(Messages.get(MasterThievesArmband.class, "failed_steal"));
									Buff.affect(ch, StolenTracker.class).setItemStolen(false);
								} else {
									if (loot.doPickUp(curUser)) {
										//item collection happens instantly
										curUser.spend(-TIME_TO_PICK_UP);
									} else {
										Dungeon.level.drop(loot, curUser.pos).sprite.drop();
									}
									GLog.i(Messages.get(MasterThievesArmband.class, "stole_item", loot.name()));
									Buff.affect(ch, StolenTracker.class).setItemStolen(true);
								}
							} else {
								GLog.i(Messages.get(MasterThievesArmband.class, "failed_steal"));
								Buff.affect(ch, StolenTracker.class).setItemStolen(false);
							}

							Buff.prolong(ch, Blindness.class, debuffDuration);
							Buff.prolong(ch, Cripple.class, debuffDuration);

							charge--;
							exp += 3;
							Talent.onArtifactUsed(Dungeon.hero);
							while (exp >= (10 + Math.round(3.33f * level())) && level() < levelCap) {
								exp -= 10 + Math.round(3.33f * level());
								GLog.p(Messages.get(MasterThievesArmband.class, "level_up"));
								upgrade();
							}
							Item.updateQuickslot();
							curUser.next();
						}
					});

				}
			}

		}

		@Override
		public String prompt() {
			return Messages.get(MasterThievesArmband.class, "prompt");
		}
	};

	//counter of 0 for attempt but no success, 1 for success
	public static class StolenTracker extends CounterBuff {
		public void setItemStolen(boolean stolen){ if (stolen) countUp(1); }
		public boolean itemWasStolen(){ return count() > 0; }
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Thievery();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;
		partialCharge += 0.1f * amount;
		partialCharge = Math.min(partialCharge, chargeCap - charge);
		while (partialCharge >= 1f){
			charge++;
			partialCharge--;
			updateQuickslot();
			if (charge == chargeCap){
				GLog.p( Messages.get(MasterThievesArmband.class, "full") );
			}
		}
	}

	@Override
	public Item upgrade() {
		chargeCap = 5 + (level()+1)/2;
		return super.upgrade();
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if ( isEquipped (Dungeon.hero) ){
			if (cursed){
				desc += "\n\n" + Messages.get(this, "desc_cursed");
			} else {
				desc += "\n\n" + Messages.get(this, "desc_worn");
			}
		}

		return desc;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		//conversion for old armband on pre-1.2.0 saves
		if (exp > Math.round(10 + level()*3.33f)){
			exp = 0;
		}
	}

	public class Thievery extends ArtifactBuff {

		@Override
		public boolean act() {
			if (cursed && Dungeon.gold > 0 && Random.Int(5) == 0){
				Dungeon.gold--;
			}

			spend(TICK);
			return true;
		}

		public void gainCharge(float levelPortion) {
			if (cursed || target.buff(MagicImmune.class) != null) return;

			if (charge < chargeCap){
				float chargeGain = 3f * levelPortion;
				chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);

				partialCharge += chargeGain;
				while (partialCharge > 1f){
					partialCharge--;
					charge++;
					updateQuickslot();

					if (charge == chargeCap){
						GLog.p( Messages.get(MasterThievesArmband.class, "full") );
						partialCharge = 0;
					}
				}

			} else {
				partialCharge = 0f;
			}
		}
		
		public boolean steal(Item item){
			int chargesUsed = chargesToUse(item);
			float stealChance = stealChance(item);
			if (Random.Float() > stealChance){
				return false;
			} else {
				charge -= chargesUsed;
				exp += 4 * chargesUsed;
				GLog.i(Messages.get(MasterThievesArmband.class, "stole_item", item.name()));

				Talent.onArtifactUsed(Dungeon.hero);
				while (exp >= (10 + Math.round(3.33f * level())) && level() < levelCap) {
					exp -= 10 + Math.round(3.33f * level());
					GLog.p(Messages.get(MasterThievesArmband.class, "level_up"));
					upgrade();
				}
				return true;
			}
		}

		public float stealChance(Item item){
			int chargesUsed = chargesToUse(item);
			float val = chargesUsed * (10 + level()/2f);
			return Math.min(1f, val/item.value());
		}

		public int chargesToUse(Item item){
			int value = item.value();
			float valUsing = 0;
			int chargesUsed = 0;
			while (valUsing < value && chargesUsed < charge){
				valUsing += 10 + level()/2f;
				chargesUsed++;
			}
			return chargesUsed;
		}
	}


}
