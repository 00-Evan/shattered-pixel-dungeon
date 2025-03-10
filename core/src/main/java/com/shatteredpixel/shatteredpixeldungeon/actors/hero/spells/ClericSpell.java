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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

import java.util.ArrayList;

public abstract class ClericSpell {

	public abstract void onCast(HolyTome tome, Hero hero);

	public float chargeUse( Hero hero ){
		return 1;
	}

	public boolean canCast( Hero hero ){
		return true;
	}

	public String name(){
		return Messages.get(this, "name");
	}

	public String shortDesc(){
		return Messages.get(this, "short_desc") + " " + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	public String desc(){
		return Messages.get(this, "desc") + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	public boolean usesTargeting(){
		return false;
	}

	public int targetingFlags(){
		return -1; //-1 for no targeting
	}

	public int icon(){
		return HeroIcon.NONE;
	}

	public void onSpellCast(HolyTome tome, Hero hero){
		Invisibility.dispel();
		if (hero.hasTalent(Talent.SATIATED_SPELLS) && hero.buff(Talent.SatiatedSpellsTracker.class) != null){
			int amount = 1 + 2*hero.pointsInTalent(Talent.SATIATED_SPELLS);
			Buff.affect(hero, Barrier.class).setShield(amount);
			Char ally = PowerOfMany.getPoweredAlly();
			if (ally != null && ally.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null){
				Buff.affect(ally, Barrier.class).setShield(amount);
			}
			hero.buff(Talent.SatiatedSpellsTracker.class).detach();
		}
		tome.spendCharge(chargeUse(hero));
		Talent.onArtifactUsed(hero);
		if (hero.subClass == HeroSubClass.PALADIN){
			if (this != HolyWeapon.INSTANCE && hero.buff(HolyWeapon.HolyWepBuff.class) != null){
				hero.buff(HolyWeapon.HolyWepBuff.class).extend(10*chargeUse(hero));
			}
			if (this != HolyWard.INSTANCE && hero.buff(HolyWard.HolyArmBuff.class) != null){
				hero.buff(HolyWard.HolyArmBuff.class).extend(10*chargeUse(hero));
			}
		}

		if (hero.buff(AscendedForm.AscendBuff.class) != null){
			hero.buff(AscendedForm.AscendBuff.class).spellCasts++;
			hero.buff(AscendedForm.AscendBuff.class).incShield((int)(10*chargeUse(hero)));
		}
	}

	public static ArrayList<ClericSpell> getSpellList(Hero cleric, int tier){
		ArrayList<ClericSpell> spells = new ArrayList<>();

		if (tier == 1) {

			spells.add(GuidingLight.INSTANCE);
			spells.add(HolyWeapon.INSTANCE);
			spells.add(HolyWard.INSTANCE);

			if (cleric.hasTalent(Talent.HOLY_INTUITION)) {
				spells.add(HolyIntuition.INSTANCE);
			}

			if (cleric.hasTalent(Talent.SHIELD_OF_LIGHT)) {
				spells.add(ShieldOfLight.INSTANCE);
			}

		} else if (tier == 2) {

			if (cleric.hasTalent(Talent.RECALL_INSCRIPTION)){
				spells.add(RecallInscription.INSTANCE);
			}

			if (cleric.hasTalent(Talent.SUNRAY)){
				spells.add(Sunray.INSTANCE);
			}

			if (cleric.hasTalent(Talent.DIVINE_SENSE)) {
				spells.add(DivineSense.INSTANCE);
			}

			if (cleric.hasTalent(Talent.BLESS)){
				spells.add(BlessSpell.INSTANCE);
			}

		} else if (tier == 3){

			if (cleric.subClass == HeroSubClass.PRIEST) {
				spells.add(Radiance.INSTANCE);

			} else if (cleric.subClass == HeroSubClass.PALADIN){
				spells.add(Smite.INSTANCE);
			}

			if (cleric.hasTalent(Talent.CLEANSE)){
				spells.add(Cleanse.INSTANCE);
			}

			if (cleric.hasTalent(Talent.HOLY_LANCE)){
				spells.add(HolyLance.INSTANCE);
			}
			if (cleric.hasTalent(Talent.HALLOWED_GROUND)){
				spells.add(HallowedGround.INSTANCE);
			}
			if (cleric.hasTalent(Talent.MNEMONIC_PRAYER)){
				spells.add(MnemonicPrayer.INSTANCE);
			}

			if (cleric.hasTalent(Talent.LAY_ON_HANDS)){
				spells.add(LayOnHands.INSTANCE);
			}
			if (cleric.hasTalent(Talent.AURA_OF_PROTECTION)){
				spells.add(AuraOfProtection.INSTANCE);
			}
			if (cleric.hasTalent(Talent.WALL_OF_LIGHT)){
				spells.add(WallOfLight.INSTANCE);
			}

		} else if (tier == 4){

			if (cleric.hasTalent(Talent.DIVINE_INTERVENTION)){
				spells.add(DivineIntervention.INSTANCE);
			}
			if (cleric.hasTalent(Talent.JUDGEMENT)){
				spells.add(Judgement.INSTANCE);
			}
			if (cleric.hasTalent(Talent.FLASH)){
				spells.add(Flash.INSTANCE);
			}

			if (cleric.hasTalent(Talent.BODY_FORM)){
				spells.add(BodyForm.INSTANCE);
			}
			if (cleric.hasTalent(Talent.MIND_FORM)){
				spells.add(MindForm.INSTANCE);
			}
			if (cleric.hasTalent(Talent.SPIRIT_FORM)){
				spells.add(SpiritForm.INSTANCE);
			}

			if (cleric.hasTalent(Talent.BEAMING_RAY)){
				spells.add(BeamingRay.INSTANCE);
			}
			if (cleric.hasTalent(Talent.LIFE_LINK)){
				spells.add(LifeLinkSpell.INSTANCE);
			}
			if (cleric.hasTalent(Talent.STASIS)){
				spells.add(Stasis.INSTANCE);
			}

		}

		return spells;
	}

	public static ArrayList<ClericSpell> getAllSpells() {
		ArrayList<ClericSpell> spells = new ArrayList<>();
		spells.add(GuidingLight.INSTANCE);
		spells.add(HolyWeapon.INSTANCE);
		spells.add(HolyWard.INSTANCE);
		spells.add(HolyIntuition.INSTANCE);
		spells.add(ShieldOfLight.INSTANCE);
		spells.add(RecallInscription.INSTANCE);
		spells.add(Sunray.INSTANCE);
		spells.add(DivineSense.INSTANCE);
		spells.add(BlessSpell.INSTANCE);
		spells.add(Cleanse.INSTANCE);
		spells.add(Radiance.INSTANCE);
		spells.add(Smite.INSTANCE);
		spells.add(LayOnHands.INSTANCE);
		spells.add(AuraOfProtection.INSTANCE);
		spells.add(WallOfLight.INSTANCE);
		spells.add(HolyLance.INSTANCE);
		spells.add(HallowedGround.INSTANCE);
		spells.add(MnemonicPrayer.INSTANCE);
		spells.add(DivineIntervention.INSTANCE);
		spells.add(Judgement.INSTANCE);
		spells.add(Flash.INSTANCE);
		spells.add(BodyForm.INSTANCE);
		spells.add(MindForm.INSTANCE);
		spells.add(SpiritForm.INSTANCE);
		spells.add(BeamingRay.INSTANCE);
		spells.add(LifeLinkSpell.INSTANCE);
		spells.add(Stasis.INSTANCE);
		return spells;
	}
}
