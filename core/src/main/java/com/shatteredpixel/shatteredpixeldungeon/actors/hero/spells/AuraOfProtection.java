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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class AuraOfProtection extends ClericSpell {

	public static AuraOfProtection INSTANCE = new AuraOfProtection();

	@Override
	public int icon() {
		return HeroIcon.AURA_OF_PROTECTION;
	}

	@Override
	public String desc() {
		int dmgReduction = Math.round(7.5f + 7.5F*Dungeon.hero.pointsInTalent(Talent.AURA_OF_PROTECTION));
		int glyphPow = 25 + 25*Dungeon.hero.pointsInTalent(Talent.AURA_OF_PROTECTION);
		return Messages.get(this, "desc", dmgReduction, glyphPow) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	public float chargeUse(Hero hero) {
		return 2f;
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero) && hero.hasTalent(Talent.AURA_OF_PROTECTION);
	}

	@Override
	public void onCast(HolyTome tome, Hero hero) {

		Buff.affect(hero,AuraBuff.class, AuraBuff.DURATION);

		Sample.INSTANCE.play(Assets.Sounds.READ);

		hero.spend( 1f );
		hero.busy();
		hero.sprite.operate(hero.pos);

		onSpellCast(tome, hero);

	}

	public static class AuraBuff extends FlavourBuff {

		public static float DURATION = 20f;

		private Emitter particles;

		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.PROT_AURA;
		}

		@Override
		public void fx(boolean on) {
			if (on && (particles == null || particles.parent == null)){
				particles = target.sprite.emitter(); //emitter is much bigger than char so it needs to manage itself
				particles.pos(target.sprite, -32, -32, 80, 80);
				particles.fillTarget = false;
				particles.pour(Speck.factory(Speck.LIGHT), 0.02f);
			} else if (!on && particles != null){
				particles.on = false;
			}
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

	}

}
