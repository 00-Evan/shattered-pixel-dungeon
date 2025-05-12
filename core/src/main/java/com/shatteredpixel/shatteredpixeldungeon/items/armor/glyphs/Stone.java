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

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.FerretTuft;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.GameMath;

public class Stone extends Armor.Glyph {

	private static ItemSprite.Glowing GREY = new ItemSprite.Glowing( 0x222222 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		
		testing = true;
		float accuracy = attacker.attackSkill(defender);
		float evasion = defender.defenseSkill(attacker);
		testing = false;

		//FIXME this is duplicated here because these apply in hit(), not in attack/defenseskill
		// the true solution is probably to refactor accuracy/evasion code a little bit
		if (attacker.buff(Bless.class) != null) accuracy *= 1.25f;
		if (attacker.buff(  Hex.class) != null) accuracy *= 0.8f;
		if (attacker.buff( Daze.class) != null) accuracy *= 0.5f;
		for (ChampionEnemy buff : attacker.buffs(ChampionEnemy.class)){
			accuracy *= buff.evasionAndAccuracyFactor();
		}
		accuracy *= AscensionChallenge.statModifier(attacker);
		if (Dungeon.hero.heroClass != HeroClass.CLERIC
				&& Dungeon.hero.hasTalent(Talent.BLESS)
				&& attacker.alignment == Char.Alignment.ALLY){
			// + 3%/5%
			accuracy *= 1.01f + 0.02f*Dungeon.hero.pointsInTalent(Talent.BLESS);
		}

		if (defender.buff(Bless.class) != null) evasion *= 1.25f;
		if (defender.buff(  Hex.class) != null) evasion *= 0.8f;
		if (defender.buff( Daze.class) != null) evasion *= 0.5f;
		for (ChampionEnemy buff : defender.buffs(ChampionEnemy.class)){
			evasion *= buff.evasionAndAccuracyFactor();
		}
		evasion *= AscensionChallenge.statModifier(defender);
		if (Dungeon.hero.heroClass != HeroClass.CLERIC
				&& Dungeon.hero.hasTalent(Talent.BLESS)
				&& defender.alignment == Char.Alignment.ALLY){
			// + 3%/5%
			evasion *= 1.01f + 0.02f*Dungeon.hero.pointsInTalent(Talent.BLESS);
		}
		evasion *= FerretTuft.evasionMultiplier();

		// end of copy-pasta

		evasion *= genericProcChanceMultiplier(defender);
		
		float hitChance;
		if (evasion >= accuracy){
			hitChance = (accuracy/evasion)/2f;
		} else {
			hitChance = 1f - (evasion/accuracy)/2f;
		}
		
		//75% of dodge chance is applied as damage reduction
		// we clamp in case accuracy or evasion were negative
		hitChance = GameMath.gate(0.25f, (1f + 3f*hitChance)/4f, 1f);
		
		damage = (int)Math.ceil(damage * hitChance);
		
		return damage;
	}
	
	private static boolean testing = false;
	
	public static boolean testingEvasion(){
		return testing;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return GREY;
	}

}
