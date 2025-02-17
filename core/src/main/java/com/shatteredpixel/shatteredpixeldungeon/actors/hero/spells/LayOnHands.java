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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class LayOnHands extends TargetedClericSpell {

	public static LayOnHands INSTANCE = new LayOnHands();

	@Override
	public int icon() {
		return HeroIcon.LAY_ON_HANDS;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 5 + 5*Dungeon.hero.pointsInTalent(Talent.LAY_ON_HANDS)) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	public int targetingFlags(){
		return -1; //auto-targeting behaviour is often wrong, so we don't use it
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero) && hero.hasTalent(Talent.LAY_ON_HANDS);
	}

	@Override
	protected void onTargetSelected(HolyTome tome, Hero hero, Integer target) {
		if (target == null) {
			return;
		}

		if (Dungeon.level.distance(hero.pos, target) > 1){
			GLog.w(Messages.get(this, "invalid_target"));
			return;
		}

		Char ch = Actor.findChar(target);
		if (ch == null){
			GLog.w(Messages.get(this, "no_target"));
			return;
		}

		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

		affectChar(hero, ch);

		if (ch == hero){
			hero.sprite.operate(ch.pos);
			hero.next();
		} else {
			hero.sprite.zap(ch.pos);
			hero.next();
		}

		Char ally = PowerOfMany.getPoweredAlly();
		if (ally != null && ally.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null){
			if (ch == hero){
				affectChar(hero, ally); //if cast on hero, duplicate to ally
			} else if (ally == ch) {
				affectChar(hero, hero); //if cast on ally, duplicate to hero
			}
		}

		onSpellCast(tome, hero);

	}

	private void affectChar(Hero hero, Char ch){
		Barrier barrier = Buff.affect(ch, Barrier.class);
		int totalHeal = 5 + 5*hero.pointsInTalent(Talent.LAY_ON_HANDS);
		int totalBarrier = 0;
		if (ch == hero){
			totalBarrier = totalHeal;
			totalBarrier = Math.min(3*totalHeal - barrier.shielding(), totalBarrier);
			totalBarrier = Math.max(0, totalBarrier);
			Buff.affect(ch, Barrier.class).incShield(totalBarrier);
			ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(totalBarrier), FloatingText.SHIELDING );
		} else {
			if (ch.HT - ch.HP < totalHeal){
				totalBarrier = totalHeal - (ch.HT - ch.HP);
				totalBarrier = Math.min(3*totalHeal - barrier.shielding(), totalBarrier);
				totalBarrier = Math.max(0, totalBarrier);
				if (ch.HP != ch.HT) {
					ch.HP = ch.HT;
					ch.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(totalHeal - totalBarrier), FloatingText.HEALING);
				}
				if (totalBarrier > 0) {
					barrier.incShield(totalBarrier);
					ch.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(totalBarrier), FloatingText.SHIELDING);
				}
			} else {
				ch.HP = ch.HP + totalHeal;
				ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(totalHeal), FloatingText.HEALING );
			}
		}
	}
}
