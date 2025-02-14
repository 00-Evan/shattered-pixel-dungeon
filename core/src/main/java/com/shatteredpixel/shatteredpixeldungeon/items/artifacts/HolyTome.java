/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndClericSpells;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class HolyTome extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_TOME;

		exp = 0;
		levelCap = 10;

		charge = Math.min(level()+3, 10);
		partialCharge = 0;
		chargeCap = Math.min(level()+3, 10);

		defaultAction = AC_CAST;

		unique = true;
		bones = false;
	}

	public static final String AC_CAST = "CAST";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if ((isEquipped( hero ) || hero.hasTalent(Talent.LIGHT_READING))
				&& !cursed
				&& hero.buff(MagicImmune.class) == null) {
			actions.add(AC_CAST);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals(AC_CAST)) {

			if (!isEquipped(hero) && !hero.hasTalent(Talent.LIGHT_READING)) GLog.i(Messages.get(Artifact.class, "need_to_equip"));
			else if (cursed)       GLog.i( Messages.get(this, "cursed") );
			else {

				GameScene.show(new WndClericSpells(this, hero, false));

			}

		}
	}

	//used to ensure tome has variable targeting logic for whatever spell is being case
	public ClericSpell targetingSpell = null;

	@Override
	public int targetingPos(Hero user, int dst) {
		if (targetingSpell == null || targetingSpell.targetingFlags() == -1) {
			return super.targetingPos(user, dst);
		} else {
			return new Ballistica( user.pos, dst, targetingSpell.targetingFlags() ).collisionPos;
		}
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			if (collect && hero.hasTalent(Talent.LIGHT_READING)){
				activate(hero);
			}

			return true;
		} else
			return false;
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)){
			if (container.owner instanceof Hero
					&& passiveBuff == null
					&& ((Hero) container.owner).hasTalent(Talent.LIGHT_READING)){
				activate((Hero) container.owner);
			}
			return true;
		} else{
			return false;
		}
	}

	@Override
	protected void onDetach() {
		if (passiveBuff != null){
			passiveBuff.detach();
			passiveBuff = null;
		}
	}

	public boolean canCast( Hero hero, ClericSpell spell ){
		return (isEquipped(hero) || (Dungeon.hero.hasTalent(Talent.LIGHT_READING) && hero.belongings.contains(this)))
				&& hero.buff(MagicImmune.class) == null
				&& charge >= spell.chargeUse(hero)
				&& spell.canCast(hero);
	}

	public void spendCharge( float chargesSpent ){
		partialCharge -= chargesSpent;
		while (partialCharge < 0){
			charge--;
			partialCharge++;
		}

		//target hero level is 1 + 2*tome level
		int lvlDiffFromTarget = Dungeon.hero.lvl - (1+level()*2);
		//plus an extra one for each level after 6
		if (level() >= 7){
			lvlDiffFromTarget -= level()-6;
		}

		if (lvlDiffFromTarget >= 0){
			exp += Math.round(chargesSpent * 10f * Math.pow(1.1f, lvlDiffFromTarget));
		} else {
			exp += Math.round(chargesSpent * 10f * Math.pow(0.75f, -lvlDiffFromTarget));
		}

		if (exp >= (level() + 1) * 50 && level() < levelCap) {
			upgrade();
			Catalog.countUse(HolyTome.class);
			exp -= level() * 50;
			GLog.p(Messages.get(this, "levelup"));

		}

		updateQuickslot();
	}

	public void directCharge(float amount){
		if (charge < chargeCap) {
			partialCharge += amount;
			while (partialCharge >= 1f) {
				charge++;
				partialCharge--;
			}
			if (charge >= chargeCap){
				partialCharge = 0;
				charge = chargeCap;
			}
			updateQuickslot();
		}
		updateQuickslot();
	}

	@Override
	public Item upgrade() {
		chargeCap = Math.min(chargeCap + 1, 10);
		return super.upgrade();
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new TomeRecharge();
	}

	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;

		if (charge < chargeCap) {
			if (!isEquipped(target)) amount *= 0.75f*target.pointsInTalent(Talent.LIGHT_READING)/3f;
			partialCharge += 0.25f*amount;
			while (partialCharge >= 1f) {
				charge++;
				partialCharge--;
			}
			if (charge >= chargeCap){
				partialCharge = 0;
				charge = chargeCap;
			}
			updateQuickslot();
		}
	}

	private ClericSpell quickSpell = null;

	public void setQuickSpell(ClericSpell spell){
		if (quickSpell == spell){
			quickSpell = null; //re-assigning the same spell clears the quick spell
			if (passiveBuff != null){
				ActionIndicator.clearAction((ActionIndicator.Action) passiveBuff);
			}
		} else {
			quickSpell = spell;
			if (passiveBuff != null){
				ActionIndicator.setAction((ActionIndicator.Action) passiveBuff);
			}
		}
	}

	private static final String QUICK_CLS = "quick_cls";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (quickSpell != null) {
			bundle.put(QUICK_CLS, quickSpell.getClass());
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(QUICK_CLS)){
			Class quickCls = bundle.getClass(QUICK_CLS);
			for (ClericSpell spell : ClericSpell.getAllSpells()){
				if (spell.getClass() == quickCls){
					quickSpell = spell;
				}
			}
		}
	}

	public class TomeRecharge extends ArtifactBuff implements ActionIndicator.Action {

		@Override
		public boolean attachTo(Char target) {
			if (super.attachTo(target)) {
				if (quickSpell != null) ActionIndicator.setAction(this);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void detach() {
			super.detach();
			ActionIndicator.clearAction(this);
		}

		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
				if (Regeneration.regenOn()) {
					float missing = (chargeCap - charge);
					if (level() > 7) missing += 5*(level() - 7)/3f;
					float turnsToCharge = (45 - missing);
					turnsToCharge /= RingOfEnergy.artifactChargeMultiplier(target);
					float chargeToGain = (1f / turnsToCharge);
					if (!isEquipped(Dungeon.hero)){
						chargeToGain *= 0.75f*Dungeon.hero.pointsInTalent(Talent.LIGHT_READING)/3f;
					}
					partialCharge += chargeToGain;
				}

				while (partialCharge >= 1) {
					charge++;
					partialCharge -= 1;
					if (charge == chargeCap){
						partialCharge = 0;
					}

				}
			} else {
				partialCharge = 0;
			}

			updateQuickslot();

			spend( TICK );

			return true;
		}

		@Override
		public String actionName() {
			return quickSpell.name();
		}

		@Override
		public int actionIcon() {
			return quickSpell.icon() + HeroIcon.SPELL_ACTION_OFFSET;
		}

		@Override
		public int indicatorColor() {
			return 0x002157;
		}

		@Override
		public void doAction() {
			if (cursed){
				GLog.w(Messages.get(HolyTome.this, "cursed"));
				return;
			}

			if (!canCast(Dungeon.hero, quickSpell)){
				GLog.w(Messages.get(HolyTome.this, "no_spell"));
				return;
			}

			if (QuickSlotButton.targetingSlot != -1 &&
					Dungeon.quickslot.getItem(QuickSlotButton.targetingSlot) == HolyTome.this) {
				targetingSpell = quickSpell;
				int cell = QuickSlotButton.autoAim(QuickSlotButton.lastTarget, HolyTome.this);

				if (cell != -1){
					GameScene.handleCell(cell);
				} else {
					//couldn't auto-aim, just target the position and hope for the best.
					GameScene.handleCell( QuickSlotButton.lastTarget.pos );
				}
			} else {
				quickSpell.onCast(HolyTome.this, Dungeon.hero);

				if (quickSpell.targetingFlags() != -1 && Dungeon.quickslot.contains(HolyTome.this)){
					targetingSpell = quickSpell;
					QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(HolyTome.this));
				}
			}
		}
	}

}
